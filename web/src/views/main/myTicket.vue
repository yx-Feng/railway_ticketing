<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
    </a-space>
  </p>
  <a-table :dataSource="tickets" :columns="columns" :pagination="pagination" @change="handleTableChange" :loading="loading">
    <template #bodyCell="{column, record}" >
      <template v-if="column.dataIndex === 'seatCol'">
        <span v-for="item in TRAIN_SEAT_COL_ARRAY" :key="item.code">
          <span v-if="item.code === record.seatCol && item.type === record.seatType ">{{item.desc}}</span>
        </span>
      </template>
      <template v-else-if="column.dataIndex === 'seatType'">
        <span v-for="item in TRAIN_CARRIAGE_TYPE_ARRAY" :key="item.key">
          <span v-if="item.key === record.seatType">{{item.value}}</span>
        </span>
      </template>
    </template>
  </a-table>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";

const visible = ref(false);
const ticket = ref({
  id: undefined,
  memberId: undefined,
  passengerId: undefined,
  passengerName: undefined,
  trainDate: undefined,
  trainCode: undefined,
  carriageIndex: undefined,
  seatRow: undefined,
  seatCol: undefined,
  startStation: undefined,
  startTime: undefined,
  endStation: undefined,
  endTime: undefined,
  seatType: undefined,
  createTime: undefined,
  updateTime: undefined
})

const tickets = ref([])
// 分页的三个属性是固定的
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 8
})
let loading = ref(false)
const columns = [
  {
    title: '乘客姓名',
    dataIndex: 'passengerName',
    key: 'passengerName',
  },
  {
    title: '日期',
    dataIndex: 'trainDate',
    key: 'trainDate',
  },
  {
    title: '车次编号',
    dataIndex: 'trainCode',
    key: 'trainCode',
  },
  {
    title: '箱序',
    dataIndex: 'carriageIndex',
    key: 'carriageIndex',
  },
  {
    title: '排号',
    dataIndex: 'seatRow',
    key: 'seatRow',
  },
  {
    title: '列号',
    dataIndex: 'seatCol',
    key: 'seatCol',
  },
  {
    title: '出发站',
    dataIndex: 'startStation',
    key: 'startStation',
  },
  {
    title: '出发时间',
    dataIndex: 'startTime',
    key: 'startTime',
  },
  {
    title: '到达站',
    dataIndex: 'endStation',
    key: 'endStation',
  },
  {
    title: '到站时间',
    dataIndex: 'endTime',
    key: 'endTime',
  },
  {
    title: '座位类型',
    dataIndex: 'seatType',
    key: 'seatType',
  },
  {
    title: '出票时间',
    dataIndex: 'createTime',
    key: 'createTime',
  }
]
const TRAIN_CARRIAGE_TYPE_ARRAY = [{key: "1", value: "一等座"}, {key: "2", value: "二等座"},
                                   {key: "3", value: "软卧"}, {key: "4", value: "硬卧"}]
const TRAIN_SEAT_COL_ARRAY = [{code: "A", desc: "A", type:"1"}, {code: "C", desc: "C", type:"1"},
                              {code: "D", desc: "D", type:"1"}, {code: "F", desc: "F", type:"1"},
                              {code: "A", desc: "A", type:"2"}, {code: "B", desc: "B", type:"2"},
                              {code: "C", desc: "C", type:"2"}, {code: "D", desc: "D", type:"2"}, {code: "F", desc: "F", type:"2"}]

// 查询座位列表
const handleQuery = (param) => {
  if(!param) {
    param = {
      page: 1,
      size: pagination.value.pageSize
    }
  }
  loading.value = true
  axios.get("/member/ticket/query-list", {
    params: {
      page: param.page,
      size: param.size,
    }
  }).then((response) => {
    loading.value = false
    let data = response.data;
    if(data.success) {
      tickets.value = data.content.list;
      // 设置分页控件的值
      pagination.value.current = param.page;
      pagination.value.total = data.content.total;
    } else {
      notification.error({description: data.message})
    }
  })
}

const handleTableChange = (pagination) => {
  handleQuery({
    page: pagination.current,
    size: pagination.pageSize
  })
}

onMounted(() => {
  handleQuery({
    page: 1,
    size: pagination.value.pageSize
  })
})
</script>