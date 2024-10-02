<template>
  <p>
    <a-space>
      <a-button type="primary" @click="handleQuery()">刷新</a-button>
      <a-button type="primary" @click="onAdd">新增</a-button>
    </a-space>
  </p>
  <a-table :dataSource="trains" :columns="columns" :pagination="pagination" @change="handleTableChange" :loading="loading">
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
    <a-form :model="train" :label-col="{ span: 8 }" :wrapper-col="{ span: 16 }">
      <a-form-item label="车次编号">
        <a-input v-model:value="train.code" />
      </a-form-item>
      <a-form-item label="车次类型">
        <a-select v-model:value="train.type">
          <a-select-option v-for="item in TRAIN_TYPE_ARRAY" :key="item.key" :value="item.key">{{item.value}}</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="始发站">
        <a-input v-model:value="train.start" />
      </a-form-item>
      <a-form-item label="始发站拼音">
        <a-input v-model:value="train.startPinyin" disabled/>
      </a-form-item>
      <a-form-item label="出发时间">
        <a-time-picker v-model:value="train.startTime" value-format="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
      <a-form-item label="终点站">
        <a-input v-model:value="train.end" />
      </a-form-item>
      <a-form-item label="终点站拼音">
        <a-input v-model:value="train.endPinyin" disabled/>
      </a-form-item>
      <a-form-item label="到站时间">
        <a-time-picker v-model:value="train.endTime" value-format="HH:mm:ss" placeholder="请选择时间"/>
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script setup>
import {onMounted, ref, watch} from "vue";
import axios from "axios";
import {notification} from "ant-design-vue";
import {pinyin} from "pinyin-pro";

const visible = ref(false);
const train = ref({
  id: undefined,
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

const trains = ref([])
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
  console.log(train.value)
  axios.post("/business/admin/train/save", train.value).then((response) => {
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
  axios.get("/business/admin/train/query-list", {
    params: {
      page: param.page,
      size: param.size
    }
  }).then((response) => {
    loading.value = false
    let data = response.data;
    if(data.success) {
      trains.value = data.content.list;
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

// 汉字转拼音，自动填入
watch(() => train.value.start, () => {
  if(train.value.start) {
    train.value.startPinyin = pinyin(train.value.start, {toneType: 'none'}).replaceAll(" ", "");
  } else {
    train.value.startPinyin = "";
  }
}, {immediate: true})
watch(() => train.value.end, () => {
  if(train.value.end) {
    train.value.endPinyin = pinyin(train.value.end, {toneType: 'none'}).replaceAll(" ", "");
  } else {
    train.value.endPinyin = "";
  }
}, {immediate: true})

// 点击增加
const onAdd = () => {
  train.value = {};
  visible.value = true;
}

// 点击编辑
const onEdit = (record) => {
  train.value = JSON.parse(JSON.stringify(record));
  visible.value = true
}

// 删除
const onDelete = (record) => {
  axios.delete("/business/admin/train/delete/" + record.id).then((response) => {
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