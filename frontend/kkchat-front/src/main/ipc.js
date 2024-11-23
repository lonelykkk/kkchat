import { shell, BrowserWindow, ipcMain } from 'electron';
const NODE_ENV = process.env.NODE_ENV
import { join } from 'path'
import { is } from '@electron-toolkit/utils'
import { initWs, closeWs } from './wsClient';
import { selectMessageList, saveMessage, updateMessage } from "./db/ChatMessageModel";
import { selectUserSessionList, updateSessionInfo4Message, readAll, delChatSession, topChatSession, updateStatus } from "./db/ChatSessionUserModel";
import { addUserSetting, selectSettingInfo, updateContactNoReadCount, loadLocalUser } from "./db/UserSetting";
import {
    saveFile2Local, checkFile, createCover, saveAs, changeLocalFolder, openLocalFolder,
    downloadUpdate, closeLocalServer, saveClipBoardFile
} from "./file"

import { saveWindow, getWindow, delWindow } from './windowProxy'

import store from "./store"
import icon from '../../resources/icon.png?asset'

const onLoginOrRegister = (callback) => {
    ipcMain.on("loginOrRegister", (e, isLogin) => {
        callback(isLogin);
    })
}

//登录成功
const onLoginSuccess = (callback) => {
    ipcMain.on("openChat", async (e, config) => {
        store.initUserId(config.userId);
        store.setUserData("token", config.token);
        addUserSetting(config.userId, config.email);
        callback(config);
        initWs(config, e.sender);
    })
}

//设置本地存储
const onSetLocalStore = () => {
    ipcMain.on("setLocalStore", (e, { key, value }) => {
        store.setData(key, value);
    })
}
//获取本地存储
const onGetLocalStore = () => {
    ipcMain.on("getLocalStore", (e, key) => {
        e.sender.send("getLocalStoreCallback", store.getData(key))
    })
}


//重新登录
const onReLogin = (callback) => {
    ipcMain.on("reLogin", (e, data) => {
        callback();
        e.sender.send("reLogin")
        closeWs();
        closeLocalServer();
    })
}


const winTitleOp = (callback) => {
    ipcMain.on("winTitleOp", (e, data) => {
        callback(e, data);
    });
}

const onLoadChatMessage = () => {
    ipcMain.on("loadChatMessage", async (e, data) => {
        const result = await selectMessageList(data)
        e.sender.send("loadChatMessage", result);
    });
}

const onLoadSessionData = () => {
    ipcMain.on("loadSessionData", async (e) => {
        console.log("开始查询session");
        const result = await selectUserSessionList()
        e.sender.send("loadSessionDataCallback", result);
    });
}

//设置选中的会话session
const onSetSessionSelect = () => {
    ipcMain.on("setSessionSelect", async (e, { contactId, sessionId }) => {
        console.log("设置选中的会话", sessionId);
        if (sessionId) {
            store.setUserData("currentSessionId", sessionId);
            readAll(contactId);
        } else {
            store.deleteUserData("currentSessionId");
        }
    });
}

//好友申请信息
const onLoadContactApply = () => {
    ipcMain.on("loadContactApply", async (e) => {
        const userId = store.getUserId();
        let result = await selectSettingInfo(userId);
        let contactNoRead = 0;
        if (result != null) {
            contactNoRead = result.contactNoRead;
        }
        e.sender.send("loadContactApplyCallback", contactNoRead);
    });
}

const onUpdateContactNoReadCount = () => {
    ipcMain.on("updateContactNoReadCount", async (e) => {
        await updateContactNoReadCount({ userId: store.getUserId() });
    });
}

//保存本地消息
const onAddLocalMessage = () => {
    ipcMain.on("addLocalMessage", async (e, data) => {
        await saveMessage(data);
        //将文件保存到本地目录
        if (data.messageType == 5) {
            //保存本地文件
            await saveFile2Local(data.messageId, data.filePath, data.fileType);
            const updateInfo = {
                status: 1
            }
            //更新本地文件状态
            await updateMessage(updateInfo, { messageId: data.messageId });
        }
        //更新session信息
        data.lastReceiveTime = data.sendTime;
        updateSessionInfo4Message(store.getUserData("currentSessionId"), data);
        e.sender.send("addLocalCallback", { status: 1, messageId: data.messageId });
    });
}

const onSaveAs = () => {
    ipcMain.on("saveAs", async (e, data) => {
        saveAs(data);
    });
}

//校验文件是否已经下载完成
checkFile();

//生成缩略图
const onCreateCover = () => {
    ipcMain.on("createCover", async (e, localFilePath) => {
        const stream = await createCover(localFilePath);
        e.sender.send("createCoverCallback", stream);
    });
}

