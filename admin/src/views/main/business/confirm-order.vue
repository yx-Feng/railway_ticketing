<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
    </a-space>
  </p>
  <a-table :dataSource="confirmOrders" :columns="columns" :pagination="pagination" @change="handleTableChange" :loading="loading">
    <template #bodyCell="{column, record}" >
      <template v-if="column.dataIndex === 'status'">
        <span v-for="item in CONFIRM_ORDER_STATUS_ARRAY" :key="item.code">
          <span v-if="item.code === record.status">{{item.desc}}</span>
        </span>
      </template>
    </template>
  </a-table>
  <a-modal v-model:open="visible" title="车站" @ok="handleOk" ok-text="确认" cancel-text="取消">
    <a-form :model="confirmOrder" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="会员id">
        <train-select v-model="confirmOrder.memberId"></train-select>
      </a-form-item>
      <a-form-item label="日期">
        <a-time-picker v-model:value="confirmOrder.date" value-format="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="车次编号">
        <train-select v-model="confirmOrder.trainCode"></train-select>
      </a-form-item>
      <a-form-item label="出发站">
        <a-input v-model:value="confirmOrder.start" />
      </a-form-item>
      <a-form-item label="到达站">
        <a-input v-model:value="confirmOrder.end" />
      </a-form-item>
      <a-form-item label="余票ID">
        <train-select v-model="confirmOrder.dailyTrainTicketId"></train-select>
      </a-form-item>
      <a-form-item label="车票">
        <a-input v-model:value="confirmOrder.tickets" />
      </a-form-item>
      <a-form-item label="订单状态">
        <a-input v-model:value="confirmOrder.status" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";
import TrainSelect from "@/components/train-select.vue";

const visible = ref(false);
const confirmOrder = ref({
  id: undefined,
  memberId: undefined,
  date: undefined,
  trainCode: undefined,
  start: undefined,
  end: undefined,
  dailyTrainTicketId: undefined,
  tickets: undefined,
  status: undefined,
  createTime: undefined,
  updateTime: undefined
})

const confirmOrders = ref([])
// 分页的三个属性是固定的
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 10
})
let loading = ref(false)
const columns = [
  {
    title: '会员id',
    dataIndex: 'memberId',
    key: 'memberId',
  },
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
    title: '到达站',
    dataIndex: 'end',
    key: 'end',
  },
  {
    title: '余票ID',
    dataIndex: 'dailyTrainTicketId',
    key: 'dailyTrainTicketId',
  },
  {
    title: '车票',
    dataIndex: 'tickets',
    key: 'tickets',
  },
  {
    title: '订单状态',
    dataIndex: 'status',
    key: 'status',
  }
]
const CONFIRM_ORDER_STATUS_ARRAY = [{code: "I", desc: "初始"},{code: "P", desc: "处理中"},{code: "S", desc: "成功"},
  {code: "F", desc: "失败"},{code: "E", desc: "无票"},{code: "C", desc: "取消"}]

// 新增座位
const handleOk = (e) => {
  console.log(confirmOrder.value)
  axios.post("/business/confirm-order/admin/save", confirmOrder.value).then((response) => {
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
  axios.get("/business/confirm-order/admin/query-list", {
    params: {
      page: param.page,
      size: param.size
    }
  }).then((response) => {
    loading.value = false
    let data = response.data;
    if(data.success) {
      confirmOrders.value = data.content.list;
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