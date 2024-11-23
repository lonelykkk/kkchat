<template>
  <div class="image-panel" @click="showImageHandler">
    <el-image :src="serverUrl" fit="scale-down" :width="width">
      <template #error>
        <div class="iconfont icon-image-error"></div>
      </template>
    </el-image>
    <div class="play-panel" v-if="showPlay">
      <span class="iconfont icon-video-play"></span>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, computed, onMounted } from 'vue'
const { proxy } = getCurrentInstance()
import { useGlobalInfoStore } from '@/stores/GlobalInfoStore'
const globalInfoStore = useGlobalInfoStore()

const props = defineProps({
  width: {
    type: Number,
    default: 170
  },
  height: {
    type: Number
  },
  showPlay: {
    type: Boolean,
    default: false
  },
  fileId: {
    type: [String, Number]
  },
  partType: {
    type: String,
    default: 'avatar'
  },
  fileType: {
    type: Number,
    default: 0
  },
  forceGet: {
    type: Boolean,
    default: false
  }
})

const serverUrl = computed(() => {
  if (!props.fileId) {
    return
  }
  const serverPort = globalInfoStore.getInfo('localServerPort')
  return `http://localhost:${serverPort}/file?fileId=${props.fileId}&partType=${
    props.partType
  }&fileType=${props.fileType}&showCover=true&forceGet=${props.forceGet}&${new Date().getTime()}`
})
</script>

<style lang="scss" scoped>
.image-panel {
  position: relative;
  display: flex;
  overflow: hidden;
  cursor: pointer;
  max-width: 170px;
  max-height: 170px;
  background: #ddd;
  .icon-image-error {
    margin: 0px auto;
    font-size: 30px;
    color: #838383;
  }
  .play-panel {
    z-index: 2;
    position: absolute;
    top: 0px;
    left: 0px;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    .icon-video-play {
      font-size: 35px;
      color: #fff;
    }
    &:hover {
      opacity: 0.8;
    }
  }
}
</style>