<template>
  <Layout>
    <template #left-content>
      <div class="drag-panel drag"></div>
      <div class="top-search">
        <el-input placeholder="搜索" v-model="searchKey" size="small" @keyup="search">
          <template #suffix>
            <span class="iconfont icon-search"></span>
          </template>
        </el-input>
      </div>
      <div class="chat-session-list" v-show="!searchKey">
        <template v-for="data in chatSessionList">
          <ChatSession
            @click="chatSessionClickHandler(data)"
            :data="data"
            :currentSession="data.contactId == currentChatSession.contactId"
            @contextmenu.stop="onContextMenu(data, $event)"
          ></ChatSession>
        </template>
      </div>
      <div class="search-list" v-show="searchKey">
        <SearchResult
          @click="searchClickHandler(item)"
          :data="item"
          v-for="item in searchList"
        ></SearchResult>
      </div>
    </template>
    <template #right-content>
      <div class="title-panel drag" v-if="Object.keys(currentChatSession).length > 0">
        <div class="title">
          <span>{{ currentChatSession.contactName }}</span>
          <span v-if="currentChatSession.contactType == 1"
            >({{ currentChatSession.memberCount }})</span
          >
        </div>
      </div>
      <div
        v-if="currentChatSession.contactType == 1"
        class="iconfont icon-more no-drag"
        @click="showGroupDetail"
      ></div>

      <div class="chat-panel" v-show="Object.keys(currentChatSession).length > 0">
        <div class="message-panel" id="message-panel">
          <div
            class="message-item"
            v-for="(data, index) in messageList"
            :id="'message' + data.messageId"
          >
            <template
              v-if="
                index > 1 &&
                data.sendTime - messageList[index - 1].sendTime >= 300000 &&
                (data.messageType == 2 || data.messageType == 5)
              "
            >
              <ChatMessageTime :data="data"></ChatMessageTime>
            </template>
            <template
              v-if="
                data.messageType == 3 ||
                data.messageType == 1 ||
                data.messageType == 9 ||
                data.messageType == 8 ||
                data.messageType == 11 ||
                data.messageType == 12
              "
            >
              <ChatMessageSys :data="data"></ChatMessageSys>
            </template>
            <template
              v-if="data.messageType == 1 || data.messageType == 2 || data.messageType == 5"
            >
              <ChatMessage
                :data="data"
                :currentChatSession="currentChatSession"
                @showMediaDetail="showMediaDetailHandler"
              ></ChatMessage>
            </template>
          </div>
        </div>
        <MessageSend
          ref="messageSendRef"
          :currentChatSession="currentChatSession"
          @sendMessage4Local="sendMessage4LocalHandler"
        >
        </MessageSend>
      </div>
      <div class="chat-blank" v-show="Object.keys(currentChatSession).length == 0">
        <Blank></Blank>
      </div>
    </template>
  </Layout>
  <ChatGroupDetail
    ref="chatGroupDetailRef"
    @delChatSessionCallback="delChatSession"
  ></ChatGroupDetail>
</template>
<script>
export default {
  name: 'chat'
}
</script>
<script setup>
import ContextMenu from '@imengyu/vue3-context-menu'
import '@imengyu/vue3-context-menu/lib/vue3-context-menu.css'

