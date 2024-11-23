<template>
  <ContentPanel
    v-infinite-scroll="loadApply"
    :infinite-scroll-immediate="false"
    :showTopBorder="true"
  >
    <div>
      <div class="apply-item" v-for="item in applyList">
        <div :class="['contact-type', item.contactType == 0 ? 'user-contact' : '']">
          {{ item.contactType == 0 ? '好友' : '群聊' }}
        </div>
        <Avatar
          :width="50"
          :userId="item.applyUserId"
          :lastUpdateTime="item.iconLastUpdateTime"
        ></Avatar>
        <div class="contact-info">
          <div class="nick-name">{{ item.contactName }}</div>
          <div class="apply-info">{{ item.applyInfo }}</div>
        </div>
        <div class="op-btn">
          <div v-if="item.status == 0">
            <el-dropdown placement="bottom-end" trigger="click">
              <span class="el-dropdown-link">
                <el-button type="primary" size="small">接受</el-button>
              </span>
              <template #dropdown>
                <el-dropdown-item @click="dealWithApply(item.applyId, item.contactType, 1)"
                  >同意</el-dropdown-item
                >
                <el-dropdown-item @click="dealWithApply(item.applyId, item.contactType, 2)"
                  >拒绝</el-dropdown-item
                >
                <el-dropdown-item @click="dealWithApply(item.applyId, item.contactType, 3)"
                  >拉黑</el-dropdown-item
                >
              </template>
            </el-dropdown>
          </div>
          <div v-else class="result-name">{{ item.statusName }}</div>
        </div>
      </div>
    </div>
    <div v-if="applyList.length == 0" class="no-data">暂无申请</div>
  </ContentPanel>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, watch } from 'vue'
const { proxy } = getCurrentInstance()
import { useContactStateStore } from '@/stores/ContactStateStore'
const contactStateStore = useContactStateStore()

import { useMessageCountStore } from '@/stores/MessageCountStore'
const messageCountStore = useMessageCountStore()

const applyList = ref([])
let pageNo = 0
let pageTotal = 1
const loadApply = async () => {
  pageNo++
  if (pageNo > pageTotal) {
    return
  }
  let result = await proxy.Request({
    url: proxy.Api.loadApply,
    params: {
      pageNo: pageNo
    }
  })
  if (!result) {
    return
  }
  pageTotal = result.data.pageTotal
  if (result.data.pageNo == 1) {
    applyList.value = []
  }
  applyList.value = applyList.value.concat(result.data.list)
  pageNo = result.data.pageNo
}

loadApply()

const dealWithApply = (applyId, contactType, status) => {
  contactStateStore.setContactReload(null)
  proxy.Confirm({
    message: '确定要执行操作吗?',
    okfun: async () => {
      let result = await proxy.Request({
        url: proxy.Api.dealWithApply,
        params: {
          applyId,
          status
        }
      })
      if (!result) {
        return
      }
      //初始化页面
      pageNo = 0
      loadApply()
      //如果是用户
      if (contactType == 0 && status == 1) {
        //审核通过，自己的好友要刷新
        contactStateStore.setContactReload('USER')
      } else if (contactType == 1 && status == 1) {
        contactStateStore.setContactReload('GROUP')
      }
    }
  })
}

watch(
  () => messageCountStore.messageCount.contactApplyCount,
  (newVal, oldVal) => {
    if (newVal) {
      console.log(newVal)
      pageNo = 0
      loadApply()
    }
  },
  { immediate: true, deep: true }
)
</script>

<style lang="scss" scoped>
.apply-item {
  display: flex;
  align-items: center;
  border-bottom: 1px solid #ddd;
  padding: 10px 0px;
  .contact-type {
    display: flex;
    justify-content: center;
    writing-mode: vertical-rl;
    vertical-align: middle;
    background: #2cb6fe;
    color: #fff;
    border-radius: 5px 0px 0px 5px;
    height: 50px;
  }
  .user-contact {
    background: #08bf61;
  }
  .contact-info {
    width: 260px;
    margin-left: 10px;
    .nick-name {
      color: #000000;
    }
    .apply-info {
      color: #999999;
      font-size: 12px;
      margin-top: 5px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
  .op-btn {
    width: 50px;
    text-align: center;
    .result-name {
      color: #999999;
      font-size: 12px;
    }
  }
}
</style>
