<template>
  <Dialog
    :title="dialogConfig.title"
    :buttons="dialogConfig.buttons"
    :show="dialogConfig.show"
    @close="dialogConfig.show = false"
    :width="'400px'"
  >
    <el-form ref="formDataRef" :model="formData" :rules="rules" label-width="60px">
      <el-form-item label="邮箱" prop="email">
        <el-input :maxLength="50" v-model.trim="formData.email" placeholder="请输入邮箱" />
      </el-form-item>
      <el-form-item label="靓号" prop="userId">
        <el-input :maxLength="11" v-model.trim="formData.userId" placeholder="请输入靓号" />
      </el-form-item>
    </el-form>
  </Dialog>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()

const dialogConfig = ref({
  show: false,
  title: '编辑靓号',
  buttons: [
    {
      type: 'primary',
      text: '确定',
      click: (e) => {
        submitForm()
      }
    }
  ]
})

const formData = ref({ updateDescList: [] })
const formDataRef = ref()
const rules = {
  email: [
    { required: true, message: '请输入版本号' },
    { validator: proxy.Verify.email, message: '请输入正确的邮箱' }
  ],
  userId: [
    { required: true, message: '请选择更新文件' },
    { min: 11, max: 11, message: '靓号必须11位' },
    { validator: proxy.Verify.number, message: '靓号只能是数字' }
  ]
}

const emit = defineEmits(['reload'])
const submitForm = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return
    }
    let params = {}
    Object.assign(params, formData.value)
    let result = await proxy.Request({
      url: proxy.Api.saveBeautAccount,
      params
    })
    if (!result) {
      return
    }
    dialogConfig.value.show = false
    emit('reload')
  })
}

const showEdit = (data = {}) => {
  dialogConfig.value.show = true
  nextTick(() => {
    formDataRef.value.resetFields()
    formData.value = Object.assign({}, data)
  })
}
defineExpose({
  showEdit
})
</script>

<style lang="scss" scoped>
</style>
