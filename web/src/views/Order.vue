<template>
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
    <br/>
    选中的乘客：{{passengerChecks}}
    <br/>
    购票列表：{{tickets}}
  </div>
</template>

<script setup>
import {onMounted, ref, watch} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";

const dailyTrainTicket = SessionStorage.get(SESSION_ORDER) || {};

const SEAT_TYPE = {YDZ:{code:"1", desc: "一等座", price:"0.4"}, EDZ:{code:"2", desc: "二等座", price:"0.3"}, RW:{code:"3", desc: "软卧", price:"0.6"}, YW:{code:"4", desc: "硬卧", price:"0.5"}};
const seatTypes = []
const passengers = ref([])
const passengerOptions = ref([])
const passengerChecks = ref([])

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
</style>

