<template>
  <p>
    <a-space>
      <a-date-picker v-model:value="params.date" value-format="YYYY-MM-DD" placeholder="请输入日期"></a-date-picker>
      <train-select v-model="params.trainCode" width="200px"></train-select>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainCarriages" :columns="columns" :pagination="pagination" @change="handleTableChange" :loading="loading">
    <template #bodyCell="{column, record}" >
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-popconfirm title="删除后不可恢复，确认删除?" @confirm="onDelete(record)" ok-text="确认" cancel-text="取消">
            <a style="color: red">删除</a>
          </a-popconfirm>
          <a @click="onEdit(record)">编辑</a>
        </a-space>
      </template>
      <template v-else-if="column.dataIndex === 'seatType'">
        <span v-for="item in TRAIN_CARRIAGE_TYPE_ARRAY" :key="item.key">
          <span v-if="item.key === record.seatType">{{item.value}}</span>
        </span>
      </template>
    </template>
  </a-table>
  <a-modal v-model:open="visible" title="车站" @ok="handleOk" ok-text="确认" cancel-text="取消">
    <a-form :model="dailyTrainCarriage" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="日期">
        <a-date-picker v-model:value="dailyTrainCarriage.date" value-format="YYYY-MM-DD" placeholder="请输入日期"/>
      </a-form-item>
      <a-form-item label="车次编号">
        <train-select v-model="dailyTrainCarriage.trainCode"></train-select>
      </a-form-item>
      <a-form-item label="箱号">
        <a-input v-model:value="dailyTrainCarriage.index" />
      </a-form-item>
      <a-form-item label="座位类型">
        <a-select v-model:value="dailyTrainCarriage.seatType">
          <a-select-option v-for="item in TRAIN_CARRIAGE_TYPE_ARRAY" :key="item.key" :value="item.key">{{item.value}}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="排数">
        <a-input v-model:value="dailyTrainCarriage.rowCount" />
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
const dailyTrainCarriage = ref({
  id: undefined,
  date: undefined,
  trainCode: undefined,
  index: undefined,
  seatType: undefined,
  rowCount: undefined,
  createTime: undefined,
  updateTime: undefined
})

const dailyTrainCarriages = ref([])
// 分页的三个属性是固定的
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 10
})
const loading = ref(false)
const params = ref({
  trainCode: null,
  date: null
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
    title: '箱号',
    dataIndex: 'index',
    key: 'index',
  },
  {
    title: '座位类型',
    dataIndex: 'seatType',
    key: 'seatType',
  },
  {
    title: '座位数',
    dataIndex: 'seatCount',
    key: 'seatCount',
  },
  {
    title: '排数',
    dataIndex: 'rowCount',
    key: 'rowCount',
  },
  {
    title: '列数',
    dataIndex: 'columnCount',
    key: 'columnCount',
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
  },
  {
    title: '操作',
    dataIndex: 'operation',
  },
]
const TRAIN_CARRIAGE_TYPE_ARRAY = [{key: "1", value: "一等座"}, {key: "2", value: "二等座"}, {key: "3", value: "软卧"}, {key: "4", value: "硬卧"}]

// 新增车箱
const handleOk = (e) => {
  console.log(dailyTrainCarriage.value)
  axios.post("/business/admin/daily-train-carriage/save", dailyTrainCarriage.value).then((response) => {
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

// 查询车箱列表
const handleQuery = (param) => {
  if(!param) {
    param = {
      page: 1,
      size: pagination.value.pageSize
    }
  }
  loading.value = true
  axios.get("/business/admin/daily-train-carriage/query-list", {
    params: {
      page: param.page,
      size: param.size,
      trainCode: params.value.trainCode,
      date: params.value.date
    }
  }).then((response) => {
    loading.value = false
    let data = response.data;
    if(data.success) {
      dailyTrainCarriages.value = data.content.list;
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

// 点击增加
const onAdd = () => {
  dailyTrainCarriage.value = {};
  visible.value = true;
}

// 点击编辑
const onEdit = (record) => {
  dailyTrainCarriage.value = JSON.parse(JSON.stringify(record));
  visible.value = true
}

// 删除
const onDelete = (record) => {
  axios.delete("/business/admin/daily-train-carriage/delete/" + record.id).then((response) => {
    const data = response.data;
    if(data.success) {
      notification.success({description: "删除成功!"});
      handleQuery({
        page: pagination.value.current,
        size: pagination.value.pageSize
      })
    } else {
      notification.error({description: data.message})
    }
  })
}

onMounted(() => {
  handleQuery({
    page: 1,
    size: pagination.value.pageSize
  })
})
</script>