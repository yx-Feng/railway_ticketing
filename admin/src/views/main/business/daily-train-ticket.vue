<template>
  <p>
    <a-space>
      <train-select v-model="params.trainCode" width="200px"></train-select>
      <a-button type="primary" @click="handleQuery()">查找</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainTickets" :columns="columns" :pagination="pagination" @change="handleTableChange" :loading="loading">
    <template #bodyCell="{column, record}" >
      <template v-if="column.dataIndex === 'col'">
        <span v-for="item in TRAIN_SEAT_COL_ARRAY" :key="item.code">
          <span v-if="item.code === record.col && item.type === record.seatType ">{{item.desc}}</span>
        </span>
      </template>
      <template v-else-if="column.dataIndex === 'seatType'">
        <span v-for="item in TRAIN_CARRIAGE_TYPE_ARRAY" :key="item.key">
          <span v-if="item.key === record.seatType">{{item.value}}</span>
        </span>
      </template>
    </template>
  </a-table>
  <a-modal v-model:open="visible" title="车站" @ok="handleOk" ok-text="确认" cancel-text="取消">
    <a-form :model="dailyTrainTicket" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="日期">
        <a-date-picker v-model:value="dailyTrainTicket.date" value-format="YYYY-MM-DD" placeholder="请输入日期"/>
      </a-form-item>
      <a-form-item label="车次编号">
        <train-select v-model="dailyTrainTicket.trainCode"></train-select>
      </a-form-item>
      <a-form-item label="出发站">
        <station-select v-model="dailyTrainTicket.start"></station-select>
      </a-form-item>
      <a-form-item label="出发站拼音">
        <a-input v-model:value="dailyTrainTicket.startPinyin" disabled/>
      </a-form-item>
      <a-form-item label="出发时间">
        <a-time-picker v-model:value="dailyTrainTicket.startTime" value-format="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="出发站序">
        <a-input v-model:value="dailyTrainTicket.startIndex" />
      </a-form-item>
      <a-form-item label="到达站">
        <station-select v-model="dailyTrainTicket.end"></station-select>
      </a-form-item>
      <a-form-item label="到达站拼音">
        <a-input v-model:value="dailyTrainTicket.endPinyin" disabled/>
      </a-form-item>
      <a-form-item label="到站时间">
        <a-time-picker v-model:value="dailyTrainTicket.endTime" value-format="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="到站站序">
        <a-input v-model:value="dailyTrainTicket.endIndex" />
      </a-form-item>
      <a-form-item label="一等座余票">
        <a-input v-model:value="dailyTrainTicket.ydz" />
      </a-form-item>
      <a-form-item label="一等座票价">
        <a-input v-model:value="dailyTrainTicket.ydzPrice" />
      </a-form-item>
      <a-form-item label="二等座余票">
        <a-input v-model:value="dailyTrainTicket.edz" />
      </a-form-item>
      <a-form-item label="二等座票价">
        <a-input v-model:value="dailyTrainTicket.edzPrice" />
      </a-form-item>
      <a-form-item label="软卧余票">
        <a-input v-model:value="dailyTrainTicket.rw" />
      </a-form-item>
      <a-form-item label="软卧票价">
        <a-input v-model:value="dailyTrainTicket.rw" />
      </a-form-item>
      <a-form-item label="硬卧余票">
        <a-input v-model:value="dailyTrainTicket.yw" />
      </a-form-item>
      <a-form-item label="硬卧余票">
        <a-input v-model:value="dailyTrainTicket.yw" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";
import TrainSelect from "@/components/train-select.vue";
import StationSelect from "@/components/station-select.vue";

const visible = ref(false);
const dailyTrainTicket = ref({
  id: undefined,
  date: undefined,
  trainCode: undefined,
  start: undefined,
  startPinyin: undefined,
  startTime: undefined,
  startIndex: undefined,
  end: undefined,
  endPinyin: undefined,
  endTime: undefined,
  endIndex: undefined,
  rw: undefined,
  rwPrice: undefined,
  edz: undefined,
  edzPrice: undefined,
  ydz: undefined,
  ydzPrice: undefined,
  yw: undefined,
  ywPrice: undefined,
  createTime: undefined,
  updateTime: undefined
})

