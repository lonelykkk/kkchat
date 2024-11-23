<template>
  <div class="main">
    <div class="left-sider">
      <Avatar :userId="userInfoStore.getInfo().userId" :width="35" :showDetail="false"></Avatar>
      <div class="menu-list">
        <template v-for="item in menuList">
          <div
            :class="['tab-item iconfont', item.icon, item.path == currentMenu.path ? 'active' : '']"
            @click="changeMenu(item)"
            v-if="item.position == 'top'"
          >
            <template v-if="item.name == 'chat' || item.name == 'contact'">
              <Badge :count="messageCountStore.getCount(item.countKey)" :top="5" :left="15"></Badge>
            </template>
          </div>
        </template>
      </div>
      <div class="menu-list menu-buttom">
        <template v-for="item in menuList">
          <div
            :class="['tab-item iconfont', item.icon, item.path == currentMenu.path ? 'active' : '']"
            @click="changeMenu(item)"
            v-if="item.position == 'bottom'"
          ></div>
        </template>
      </div>
    </div>
    <div class="right-container">
      <router-view v-slot="{ Component }">
        <keep-alive include="chat">
          <component :is="Component" ref="componentRef" />
        </keep-alive>
      </router-view>
    </div>
    <Update></Update>
  </div>
  <WinOp></WinOp>
</template>

<script setup>
import Update from './Update.vue'
import Badge from '@/components/Badge.vue'
import { ref, reactive, getCurrentInstance, nextTick, onUnmounted, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
const { proxy } = getCurrentInstance()
const router = useRouter()
const route = useRoute()
import { useUserInfoStore } from '@/stores/UserInfoStore'
const userInfoStore = useUserInfoStore()

import { useSysSettingStore } from '@/stores/SysSettingStore'
const sysSettingStore = useSysSettingStore()

import { useMessageCountStore } from '@/stores/MessageCountStore'
const messageCountStore = useMessageCountStore()

import { useGlobalInfoStore } from '@/stores/GlobalInfoStore'
const globalInfoStore = useGlobalInfoStore()

import { useAvatarInfoStore } from '@/stores/AvatarUpdateStore'
const avatarInfoStore = useAvatarInfoStore()

const menuList = ref([
  {
    name: 'chat',
    icon: 'icon-chat',
    path: '/chat',
    countKey: 'chatCount',
    position: 'top'
  },
  {
    name: 'contact',
    icon: 'icon-user',
    path: '/contact',
    countKey: 'contactApplyCount',
    position: 'top'
  },
  {
    name: 'mysetting',
    icon: 'icon-more2',
    path: '/setting',
    position: 'bottom'
  }
])

const componentRef = ref(null)
const changeMenu = (item) => {
  currentMenu.value = item
  router.push(item.path)
}

const currentMenu = ref(menuList.value[0])
const menuSelect = (path) => {
  currentMenu.value = menuList.value.find((item) => {
    return path.includes(item.path)
  })
}

//获取登录信息
const getLoginInfo = async () => {
  let result = await proxy.Request({
    url: proxy.Api.getUserInfo
  })
  if (!result) {
    return
  }
  userInfoStore.setInfo(result.data)
  window.ipcRenderer.send('getLocalStore', result.data.userId + 'localServerPort')
}

//获取系统设置信息
const getSysSetting = async () => {
  let result = await proxy.Request({
    url: proxy.Api.getSysSetting
  })
  if (!result) {
    return
  }
  sysSettingStore.setSetting(result.data)
}
onMounted(() => {
  window.ipcRenderer.on('getLocalStoreCallback', (e, serverPort) => {
    globalInfoStore.setInfo('localServerPort', serverPort)
  })

  getLoginInfo()

  getSysSetting()

  window.ipcRenderer.on('reLogin', (e, info) => {
    router.push('/login')
  })

  //重新加载头像
  window.ipcRenderer.on('reloadAvatar', (e, fileId) => {
    avatarInfoStore.setFoceReload(fileId, true)
  })
})

onUnmounted(() => {
  window.ipcRenderer.removeAllListeners('getLocalStoreCallback')
  window.ipcRenderer.removeAllListeners('reLogin')
  window.ipcRenderer.removeAllListeners('reloadAvatar')
})

watch(
  () => route.path,
  (newVal, oldVal) => {
    if (newVal) {
      menuSelect(newVal)
    }
  },
  { immediate: true, deep: true }
)
</script>

<style lang="scss" scoped>
.main {
  background: #ddd;
  display: flex;
  border-radius: 0px 3px 3px 0px;
  overflow: hidden;
  .left-sider {
    width: 55px;
    background: #2e2e2e;
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-top: 35px;
    border: 1px solid #2e2e2e;
    border-right: none;
    padding-bottom: 10px;
    .menu-list {
      width: 100%;
      flex: 1;
      .tab-item {
        color: #d3d3d3;
        font-size: 20px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-top: 10px;
        cursor: pointer;
        font-size: 22px;
        position: relative;
      }
      .active {
        color: #07c160;
      }
    }
    .menu-buttom {
      display: flex;
      flex-direction: column;
      justify-content: flex-end;
    }
  }
  .right-container {
    flex: 1;
    overflow: hidden;
    border: 1px solid #ddd;
    border-left: none;
  }
}

.popover-user-panel {
  padding: 10px;
  .popover-user {
    display: flex;
    border-bottom: 1px solid #ddd;
    padding-bottom: 20px;
  }
  .send-message {
    margin-top: 10px;
    text-align: center;
    padding: 20px 0px 0px 0px;
  }
}
</style>
