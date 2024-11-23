import { createApp } from 'vue'
import App from './App.vue'
import * as Pinia from 'pinia';
import router from '@/router'
import ElementPlus from 'element-plus'
import VueCookies from 'vue-cookies'

import 'element-plus/dist/index.css'
//全局重写样式
import "@/assets/cust-elementplus.scss";

import "@/assets/icon/iconfont.css"
import '@/assets/base.scss';

import WinOp from "@/components/WinOp.vue"
import Avatar from "@/components/Avatar.vue"
import Dialog from "@/components/Dialog.vue"
import ContentPanel from "@/components/ContentPanel.vue"
import Layout from "@/components/Layout.vue"
import UserBaseInfo from "@/components/UserBaseInfo.vue"
import Badge from "@/components/Badge.vue"
import Table from "@/components/Table.vue"
import ShowLocalImage from '@/components/ShowLocalImage.vue'

import Verify from "@/utils/Verify.js"
import Utils from "@/utils/Utils.js"
import Request from '@/utils/Request';
import Message from './utils/Message'
import Confirm from './utils/Confirm'
import Api from './utils/Api'

const app = createApp(App);
app.use(Pinia.createPinia());
app.use(router);
app.use(ElementPlus);

app.component("WinOp", WinOp);
app.component("Avatar", Avatar);
app.component("Dialog", Dialog);
app.component("ContentPanel", ContentPanel);
app.component("Layout", Layout);
app.component("UserBaseInfo", UserBaseInfo);
app.component("Badge", Badge);
app.component("Table", Table);
app.component("ShowLocalImage", ShowLocalImage);

app.config.globalProperties.Verify = Verify;
app.config.globalProperties.Utils = Utils;
app.config.globalProperties.Request = Request;
app.config.globalProperties.Message = Message;
app.config.globalProperties.Confirm = Confirm;
app.config.globalProperties.VueCookies = VueCookies;
app.config.globalProperties.Api = Api;
app.mount('#app');