<template>
  <div>
    <div class="order-train">
      <span class="order-train-main">{{dailyTrainTicket.date}}</span>&nbsp;
      <span class="order-train-main">{{dailyTrainTicket.trainCode}}</span>次&nbsp;
      <span class="order-train-main">{{dailyTrainTicket.start}}</span>站
      <span class="order-train-main">({{dailyTrainTicket.startTime}})</span>&nbsp;
      <span class="order-train-main"> —— </span>&nbsp;
      <span class="order-train-main">{{dailyTrainTicket.end}}</span>站
      <span class="order-train-main">({{dailyTrainTicket.endTime}})</span>&nbsp;
    </div>

    <div class="order-train-ticket">
    <span class="order-train-ticket-container" v-for="item in seatTypes" :key="item.type">
      <span>{{item.desc}}</span>:
      <span class="order-train-ticket-main">{{item.price}} ¥</span>&nbsp;
      <span class="order-train-ticket-main">{{item.count}}</span>&nbsp;张票&nbsp;&nbsp;
    </span>
      <a-divider></a-divider>
      <a-checkbox-group v-model:value="passengerChecks" :options="passengerOptions" />
      <!--    <br/>-->
      <!--    选中的乘客：{{passengerChecks}}-->
      <!--    <br/>-->
      <!--    购票列表：{{tickets}}-->
      <div class="order-tickets">
        <a-row class="order-tickets-header" v-if="tickets.length > 0">
          <a-col :span="2">乘客</a-col>
          <a-col :span="6">身份证</a-col>
          <a-col :span="4">票种</a-col>
          <a-col :span="4">座位类型</a-col>
        </a-row>
        <a-row class="order-tickets-row" v-for="ticket in tickets" :key="ticket.passengerId">
          <a-col :span="2">{{ ticket.passengerName }}</a-col>
          <a-col :span="6">{{ ticket.passengerIdCard }}</a-col>
          <a-col :span="4">
            <a-select v-model:value="ticket.passengerType" style="width: 100%">
              <a-select-option v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code" :value="item.code">
                {{item.desc}}
              </a-select-option>
            </a-select>
          </a-col>
          <a-col :span="4">
            <a-select v-model:value="ticket.seatTypeCode" style="width: 100%">
              <a-select-option v-for="item in seatTypes" :key="item.code" :value="item.code">
                {{item.desc}}
              </a-select-option>
            </a-select>
          </a-col>
        </a-row>
      </div>
    </div>
    <div v-if="tickets.length > 0" >
      <a-button type="primary" size="large" @click="finishCheckPassenger">提交订单</a-button>
    </div>

    <a-modal v-model:open="visible" title="请核对以下信息" class="checkOrder" ok-text="确认" cancel-text="取消" @ok="handelOk">
      <div class="order-tickets">
        <a-row class="order-tickets-header" v-if="tickets.length > 0">
          <a-col :span="3">乘客</a-col>
          <a-col :span="12">身份证</a-col>
          <a-col :span="3">票种</a-col>
          <a-col :span="6">座位类型</a-col>
        </a-row>
        <a-row class="order-tickets-row" v-for="ticket in tickets" :key="ticket.passengerId">
          <a-col :span="3">{{ ticket.passengerName }}</a-col>
          <a-col :span="12">{{ ticket.passengerIdCard }}</a-col>
          <a-col :span="3">
          <span v-for="item in PASSENGER_TYPE_ARRAY" :key="item.code">
            <span v-if="item.code === ticket.passengerType">
              {{item.desc}}
            </span>
          </span>
          </a-col>
          <a-col :span="6">
          <span v-for="item in seatTypes" :key="item.code">
            <span v-if="item.code === ticket.seatTypeCode">
              {{item.desc}}
            </span>
          </span>
          </a-col>
        </a-row>
<!--        <br/>-->
<!--        选座类型chooseSeatType:{{chooseSeatType}}-->
<!--        <br/>-->
<!--        选座对象chooseSeatObj:{{chooseSeatObj}}-->
<!--        <br/>-->
<!--        座位类型seatType:{{SEAT_COL_ARRAY_FILTER}}-->
        <div v-if="chooseSeatType === 0" style="color: red">
          您购买的车票不支持选座
          <div>12306规则：只有全部是一等座或全部是二等座才支持选座</div>
          <div>12306规则：余票小于一定数量时，不允许选座(本项目以20为例)</div>
        </div>
        <div v-else style="text-align: center">
          <a-switch class="choose-seat-item" v-for="item in SEAT_COL_ARRAY_FILTER" :key="item.code" v-model:checked="chooseSeatObj[item.code+'1']" :checked-children="item.code"></a-switch>
          <div v-if="tickets.length > 1">
            <a-switch class="choose-seat-item" v-for="item in SEAT_COL_ARRAY_FILTER" :key="item.code" v-model:checked="chooseSeatObj[item.code+'2']" :checked-children="item.code"></a-switch>
          </div>
          <div style="color: #999999">提示：您可以选择{{tickets.length}}个座位</div>
        </div>

        <br/>
        最终购票：{{tickets}}
        最终选座：{{chooseSeatObj}}
      </div>
    </a-modal>
  </div>
