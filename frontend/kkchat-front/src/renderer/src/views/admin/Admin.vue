<template>
  <div class="admin-window">
    <div class="title drag">管理员</div>
    <div class="body-content">
      <div class="left-side">
        <div
          :class="['menu-item', item.path == route.path ? 'active' : '']"
          @click="menuJump(item)"
          v-for="item in menuList"
        >
          <div :class="['iconfont', item.icon]" :style="{ background: item.iconBgColor }"></div>
          <div class="text">{{ item.name }}</div>
        </div>
      </div>
      <div class="right-content">
        <router-view v-slot="{ Component }">
          <component :is="Component" ref="componentRef" />
        </router-view>
      </div>
    </div>
  </div>
  <WinOp :showMax="false" :showSetTop="false"></WinOp>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick, onMounted, onUnmounted } from 'vue'
const { proxy } = getCurrentInstance()
import { useRouter, useRoute } from 'vue-router'
const router = useRouter()
const route = useRoute()

import { useGlobalInfoStore } from '@/stores/GlobalInfoStore'
const globalInfoStore = useGlobalInfoStore()

const menuList = ref([
  {
    name: '用户管理',
    icon: 'icon-user',
    path: '/admin/userList',
    iconBgColor: '#fa9d3b'
  },
  {
    name: '靓号管理',
    icon: 'icon-beauty-beauty',
    path: '/admin/beautyAccount',
    iconBgColor: '#fe90b3'
  },
  {
    name: '群组管理',
    icon: 'icon-group',
    path: '/admin/groupList',
    iconBgColor: '#1485ee'
  },
  {
    name: '系统设置',
    icon: 'icon-setting',
    path: '/admin/sysSetting',
    iconBgColor: '#fa5151'
  },
  {
    name: '版本管理',
    icon: 'icon-refresh',
    path: '/admin/update',
    iconBgColor: '#07c160'
  }
])

const menuJump = (item) => {
  router.push(item.path)
}

onMounted(() => {
  window.ipcRenderer.on('pageInitData', (e, data) => {
    localStorage.setItem('token', data.token)
    globalInfoStore.setInfo('localServerPort', data.localServerPort)
  })
})
onUnmounted(() => {
  window.ipcRenderer.removeAllListeners('pageInitData')
})
</script>

<style lang="scss" scoped>
.admin-window {
  padding: 0px;
  border: 1px solid #ddd;
  background: #fff;
  position: relative;
  overflow: hidden;
  .title {
    height: 40px;
    line-height: 40px;
    border-bottom: 1px solid #ddd;
    text-align: center;
    font-weight: bold;
  }
  .body-content {
    height: calc(100vh - 42px);
    display: flex;
    .left-side {
      width: 200px;
      border-right: 1px solid #ddd;
      background: #e6e5e5;
      .menu-item {
        display: flex;
        align-items: center;
        padding: 10px 10px;
        position: relative;
        &:hover {
          cursor: pointer;
          background: #d6d6d7;
        }
        .iconfont {
          width: 35px;
          height: 35px;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 20px;
          color: #fff;
        }
        .text {
          flex: 1;
          color: #000000;
          margin-left: 10px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
      .active {
        background: #c4c4c4;
        &:hover {
          background: #c4c4c4;
        }
      }
    }
    .right-content {
      flex: 1;
      padding: 10px;
    }
  }
}
</style>
