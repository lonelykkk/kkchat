<template>
  <div class="send-panel">
    <div class="toolbar">
      <el-popover
        :visible="showEmojiPopover"
        trigger="click"
        placement="top"
        :teleported="false"
        @show="openPopover"
        @hide="closePopover"
        :popper-style="{
          padding: '0px 10px 10px 10px',
          width: '490px'
        }"
      >
        <template #default>
          <el-tabs v-model="activeEmoji" @click.stop>
            <el-tab-pane :label="emoji.name" :name="emoji.name" v-for="emoji in emojiList">
              <div class="emoji-list">
                <div class="emoji-item" v-for="item in emoji.emojiList" @click="sendEmoji(item)">
                  {{ item }}
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </template>
        <template #reference>
          <div class="iconfont icon-emoji" @click="showEmojiPopoverHandler"></div>
        </template>
      </el-popover>
      <el-upload
        ref="uploadRef"
        name="file"
        :show-file-list="false"
        :multiple="true"
        :limit="fileLimit"
        :http-request="uploadFile"
        :on-exceed="uploadExceed"
      >
        <div class="iconfont icon-folder"></div>
      </el-upload>
    </div>
    <div class="input-area" @drop="dropHandler" @dragover="dragOverHandler">
      <el-input
        rows="5"
        v-model="msgContent"
        type="textarea"
        resize="none"
        maxlength="500"
        show-word-limit
        spellcheck="false"
        input-style="background:#f5f5f5;border:none;"
        @keydown.enter="sendMessage"
        @paste="pasteFile"
      />
    </div>
    <div class="send-btn-panel">
      <el-popover
        trigger="click"
        :visible="showSendMsgPopover"
        :hide-after="1500"
        placement="top-end"
        :teleported="false"
        @show="openPopover"
        @hide="closePopover"
        :popper-style="{
          padding: '5px',
          'min-width': '0px',
          width: '120px'
        }"
      >
        <template #default> <span class="empty-msg">不能发送空白信息</span> </template>
        <template #reference>
          <span class="send-btn" @click="sendMessage">发送(S)</span>
        </template>
      </el-popover>
    </div>
    <!--添加好友-->
    <SearchAdd ref="searchAddRef"></SearchAdd>
  </div>
</template>

<script setup>
import SearchAdd from '@/views/contact/SearchAdd.vue'
import { getFileType } from '@/utils/Constants.js'
import { ref, reactive, getCurrentInstance, nextTick, onMounted, onUnmounted } from 'vue'
const { proxy } = getCurrentInstance()
import emojiList from '@/utils/Emoji.js'
import { useUserInfoStore } from '@/stores/UserInfoStore'
const userInfoStore = useUserInfoStore()

import { useSysSettingStore } from '@/stores/SysSettingStore'
const sysSettingStore = useSysSettingStore()

const props = defineProps({
  currentChatSession: {
    type: Object,
    default: {}
  }
})

const cleanMessage = () => {
  msgContent.value = ''
}
defineExpose({
  cleanMessage
})

const activeEmoji = ref('笑脸')

//发送消息
const msgContent = ref('')

const emit = defineEmits(['sendMessage4Local'])
const sendMessage = async (e) => {
  //shift +enter 换行  enter 发送
  if (e.shiftKey && e.keyCode === 13) {
    return
  }
  e.preventDefault()
  const messageContent = msgContent.value ? msgContent.value.replace(/\s*$/g, '') : ''
  if (messageContent == '') {
    showSendMsgPopover.value = true
    return
  }
  sendMessageDo({ messageContent, messageType: 2 }, true)
}

//添加好友
const searchAddRef = ref()
const addContact = (contactId, code) => {
  searchAddRef.value.show({
    contactId,
    contactType: code == 902 ? 'USER' : 'GROUP'
  })
}

//发送消息
const sendMessageDo = async (
  messageObj = {
    messageContent,
    messageType,
    localFilePath,
    fileSize,
    fileName,
    filePath,
    fileType
  },
  cleanMsgContent
) => {
  if (!checkFileSize(messageObj.fileType, messageObj.fileSize, messageObj.fileName)) {
    return
  }

  if (messageObj.fileSize == 0) {
    proxy.Confirm({
      message: `"${messageObj.fileName}"是一个空文件无法发送，请重新选择`,
      showCancelBtn: false
    })
    return
  }
  messageObj.sessionId = props.currentChatSession.sessionId
  messageObj.sendUserId = userInfoStore.getInfo().userId

  //请求服务器发送消息
  let result = await proxy.Request({
    url: proxy.Api.sendMessage,
    showLoading: false,
    params: {
      messageContent: messageObj.messageContent,
      contactId: props.currentChatSession.contactId,
      messageType: messageObj.messageType,
      fileSize: messageObj.fileSize,
      fileName: messageObj.fileName,
      fileType: messageObj.fileType
    },
    showError: false,
    errorCallback: (responseData) => {
      proxy.Confirm({
        message: responseData.info,
        okfun: () => {
          addContact(props.currentChatSession.contactId, responseData.code)
        },
        okText: '重新申请'
      })
    }
  })
  if (!result) {
    return
  }
  //更新本地消息
  if (cleanMsgContent) {
    msgContent.value = ''
  }
  Object.assign(messageObj, result.data)
  //更新列表
  emit('sendMessage4Local', messageObj)
  //保存消息到本地
  window.ipcRenderer.send('addLocalMessage', messageObj)
}

