<template>
  <a-select v-model:value="trainCode" show-search :filter-option="filterTrainCodeOption" @change="onChange" placeholder="请选择车次" :style="'width:'+locaWidth">
    <a-select-option v-for="item in trains" :key="item.code" :value="item.code" :label="item.code+item.start+item.end">
      {{item.code}} | {{item.start}} ~ {{item.end}}
    </a-select-option>
  </a-select>
</template>

<script setup>
import {onMounted, ref, watch} from "vue";
import axios from "axios";

const trainCode = ref()
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
  trainCode.value = props.modelValue
}, {immediate: true})

// 查询所有的火车车次
const trains = ref([])
const queryTrainCode = () => {
  axios.get("/business/train/query-all").then((response) => {
    let data = response.data;
    if(data.success) {
      trains.value = data.content;
    } else {
      notification.error({description: data.message});
    }
  })
}

// 车次下拉框筛选
const filterTrainCodeOption = (input, option) => {
  console.log(input, option)
  return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
}

// 将当前组件的值相应给父组件
const emit = defineEmits(["update:modelValue", "change"])
const onChange = (value) => {
  emit("update:modelValue", value)
  // 功能扩展，将子组件的时间暴露给父组件使用
  let train = trains.value.filter(item => item.code == value)[0];
  if(!train) {
    train = {}
  }
  emit('change', train)
}

onMounted(() => {
  queryTrainCode()
})
</script>

<style scoped>

</style>