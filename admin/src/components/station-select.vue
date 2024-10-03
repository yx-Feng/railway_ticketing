<template>
  <a-select v-model:value="name" show-search :filter-option="filterNameOption" @change="onchange" placeholder="请选择车站" :style="'width:'+locaWidth">
    <a-select-option v-for="item in stations" :key="item.name" :value="item.name" :label="item.name+item.namePinyin+item.namePy">
      {{item.name}} | {{item.namePinyin}} ~ {{item.namePy}}
    </a-select-option>
  </a-select>
</template>

<script setup>
import {onMounted, ref, watch} from "vue";
import axios from "axios";

const name = ref()
const props = defineProps({
  modelValue: String,
  width: String
});
const locaWidth = ref(props.width) // 接受父组件传过来的下拉框宽度
if(!locaWidth) {
  locaWidth.value = "100%"
}

// 使用watch动态获取父组件的值，如果放在onMounted或其它方法里，只有第一次有效
watch(() => props.modelValue, () => {
  name.value = props.modelValue
}, {immediate: true})

// 查询所有的火车车站
const stations = ref([])
const queryAllStation = () => {
  axios.get("/business/admin/station/query-all").then((response) => {
    let data = response.data;
    if(data.success) {
      stations.value = data.content;
    } else {
      notification.error({description: data.message});
    }
  })
}

// 车站下拉框筛选
const filterNameOption = (input, option) => {
  console.log(input, option)
  return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
}

// 将当前组件的值相应给父组件
const emit = defineEmits(["update:modelValue", "change"])
const onchange = (value) => {
  emit("update:modelValue", value)
  // 功能扩展，将子组件的时间暴露给父组件使用
  let station = stations.value.filter(item => item.code == value)[0];
  if(!station) {
    station = {}
  }
  emit('change', station)
}

onMounted(() => {
  queryAllStation()
})
</script>

<style scoped>

</style>