</template>

<script setup>
import {computed, onMounted, ref, watch} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";
import {iterateReferencesTraceMap} from "eslint-plugin-vue/lib/utils/index.js";

const dailyTrainTicket = SessionStorage.get(SESSION_ORDER) || {};

const PASSENGER_TYPE_ARRAY = [{code:"1", desc: "成人"}, {code:"2", desc: "儿童"}, {code:"3", desc: "学生"}]
const SEAT_TYPE = {YDZ:{code:"1", desc: "一等座", price:"0.4"}, EDZ:{code:"2", desc: "二等座", price:"0.3"}, RW:{code:"3", desc: "软卧", price:"0.6"}, YW:{code:"4", desc: "硬卧", price:"0.5"}};
const seatTypes = []
const passengers = ref([])
const passengerOptions = ref([])
const passengerChecks = ref([])
const visible = ref(false)

for (let KEY in SEAT_TYPE) {
  let key = KEY.toLowerCase();
  if (dailyTrainTicket[key] >= 0) {
    seatTypes.push({
      type: KEY,
      code: SEAT_TYPE[KEY]["code"],
      desc: SEAT_TYPE[KEY]["desc"],
      count: dailyTrainTicket[key],
      price: dailyTrainTicket[key + 'Price']
    })
  }
}

const handleQueryPassenger = () => {
  axios.get("/member/passenger/query-mine").then((response) => {
    let data = response.data;
    if(data.success) {
      passengers.value = data.content;
      passengers.value.forEach((item) => passengerOptions.value.push({
        label: item.name,
        value: item
      }))
    } else {
      notification.error({description: data.message});
    }
  })
}

// 购票列表，用于界面展示，并传递到后端接口，用来描述：哪个乘客购买什么座位的票
const tickets = ref([])
// 勾选或去掉某个乘客时，在购票列表上加上或去掉一张票
watch(() => passengerChecks.value, (newVal, oldValue) => {
  tickets.value = [];
  passengerChecks.value.forEach((item) => tickets.value.push({
    passengerId:item.id,
    passengerType: item.type,
    seatTypeCode: seatTypes[0].code, // 初始化默认值
    passengerName: item.name,
    passengerIdCard: item.idCard
  }))
}, {immediate: true})

// 0: 不支持选座; 1：选一等座位=；2：选二等座
const chooseSeatType = ref(0);
// 根据选择的座位类型，计算出对应的列，比如要选一等座，就筛选出ACDF，要选二等座，就筛选出ABCDF
const SEAT_COL_ARRAY = [{code: "A", desc: "A", type:"1"}, {code: "C", desc: "C", type:"1"},
  {code: "D", desc: "D", type:"1"}, {code: "F", desc: "F", type:"1"},
  {code: "A", desc: "A", type:"2"}, {code: "B", desc: "B", type:"2"},
  {code: "C", desc: "C", type:"2"}, {code: "D", desc: "D", type:"2"}, {code: "F", desc: "F", type:"2"}]
const SEAT_COL_ARRAY_FILTER = computed(() => {
  return SEAT_COL_ARRAY.filter(item => item.type === chooseSeatType.value)
})
// 选择的座位
// {
//   A1: false, C1: true, D1: false, F1: false,
//   A2: false, C2: false, D2: true, F2: false,
// }
const chooseSeatObj = ref({})
watch(() => SEAT_COL_ARRAY_FILTER.value, () => {
  chooseSeatObj.value = {}
  for (let i = 1; i <= 2; i++) {
    SEAT_COL_ARRAY_FILTER.value.forEach((item) => {
      chooseSeatObj.value[item.code + i] = false
    })
  }
}, {immediate: true})

