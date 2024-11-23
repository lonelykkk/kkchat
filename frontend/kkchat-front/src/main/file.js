
const fs = require('fs');
const fse = require('fs-extra');
const NODE_ENV = process.env.NODE_ENV
const path = require('path')
const { app, ipcMain, shell } = require('electron')
const { exec } = require("child_process");
const FormData = require('form-data'); // 引入FormData模块（用于构建表单数据）
const axios = require('axios'); // 引入axios库
import store from "./store"
const moment = require('moment')
moment.locale('zh-cn', {});
const { dialog } = require('electron')
//express服务器
const express = require('express');
const expressServer = express();

import { selectByMessageId } from './db/ChatMessageModel'
import { selectSettingInfo, updateSysSetting } from './db/UserSetting'

import { getWindow } from "./windowProxy";

//缩略图后缀
const cover_image_suffix = "_cover.png";
const image_suffix = ".png";

const ffprobePath = "/assets/ffprobe.exe";
const ffmpegPath = "/assets/ffmpeg.exe"

const mkdirs = (dir) => {
    if (!fs.existsSync(dir)) { // 如果目录不存在则进行创建
        const parentDir = path.dirname(dir); // 获取上级目录
        if (parentDir !== dir) { // 确保当前目录不为根目录
            mkdirs(parentDir); // 递归调用自身创建上级目录
        }
        fs.mkdirSync(dir); // 创建当前目录
    }
}

const getResourcesPath = () => {
    let resourcesPath = app.getAppPath();
    if (NODE_ENV !== 'development') {
        resourcesPath = path.dirname(app.getPath('exe')) + "/resources";
    }
    return resourcesPath;

}

const getFFprobePath = () => {
    return path.join(getResourcesPath(), ffprobePath);
}

const getFFmegPath = () => {
    return path.join(getResourcesPath(), ffmpegPath);
}

const saveFile2Local = async (messageId, filePath, fileType) => {
    return new Promise(async (resolve, reject) => {
        let ffmpegPath = getFFmegPath();
        let savePath = await getLocalFilePath("chat", false, messageId);
        let coverPath = null;
        fs.copyFileSync(filePath, savePath);
        //生成缩略图
        if (fileType != 2) {
            //判断视频格式
            let command = `${getFFprobePath()} -v error -select_streams v:0 -show_entries stream=codec_name "${filePath}"`
            let result = await execCommand(command);
            result = result.replaceAll("\r\n", "");
            result = result.substring(result.indexOf("=") + 1);
            let codec = result.substring(0, result.indexOf("["));
            if ("hevc" === codec) {
                command = `${ffmpegPath}  -y -i "${filePath}" -c:v libx264 -crf 20 "${savePath}"`;
                await execCommand(command);
            }
            //生成缩略图
            coverPath = savePath + cover_image_suffix;
            command = `${ffmpegPath} -i "${savePath}" -y -vframes 1 -vf "scale=min(170\\,iw*min(170/iw\\,170/ih)):min(170\\,ih*min(170/iw\\,170/ih))" "${coverPath}"`
            await execCommand(command);
        }
        //上传文件
        uploadFile(messageId, savePath, coverPath);
        resolve();
    });
}

const uploadFile = (messageId, savePath, coverPath) => {
    // 创建FormData对象，并添加图片文件到其中
    const formData = new FormData();
    formData.append("messageId", messageId);
    formData.append('file', fs.createReadStream(savePath));
    //封面存在就上传封面
    if (coverPath) {
        formData.append('cover', fs.createReadStream(coverPath));
    }
    // 设置POST请求参数
    const url = `${getDomain()}/api/chat/uploadFile`;
    const token = store.getUserData("token");
    const config = { headers: { 'Content-Type': 'multipart/form-data', "token": token } };
    // 发送POST请求
    axios.post(url, formData, config)
        .then((response) => {

        })
        .catch((error) => {
            console.error('文件上传成功失败', error);
        });
}

const createCover = (filePath) => {
    return new Promise(async (resolve, reject) => {
        let ffmpegPath = getFFmegPath();
        let avatarPath = await getLocalFilePath("avatar", false, store.getUserId() + "_temp");
        let command = `${ffmpegPath} -i "${filePath}" "${avatarPath}" -y`
        await execCommand(command);

        let coverPath = await getLocalFilePath("avatar", false, store.getUserId() + "_temp_cover");
        command = `${ffmpegPath} -i "${filePath}" -y -vframes 1 -vf "scale=min(60\\,iw*min(60/iw\\,60/ih)):min(60\\,ih*min(60/iw\\,60/ih))" "${coverPath}"`
        await execCommand(command);

        resolve({
            avatarStream: fs.readFileSync(avatarPath),
            coverStream: fs.readFileSync(coverPath),
        });
    });

}

