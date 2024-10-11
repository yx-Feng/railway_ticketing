<template>
  <p>
    <a-space>
      <a-date-picker v-model:value="params.date" value-format="YYYY-MM-DD" placeholder="请输入日期"></a-date-picker>
      <train-select v-model="params.trainCode" width="200px"></train-select>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainStations" :columns="columns" :pagination="pagination" @change="handleTableChange" :loading="loading">
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
    <a-form :model="dailyTrainStation" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="日期">
        <a-date-picker v-model:value="dailyTrainStation.date" value-format="YYYY-MM-DD" placeholder="请输入日期"/>
      </a-form-item>
      <a-form-item label="车次编号">
        <train-select v-model="dailyTrainStation.trainCode" width="50%"></train-select>
      </a-form-item>
      <a-form-item label="站序">
        <a-input v-model:value="dailyTrainStation.index" />
      </a-form-item>
      <a-form-item label="站名">
        <station-select v-model="dailyTrainStation.name"></station-select>
      </a-form-item>
      <a-form-item label="站名拼音">
        <a-input v-model:value="dailyTrainStation.namePinyin" disabled/>
      </a-form-item>
      <a-form-item label="进站时间">
        <a-time-picker v-model:value="dailyTrainStation.inTime" value-format="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="出站时间">
        <a-time-picker v-model:value="dailyTrainStation.outTime" value-format="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="停站时长">
        <a-time-picker v-model:value="dailyTrainStation.stopTime" value-format="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="里程(公里)">
        <a-input v-model:value="dailyTrainStation.km" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import {onMounted, ref, watch} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";
import {pinyin} from "pinyin-pro";
import TrainSelect from "@/components/train-select.vue"; // 自定义车次下拉框组件
import StationSelect from "@/components/station-select.vue"; // 自定义车站下拉框组件

const visible = ref(false);
const dailyTrainStation = ref({
  id: undefined,
  date: undefined,
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

const dailyTrainStations = ref([])
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
    title: '里程(公里)',
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
  console.log(dailyTrainStation.value)
  axios.post("/business/admin/daily-train-station/save", dailyTrainStation.value).then((response) => {
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
  axios.get("/business/admin/daily-train-station/query-list", {
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
      dailyTrainStations.value = data.content.list;
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

// 汉字转拼音，自动填入
watch(() => dailyTrainStation.value.name, () => {
  if(dailyTrainStation.value.name) {
    dailyTrainStation.value.namePinyin = pinyin(dailyTrainStation.value.name, {toneType: 'none'}).replaceAll(" ", "");
  } else {
    dailyTrainStation.value.namePinyin = "";
  }
}, {immediate: true})

// 点击增加
const onAdd = () => {
  dailyTrainStation.value = {};
  visible.value = true;
}

// 点击编辑
const onEdit = (record) => {
  dailyTrainStation.value = JSON.parse(JSON.stringify(record));
  visible.value = true
}

// 删除
const onDelete = (record) => {
  axios.delete("/business/admin/daily-train-station/delete/" + record.id).then((response) => {
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