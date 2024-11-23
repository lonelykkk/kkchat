<template>
  <div class="avatar-upload">
    <div class="avatar-show">
      <template v-if="modelValue">
        <el-image v-if="preview" :src="localFile" fit="scale-down"></el-image>
        <ShowLocalImage
          :fileId="props.modelValue"
          partType="avatar"
          :width="40"
          v-else
        ></ShowLocalImage>
      </template>
      <template v-else>
        <el-upload
          name="file"
          :show-file-list="false"
          accept=".png,.PNG,.jpg,.JPG,.jpeg,.JPEG,.gif,.GIF,.bmp,.BMP"
          :multiple="false"
          :http-request="uploadImage"
        >
          <span class="iconfont icon-add"></span>
        </el-upload>
      </template>
    </div>
    <div class="select-btn">
      <el-upload
        name="file"
        :show-file-list="false"
        accept=".png,.PNG,.jpg,.JPG,.jpeg,.JPEG,.gif,.GIF,.bmp,.BMP"
        :multiple="false"
        :http-request="uploadImage"
      >
        <el-button type="primary" size="small">选择</el-button>
      </el-upload>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, computed, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
const { proxy } = getCurrentInstance()
const router = useRouter()
const route = useRoute()

const timestamp = ref('')

const props = defineProps({
  modelValue: {
    type: [String, Object],
    default: null
  }
})

const preview = computed(() => {
  return props.modelValue instanceof File
})

const localFile = ref(null)
const emit = defineEmits(['coverFile'])
const uploadImage = async (file) => {
  file = file.file
  window.ipcRenderer.send('createCover', file.path)
}

const clear = () => {
  localFile.value = null
}

defineExpose({
  clear
})

onMounted(() => {
  window.ipcRenderer.on('createCoverCallback', (e, { avatarStream, coverStream }) => {
    const coverBlob = new Blob([coverStream], { type: 'image/png' })
    const coverFile = new File([coverBlob], 'thumbnail.jpg')

    let img = new FileReader()
    img.readAsDataURL(coverFile)
    img.onload = ({ target }) => {
      localFile.value = target.result
    }
    const avatarBlob = new Blob([avatarStream], { type: 'image/png' })
    const avatarFile = new File([avatarBlob], 'thumbnai2.jpg')

    emit('coverFile', { avatarFile, coverFile })
  })
})

onUnmounted(() => {
  window.ipcRenderer.removeAllListeners('createCoverCallback')
})
</script>

<style lang="scss">
.avatar-upload {
  display: flex;
  justify-content: center;
  align-items: end;
  line-height: normal;
  .avatar-show {
    background: #ededed;
    width: 60px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
    position: relative;
    .icon-add {
      font-size: 30px;
      color: #b9b9b9;
      width: 60px;
      height: 60px;
      text-align: center;
      line-height: 60px;
    }
    img {
      width: 100%;
      height: 100%;
    }
    .op {
      position: absolute;
      color: #0e8aef;
      top: 80px;
    }
  }
  .select-btn {
    vertical-align: bottom;
    margin-left: 5px;
  }
}
</style>