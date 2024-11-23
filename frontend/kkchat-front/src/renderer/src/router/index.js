import { createRouter, createWebHashHistory } from 'vue-router'

const router = createRouter({
  mode: "hash",
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "默认路径",
      redirect: "/login",
    },
    {
      path: "/login",
      name: "登录",
      component: () => import('@/views/Login.vue'),
    },
    {
      path: "/showMedia",
      name: "展示媒体信息",
      component: () => import('@/views/show/ShowMedia.vue'),
    },
    {
      path: "/admin",
      redirect: "/admin/userList",
      name: "管理员设置",
      component: () => import('@/views/admin/Admin.vue'),
      children: [{
        path: "/admin/userList",
        name: "用户管理",
        component: () => import('@/views/admin/UserList.vue'),
      }, {
        path: "/admin/beautyAccount",
        name: "靓号管理",
        component: () => import('@/views/admin/BeautyAccount.vue'),
      }, {
        path: "/admin/groupList",
        name: "群组管理",
        component: () => import('@/views/admin/GroupList.vue'),
      }, {
        path: "/admin/sysSetting",
        name: "系统设置",
        component: () => import('@/views/admin/SysSetting.vue'),
      }, {
        path: "/admin/update",
        name: "版本管理",
        component: () => import('@/views/admin/Update.vue'),
      }]
    },
    {
      path: "/main",
      redirect: "/chat",
      name: "主窗口",
      component: () => import('@/views/Main.vue'),
      children: [{
        path: "/chat",
        name: "聊天",
        component: () => import('@/views/chat/Chat.vue'),
      }, {
        path: "/contact",
        name: "联系人",
        redirect: "/contact/blank",
        component: () => import('@/views/contact/Contact.vue'),
        children: [{
          path: "/contact/blank",
          name: "空白页",
          component: () => import('@/views/contact/BlankPage.vue'),
        }, {
          path: "/contact/search",
          name: "搜索",
          component: () => import('@/views/contact/Search.vue'),
        }, {
          path: "/contact/contactNotice",
          name: "新的朋友",
          component: () => import('@/views/contact/ContactApply.vue'),
        }, {
          path: "/contact/createGroup",
          name: "新建群聊",
          component: () => import('@/views/contact/GroupEdit.vue'),
        }, {
          path: "/contact/editGroup",
          name: "修改群聊",
          component: () => import('@/views/contact/GroupEdit.vue'),
        }, {
          path: "/contact/groupDetail",
          name: "群组详情",
          component: () => import('@/views/contact/GroupDetail.vue'),
        }, {
          path: "/contact/userDetail",
          name: "用户详情",
          component: () => import('@/views/contact/UserDetail.vue'),
        }]
      }, {
        path: "/setting",
        name: "设置",
        redirect: "/setting/userInfo",
        component: () => import('@/views/setting/Setting.vue'),
        children: [{
          path: "/setting/userInfo",
          name: "个人信息",
          component: () => import('@/views/setting/UserInfo.vue'),
        }, {
          path: "/setting/fileManage",
          name: "文件管理",
          component: () => import('@/views/setting/FileManage.vue'),
        }, {
          path: "/setting/about",
          name: "关于",
          component: () => import('@/views/setting/About.vue'),
        }]
      }]
    }
  ]
})
export default router