const execCommand = (command) => {
    return new Promise((resolve, reject) => {
        exec(command, (error, stdout, stderr) => {
            console.log("ffmpeg命令:", command);
            if (error) {
                console.error('执行命令失败', error);
            }
            console.log("ffmpeg命令:", command, stdout);
            resolve(stdout);
        });
    })
}

const getDomain = () => {
    return NODE_ENV !== 'development' ? store.getData("prodDomain") : store.getData("devDomain")
}


//express 本地服务器
const FILE_TYPE_CONTENT_TYPE = {
    "0": "image/",
    "1": "video/",
    "2": "application/octet-stream"
}


const checkFile = () => {
    ipcMain.on("checkFile", async (e, fileId) => {
        const localPath = await getLocalFilePath("chat", false, fileId);
        if (!fs.existsSync(localPath)) {
            await downloadFile(fileId, false, localPath, "chat");
        }
        e.sender.send("checkFileCallback", { fileId, status: 1 });
    });
}
let server = null;
const startLocalServer = (serverPort) => {
    server = expressServer.listen(serverPort, () => {
        console.log('本地服务在 http://127.0.0.1:' + serverPort + "开启");
    })
}

const closeLocalServer = () => {
    server.close();
}

const getLocalFilePath = async (partType, showCover, fileId) => {
    return new Promise(async (resolve, reject) => {
        let localFolder = store.getUserData("localFileFolder");
        let localPath = null;
        if (partType == "avatar") { //头像
            localFolder = localFolder + "/avatar/"
            if (!fs.existsSync(localFolder)) {
                mkdirs(localFolder);
            }
            localPath = localFolder + fileId + image_suffix;
        } else if (partType == "chat") {
            let messageInfo = await selectByMessageId(fileId);
            const month = moment(Number.parseInt(messageInfo.sendTime)).format('YYYYMM');
            localFolder = localFolder + "/" + month;
            if (!fs.existsSync(localFolder)) {
                mkdirs(localFolder);
            }
            let fileSuffix = messageInfo.fileName;
            fileSuffix = fileSuffix.substring(fileSuffix.lastIndexOf("."));
            localPath = localFolder + "/" + fileId + fileSuffix;
        } else if (partType == "tmp") {
            localFolder = localFolder + "/tmp/"
            if (!fs.existsSync(localFolder)) {
                mkdirs(localFolder);
            }
            localPath = localFolder + "/" + fileId
        } else {
            localPath = localFolder + "/" + fileId
        }
        if (showCover) {
            localPath = localPath + cover_image_suffix;
        }
        resolve(localPath);
    });

}

expressServer.get('/file', async (req, res) => {
    let { partType, fileType, fileId, showCover, forceGet } = req.query;
    //console.log("getFile", partType, fileType, fileId, showCover, forceGet);
    if (!partType || !fileId) {
        res.send("请求参数错误");
        return;
    }
    showCover = showCover == undefined ? false : Boolean(showCover);
    const localPath = await getLocalFilePath(partType, showCover, fileId);
    if (!fs.existsSync(localPath) || forceGet == "true") {
        if (forceGet == "true" && partType == "avatar") {
            await downloadFile(fileId, true, localPath + cover_image_suffix, partType);
        }
        await downloadFile(fileId, showCover, localPath, partType);
    }

    //console.log("获取图片", new Date().getTime(), fileId, forceGet, partType);
    if (forceGet == "true" && partType == "avatar") {
        getWindow("main").webContents.send("reloadAvatar", fileId);
    }

    const fileSuffix = localPath.substring(localPath.lastIndexOf(".") + 1);
    //图片直接返回
    let contentType = FILE_TYPE_CONTENT_TYPE[fileType] + fileSuffix;
    res.setHeader("Access-Control-Allow-Origin", "*");
    res.setHeader('Content-Type', contentType);
    //头像，缩略图，文件
    if (showCover || fileType != "1") {
        fs.createReadStream(localPath).pipe(res);
        return;
    }
    //视频文件 需要能够拖动需要通过range来获取文件流
    let stat = fs.statSync(localPath);
    let fileSize = stat.size;
    let range = req.headers.range;
    if (range) {
        //有range头才使用206状态码
        let parts = range.replace(/bytes=/, "").split("-");
        let start = parseInt(parts[0], 10);
        let end = parts[1] ? parseInt(parts[1], 10) : start + 999999;

        // end 在最后取值为 fileSize - 1 
        end = end > fileSize - 1 ? fileSize - 1 : end;

        let chunksize = (end - start) + 1;
        let stream = fs.createReadStream(localPath, {
            start,
            end
        });
        let head = {
            'Content-Range': `bytes ${start}-${end}/${fileSize}`,
            'Accept-Ranges': 'bytes',
            'Content-Length': chunksize,
            'Content-Type': 'video/mp4',
        };
        res.writeHead(206, head);
        stream.pipe(res);
    } else {
        let head = {
            'Content-Length': fileSize,
            'Content-Type': 'video/mp4',
        };
        res.writeHead(200, head);
        fs.createReadStream(localPath).pipe(res);
    }
})

