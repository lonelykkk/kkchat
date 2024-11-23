import { app, shell, BrowserWindow, Menu, Tray } from 'electron'
import { join } from 'path'
const NODE_ENV = process.env.NODE_ENV
import { electronApp, optimizer, is } from '@electron-toolkit/utils'
import icon from '../../resources/icon.png?asset'
import {
  onSetLocalStore, onGetLocalStore,
  onLoginSuccess, onLoadChatMessage, onLoadSessionData, onReLogin, onLoginOrRegister, winTitleOp,
  onOpenNewWindow, openWindow, onSetSessionSelect, onLoadContactApply, onUpdateContactNoReadCount,
  onAddLocalMessage, onCreateCover, onSaveAs, onGetSettingInfo, onChangeLocalFolder,
  onOpenLocalFolder, onDownloadUpdate, onOpenUrl, onSaveClipBoardFile, onLoadLocalUser, onDelChatSession,
  onTopChatSession, onReloadChatSession
} from "./ipc"
import { saveWindow } from './windowProxy'

const login_width = 300;
const login_height = 370;
const register_height = 490;


function createWindow() {
  // Create the browser window.
  const mainWindow = new BrowserWindow({
    icon: icon,
    width: login_width,
    height: login_height,//380
    show: false,
    fullscreenable: false,
    fullscreen: false,
    maximizable: false,
    autoHideMenuBar: true,//隐藏菜单
    resizable: false,//自动拖动大小
    frame: true,// 创建无边框窗口，没有窗口的某些部分（例如工具栏、控件等）
    transparent: true,//创建一个完全透明的窗口
    hasShadow: false,
    titleBarStyle: 'hidden',
    webPreferences: {
      preload: join(__dirname, '../preload/index.js'),
      sandbox: false,
      contextIsolation: false
    }
  })

  saveWindow("main", mainWindow);

  //打开控制台
  if (NODE_ENV === 'development') {
    mainWindow.webContents.openDevTools();
  }


  mainWindow.on('ready-to-show', () => {
    mainWindow.show()
    mainWindow.setTitle("EasyChat")
  })

  mainWindow.once('focus', () => mainWindow.flashFrame(false));

  mainWindow.on('close', (e) => {
    mainWindow.hide();
  })

  mainWindow.webContents.setWindowOpenHandler((details) => {
    shell.openExternal(details.url)
    return { action: 'deny' }
  })

  // HMR for renderer base on electron-vite cli.
  // Load the remote URL for development or the local html file for production.
  if (is.dev && process.env['ELECTRON_RENDERER_URL']) {
    mainWindow.loadURL(process.env['ELECTRON_RENDERER_URL'])
  } else {
    mainWindow.loadFile(join(__dirname, '../renderer/index.html'))
  }

  //托盘
  const tray = new Tray(icon)
  const contextMenu = [
    {
      label: '退出EasyChat', click: function () {
        app.exit();
      }
    }
  ];

  const menu = Menu.buildFromTemplate(contextMenu)
  tray.setToolTip('EasyChat')
  tray.setContextMenu(menu)
  // 点击托盘图标，显示主窗口
  tray.on("click", () => {
    mainWindow.setSkipTaskbar(false)
    mainWindow.show();
  })

  //设置本地store存储
  onSetLocalStore();

  //获取本地store存储
  onGetLocalStore();

  onLoginSuccess((config) => {
    contextMenu.splice(0);
    contextMenu.push({
      label: '退出EasyChat', click: function () {
        app.exit();
      }
    })

    //改变窗口大小
    mainWindow.setResizable(true);
    //设置窗口大小
    mainWindow.setSize(850, 800);
    //居中
    mainWindow.center();
    //最大化
    mainWindow.setMaximizable(true);
    //设置最小窗口大小
    mainWindow.setMinimumSize(800, 600);

    //更新托盘
    if (config.admin) {
      //管理员 可以进入后台
      contextMenu.unshift({
        label: "管理后台", click: function () {
          openWindow({
            windowId: 'admin',
            title: "管理后台",
            path: `/admin`,
            width: config.screenWidth * 0.8,
            height: config.screenHeight * 0.8,
            data: {
              token: config.token
            }
          });
        }
      })
    }

    //设置用户
    contextMenu.unshift({
      label: "用户：" + config.nickName, click: function () {
      }
    })
    tray.setContextMenu(Menu.buildFromTemplate(contextMenu));
  });

  onLoginOrRegister((isLogin) => {
    mainWindow.setResizable(true);
    if (isLogin) {
      mainWindow.setSize(login_width, login_height);
    } else {
      mainWindow.setSize(login_width, register_height);
    }
    mainWindow.setResizable(false);
  })

  onReLogin(() => {
    mainWindow.setResizable(true);
    //上面设置了最小窗口大小，所这里需要重新设置窗口大小，否则无法修改大小
    mainWindow.setMinimumSize(login_width, login_height);
    mainWindow.setSize(login_width, login_height);
    mainWindow.center();
    mainWindow.setResizable(false);
  })

  winTitleOp((e, { action, data }) => {
    const webContents = e.sender
    const win = BrowserWindow.fromWebContents(webContents)
    switch (action) {
      case "close": {
        if (data.closeType == 0) {
          win.close();
        } else {
          win.setSkipTaskbar(true) // 使窗口不显示在任务栏中
          win.hide()
        }
        break;
      }
      case "minimize": {
        win.minimize();
        break;
      }
      case "maximize": {
        win.maximize();
        break;
      }
      case "unmaximize": {
        win.unmaximize();
        break;
      }
      case "top": {
        win.setAlwaysOnTop(data.top);
      }
    };
  });

  onLoadChatMessage();

  onLoadSessionData();

  //打开新窗口
  onOpenNewWindow();

  //设置session
  onSetSessionSelect();

  //查询好友申请
  onLoadContactApply();

  //设置未读数
  onUpdateContactNoReadCount();

  //增加本地消息
  onAddLocalMessage();

  //生成缩略图
  onCreateCover();

  //文件另存为
  onSaveAs();

  //获取设置信息
  onGetSettingInfo();

  //更改本地目录
  onChangeLocalFolder();

  //打开本地目录
  onOpenLocalFolder();

  //下载更新
  onDownloadUpdate();

  //打开URL
  onOpenUrl();

  //保存剪切板的内功
  onSaveClipBoardFile();

  //查询本地用户
  onLoadLocalUser();

  //删除好友
  onDelChatSession();

  //置顶会话
  onTopChatSession();

  onReloadChatSession();
}

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.whenReady().then(() => {
  // Set app user model id for windows
  electronApp.setAppUserModelId('com.electron')

  // Default open or close DevTools by F12 in development
  // and ignore CommandOrControl + R in production.
  // see https://github.com/alex8088/electron-toolkit/tree/master/packages/utils
  app.on('browser-window-created', (_, window) => {
    optimizer.watchWindowShortcuts(window)
  })
  createWindow()
  app.on('activate', function () {
    // On macOS it's common to re-create a window in the app when the
    // dock icon is clicked and there are no other windows open.
    if (BrowserWindow.getAllWindows().length === 0) createWindow()
  })
  //
})

// Quit when all windows are closed, except on macOS. There, it's common
// for applications and their menu bar to stay active until the user quits
// explicitly with Cmd + Q.
app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit()
  }
})
app.commandLine.appendSwitch('wm-window-animations-disabled');

// In this file you can include the rest of your app"s specific main process
// code. You can also put them in separate files and require them here.