<template>
  <p>
    <a-space>
      <a-date-picker v-model:value="params.date" value-format="YYYY-MM-DD" placeholder="请输入日期"></a-date-picker>
      <train-select v-model="params.code" width="200px"></train-select>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
      <a-button type="primary" @click="onClickGenDaily" danger>手动生成车次信息</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrains" :columns="columns" :pagination="pagination" @change="handleTableChange" :loading="loading">
    <template #bodyCell="{column, record}" >
      <template v-if="column.dataIndex === 'operation'">
        <a-space>
          <a-popconfirm title="删除后不可恢复，确认删除?" @confirm="onDelete(record)" ok-text="确认" cancel-text="取消">
            <a style="color: red">删除</a>
          </a-popconfirm>
          <a @click="onEdit(record)">编辑</a>
        </a-space>
      </template>
      <template v-else-if="column.dataIndex === 'type'">
        <span v-for="item in TRAIN_TYPE_ARRAY" :key="item.key">
          <span v-if="item.key === record.type">{{item.value}}</span>
        </span>
      </template>
    </template>
  </a-table>
  <a-modal v-model:open="visible" title="车站" @ok="handleOk" ok-text="确认" cancel-text="取消">
    <a-form :model="dailyTrain" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="日期">
        <a-date-picker v-model:value="dailyTrain.date" value-format="YYYY-MM-DD" placeholder="请输入日期"/>
      </a-form-item>
      <a-form-item label="车次编号">
        <train-select v-model="dailyTrain.code" @change="onChangeCode"></train-select>
      </a-form-item>
      <a-form-item label="车次类型">
        <a-select v-model:value="dailyTrain.type">
          <a-select-option v-for="item in TRAIN_TYPE_ARRAY" :key="item.key" :value="item.key">{{item.value}}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="始发站">
        <station-select v-model="dailyTrain.start"></station-select>
      </a-form-item>
      <a-form-item label="始发站拼音">
        <a-input v-model:value="dailyTrain.startPinyin" disabled/>
      </a-form-item>
      <a-form-item label="出发时间">
        <a-time-picker v-model:value="dailyTrain.startTime" value-format="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="终点站">
        <station-select v-model="dailyTrain.end"></station-select>
      </a-form-item>
      <a-form-item label="终点站拼音">
        <a-input v-model:value="dailyTrain.endPinyin" disabled/>
      </a-form-item>
      <a-form-item label="到站时间">
        <a-time-picker v-model:value="dailyTrain.endTime" value-format="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
    </a-form>
  </a-modal>
  <a-modal v-model:visible="genDailyVisible" title="生成车次" @ok="handleGenDailyOk" ok-text="确认" cancel-text="取消" :confirm-loading="genDailyLoading">
    <a-form :model="genDaily" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">
      <a-form-item label="日期">
        <a-date-picker v-model:value="genDaily.date" placeholder="请选择日期"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import {onMounted, ref, watch} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";
import {pinyin} from "pinyin-pro";
import StationSelect from "@/components/station-select.vue";
import TrainSelect from "@/components/train-select.vue";
import dayjs from 'dayjs';

const visible = ref(false);
const dailyTrain = ref({
  id: undefined,
  date:undefined,
  code: undefined,
  type: undefined,
  start: undefined,
  startPinyin: undefined,
  startTime: undefined,
  end: undefined,
  endPinyin: undefined,
  endTime: undefined,
  createTime: undefined,
  updateTime: undefined,
})

const dailyTrains = ref([])
// 分页的三个属性是固定的
const pagination = ref({
  total: 0,
  current: 1,
  pageSize: 8
})
const loading = ref(false)
const params = ref({
  code: null,
  date: null
})
const genDaily = ref({
  date: null
})
const genDailyVisible = ref(false)
const genDailyLoading = ref(false)
const columns = [
  {
    title: '日期',
    dataIndex: 'date',
    key: 'date',
  },
  {
    title: '车次编号',
    dataIndex: 'code',
    key: 'code',
  },
  {
    title: '车次类型',
    dataIndex: 'type',
    key: 'type',
  },
  {
    title: '始发站',
    dataIndex: 'start',
    key: 'start',
  },
  {
    title: '始发站拼音',
    dataIndex: 'startPinyin',
    key: 'startPinyin',
  },
  {
    title: '出发时间',
    dataIndex: 'startTime',
    key: 'startTime',
  },
  {
    title: '终点站',
    dataIndex: 'end',
    key: 'end',
  },
  {
    title: '终点站拼音',
    dataIndex: 'endPinyin',
    key: 'endPinyin',
  },
  {
    title: '到站时间',
    dataIndex: 'endTime',
    key: 'endTime',
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
const TRAIN_TYPE_ARRAY = [{key: "G", value: "高铁"}, {key: "D", value: "动车"}, {key: "K", value: "快速"}]

// 新增车站
const handleOk = (e) => {
  console.log(dailyTrain.value)
  axios.post("/business/admin/daily-train/save", dailyTrain.value).then((response) => {
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
  axios.get("/business/admin/daily-train/query-list", {
    params: {
      page: param.page,
      size: param.size,
      code: params.value.code,
      date: params.value.date
    }
  }).then((response) => {
    loading.value = false
    let data = response.data;
    if(data.success) {
      dailyTrains.value = data.content.list;
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
watch(() => dailyTrain.value.start, () => {
  if(dailyTrain.value.start) {
    dailyTrain.value.startPinyin = pinyin(dailyTrain.value.start, {toneType: 'none'}).replaceAll(" ", "");
  } else {
    dailyTrain.value.startPinyin = "";
  }
}, {immediate: true})
watch(() => dailyTrain.value.end, () => {
  if(dailyTrain.value.end) {
    dailyTrain.value.endPinyin = pinyin(dailyTrain.value.end, {toneType: 'none'}).replaceAll(" ", "");
  } else {
    dailyTrain.value.endPinyin = "";
  }
}, {immediate: true})

// 点击增加
const onAdd = () => {
  dailyTrain.value = {};
  visible.value = true;
}

// 点击编辑
const onEdit = (record) => {
  dailyTrain.value = JSON.parse(JSON.stringify(record));
  visible.value = true
}

// 删除
const onDelete = (record) => {
  axios.delete("/business/admin/daily-train/delete/" + record.id).then((response) => {
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

const onChangeCode = (train) => {
  let t = train
  delete t.id
  dailyTrain.value = Object.assign(dailyTrain.value, t)
}

const onClickGenDaily = () => {
  genDailyVisible.value = true
}

const handleGenDailyOk = () => {
  let date = dayjs(genDaily.value.date).format("YYYY-MM-DD");
  genDailyLoading.value = true;
  axios.get("/business/admin/daily-train/gen-daily/" + date).then((response) => {
    genDailyLoading.value = false;
    let data = response.data;
    if(data.success) {
      notification.success({description: "生成成功！"})
      genDailyVisible.value = false
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