//获取设置信息
const onGetSettingInfo = () => {
    ipcMain.on("getSysSetting", async (e) => {
        const userId = store.getUserId();
        let result = await selectSettingInfo(userId);
        let sysSetting = result.sysSetting;
        e.sender.send("getSysSettingCallback", sysSetting);
    });
}

//更换目录
const onChangeLocalFolder = () => {
    ipcMain.on("changeLocalFolder", async (e) => {
        changeLocalFolder();
    });
}
//打开文件夹
const onOpenLocalFolder = () => {
    ipcMain.on("openLocalFolder", async (e) => {
        openLocalFolder();
    });
}

//下载更新
const onDownloadUpdate = () => {
    ipcMain.on("downloadUpdate", async (e, { id, fileName }) => {
        downloadUpdate(id, fileName);
    });
}
//打开链接
const onOpenUrl = () => {
    ipcMain.on("openUrl", async (e, { url }) => {
        shell.openExternal(url)
    });
}

//读取剪切板内容
const onSaveClipBoardFile = () => {
    ipcMain.on("saveClipBoardFile", async (e, file) => {
        const result = await saveClipBoardFile(file);
        console.log("result", result);
        e.sender.send("saveClipBoardFileCallback", result);
    });
}

const onOpenNewWindow = () => {
    ipcMain.on("newWindow", (e, config) => {
        openWindow(config);
    })
}

//查询所有用户
const onLoadLocalUser = () => {
    ipcMain.on("loadLocalUser", async (e) => {
        let userList = await loadLocalUser();
        e.sender.send("loadLocalUserCallback", userList);
    })
}

//删除会话
const onDelChatSession = () => {
    ipcMain.on("delChatSession", (e, contactId) => {
        delChatSession(contactId);
    })
}

//置顶回话
const onTopChatSession = () => {
    ipcMain.on("topChatSession", (e, { contactId, topType }) => {
        topChatSession(contactId, topType);
    })
}


//更新会话状态，重新获取会话
const onReloadChatSession = () => {
    ipcMain.on("reloadChatSession", async (e, { contactId }) => {
        await updateStatus(contactId);
        const chatSessionDataList = await selectUserSessionList();
        e.sender.send("reloadChatSessionCallback", { contactId, chatSessionDataList });
    })
}

const openWindow = ({ windowId, title = "EasyChat", path, width = 960, height = 720, data }) => {
    const localServerPort = store.getUserData("localServerPort");
    data.localServerPort = localServerPort;
    let newWindow = getWindow(windowId);
    if (!newWindow) {
        newWindow = new BrowserWindow({
            icon: icon,
            width: width,
            height: height,//380
            fullscreenable: false,
            fullscreen: false,
            maximizable: false,
            autoHideMenuBar: true,
            resizable: true,
            titleBarStyle: 'hidden',
            frame: true,
            transparent: true,
            hasShadow: false,
            webPreferences: {
                preload: join(__dirname, '../preload/index.js'),
                sandbox: false,
                contextIsolation: false
            }
        })
        //保存窗口
        saveWindow(windowId, newWindow);

        newWindow.setMinimumSize(600, 484);
        if (is.dev && process.env['ELECTRON_RENDERER_URL']) {
            // newWindow.loadURL(process.env['ELECTRON_RENDERER_URL'] + "#" + path)
            newWindow.loadURL(`${process.env['ELECTRON_RENDERER_URL']}/index.html#${path}`);
        } else {
            newWindow.loadFile(join(__dirname, `../renderer/index.html`), { hash: `${path}` });
        }
        //打开调试窗口
        if (NODE_ENV === 'development') {
            newWindow.webContents.openDevTools();
        }

        newWindow.on('ready-to-show', () => {
            console.log("设置title", title);
            newWindow.setTitle(title);
            newWindow.show()
        })

        newWindow.once('show', () => {
            setTimeout(() => {
                newWindow.webContents.send('pageInitData', data);
            }, 500);
        })
        newWindow.on('closed', () => {
            console.log("关闭窗口");
            delWindow(windowId);
        })
    } else {
        newWindow.show();
        newWindow.setSkipTaskbar(false)
        newWindow.webContents.send('pageInitData', data);
    }
}

export {
    onLoginSuccess,
    onSetLocalStore,
    onGetLocalStore,
    winTitleOp,
    onLoginOrRegister,
    onOpenNewWindow,
    openWindow,
    onReLogin,
    onLoadChatMessage,
    onLoadSessionData,
    onSetSessionSelect,
    onLoadContactApply,
    onUpdateContactNoReadCount,
    onAddLocalMessage,
    onCreateCover,
    onSaveAs,
    onGetSettingInfo,
    onChangeLocalFolder,
    onOpenLocalFolder,
    onDownloadUpdate,
    onOpenUrl,
    onSaveClipBoardFile,
    onLoadLocalUser,
    onDelChatSession,
    onTopChatSession,
    onReloadChatSession
}
