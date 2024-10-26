<template>
  <p>
    <a-space>
      <a-date-picker v-model:value="params.date" value-format="YYYY-MM-DD" placeholder="请输入日期"></a-date-picker>
      <station-select v-model="params.start" width="200px"></station-select>
      <station-select v-model="params.end" width="200px"></station-select>
      <a-button type="primary" @click="handleQuery()">查找</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainTickets" :columns="columns" :pagination="pagination" @change="handleTableChange" :loading="loading">
    <template #bodyCell="{column, record}" >
      <template v-if="column.dataIndex === 'station'">
        {{record.start}} <br/>
        {{record.end}}
      </template>
      <template v-else-if="column.dataIndex === 'time'">
        {{record.startTime}} <br/>
        {{record.endTime}}
      </template>
      <template v-else-if="column.dataIndex === 'duration'">
        {{calDuration(record.startTime, record.endTime)}}<br/>
        <div v-if="record.startTime.replaceAll(':', '') >= record.endTime.replaceAll(':', '')">
          次日到达
        </div>
        <div v-else>
          当日到达
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'ydz'">
        <div v-if="record.ydz >= 0">
          {{record.ydz}}<br/>
          {{record.ydzPrice}}¥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'edz'">
        <div v-if="record.edz >= 0">
          {{record.edz}}<br/>
          {{record.edzPrice}}¥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'rw'">
        <div v-if="record.rw >= 0">
          {{record.rw}}<br/>
          {{record.rwPrice}}¥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'yw'">
        <div v-if="record.yw >= 0">
          {{record.yw}}<br/>
          {{record.ywPrice}}¥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'operation'">
        <a-button type="primary" @click="toOrder(record)">预订</a-button>
      </template>
    </template>
  </a-table>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";
import StationSelect from "@/components/station-select.vue";
import dayjs from "dayjs";
import router from "@/router/index.js";

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
  trainCode:null,
  date: null,
  start: null,
  end: null
})
const columns = [
  {
    title: '车次编号',
    dataIndex: 'trainCode',
    key: 'trainCode',
  },
  {
    title: '车站',
    dataIndex: 'station',
    key: 'station',
  },
  {
    title: '时间',
    dataIndex: 'time',
    key: 'time',
  },
  {
    title: '历时',
    dataIndex: 'duration',
    key: 'duration',
  },
  {
    title: '一等座',
    dataIndex: 'ydz',
    key: 'ydz',
  },
  {
    title: '二等座',
    dataIndex: 'edz',
    key: 'edz',
  },
  {
    title: '软卧',
    dataIndex: 'rw',
    key: 'rw',
  },
  {
    title: '硬卧',
    dataIndex: 'yw',
    key: 'yw',
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
  }
]

// 余票查询
const handleQuery = (param) => {
  if (!params.value.date) {
    notification.error({description: "请输入日期"})
    return;
  }
  if (!params.value.start) {
    notification.error({description: "出发地"})
    return;
  }
  if (!params.value.end) {
    notification.error({description: "目的地"})
    return;
  }
  if(!param) {
    param = {
      page: 1,
      size: pagination.value.pageSize
    }
  }

  // 保存查询参数
  SessionStorage.set(SESSION_TICKET_PARAMS, params.value)

  loading.value = true
  axios.get("/business/daily-train-ticket/query-list", {
    params: {
      page: param.page,
      size: param.size,
      trainCode: params.value.trainCode,
      date: params.value.date,
      start: params.value.start,
      end: params.value.end
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

const calDuration = (startTime, endTime) => {
  let diff = dayjs(endTime, 'HH:mm:ss').diff(dayjs(startTime, 'HH:mm:ss'), 'second');
  return dayjs('00:00:00', 'HH:mm:ss').second(diff).format('HH:mm:ss');
}

// 跳转到预定页面
const toOrder = (record) => {
  dailyTrainTicket.value = record
  SessionStorage.set(SESSION_ORDER, dailyTrainTicket.value)
  router.push("/order")
}

onMounted(() => {
  params.value = SessionStorage.get(SESSION_TICKET_PARAMS) || {}
  if (params.value) {
    handleQuery({
      page: 1,
      size: pagination.value.pageSize
    })
  }
})
</script>