<template>
  <Dialog
    :title="dialogConfig.title"
    :buttons="dialogConfig.buttons"
    :show="dialogConfig.show"
    @close="dialogConfig.show = false"
    :width="'500px'"
  >
    <el-form ref="formDataRef" :model="formData" :rules="rules" label-width="100px">
      <el-form-item label="版本号" prop="version">
        <el-input :maxLength="10" v-model="formData.version" placeholder="eg:1.0.0" />
      </el-form-item>
      <el-form-item label="文件类型" prop="fileType">
        <el-radio-group v-model="formData.fileType">
          <el-radio :label="0">本地文件</el-radio>
          <el-radio :label="1">外链</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="文件" prop="fileName" class="file-select" v-if="formData.fileType == 0">
        <div class="file-name">{{ formData.fileName }}</div>
        <el-upload
          name="file"
          :show-file-list="false"
          accept=".exe"
          :multiple="false"
          :http-request="selectFile"
        >
          <el-button type="primary" size="small">选择文件</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="外链地址" prop="outerLink" v-if="formData.fileType == 1">
        <el-input
          :maxLength="200"
          v-model="formData.outerLink"
          placeholder="请输入完整的外链地址"
        />
      </el-form-item>
      <el-form-item label="更新内容" class="updte-form-item">
        <div class="update-desc-item" v-for="(item, index) in formData.updateDescList">
          <el-form-item
            :prop="'updateDescList.' + index + '.title'"
            :rules="{
              required: true,
              message: '更新内容不能为空'
            }"
          >
            <div class="update-desc">
              <div class="num">{{ index + 1 }}</div>
              <div class="input">
                <el-input :maxLength="11" v-model="item.title" placeholder="请输入更新内容" />
              </div>
              <div class="iconfont icon-add" v-if="index == 0" @click="addLine"></div>
              <div class="iconfont btn-del" v-if="index > 0" @click="delLie(index)">删除</div>
            </div>
          </el-form-item>
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

const formData = ref({ updateDescList: [] })
const formDataRef = ref()
const rules = {
  version: [
    { required: true, message: '请输入版本号' },
    { validator: proxy.Verify.version, message: '版本号只能是数字和点' }
  ],
  fileType: [{ required: true, message: '请选择文件类型' }],
  fileName: [{ required: true, message: '请选择更新文件' }],
  outerLink: [{ required: true, message: '请输入外链地址' }],
  updateType: [{ required: true, message: '请选择更新类型' }]
}

const selectFile = (file) => {
  file = file.file
  formData.value.file = file
  formData.value.fileName = file.name
}

const addLine = () => {
  formData.value.updateDescList.push({
    title: ''
  })
}

const delLie = (index) => {
  formData.value.updateDescList.splice(index, 1)
}

const emit = defineEmits(['reload'])
const submitForm = () => {
  formDataRef.value.validate(async (valid) => {
    if (!valid) {
      return
    }
    let params = {}
    Object.assign(params, formData.value)
    const updateDescArray = params.updateDescList.map((item) => {
      return item.title
    })
    params.updateDesc = updateDescArray.join('|')
    delete params.updateDescList
    let result = await proxy.Request({
      url: proxy.Api.saveUpdate,
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
    if (data) {
      data.updateDescList = data.updateDescArray.map((item) => {
        return { title: item }
      })
      data.fileName = 'EasyChat.' + data.version + '.exe'
    }
    formData.value = Object.assign(
      {},
      data || {
        updateDescList: [{ title: '' }]
      }
    )
  })
}

defineExpose({
  showEdit
})
</script>

<style lang="scss" scoped>
.file-select {
  display: flex;
  .file-name {
    color: #409eff;
    margin-right: 10px;
  }
}
.updte-form-item {
  margin-bottom: 0px;
  .update-desc-item {
    width: 100%;
    margin-bottom: 15px;
    .update-desc {
      width: 100%;
      display: flex;
      .num {
        width: 15px;
        margin-right: 2px;
      }
      .input {
        flex: 1;
      }
      .iconfont {
        cursor: pointer;
        margin-left: 10px;
        text-align: right;
      }
    }
  }
}

.btn-del {
  font-size: 13px;
  color: red;
}
</style>
