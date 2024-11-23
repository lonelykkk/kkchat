<template>
  <ChatMessageTime :data="data"></ChatMessageTime>
  <div class="sys-message">{{ dataMessage.messageContent }}</div>
</template>

<script setup>
import ChatMessageTime from './ChatMessageTime.vue'
import { ref, reactive, getCurrentInstance, nextTick, computed } from 'vue'
const { proxy } = getCurrentInstance()

const props = defineProps({
  data: {
    type: Object,
    default: {}
  }
})

const dataMessage = computed(() => {
  const data = { ...props.data }
  if (data.messageType == 1) {
    data.messageContent = '已经添加为好友，以下是打招呼的信息'
  }
  return data
})
</script>

<style lang="scss" scoped>
.sys-message {
  text-align: center;
  font-size: 12px;
  color: #999999;
  margin-bottom: 20px;
}
</style>