const dailyTrainTickets = ref([])
// 分页的三个属性是固定的
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 8
})
let loading = ref(false)
let params = ref({
  trainCode:null
})
const columns = [
  {
    title: '日期',
    dataIndex: 'date',
    key: 'date',
  },
  {
    title: '车次编号',
    dataIndex: 'trainCode',
    key: 'trainCode',
  },
  {
    title: '出发站',
    dataIndex: 'start',
    key: 'start',
  },
  {
    title: '出发站拼音',
    dataIndex: 'startPinyin',
    key: 'startPinyin',
  },
  {
    title: '出发时间',
    dataIndex: 'startTime',
    key: 'startTime',
  },
  {
    title: '出发站序',
    dataIndex: 'startIndex',
    key: 'startIndex',
  },
  {
    title: '到达站',
    dataIndex: 'end',
    key: 'end',
  },
  {
    title: '到达站拼音',
    dataIndex: 'endPinyin',
    key: 'endPinyin',
  },
  {
    title: '到站时间',
    dataIndex: 'endTime',
    key: 'endTime',
  },
  {
    title: '到站站序',
    dataIndex: 'endIndex',
    key: 'endIndex',
  },
  {
    title: '一等座余票',
    dataIndex: 'ydz',
    key: 'ydz',
  },
  {
    title: '一等座票价',
    dataIndex: 'ydzPrice',
    key: 'ydzPrice',
  },
  {
    title: '二等座余票',
    dataIndex: 'edz',
    key: 'edz',
  },
  {
    title: '二等座票价',
    dataIndex: 'edzPrice',
    key: 'edzPrice',
  },
  {
    title: '软卧余票',
    dataIndex: 'rw',
    key: 'rw',
  },
  {
    title: '软卧票价',
    dataIndex: 'rwPrice',
    key: 'rwPrice',
  },
  {
    title: '硬卧余票',
    dataIndex: 'yw',
    key: 'yw',
  },
  {
    title: '硬卧票价',
    dataIndex: 'ywPrice',
    key: 'ywPrice',
  }
]
const TRAIN_CARRIAGE_TYPE_ARRAY = [{key: "1", value: "一等座"}, {key: "2", value: "二等座"},
                                   {key: "3", value: "软卧"}, {key: "4", value: "硬卧"}]
const TRAIN_SEAT_COL_ARRAY = [{code: "A", desc: "A", type:"1"}, {code: "C", desc: "C", type:"1"},
                              {code: "D", desc: "D", type:"1"}, {code: "F", desc: "F", type:"1"},
                              {code: "A", desc: "A", type:"2"}, {code: "B", desc: "B", type:"2"},
                              {code: "C", desc: "C", type:"2"}, {code: "D", desc: "D", type:"2"}, {code: "F", desc: "F", type:"2"}]

// 新增座位
const handleOk = (e) => {
  console.log(dailyTrainTicket.value)
  axios.post("/business/admin/daily-train-ticket/save", dailyTrainTicket.value).then((response) => {
    let data = response.data;
    if(data.success) {
      notification.success({description: "保存成功!"})
      visible.value = false;
      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize
      })
    } else {
      notification.error({description: data.message})
    }
  })
}

// 查询座位列表
const handleQuery = (param) => {
  if(!param) {
    param = {
      page: 1,
      size: pagination.value.pageSize
    }
  }
  loading.value = true
  axios.get("/business/admin/daily-train-ticket/query-list", {
    params: {
      page: param.page,
      size: param.size,
      trainCode: params.value.trainCode
    }
  }).then((response) => {
    loading.value = false
    let data = response.data;
    if(data.success) {
      dailyTrainTickets.value = data.content.list;
      // 设置分页控件的值
      pagination.value.current = param.page;
      pagination.value.total = data.content.total;
    } else {
      notification.error({description: data.message})
    }
  })
}

const handleTableChange = (page) => {
  pagination.value.pageSize = page.pageSize;
  handleQuery({
    page: page.current,
    size: page.pageSize
  })
}

onMounted(() => {
  handleQuery({
    page: 1,
    size: pagination.value.pageSize
  })
})
</script>