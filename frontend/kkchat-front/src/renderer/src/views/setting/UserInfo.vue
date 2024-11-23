<template>
  <ContentPanel>
    <div class="show-info" v-if="showType == 0">
      <div class="user-info">
        <UserBaseInfo :userInfo="userInfo"></UserBaseInfo>
        <div class="more-op">
          <el-dropdown placement="bottom-end" trigger="click">
            <span class="el-dropdown-link">
              <div class="iconfont icon-more"></div>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="changePart(1)">修改个人信息</el-dropdown-item>
                <el-dropdown-item @click="changePart(2)">修改密码</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
      <div class="part-item">
        <div class="part-title">朋友权限</div>
        <div class="part-content">
          {{ userInfo.joinType == 0 ? '直接加入' : '加我为好友时需要验证' }}
        </div>
      </div>
      <div class="part-item">
        <div class="part-title">个性签名</div>
        <div class="part-content">{{ userInfo.personalSignature || '-' }}</div>
      </div>
      <div class="logout">
        <el-button @click="logout">退出登录</el-button>
      </div>
    </div>
    <div v-if="showType == 1">
      <UserInfoEdit @editBack="editBack" :data="userInfo"></UserInfoEdit>
    </div>
    <div v-if="showType == 2">
      <UserPassword @editBack="editBack"></UserPassword>
    </div>
  </ContentPanel>
</template>

<script setup>
import UserInfoEdit from './UserInfoEdit.vue'
import UserPassword from './UserInfoPassword.vue'
import { ref, reactive, getCurrentInstance, nextTick, watch, computed } from 'vue'
const { proxy } = getCurrentInstance()
import { useRoute } from 'vue-router'
const route = useRoute()

const userInfo = ref({})

const getUserInfo = async () => {
  let result = await proxy.Request({
    url: proxy.Api.getUserInfo
  })
  if (!result) {
    return
  }
  userInfo.value = result.data
}
getUserInfo()

const showType = ref(0)

const changePart = (part) => {
  showType.value = part
}

const editBack = () => {
  showType.value = 0
  getUserInfo()
}

const logout = () => {
  proxy.Confirm({
    message: '确定要退出登录吗？',
    okfun: async () => {
      window.ipcRenderer.send('reLogin')
      let result = await proxy.Request({
        url: proxy.Api.logout
      })
      if (!result) {
        return
      }
    }
  })
}
</script>

<style lang="scss" scoped>
.show-info {
  .user-info {
    position: relative;
    .more-op {
      position: absolute;
      right: 0px;
      top: 20px;
      .icon-more {
        color: #9e9e9e;
        &:hover {
          background: #dddddd;
        }
      }
    }
  }
  .part-item {
    display: flex;
    border-bottom: 1px solid #eaeaea;
    padding: 20px 0px;
    .part-title {
      width: 60px;
      color: #9e9e9e;
    }
    .part-content {
      flex: 1;
      margin-left: 15px;
      color: #161616;
    }
  }

  .logout {
    text-align: center;
    margin-top: 20px;
  }
}
</style>
