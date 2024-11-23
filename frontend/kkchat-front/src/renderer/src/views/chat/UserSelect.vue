<template>
  <div>
    <Dialog
      :show="dialogConfig.show"
      :title="dialogConfig.title"
      :buttons="dialogConfig.buttons"
      width="660px"
      @close="dialogConfig.show = false"
    >
      <el-transfer
        v-model="formData.selectContacts"
        :titles="['全部', '已选']"
        :format="{
          noChecked: '${total}',
          hasChecked: '${checked}/${total}'
        }"
        :data="dataList"
        :props="{
          key: 'contactId',
          label: 'contactName'
        }"
        filterable
        :filter-method="search"
      >
        <template #default="{ option }">
          <div class="select-item">
            <div class="avatar">
              <AvatarBase
                :userId="option.contactId"
                :width="30"
                :borderRadius="5"
                :showDetail="false"
              >
              </AvatarBase>
            </div>
            <div class="nick-name">{{ option.contactName }}</div>
          </div>
        </template>
      </el-transfer>
    </Dialog>
  </div>
</template>

<script setup>
import AvatarBase from '@/components/AvatarBase.vue'
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()

const dialogConfig = ref({
  show: false,
  title: '选择联系人',
  buttons: [
    {
      type: 'primary',
      text: '确定',
      click: (e) => {
        submitData()
      }
    }
  ]
})

const search = (query, item) => {
  return item.contactName.toLowerCase().includes(query.toLowerCase())
}

const dataList = ref([])
const formData = ref({
  selectContacts: []
})
const show = ({ contactList, groupId, opType }) => {
  dialogConfig.value.title = opType == 0 ? '移除群员' : '添加群员'
  dialogConfig.value.show = true
  dataList.value = contactList
  formData.value = {
    selectContacts: [],
    groupId,
    opType
  }
}
defineExpose({ show })

const emit = defineEmits(['callback'])
const submitData = async () => {
  if (formData.value.selectContacts.length == 0) {
    proxy.Message.warning('请选择联系人')
    return
  }
  let params = {}
  Object.assign(params, formData.value)
  params.selectContacts = params.selectContacts.join(',')
  let result = await proxy.Request({
    url: proxy.Api.addOrRemoveGroupUser,
    params
  })
  if (!result) {
    return
  }
  dialogConfig.value.show = false
  emit('callback')
}
</script>

<style lang="scss" scoped>
.el-transfer {
  width: 100%;
  display: block !important;
  display: flex;
  :deep(.el-transfer-panel) {
    width: 280px;
  }
  :deep(.el-transfer-panel__item) {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 5px;
  }
}
:deep(.el-transfer__buttons) {
  width: 60px;
  flex-direction: column;
  text-align: center;
  padding: 0px;
  .el-transfer__button {
    text-align: center;
    margin-left: 0px;
    margin-right: 0px;
    margin-top: 5px;
    padding: 10px;
    height: 36px;
    border-radius: 50%;
  }
}

.select-item {
  display: flex;
  .avatar {
    width: 30px;
    height: 30px;
  }
  .nick-name {
    flex: 1;
    margin-left: 5px;
  }
}
</style>