const downloadFile = (fileId, showCover, savePath, partType) => {
    showCover = showCover + "";
    let url = `${getDomain()}/api/chat/downloadFile`;
    const token = store.getUserData("token");
    return new Promise(async (resolve, reject) => {
        const config = { responseType: 'stream', headers: { 'Content-Type': 'multipart/form-data', "token": token } };
        // 发送POST请求
        let response = await axios.post(url, {
            fileId,
            showCover
        }, config);
        const folder = savePath.substring(0, savePath.lastIndexOf("/"));
        mkdirs(folder);
        const stream = fs.createWriteStream(savePath);
        if (response.headers["content-type"] == "application/json") {
            //console.log("获取图片失败", url);
            let resourcesPath = path.join(app.getAppPath(), '/');
            if (NODE_ENV !== 'development') {
                resourcesPath = path.join(path.dirname(app.getPath('exe')), '/resources/');
            }
            if (partType == "avatar") {
                fs.createReadStream(resourcesPath + "assets/user.png").pipe(stream);
            } else {
                fs.createReadStream(resourcesPath + "assets/404.png").pipe(stream);
            }
        } else {
            response.data.pipe(stream);
        }
        stream.on('finish', () => {
            stream.close();
            resolve();
        });
    });
}

const saveAs = async ({ partType, fileId }) => {
    let fileName = "";
    if (partType == "avatar") {
        fileName = fileId + image_suffix;
    } else if (partType == "chat") {
        let messageInfo = await selectByMessageId(fileId);
        fileName = messageInfo.fileName;
    }
    const localPath = await getLocalFilePath(partType, false, fileId);
    const options = {
        title: '保存文件', // 对话框标题
        defaultPath: fileName
    }
    // 显示保存文件的对话框
    let result = await dialog.showSaveDialog(options);
    if (result.canceled || result.filePath == '') {
        return;
    }
    const filePath = result.filePath;
    fs.copyFileSync(localPath, filePath);
}

const changeLocalFolder = async () => {
    let settingInfo = await selectSettingInfo(store.getUserId());
    const sysSetting = JSON.parse(settingInfo.sysSetting);
    let localFileFolder = sysSetting.localFileFolder
    const options = {
        properties: ['openDirectory'],
        defaultPath: localFileFolder
    }
    // 显示保存文件的对话框
    let result = await dialog.showOpenDialog(options);
    if (result.canceled) {
        return;
    }
    //拷贝目录
    if (localFileFolder !== result.filePaths[0]) {
        const userId = store.getUserId();
        getWindow("main").webContents.send("copyingCallback");
        await fse.copy(localFileFolder + "/" + userId, result.filePaths[0] + "/" + userId)
    }
    sysSetting.localFileFolder = result.filePaths[0] + "\\";
    const sysSettingJson = JSON.stringify(sysSetting)
    await updateSysSetting(sysSettingJson);
    //更新路径
    store.setUserData("localFileFolder", sysSetting.localFileFolder + store.getUserId())

    getWindow("main").webContents.send("getSysSettingCallback", sysSettingJson);
}

const openLocalFolder = async () => {
    let settingInfo = await selectSettingInfo(store.getUserId());
    const sysSetting = JSON.parse(settingInfo.sysSetting);
    const localFileFolder = sysSetting.localFileFolder;

    if (!fs.existsSync(localFileFolder)) {
        mkdirs(localFileFolder);
    }

    shell.openPath('file:///' + localFileFolder);
}

const downloadUpdate = async (id, fileName) => {
    let url = `${store.getData("domain")}/api/update/download`;
    const token = store.getUserData("token");
    const config = {
        responseType: 'stream',
        headers: { 'Content-Type': 'multipart/form-data', "token": token }, onDownloadProgress(progress) {
            const loaded = progress.loaded
            getWindow("main").webContents.send("updateDownloadCallback", loaded);
        }
    }
    // 发送POST请求
    const response = await axios.post(url, { id }, config);
    const localFile = await getLocalFilePath(null, false, fileName);
    const stream = fs.createWriteStream(localFile);
    response.data.pipe(stream);
    stream.on('finish', async () => {
        stream.close();
        //开始安装
        const command = `${localFile}`
        execCommand(command);
    });
}

//保存剪切板内容
const saveClipBoardFile = async (file) => {
    const fileSuffix = file.name.substring(file.name.lastIndexOf("."));
    const filePath = await getLocalFilePath("tmp", false, "tmp" + fileSuffix);
    let byteArray = file.byteArray;
    const buffer = Buffer.from(byteArray);
    fs.writeFileSync(filePath, buffer);
    return {
        size: byteArray.length,
        name: file.name,
        path: filePath
    };
}

//文件管理
export {
    saveClipBoardFile,
    saveFile2Local,
    startLocalServer,
    closeLocalServer,
    checkFile,
    createCover,
    saveAs,
    changeLocalFolder,
    openLocalFolder,
    downloadUpdate
}