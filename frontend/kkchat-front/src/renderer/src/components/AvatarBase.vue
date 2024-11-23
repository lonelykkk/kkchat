<template>
  <div class="user-avatar" :style="{ width: width + 'px', height: width + 'px', 'border-radius': borderRadius + 'px' }"
    @click="showDetailHandler">
    <ShowLocalImage :width="width" :fileId="userId" partType="avatar" :forceGet="avatarInfoStore.getFoceReload(userId)">
    </ShowLocalImage>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, onMounted } from 'vue'
const { proxy } = getCurrentInstance()
import { useAvatarInfoStore } from '@/stores/AvatarUpdateStore'
const avatarInfoStore = useAvatarInfoStore()

const props = defineProps({
  userId: {
    type: String
  },
  width: {
    type: Number,
    default: 40
  },
  borderRadius: {
    type: Number,
    default: 0
  },
  showDetail: {
    type: Boolean,
    default: false
  }
})

const showDetailHandler = () => {
  if (!props.showDetail) {
    return
  }
  //强制更新设置为false
  avatarInfoStore.setFoceReload(props.userId, false)
  //打开窗口查看,查看详情强制更新图片
  window.ipcRenderer.send('newWindow', {
    windowId: 'media',
    title: '图片查看',
    path: `/showMedia`,
    data: {
      fileList: [
        {
          fileId: props.userId,
          fileType: 0,
          partType: 'avatar',
          status: 1,
          forceGet: true
        }
      ]
    }
  })
}
</script>

<style lang="scss" scoped>
.user-avatar {
  background: #d3d3d3;
  display: flex;
  overflow: hidden;
  cursor: pointer;
  align-items: center;
  justify-content: center;
}
</style>