const finishCheckPassenger = () => {
  if (tickets.value.length > 5) {
    notification.error({description: '最多只能购买5张车票'})
  }

  // 校验余票是否充足，购票列表中的每个座位类型，都去车次座位余票信息中，看余票是否充足
  // 前端校验不一定准，但可以减轻后端很多压力
  let seatTypesTemp = seatTypes;
  for(let i=0;i < tickets.value.length; i++) {
    let ticket = tickets.value[i];
    for (let j = 0; j < seatTypesTemp.length; j++) {
      let seatType = seatTypesTemp[j];
      // 同类型座位余票-1，这里扣减的是临时copy出来的库存，不是真正的库存，只是为了校验
      if(ticket.seatTypeCode === seatType.code){
        seatType.count--;
        if(seatType.count < 0) {
          notification.error({description: seatType.desc + '余票不足'});
          return;
        }
      }
    }
  }

  // 判断是否支持选座，只有纯一等座和二等座支持选座
  // 先筛选出购票列表中的所有座位类型，比如四张表：[1,1,2,2]
  let ticketSeatTypeCodes = [];
  for (let i = 0; i < tickets.value.length; i++) {
    let ticket = tickets.value[i];
    ticketSeatTypeCodes.push(ticket.seatTypeCode);
  }
  // 购票列表中的所有座位类型,去重：[1,2]
  const ticketSeatTypeCodesSet = Array.from(new Set(ticketSeatTypeCodes));
  if (ticketSeatTypeCodesSet.length !== 1) {
    console.log("选了多种座位，不支持选座")
    chooseSeatType.value = 0
  } else {
    if (ticketSeatTypeCodesSet[0] === SEAT_TYPE.YDZ.code) {
      console.log("一等座选座")
      chooseSeatType.value = SEAT_TYPE.YDZ.code
    } else if (ticketSeatTypeCodesSet[0] === SEAT_TYPE.EDZ.code) {
      console.log("二等座选座")
      chooseSeatType.value = SEAT_TYPE.EDZ.code
    } else {
      console.log("不是一等座或二等座，不支持选座")
      chooseSeatType.value = 0
    }

    // 余票小于20张时，不允许选座，否则选座成功率不高，影响出票
    if (chooseSeatType.value !== 0) {
      for (let i = 0; i < seatTypes.length; i++) {
        let seatType = seatTypes[i];
        // 找到同类型座位
        if(seatType.code === ticketSeatTypeCodesSet[0]) {
          // 判断余票，小于20张就不支持选座
          if (seatType.count < 20) {
            console.log("票数小于20张就不支持选座")
            chooseSeatType.value = 0;
            break;
          }
        }
      }
    }
  }

  // 弹出确认界面
  visible.value = true
}

const handelOk = () => {
  // 清空购票列表的座位
  for(let i = 0; i < tickets.value.length; i++) {
    tickets.value[i].seat = null;
  }
  let i = -1;
  // 要么不选座位，要么所选座位应该等于购票数，即i ===(tickets.valve.length - 1)
  for(let key in chooseSeatObj.value) {
    if(chooseSeatObj.value[key]) {
      i++;
      if(i > tickets.value.length - 1){
        notification.error({description: '所选座位数大于购票数'});
        return;
      }
      tickets.value[i].seat = key;
    }
  }
  if(i > -1 && i < tickets.value.length - 1){
    notification.error({description:'所选座位数小于购票数'});
    return;
  }
  console.log("最终购票:", tickets.value)

  // 确认购票订单
  axios.post("/business/confirm-order/do", {
    dailyTrainTicketId: dailyTrainTicket.id,
    date: dailyTrainTicket.date,
    trainCode: dailyTrainTicket.trainCode,
    start: dailyTrainTicket.start,
    end: dailyTrainTicket.end,
    tickets: tickets.value
  }).then((response) => {
    let data = response.data
    if(data.success) {
      notification.success({description: "下单成功！"})
    } else {
      notification.error({description: data.message})
    }
  })
}

onMounted(() => {
  handleQueryPassenger();
})
</script>

<style>
.order-train .order-train-main {
  font-size: 18px;
  font-weight: bold;
}
.order-train-ticket {
  margin-top: 20px;
  font-weight: bold;
}
.order-train-ticket .order-train-ticket-main {
  font-size: 18px;
  font-weight: bold;
  color: red;
  padding-left: 10px;
}

.order-train-ticket .order-train-ticket-container{
  padding-right: 50px;
}

.order-tickets {
  margin: 10px 0;
}

.order-tickets .ant-col {
  padding: 5px 10px;
}

.order-tickets .order-tickets-header {
  background-color: cornflowerblue;
  border: solid 1px cornflowerblue;
  color: white;
  font-size: 16px;
  padding: 5px 0;
}

.order-tickets .order-tickets-row{
  border: solid 1px cornflowerblue;
  border-top:none;
  vertical-align:middle;
  Line-height: 30px;
}

.order-tickets .choose-seat-item{
  margin: 5px 5px;
}

.checkOrder {
  top: 20%;
  width: 100%;
}
</style>

