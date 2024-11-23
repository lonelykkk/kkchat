<template>
  <ContentPanel>
    <el-form ref="formDataRef" label-width="80px" @submit.prevent>
      <!--input输入-->
      <el-form-item label="版本信息">
        <div class="version-info">
          <div>EasyChat {{ config.version }}</div>
          <div>
            <el-button type="primary" @click="checkUpdate">检查更新</el-button>
          </div>
        </div>
      </el-form-item>
    </el-form>
  </ContentPanel>

  <Update :autoUpdate="false" ref="updateRef"></Update>
</template>

<script setup>
import config from '../../../../../package.json'
import Update from '../Update.vue'
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()

const updateRef = ref()
const checkUpdate = () => {
  updateRef.value.checkUpdate()
}
</script>

<style lang="scss" scoped>
</style>
