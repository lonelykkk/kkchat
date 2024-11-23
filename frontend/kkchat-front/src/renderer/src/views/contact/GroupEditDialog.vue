<template>
  <Dialog
    :show="dialogConfig.show"
    :title="dialogConfig.title"
    :buttons="dialogConfig.buttons"
    width="400px"
    :showCancel="false"
    @close="dialogConfig.show = false"
  >
    <GroupEditForm ref="groupEditRef" @eidtBack="eidtBack"></GroupEditForm>
  </Dialog>
</template>

<script setup>
import GroupEditForm from './GroupEditForm.vue'
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()

const dialogConfig = ref({
  show: false,
  title: '修改群组',
  buttons: []
})

const groupEditRef = ref()
const show = (data) => {
  dialogConfig.value.show = true
  nextTick(() => {
    groupEditRef.value.show(data)
  })
}

const emit = defineEmits(['reloadGroupInfo'])
const eidtBack = () => {
  dialogConfig.value.show = false
  emit('reloadGroupInfo')
}

defineExpose({
  show
})
</script>

<style lang="scss" scoped>
</style>