//表情相关
const sendEmoji = (emoji) => {
  msgContent.value = msgContent.value + emoji
  showEmojiPopover.value = false
}

const showEmojiPopoverHandler = () => {
  showEmojiPopover.value = true
}

const showSendMsgPopover = ref(false)
const showEmojiPopover = ref(false)

const hidePopover = () => {
  showSendMsgPopover.value = false
  showEmojiPopover.value = false
}
const openPopover = () => {
  document.addEventListener('click', hidePopover, false)
}
const closePopover = () => {
  document.removeEventListener('click', hidePopover, false)
}

//校验文件大小
const checkFileSize = (fileType, fileSize, fileName) => {
  const SIZE_MB = 1024 * 1024
  const settingArray = Object.values(sysSettingStore.getSetting())
  //图片
  if (fileSize > settingArray[fileType] * SIZE_MB) {
    proxy.Confirm({
      message: `文件${fileName}超过大小${settingArray[fileType]}MB限制`,
      showCancelBtn: false
    })
    return false
  }
  return true
}

//发送文件
const fileLimit = 10
const checkFileLimit = (files) => {
  if (files.length > fileLimit) {
    proxy.Confirm({
      message: `一次最多可以上传10个文件`,
      showCancelBtn: false
    })
    return
  }
  return true
}
//拖入文件
const dragOverHandler = () => {
  event.preventDefault()
}
const dropHandler = (event) => {
  event.preventDefault()
  const files = event.dataTransfer.files
  if (!checkFileLimit(files)) {
    return
  }
  for (let i = 0; i < files.length; i++) {
    uploadFileDo(files[i])
  }
}

//文件个数超过指定值
const uploadExceed = (files) => {
  checkFileLimit(files)
}
//上传文件
const uploadRef = ref()
const uploadFile = (file) => {
  uploadFileDo(file.file)
  uploadRef.value.clearFiles()
}

const getFileTypeByName = (fileName) => {
  const fileSuffix = fileName.substr(fileName.lastIndexOf('.') + 1)
  return getFileType(fileSuffix)
}

const uploadFileDo = (file) => {
  const fileType = getFileTypeByName(file.name)
  sendMessageDo(
    {
      messageContent: '[' + getFileType(fileType) + ']',
      messageType: 5,
      fileSize: file.size,
      fileName: file.name,
      filePath: file.path,
      fileType: fileType
    },
    false
  )
}

//截图粘贴上传文件
const pasteFile = async (event) => {
  let items = event.clipboardData && event.clipboardData.items
  const fileData = {}
  for (const item of items) {
    if (item.kind != 'file') {
      break
    }
    const file = await item.getAsFile()
    if (file.path != '') {
      uploadFileDo(file)
    } else {
      const imageFile = new File([file], 'temp.jpg')
      let fileReader = new FileReader()
      fileReader.onloadend = function () {
        // 读取完成后获得结果
        const byteArray = new Uint8Array(this.result)
        fileData.byteArray = byteArray
        fileData.name = imageFile.name
        window.ipcRenderer.send('saveClipBoardFile', fileData)
      }
      fileReader.readAsArrayBuffer(imageFile)
    }
  }
}

onMounted(() => {
  window.ipcRenderer.on('saveClipBoardFileCallback', (e, file) => {
    const fileType = 0
    sendMessageDo(
      {
        messageContent: '[' + getFileType(fileType) + ']',
        messageType: 5,
        fileSize: file.size,
        fileName: file.name,
        filePath: file.path,
        fileType: fileType
      },
      false
    )
  })
})
onUnmounted(() => {
  window.ipcRenderer.removeAllListeners('saveClipBoardFileCallback')
})
</script>

<style lang="scss" scoped>
.emoji-list {
  .emoji-item {
    float: left;
    font-size: 23px;
    padding: 2px;
    text-align: center;
    border-radius: 3px;
    margin-left: 10px;
    margin-top: 5px;
    cursor: pointer;
    &:hover {
      background: #ddd;
    }
  }
}
.send-panel {
  height: 200px;
  border-top: 1px solid #ddd;
  .toolbar {
    height: 40px;
    display: flex;
    align-items: center;
    padding-left: 10px;
    .iconfont {
      color: #494949;
      font-size: 20px;
      margin-left: 10px;
      cursor: pointer;
    }
    :deep(.el-tabs__header) {
      margin-bottom: 0px;
    }
  }
  .input-area {
    padding: 0px 10px;
    outline: none;
    width: 100%;
    height: 115px;
    overflow: auto;
    word-wrap: break-word;
    word-break: break-all;

    :deep(.el-textarea__inner) {
      box-shadow: none;
    }
    :deep(.el-input__count) {
      background: none;
      right: 12px;
    }
  }
  .send-btn-panel {
    text-align: right;
    padding-top: 10px;
    margin-right: 22px;
    .send-btn {
      cursor: pointer;
      color: #07c160;
      background: #e9e9e9;
      border-radius: 5px;
      padding: 8px 25px;
      &:hover {
        background: #d2d2d2;
      }
    }
    .empty-msg {
      font-size: 13px;
    }
  }
}
</style>
