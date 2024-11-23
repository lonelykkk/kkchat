<template>
  <div class="media-window">
    <div class="win-title drag"></div>
    <div class="media-op no-drag">
      <div
        :class="['iconfont icon-left', currentIndex == 0 ? 'not-allow' : '']"
        @dblclick.stop
        title="上一张"
        @click="next(-1)"
      ></div>
      <div
        :class="['iconfont icon-right', currentIndex >= allFileList.length - 1 ? 'not-allow' : '']"
        @dblclick.stop
        title="下一张"
        @click="next(1)"
      ></div>
      <template v-if="fileList[0].fileType == 0">
        <el-divider direction="vertical" />
        <div
          class="iconfont icon-enlarge"
          @click.stop="changeSize(0.1)"
          @dblclick.stop
          title="放大"
        ></div>
        <div
          class="iconfont icon-narrow"
          @click="changeSize(-0.1)"
          @dblclick.stop
          title="缩小"
        ></div>
        <div
          :class="['iconfont', isOne2One ? 'icon-resize' : 'icon-source-size']"
          @dblclick.stop
          @click="resize"
          :title="isOne2One ? '图片适应窗口大小' : '图片原始大小'"
        ></div>
        <div class="iconfont icon-rotate" @dblclick.stop @click="rotate" title="旋转"></div>
        <el-divider direction="vertical" />
      </template>
      <div class="iconfont icon-download" @dblclick.stop @click="saveAs" title="另存为..."></div>
    </div>
    <div class="media-panel">
      <viewer
        :options="options"
        @inited="inited"
        :images="fileList"
        v-if="fileList[0].fileType == 0 && fileList[0].status == 1"
      >
        <img :src="fileList[0].url" />
      </viewer>
      <div
        ref="player"
        id="player"
        v-show="fileList[0].fileType == 1 && fileList[0].status == 1"
        style="width: 100%; height: 100%"
      ></div>
      <div v-if="fileList[0].fileType == 2" class="file-panel">
        <div class="file-item">文件名：{{ fileList[0].fileName }}</div>
        <div class="file-item">文件大小：{{ Utils.size2Str(fileList[0].fileSize) }}</div>
        <div class="file-item donwload">
          <el-button type="primary" @click="saveAs">下载文件</el-button>
        </div>
      </div>
      <div class="loading" v-if="fileList[0].status != 1">加载中....</div>
    </div>
  </div>
  <WinOp @closeCallback="closeWin"></WinOp>
</template>

<script setup>
import DPlayer from 'dplayer'
import 'viewerjs/dist/viewer.css'
import { component as Viewer } from 'v-viewer'
import { ref, reactive, getCurrentInstance, nextTick, computed, onMounted, onUnmounted } from 'vue'
const { proxy } = getCurrentInstance()
import { useRouter } from 'vue-router'
const router = useRouter()
import { useGlobalInfoStore } from '@/stores/GlobalInfoStore'
const globalInfoStore = useGlobalInfoStore()

const options = ref({
  inline: true,
  toolbar: false,
  navbar: false,
  button: false,
  title: false,
  zoomRatio: 0.1,
  zoomOnWheel: false
})

const viewerMy = ref(null)
const inited = (e) => {
  viewerMy.value = e
}

//放大缩小
const changeSize = (zoomRatio) => {
  viewerMy.value.zoom(zoomRatio, true)
}
//旋转
const rotate = () => {
  viewerMy.value.rotate(90, true)
}
//原始大小
const isOne2One = ref(false)
const resize = () => {
  isOne2One.value = !isOne2One.value
  if (!isOne2One.value) {
    viewerMy.value.zoomTo(viewerMy.value.initialImageData.ratio, true)
  } else {
    viewerMy.value.zoomTo(1, true)
  }
}

const onWheel = (e) => {
  if (fileList.value[0].fileType !== 0) {
    return
  }
  if (e.deltaY < 0) {
    changeSize(0.1)
  } else {
    changeSize(-0.1)
  }
}

//初始化视频播放器
const player = ref()
const dPlayer = ref()
const initPlayer = () => {
  dPlayer.value = new DPlayer({
    element: player.value,
    theme: '#b7daff',
    screenshot: true,
    video: {
      url: ''
    }
  })
}

const localServerPort = ref()
const currentIndex = ref(0)
const allFileList = ref([])
const fileList = ref([{ fileType: 0, status: 0 }])

onMounted(() => {
  initPlayer()
  window.addEventListener('wheel', onWheel)

  window.ipcRenderer.on('pageInitData', (e, data) => {
    localServerPort.value = data.localServerPort
    allFileList.value = data.fileList
    let index = 0
    if (data.currentFileId) {
      index = data.fileList.findIndex((item) => item.fileId === data.currentFileId)
      index = index == -1 ? 0 : index
    }
    currentIndex.value = index
    //设置当前文件信息
    getCurrentFile()
  })
})
onUnmounted(() => {
  window.ipcRenderer.removeAllListeners('pageInitData')
  window.ipcRenderer.removeAllListeners('checkFileCallback')
})
const getCurrentFile = () => {
  if (dPlayer.value) {
    dPlayer.value.pause()
  }
  const curFile = allFileList.value[currentIndex.value]
  const url = getUrl(curFile)

  fileList.value.splice(0, 1, {
    url: url,
    fileType: curFile.fileType,
    status: 1,
    fileSize: curFile.fileSize,
    fileName: curFile.fileName
  })
  if (curFile.fileType == 1) {
    dPlayer.value.switchVideo({
      url: url
    })
  }
}

//上一个下一个
const next = (index) => {
  if (currentIndex.value + index < 0 || currentIndex.value + index >= allFileList.value.length) {
    return
  }
  currentIndex.value = currentIndex.value + index
  getCurrentFile()
}

const getUrl = (curFile) => {
  return `http://localhost:${localServerPort.value}/file?fileId=${curFile.fileId}&partType=${
    curFile.partType
  }&fileType=${curFile.fileType}&forceGet=${curFile.forceGet}&${new Date().getTime()}`
}

onUnmounted(() => {
  window.removeEventListener('wheel', onWheel)
})

const saveAs = () => {
  const curFile = allFileList.value[currentIndex.value]
  window.ipcRenderer.send('saveAs', {
    partType: curFile.partType,
    fileId: curFile.fileId
  })
}
//关闭窗口 只是隐藏了窗口，播放的媒体还在播放，要清除
const closeWin = () => {
  dPlayer.value.pause()
}
</script>
<style lang="scss" scoped>
.media-window {
  padding: 0px;
  height: calc(100vh);
  border: 1px solid #ddd;
  background: #fff;
  position: relative;
  overflow: hidden;
  .win-title {
    height: 37px;
  }
  .media-op {
    position: absolute;
    left: 0px;
    top: 0px;
    height: 35px;
    line-height: 35px;
    display: flex;
    align-items: center;
    .iconfont {
      font-size: 18px;
      padding: 0px 10px;
      &:hover {
        background: #f3f3f3;
        cursor: pointer;
      }
    }
    .not-allow {
      cursor: not-allowed;
      color: #ddd;
      text-decoration: none;
      &:hover {
        color: #ddd;
        cursor: not-allowed;
        background: none;
      }
    }
  }
  .media-panel {
    height: calc(100vh - 37px);
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    :deep(.viewer-backdrop) {
      background: #f5f5f5;
    }

    .file-panel {
      .file-item {
        margin-top: 5px;
      }
      .donwload {
        margin-top: 20px;
        text-align: center;
      }
    }
  }
}
</style>
