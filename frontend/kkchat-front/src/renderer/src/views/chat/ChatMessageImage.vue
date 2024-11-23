<template>
  <div class="image-panel">
    <ShowLocalImage
      :fileId="data.messageId"
      partType="chat"
      :fileType="data.fileType"
    ></ShowLocalImage>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()
import { useGlobalInfoStore } from '@/stores/GlobalInfoStore'
const globalInfoStore = useGlobalInfoStore()

const props = defineProps({
  data: {
    type: Object,
    default: {}
  }
})
</script>

<style lang="scss" scoped>
</style>
