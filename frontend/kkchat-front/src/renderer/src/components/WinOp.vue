<template>
  <div class="win-op no-drag">
    <div
      v-if="showSetTop"
      :class="['iconfont icon-top', isTop ? 'win-top' : '']"
      @click="top"
      :title="isTop ? '取消置顶' : '置顶'"
    ></div>
    <div v-if="showMin" class="iconfont icon-min" @click="minimize" title="最小化"></div>
    <div
      v-if="showMax"
      :class="['iconfont', isMax ? 'icon-maximize' : 'icon-max']"
      :title="isMax ? '向下还原' : '最大化'"
      @click="maximize"
    ></div>
    <div v-if="showClose" class="iconfont icon-close" @click="close" title="关闭"></div>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, onMounted } from 'vue'
const { proxy } = getCurrentInstance()

const props = defineProps({
  showSetTop: {
    type: Boolean,
    default: true
  },
  showMin: {
    type: Boolean,
    default: true
  },
  showMax: {
    type: Boolean,
    default: true
  },
  showClose: {
    type: Boolean,
    default: true
  },
  //关闭类型 0:关闭，1:隐藏
  closeType: {
    type: Number,
    default: 1
  }
})

const emit = defineEmits(['closeCallback'])

onMounted(() => {
  isMax.value = false
})

//窗口操作
const winOp = (action, data) => {
  window.ipcRenderer.send('winTitleOp', { action, data })
}

//关闭窗口
const close = () => {
  winOp('close', { closeType: props.closeType })
  emit('closeCallback')
}
//最小化
const minimize = () => {
  winOp('minimize')
}

const isMax = ref(false)
const isTop = ref(false)

//最大化
const maximize = () => {
  if (isMax.value) {
    winOp('unmaximize')
    isMax.value = false
  } else {
    winOp('maximize')
    isMax.value = true
  }
}

//置顶窗口
const top = () => {
  isTop.value = !isTop.value
  winOp('top', { top: isTop.value })
}
</script>

<style lang="scss" scoped>
.win-op {
  top: 0px;
  right: 0px;
  position: absolute;
  z-index: 1;
  overflow: hidden;
  border-radius: 0px 3px 0px 0px;
  .iconfont {
    float: left;
    font-size: 12px;
    color: #101010;
    text-align: center;
    display: flex;
    justify-content: center;
    cursor: pointer;
    height: 25px;
    align-items: center;
    padding: 0px 10px;
    &:hover {
      background: #ddd;
    }
  }
  .icon-close {
    &:hover {
      background: #fb7373;
      color: #fff;
    }
  }
  .win-top {
    background: #ddd;
    color: #07c160;
  }
}
</style>
