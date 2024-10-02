<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="trainStations" :columns="columns" :pagination="pagination" @change="handleTableChange" :loading="loading">
    <template #bodyCell="{column, record}" >
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-popconfirm title="删除后不可恢复，确认删除?" @confirm="onDelete(record)" ok-text="确认" cancel-text="取消">
            <a style="color: red">删除</a>
          </a-popconfirm>
          <a @click="onEdit(record)">编辑</a>
        </a-space>
      </template>
    </template>
  </a-table>
  <a-modal v-model:open="visible" title="车站" @ok="handleOk" ok-text="确认" cancel-text="取消">
    <a-form :model="trainStation" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="车次编号">
        <a-input v-model:value="trainStation.trainCode" />
      </a-form-item>
      <a-form-item label="站序">
        <a-input v-model:value="trainStation.index" />
      </a-form-item>
      <a-form-item label="站名">
        <a-input v-model:value="trainStation.name" />
      </a-form-item>
      <a-form-item label="站名拼音">
        <a-input v-model:value="trainStation.namePinyin" />
      </a-form-item>
      <a-form-item label="进站时间">
        <a-time-picker v-model:value="trainStation.inTime" value-format="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="出站时间">
        <a-time-picker v-model:value="trainStation.outTime" value-format="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="停站时长">
        <a-time-picker v-model:value="trainStation.stopTime" value-format="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="里程">
        <a-input v-model:value="trainStation.km" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";

const visible = ref(false);
const trainStation = ref({
  id: undefined,
  trainCode: undefined,
  index: undefined,
  name: undefined,
  namePinyin: undefined,
  inTime: undefined,
  outTime: undefined,
  stopTime: undefined,
  km: undefined,
  createTime: undefined,
  updateTime: undefined
})

const trainStations = ref([])
// 分页的三个属性是固定的
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 2
})
const loading = ref(false)
const columns = [
  {
    title: '车次编号',
    dataIndex: 'trainCode',
    key: 'trainCode',
  },
  {
    title: '站序',
    dataIndex: 'index',
    key: 'index',
  },
  {
    title: '站名',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '站名拼音',
    dataIndex: 'namePinyin',
    key: 'namePinyin',
  },
  {
    title: '进站时间',
    dataIndex: 'inTime',
    key: 'inTime',
  },
  {
    title: '出站时间',
    dataIndex: 'outTime',
    key: 'outTime',
  },
  {
    title: '停站时长',
    dataIndex: 'stopTime',
    key: 'stopTime',
  },
  {
    title: '里程(公里)|从上一站到本站的距离',
    dataIndex: 'km',
    key: 'km',
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

// 新增车站
const handleOk = (e) => {
  console.log(trainStation.value)
  axios.post("/business/admin/train-station/save", trainStation.value).then((response) => {
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

// 查询车站列表
const handleQuery = (param) => {
  if(!param) {
    param = {
      page: 1,
      size: pagination.value.pageSize
    }
  }
  loading.value = true
  axios.get("/business/admin/train-station/query-list", {
    params: {
      page: param.page,
      size: param.size
    }
  }).then((response) => {
    loading.value = false
    let data = response.data;
    if(data.success) {
      trainStations.value = data.content.list;
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

// 点击增加
const onAdd = () => {
  trainStation.value = {};
  visible.value = true;
}

// 点击编辑
const onEdit = (record) => {
  trainStation.value = JSON.parse(JSON.stringify(record));
  visible.value = true
}

// 删除
const onDelete = (record) => {
  axios.delete("/business/admin/train-station/delete/" + record.id).then((response) => {
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