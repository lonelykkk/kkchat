<template>
  <div>
    <Dialog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      width="400px"
      :showCancel="true"
      @close="dialogConfig.show = false"
    >
      <el-form :model="formData" ref="formDataRef" @submit.prevent>
        <el-form-item label="" prop="applyInfo">
          <el-input
            type="textarea"
            :rows="5"
            clearable
            placeholder="输入申请信息，更容易被通过"
            v-model.trim="formData.applyInfo"
            resize="none"
            show-word-limit
            maxlength="100"
          ></el-input>
        </el-form-item>
      </el-form>
    </Dialog>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()
import { useUserInfoStore } from '@/stores/UserInfoStore'
const userInfoStore = useUserInfoStore()
import { useContactStateStore } from '@/stores/ContactStateStore'
const contactStateStore = useContactStateStore()

const dialogConfig = ref({
  show: false,
  title: '提交申请',
  buttons: [
    {
      type: 'primary',
      text: '确定',
      click: (e) => {
        submitApply()
      }
    }
  ]
})

const formData = ref({})
const formDataRef = ref()

const show = (data) => {
  dialogConfig.value.show = true
  nextTick(() => {
    formDataRef.value.resetFields()
    formData.value = Object.assign({}, data)
    formData.value.applyInfo = '我是' + userInfoStore.getInfo().nickName
  })
}

defineExpose({ show })

const emit = defineEmits(['reload'])
const submitApply = async () => {
  contactStateStore.setContactReload(null)
  const { contactId, contactType, applyInfo } = formData.value
  let result = await proxy.Request({
    url: proxy.Api.applyAdd,
    params: {
      contactId,
      applyInfo,
      contactType
    }
  })
  if (!result) {
    return
  }
  if (result.data == 0) {
    proxy.Message.success('加入成功')
  } else {
    proxy.Message.success('申请成功，等待对方同意')
  }

  dialogConfig.value.show = false
  emit('reload')
  //直接加入刷新联系人列表
  if (result.data == 0) {
    contactStateStore.setContactReload(contactType)
  }
}
</script>

<style lang="scss" scoped>
</style>
