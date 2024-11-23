<template>
  <div>
    <el-form :model="formData" :rules="rules" ref="formDataRef" label-width="80px" @submit.prevent>
      <el-form-item label="密码" prop="password">
        <el-input
          type="password"
          clearable
          placeholder="请输入新密码"
          v-model.trim="formData.password"
          show-password
        ></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="rePassword">
        <el-input
          type="password"
          clearable
          placeholder="请再次输入新密码"
          v-model.trim="formData.rePassword"
          show-password
        ></el-input>
      </el-form-item>
      <!--input输入-->
      <el-form-item>
        <el-button type="primary" @click="saveUserInfo">修改密码</el-button>
        <el-button link @click="cancel">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import AreaSelect from '@/components/AreaSelect.vue'
import { ref, reactive, getCurrentInstance, nextTick, computed } from 'vue'
const { proxy } = getCurrentInstance()
import { useRouter } from 'vue-router'
const router = useRouter()

const formData = ref({})
const formDataRef = ref()

const validateRePass = (rule, value, callback) => {
  if (value !== formData.value.password) {
    callback(new Error(rule.message))
  } else {
    callback()
  }
}

const rules = {
  password: [
    { required: true, message: '请输入新密码' },
    { validator: proxy.Verify.password, message: '密码只能是数字、字母、特殊字符8~18位' }
  ],
  rePassword: [
    { required: true, message: '请再次输入密码' },
    {
      validator: validateRePass,
      message: '两次输入的密码不一致'
    }
  ]
}

const emit = defineEmits(['editBack'])
const saveUserInfo = () => {
  formDataRef.value.validate((valid) => {
    if (!valid) {
      return
    }
    proxy.Confirm({
      message: '修改密码后将退出登录，需重新登录，确认要修改吗?',
      okfun: async () => {
        let params = {}
        Object.assign(params, formData.value)
        let result = await proxy.Request({
          url: proxy.Api.updatePassword,
          params
        })
        if (!result) {
          return
        }
        proxy.Message.success('修改成功请重新登录', () => {
          window.ipcRenderer.send('reLogin')
        })
      }
    })
  })
}

const cancel = () => {
  emit('editBack')
}
</script>

<style lang="scss" scoped>
.info {
  margin-left: 5px;
  color: #949494;
  font-size: 12px;
}
</style>
