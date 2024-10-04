<template>
  <p>
    <a-space>
      <train-select v-model="params.trainCode" width="200px"></train-select>
      <a-button type="primary" @click="handleQuery()">查找</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainSeats" :columns="columns" :pagination="pagination" @change="handleTableChange" :loading="loading">
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
    <a-form :model="dailyTrainSeat" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="日期">
        <a-date-picker v-model:value="dailyTrainSeat.date" value-format="YYYY-MM-DD" placeholder="请输入日期"/>
      </a-form-item>
      <a-form-item label="车次编号">
        <train-select v-model="dailyTrainSeat.trainCode"></train-select>
      </a-form-item>
      <a-form-item label="箱序">
        <a-input v-model:value="dailyTrainSeat.carriageIndex" />
      </a-form-item>
      <a-form-item label="排号">
        <a-select v-model:value="dailyTrainSeat.row">
          <a-select-option v-for="item in TRAIN_SEAT_COL_ARRAY" :key="item.key" :value="item.key">{{item.value}}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="列号">
        <a-select v-model:value="dailyTrainSeat.col">
          <a-select-option v-for="item in TRAIN_SEAT_COL_ARRAY" :key="item.key" :value="item.key">{{item.value}}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="座位类型">
        <a-select v-model:value="dailyTrainSeat.seatType">
          <a-select-option v-for="item in TRAIN_CARRIAGE_TYPE_ARRAY" :key="item.key" :value="item.key">{{item.value}}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="同车箱座序">
        <a-input v-model:value="dailyTrainSeat.carriageSeatIndex" />
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
const dailyTrainSeat = ref({
  id: undefined,
  date: undefined,
  trainCode: undefined,
  carriageIndex: undefined,
  row: undefined,
  col: undefined,
  seatType: undefined,
  carriageSeatIndex: undefined,
  sell: undefined,
  createTime: undefined,
  updateTime: undefined
})

const dailyTrainSeats = ref([])
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
    title: '箱序',
    dataIndex: 'carriageIndex',
    key: 'carriageIndex',
  },
  {
    title: '排号',
    dataIndex: 'row',
    key: 'row',
  },
  {
    title: '列号',
    dataIndex: 'col',
    key: 'col',
  },
  {
    title: '座位类型',
    dataIndex: 'seatType',
    key: 'seatType',
  },
  {
    title: '同车箱座序',
    dataIndex: 'carriageSeatIndex',
    key: 'carriageSeatIndex',
  },
  {
    title: '售卖情况',
    dataIndex: 'sell',
    key: 'sell',
  },
  {
    title: '新增时间',
    dataIndex: 'createTime',
    key: 'createTime',
  },
  {
    title: '修改时间',
    dataIndex: 'updateTime',
    key: 'updateTime',
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
  console.log(dailyTrainSeat.value)
  axios.post("/business/admin/daily-train-seat/save", dailyTrainSeat.value).then((response) => {
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
  axios.get("/business/admin/daily-train-seat/query-list", {
    params: {
      page: param.page,
      size: param.size,
      trainCode: params.value.trainCode
    }
  }).then((response) => {
    loading.value = false
    let data = response.data;
    if(data.success) {
      dailyTrainSeats.value = data.content.list;
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