import SearchResult from './SearchResult.vue'
import ChatGroupDetail from './ChatGroupDetail.vue'
import { getFileType } from '@/utils/Constants.js'
import Blank from '@/components/Blank.vue'
import ChatSession from './ChatSession.vue'
import ChatMessage from './ChatMessage.vue'
import ChatMessageTime from './ChatMessageTime.vue'
import ChatMessageSys from './ChatMessageSys.vue'
import MessageSend from './MessageSend.vue'
import { ref, reactive, getCurrentInstance, nextTick, onMounted, watch, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
const route = useRoute()

const { proxy } = getCurrentInstance()
import { useUserInfoStore } from '@/stores/UserInfoStore'
const userInfoStore = useUserInfoStore()
//消息数
import { useMessageCountStore } from '@/stores/MessageCountStore'
const messageCountStore = useMessageCountStore()

import { useContactStateStore } from '@/stores/ContactStateStore'
const contactStateStore = useContactStateStore()

//会话列表
const chatSessionList = ref([])
//加载会话信息
const loadChatSession = () => {
  window.ipcRenderer.send('loadSessionData')
}

//会话排序
const sortChatSessionList = (dataList) => {
  dataList.sort((a, b) => {
    const topTypeResult = b['topType'] - a['topType']
    if (topTypeResult == 0) {
      return b['lastReceiveTime'] - a['lastReceiveTime']
    }
    return topTypeResult
  })
}

const delChatSessionList = (contactId) => {
  setTimeout(() => {
    chatSessionList.value = chatSessionList.value.filter((item) => {
      return item.contactId !== contactId
    })
  }, 100)
}

//当前会话
const currentChatSession = ref({})
const messageCountInfo = {
  totalPage: 0,
  pageNo: 0,
  maxMessageId: null,
  noData: false
}

//消息列表
const messageList = ref([])
//是否自动滚动到底部
let distanceBottom = 0
const messageSendRef = ref()
//是否正在加载消息
const loadingMessage = ref(false)

const chatSessionClickHandler = (item) => {
  distanceBottom = 0
  currentChatSession.value = Object.assign({}, item)
  messageCountStore.setCount('chatCount', -item.noReadCount, false)
  item.noReadCount = 0
  //加载本地数据库中的消息
  messageList.value = []
  loadingMessage.value = false
  messageCountInfo.pageNo = 0
  messageCountInfo.totalPage = 1
  messageCountInfo.maxMessageId = null
  messageCountInfo.noData = false
  loadChatMessage()
  //设置session
  setSessionSelect({ contactId: item.contactId, sessionId: item.sessionId })
  //清空输入框中的消息
  messageSendRef.value.cleanMessage()
}

const setSessionSelect = ({ contactId, sessionId }) => {
  window.ipcRenderer.send('setSessionSelect', {
    contactId,
    sessionId
  })
}

const loadChatMessage = () => {
  if (loadingMessage.value || messageCountInfo.noData) {
    return
  }
  messageCountInfo.pageNo++
  //正在加载
  loadingMessage.value = true
  window.ipcRenderer.send('loadChatMessage', {
    sessionId: currentChatSession.value.sessionId,
    pageNo: messageCountInfo.pageNo,
    maxMessageId: messageCountInfo.maxMessageId
  })
}

const onReciveMessage = () => {
  //监听聊天消息
  window.ipcRenderer.on('reciveMessage', (e, message) => {
    console.log('收到消息', message)
    if (message.messageType == 0) {
      return
    }
    //查询好友申请信息
    if (message.messageType == 4) {
      loadContactApply()
      return
    }
    //强制下线
    if (message.messageType == 7) {
      proxy.Confirm({
        message: `你已经被管理员强制下线`,
        okfun: () => {
          setTimeout(() => {
            window.ipcRenderer.send('reLogin')
          }, 200)
        },
        showCancelBtn: false
      })
      return
    }
    //更新群昵称
    if (message.messageType == 10) {
      let curSession = chatSessionList.value.find((item) => {
        return item.contactId == message.contactId
      })
      curSession.contactName = message.extendData
      return
    }
    //如果是 文件上传完成的消息，更新文件即可
    if (message.messageType == 6) {
      const localMessage = messageList.value.find((item) => {
        if (item.messageId == message.messageId) {
          return item
        }
      })
      if (localMessage != null) {
        localMessage.status = 1
      }
      return
    }
    //添加好友、创建群、加入群
    //刷新我的联系人列表，如果当前页面正在联系人列表页面，加入群组申请通过后需要刷新列表
    if (message.messageType == 9 && message.extendData.userId == userInfoStore.getInfo().userId) {
      contactStateStore.setContactReload('GROUP')
    }

    //查询当前消息对应的会话
    let curSession = chatSessionList.value.find((item) => {
      return item.sessionId == message.sessionId
    })
    if (curSession == null) {
      chatSessionList.value.push(message.extendData)
      curSession = message.extendData
    } else {
      Object.assign(curSession, message.extendData)
    }
    //会话列表重新排序
    sortChatSessionList(chatSessionList.value)
    //如果没有选中当前消息的会话，就显示消息数量 不增加消息
    if (message.sessionId !== currentChatSession.value.sessionId) {
      messageCountStore.setCount('chatCount', 1, false)
    } else {
      //选中了当前消息的会话更新会话信息，添加消息
      Object.assign(currentChatSession.value, message.extendData)
      messageList.value.push(message)
      gotoBottom()
    }
  })
}
//监听加载会话列表
const onLoadSessionData = () => {
  window.ipcRenderer.on('loadSessionDataCallback', (e, data) => {
    //设置未读数
    let noReadCount = 0
    data.forEach((element) => {
      noReadCount = noReadCount + element.noReadCount
    })
    messageCountStore.setCount('chatCount', noReadCount, true)
    //排序
    sortChatSessionList(data)
    chatSessionList.value = data
  })
}

//获取会话信息
const onLoadChatMessage = () => {
  //监听查询本地数据库消息列表
  window.ipcRenderer.on('loadChatMessage', (e, { dataList, pageTotal, pageNo }) => {
    if (pageNo == pageTotal) {
      messageCountInfo.noData = true
    }

    loadingMessage.value = false
    dataList.sort((a, b) => {
      return a.messageId - b.messageId
    })

    const lastMessage = messageList.value[0]

    messageList.value = dataList.concat(messageList.value)
    messageCountInfo.pageTotal = pageTotal
    messageCountInfo.pageNo = pageNo

    if (pageNo == 1) {
      messageCountInfo.maxMessageId =
        dataList.length > 0 ? dataList[dataList.length - 1].messageId : null
    }
    if (pageNo == 1) {
      gotoBottom()
    } else {
      nextTick(() => {
        document.querySelector('#message' + lastMessage.messageId).scrollIntoView()
      })
    }
  })
}

//本地消息写入完成回调
const onAddLocalMessage = () => {
  window.ipcRenderer.on('addLocalCallback', (e, { messageId, status }) => {
    const findMessage = messageList.value.find((item) => {
      if (item.messageId == messageId) {
        return item
      }
    })
    //本地有消息，说明是自己发送的，更新消息
    if (findMessage != null) {
      findMessage.status = status
    }
  })
}

//发送本地消息
const sendMessage4LocalHandler = (messageObj) => {
  messageList.value.push(messageObj)

  const chatSession = chatSessionList.value.find((item) => {
    return item.sessionId == messageObj.sessionId
  })
  if (chatSession) {
    chatSession.lastMessage = messageObj.lastMessage
    chatSession.lastReceiveTime = messageObj.sendTime
  }
  sortChatSessionList(chatSessionList.value)
  gotoBottom()
}

const gotoBottom = () => {
  nextTick(() => {
    //距离底部距离超过200就不自动滚动到底部
    if (distanceBottom > 200) {
      return
    }
    const feedItems = document.querySelectorAll('.message-item')
    if (feedItems.length > 0) {
      setTimeout(() => {
        feedItems[feedItems.length - 1].scrollIntoView()
      }, 100)
    }
  })
}

//获取好友申请信息
const loadContactApply = () => {
  window.ipcRenderer.send('loadContactApply')
}

const onLoadContactApply = () => {
  window.ipcRenderer.on('loadContactApplyCallback', (e, contactNoRead) => {
    messageCountStore.setCount('contactApplyCount', contactNoRead, true)
  })
}

//重新加载会话
const onReloadChatSession = () => {
  window.ipcRenderer.on('reloadChatSessionCallback', (e, { contactId, chatSessionDataList }) => {
    //重新排序
    sortChatSessionList(chatSessionDataList)
    //设置会话
    chatSessionList.value = chatSessionDataList
    //选中会话发送消息
    sendMessage(contactId)
  })
}

//监听主进程消息
onMounted(() => {
  onReciveMessage()

  onLoadChatMessage()

  loadChatSession()

  onLoadSessionData()

  loadContactApply()

  onLoadContactApply()

  onAddLocalMessage()

  //重新加载已删除的会话
  onReloadChatSession()

  nextTick(() => {
    const messagePanel = document.querySelector('#message-panel')
    messagePanel.addEventListener('scroll', (e) => {
      const scrollTop = e.target.scrollTop
      //计算距离底部的距离
      distanceBottom = e.target.scrollHeight - e.target.clientHeight - scrollTop
      //滚动到顶部，开始分页查询
      if (scrollTop == 0 && messageList.value.length > 0) {
        loadChatMessage()
      }
    })
  })
  //设置选中session为空
  setSessionSelect({})
})

onUnmounted(() => {
  window.ipcRenderer.removeAllListeners('loadSessionDataCallback')
  window.ipcRenderer.removeAllListeners('reciveMessage')
  window.ipcRenderer.removeAllListeners('loadChatMessage')
  window.ipcRenderer.removeAllListeners('loadContactApply')
  window.ipcRenderer.removeAllListeners('addLocalCallback')
  window.ipcRenderer.removeAllListeners('reloadChatSessionCallback')
})

const setTop = (data) => {
  data.topType = data.topType == 0 ? 1 : 0
  sortChatSessionList(chatSessionList.value)
  window.ipcRenderer.send('topChatSession', { contactId: data.contactId, topType: data.topType })
}

//删除会话
const delChatSession = (contactId) => {
  delChatSessionList(contactId)
  setSessionSelect({})
  currentChatSession.value = {}
  window.ipcRenderer.send('delChatSession', contactId)
}

//右键
const onContextMenu = (data, e) => {
  ContextMenu.showContextMenu({
    x: e.x,
    y: e.y,
    items: [
      {
        label: data.topType == 0 ? '置顶' : '取消置顶',
        onClick: () => {
          setTop(data)
        }
      },
      {
        label: '删除聊天',
        onClick: () => {
          proxy.Confirm({
            message: `确定要删除聊天【${data.contactName}】吗？`,
            okfun: () => {
              delChatSession(data.contactId, true)
            }
          })
        }
      }
    ]
  })
}
//查看媒体详情
const showMediaDetailHandler = (messageId) => {
  let showFileList = messageList.value.filter((item) => {
    return item.messageType == 5
  })
  showFileList = showFileList.map((item) => {
    return {
      partType: 'chat',
      fileId: item.messageId,
      fileType: item.fileType,
      fileName: item.fileName,
      fileSize: item.fileSize,
      forceGet: false
    }
  })
  window.ipcRenderer.send('newWindow', {
    windowId: 'media',
    title: '图片查看',
    path: `/showMedia`,
    data: {
      currentFileId: messageId,
      fileList: showFileList
    }
  })
}

//群详情
const chatGroupDetailRef = ref()
const showGroupDetail = () => {
  chatGroupDetailRef.value.show(currentChatSession.value.contactId)
}

//发送消息
const sendMessage = (contactId) => {
  let curSession = chatSessionList.value.find((item) => {
    return item.contactId == contactId
  })
  //没有找到会话记录可能是已经删除
  if (!curSession) {
    //修改会话状态
    window.ipcRenderer.send('reloadChatSession', { contactId })
    return
  } else {
    chatSessionClickHandler(curSession)
  }
}

//发送消息监听 发送消息给联系人
watch(
  () => route.query.timestamp,
  (newVal, oldVal) => {
    if (newVal && route.query.chatId) {
      sendMessage(route.query.chatId)
    }
  },
  { immediate: true, deep: true }
)

//搜索
const searchKey = ref()
const searchList = ref([])
const search = () => {
  if (!searchKey.value) {
    return
  }
  searchList.value = []
  var regex = new RegExp('(' + searchKey.value + ')', 'gi')
  chatSessionList.value.forEach((item) => {
    if (item.contactName.includes(searchKey.value) || item.lastMessage.includes(searchKey.value)) {
      let newData = Object.assign({}, item)
      newData.searchContactName = newData.contactName.replace(
        regex,
        "<span class='highlight'>$1</span>"
      )
      newData.searchLastMessage = newData.lastMessage.replace(
        regex,
        "<span class='highlight'>$1</span>"
      )
      searchList.value.push(newData)
    }
  })
}

const searchClickHandler = (data) => {
  searchKey.value = undefined
  chatSessionClickHandler(data)
}
</script>

<style lang="scss" scoped>
.drag-panel {
  height: 25px;
  background: #f7f7f7;
}
.top-search {
  padding: 0px 10px 9px 10px;
  background: #f7f7f7;
  display: flex;
  align-items: center;
  .iconfont {
    font-size: 12px;
  }
}

.chat-session-list {
  height: calc(100vh - 62px);
  overflow: hidden;
  border-top: 1px solid #ddd;
  &:hover {
    overflow: auto;
  }
}

.search-list {
  height: calc(100vh - 62px);
  background: #f7f7f7;
  overflow: hidden;
  &:hover {
    overflow: auto;
  }
}

.title-panel {
  display: flex;
  align-items: center;
  .title {
    height: 60px;
    line-height: 60px;
    padding-left: 10px;
    font-size: 18px;
    color: #000000;
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.icon-more {
  position: absolute;
  z-index: 1;
  top: 30px;
  right: 3px;
  width: 20px;
  font-size: 20px;
  margin-right: 5px;
  cursor: pointer;
}
.chat-panel {
  border-top: 1px solid #ddd;
  background: #f5f5f5;
  .message-panel {
    padding: 10px 30px 0px 30px;
    height: calc(100vh - 200px - 62px);
    overflow-y: auto;
    .message-item {
      margin-bottom: 15px;
      text-align: center;
    }
  }
}
</style>