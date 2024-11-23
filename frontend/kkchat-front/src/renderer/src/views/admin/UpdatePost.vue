<template>
  <Dialog
    :title="dialogConfig.title"
    :buttons="dialogConfig.buttons"
    :show="dialogConfig.show"
    @close="dialogConfig.show = false"
    :width="'600px'"
  >
    <el-form ref="formDataRef" :model="formData" :rules="rules" label-width="100px" @submit.prevent>
      <el-form-item label="版本号">
        {{ formData.version }}
      </el-form-item>
      <el-form-item label="发布状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio :label="0">取消发布</el-radio>
          <el-radio :label="1">灰度发布</el-radio>
          <el-radio :label="2">全网发布</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="灰度UID" prop="grayscaleUid" v-if="formData.status == 1">
        <div class="tag-panel">
          <el-tag
            v-for="(tag, index) in formData.grayscaleUid"
            :key="tag"
            closable
            @close="closeTag(index)"
            :type="tag.type"
            class="tag"
          >
            {{ tag }}
          </el-tag>
          <div class="tag input" v-if="showInput">
            <el-input
              size="small"
              clearable
              placeholder="请输入UID"
              v-model.trim="tagInput"
              @blur="addDeviceId"
              @keyup.enter="addDeviceId"
            ></el-input>
          </div>
          <div class="tag" v-if="!showInput">
            <el-button type="primary" size="small" @click="showInputHandler">新增</el-button>
          </div>
        </div>
      </el-form-item>
    </el-form>
  </Dialog>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()

const dialogConfig = ref({
  show: false,
  title: '发布更新',
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

const formData = ref({})
const formDataRef = ref()
const rules = {
  status: [{ required: true, message: '请选择发布状态' }]
}

const emit = defineEmits(['reload'])
const submitForm = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return
    }
    let params = {}
    Object.assign(params, formData.value)
    params.grayscaleUid = params.grayscaleUid.join(',')
    let result = await proxy.Request({
      url: proxy.Api.postUpdate,
      params
    })
    if (!result) {
      return
    }
    dialogConfig.value.show = false
    emit('reload')
  })
}

const showEdit = (data) => {
  dialogConfig.value.show = true
  nextTick(() => {
    formDataRef.value.resetFields()
    formData.value = Object.assign({
      id: data.id,
      version: data.version,
      status: data.status,
      grayscaleUid: data.grayscaleUid ? data.grayscaleUid.split(',') : []
    })
  })
}

const showInput = ref(false)
const tagInput = ref()
const addDeviceId = () => {
  if (tagInput.value) {
    formData.value.grayscaleUid.push(tagInput.value)
  }
  tagInput.value = ''
  showInput.value = false
}

const showInputHandler = () => {
  showInput.value = true
}

const closeTag = (index) => {
  formData.value.grayscaleUid.splice(index, 1)
}

defineExpose({
  showEdit
})
</script>

<style lang="scss" scoped>
.tag-panel {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  .tag {
    margin: 0px 5px 5px 0px;
  }
  .input {
    width: 150px;
  }
}
</style>
