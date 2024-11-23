"use strict";
const electron = require("electron");
const path$1 = require("path");
const utils = require("@electron-toolkit/utils");
const WebSocket = require("ws");
const icon = path$1.join(__dirname, "../../resources/icon.png");
const add_tables = [
  "create table if not exists  chat_message(   user_id varchar not null,   message_id bigint not null default null,   session_id varchar,   message_type integer,   message_content varchar,   contact_type integer,   send_user_id varchar,   send_user_nick_name varchar,   send_time bigint,   status integer,   file_size bigint,   file_name varchar,   file_path varchar,   file_type integer,   primary key(user_id, message_id));",
  "create table if not exists chat_session_user(   user_id varchar not null default 0,   contact_id varchar(11) not null,   contact_type integer,   session_id varchar(11),   status integer default 1,   contact_name varchar(20),   last_message varchar(500),   last_receive_time bigint,   no_read_count integer default 0,   member_count integer,   top_type integer default 0,   primary key (user_id, contact_id));",
  "create table if not exists user_setting (   user_id varchar not null,   email varchar not null,   sys_setting varchar,   contact_no_read integer,   server_port integer,   primary key (user_id));"
];
const add_indexes = [
  "create index if not exists idx_session_id on chat_message( session_id asc);"
];
const alter_tables = [
  /* {
      tableName: "user_setting",
      field: "email",
      sql: "alter table user_setting add column email varchar"
  } */
];
const fs$1 = require("fs");
const sqlite3 = require("sqlite3").verbose();
const os$1 = require("os");
const NODE_ENV$4 = process.env.NODE_ENV;
const userDir$1 = os$1.homedir();
const dbFolder = userDir$1 + (NODE_ENV$4 === "development" ? "/.easychatdev/" : "/.easychat/");
if (!fs$1.existsSync(dbFolder)) {
  fs$1.mkdirSync(dbFolder);
}
const db = new sqlite3.Database(dbFolder + "local.db");
const createTable = async () => {
  return new Promise(async (resolve, reject) => {
    for (const item of add_tables) {
      await run(item, []);
    }
    for (const item of add_indexes) {
      await run(item, []);
    }
    for (const item of alter_tables) {
      const fieldList = await queryAll(`pragma table_info(${item.tableName})`, []);
      const field = fieldList.some((row) => row.name === item.field);
      if (!field) {
        await run(item.sql, []);
      }
    }
    resolve();
  });
};
const toCamelCase = (str) => {
  return str.replace(/_([a-z])/g, function(match, p1) {
    return String.fromCharCode(p1.charCodeAt(0) - 32);
  });
};
const convertDbObj2BizObj = (data) => {
  if (!data) {
    return null;
  }
  const bizData = {};
  for (let item in data) {
    bizData[toCamelCase(item)] = data[item];
  }
  return bizData;
};
const globalColumnsMap = {};
const run = (sql, params) => {
  return new Promise((resolve, reject) => {
    const stmt = db.prepare(sql);
    stmt.run(params, function(err, row) {
      if (err) {
        console.error(`执行的sql:${sql},params:${params},执行失败:${err}`);
        reject("查询数据库失败");
      }
      console.log(`执行的sql:${sql},params:${params}执行记录数:${this.changes}`);
      resolve(this.changes);
    });
    stmt.finalize();
  }).catch((error) => {
    console.error(error);
  });
};
const queryCount = (sql, params) => {
  return new Promise((resolve, reject) => {
    const stmt = db.prepare(sql);
    stmt.get(params, function(err, row) {
      console.log(`执行的sql:${sql},params:${params},row:${row}`);
      if (err) {
        console.error(err);
        resolve(0);
      }
      resolve(Array.from(Object.values(row))[0]);
    });
    stmt.finalize();
  });
};
const queryOne = (sql, params) => {
  return new Promise((resolve, reject) => {
    const stmt = db.prepare(sql);
    stmt.get(params, function(err, row) {
      if (err) {
        console.error(err);
        resolve({});
      }
      resolve(convertDbObj2BizObj(row));
      console.log(`执行的sql:${sql},params:${params},row:${JSON.stringify(row)}`);
    });
    stmt.finalize();
  });
};
const queryAll = (sql, params) => {
  return new Promise((resolve, reject) => {
    const stmt = db.prepare(sql);
    stmt.all(params, function(err, row) {
      if (err) {
        console.error(err);
        resolve([]);
      }
      row.forEach((item, index) => {
        row[index] = convertDbObj2BizObj(item);
      });
      console.log(`执行的sql:${sql},params:${params},row:${JSON.stringify(row)}`);
      resolve(row);
    });
    stmt.finalize();
  });
};
const insert = (sqlPrefix, tableName, data) => {
  const columnsMap = globalColumnsMap[tableName];
  const dbColumns = [];
  const params = [];
  for (let item in data) {
    if (data[item] != void 0 && columnsMap[item] != void 0) {
      dbColumns.push(columnsMap[item]);
      params.push(data[item]);
    }
  }
  const preper = "?".repeat(dbColumns.length).split("").join(",");
  const sql = `${sqlPrefix} ${tableName}(${dbColumns.join(",")})values(${preper})`;
  return run(sql, params);
};
const insertOrReplace = (tableName, data) => {
  return insert("insert or replace into", tableName, data);
};
const insertOrIgnore = (tableName, data) => {
  return insert("insert or ignore into", tableName, data);
};
const update = (tableName, data, paramData) => {
  const columnsMap = globalColumnsMap[tableName];
  const dbColumns = [];
  const params = [];
  const whereColumns = [];
  for (let item in data) {
    if (data[item] != void 0 && columnsMap[item] != void 0) {
      dbColumns.push(`${columnsMap[item]} = ?`);
      params.push(data[item]);
    }
  }
  for (let item in paramData) {
    if (paramData[item]) {
      params.push(paramData[item]);
      whereColumns.push(`${columnsMap[item]} = ?`);
    }
  }
  const sql = `update ${tableName} set ${dbColumns.join(",")} ${whereColumns.length > 0 ? " where " : " "} ${whereColumns.join(" and ")}`;
  return run(sql, params);
};
const initTableColumnsMap = async () => {
  let sql = "select name from sqlite_master WHERE type='table' and name!='sqlite_sequence'";
  let tables = await queryAll(sql, []);
  for (let i = 0; i < tables.length; i++) {
    sql = `PRAGMA table_info(${tables[i].name})`;
    let columns = await queryAll(sql, []);
    const columnsMapItem = {};
    for (let j = 0; j < columns.length; j++) {
      columnsMapItem[toCamelCase(columns[j].name)] = columns[j].name;
    }
    globalColumnsMap[tables[i].name] = columnsMapItem;
  }
};
const init = () => {
  db.serialize(async () => {
    await createTable();
    initTableColumnsMap();
  });
};
init();
const Store = require("electron-store");
const store = new Store();
let userId = null;
const initUserId = (_userId) => {
  userId = _userId;
};
const setData = (key, value) => {
  store.set(key, value);
};
const getData = (key) => {
  return store.get(key);
};
const setUserData = (key, value) => {
  setData(userId + key, value);
};
const getUserData = (key) => {
  return getData(userId + key);
};
const getUserId = () => {
  return userId;
};
const deleteUserData = (key) => {
  store.delete(userId + key);
};
const store$1 = {
  initUserId,
  setData,
  getData,
  setUserData,
  getUserData,
  getUserId,
  deleteUserData
};
const addChatSession = (sessionInfo) => {
  sessionInfo.userId = store$1.getUserId();
  insertOrIgnore("chat_session_user", sessionInfo);
};
const updateChatSession = (sessionInfo) => {
  const paramData = {
    userId: store$1.getUserId(),
    contactId: sessionInfo.contactId
  };
  const updateInfo = Object.assign({}, sessionInfo);
  updateInfo.contactId = null;
  updateInfo.userId = null;
  return update("chat_session_user", updateInfo, paramData);
};
const saveOrUpdateChatSessionBatch4Init = (chatSessionList) => {
  return new Promise(async (resolve, reject) => {
    try {
      for (let i = 0; i < chatSessionList.length; i++) {
        const sessionInfo = chatSessionList[i];
        sessionInfo.status = 1;
        let sessionData = await selectUserSessionByContactId(sessionInfo.contactId);
        if (sessionData) {
          await updateChatSession(sessionInfo);
        } else {
          await addChatSession(sessionInfo);
        }
      }
      resolve();
    } catch (error) {
      resolve();
    }
  });
};
const updateNoReadCount = ({ contactId, noReadCount }) => {
  let sql = "update chat_session_user set no_read_count = no_read_count+? where user_id=? and contact_id=?";
  return run(sql, [noReadCount, store$1.getUserId(), contactId]);
};
const selectUserSessionList = () => {
  let sql = "select * from chat_session_user where user_id = ? and status = 1";
  return queryAll(sql, [store$1.getUserId()]);
};
const selectUserSessionByContactId = (concatId) => {
  let sql = "select * from chat_session_user where user_id = ? and contact_id = ?";
  return queryOne(sql, [store$1.getUserId(), concatId]);
};
const readAll = (contactId) => {
  let sql = "update chat_session_user set no_read_count = 0 where user_id=? and contact_id=?";
  return run(sql, [store$1.getUserId(), contactId]);
};
const saveOrUpdate4Message = (currentSessionId, sessionInfo) => {
  return new Promise(async (resolve, reject) => {
    let sessionData = await selectUserSessionByContactId(sessionInfo.contactId);
    if (sessionData) {
      await updateSessionInfo4Message(currentSessionId, sessionInfo);
    } else {
      sessionInfo.noReadCount = 1;
      await addChatSession(sessionInfo);
    }
    resolve();
  });
};
const updateSessionInfo4Message = async (currentSessionId, { sessionId, contactName, lastMessage, lastReceiveTime, contactId, memberCount }) => {
  const params = [lastMessage, lastReceiveTime];
  let sql = "update chat_session_user set last_message=?,last_receive_time=?,status = 1";
  if (contactName) {
    sql = sql + ",contact_name = ?";
    params.push(contactName);
  }
  if (memberCount != null) {
    sql = sql + ",member_count =?";
    params.push(memberCount);
  }
  if (sessionId !== currentSessionId) {
    sql = sql + ",no_read_count = no_read_count + 1";
  }
  sql = sql + " where user_id = ? and contact_id = ?";
  params.push(store$1.getUserId());
  params.push(contactId);
  return run(sql, params);
};
const delChatSession = (contactId) => {
  const paramData = {
    userId: store$1.getUserId(),
    contactId
  };
  const sessionInfo = {
    status: 0
  };
  return update("chat_session_user", sessionInfo, paramData);
};
const topChatSession = (contactId, topType) => {
  const paramData = {
    userId: store$1.getUserId(),
    contactId
  };
  const sessionInfo = {
    topType
  };
  return update("chat_session_user", sessionInfo, paramData);
};
const updateGroupName = (contactId, groupName) => {
  const paramData = {
    userId: store$1.getUserId(),
    contactId
  };
  const sessionInfo = {
    contactName: groupName
  };
  return update("chat_session_user", sessionInfo, paramData);
};
const updateStatus = (contactId) => {
  const paramData = {
    userId: store$1.getUserId(),
    contactId
  };
  const sessionInfo = {
    status: 1
  };
  return update("chat_session_user", sessionInfo, paramData);
};
const getPageOffset = (pageNo = 1, totalCount) => {
  const pageSize = 20;
  const pageTotal = totalCount % pageSize == 0 ? totalCount / pageSize : Number.parseInt(totalCount / pageSize) + 1;
  pageNo = pageNo <= 1 ? 1 : pageNo;
  pageNo = pageNo >= pageTotal ? pageTotal : pageNo;
  return {
    pageTotal,
    offset: (pageNo - 1) * pageSize,
    limit: pageSize
  };
};
const selectMessageList = (query) => {
  return new Promise(async (resolve, reject) => {
    const { sessionId, pageNo, maxMessageId } = query;
    let sql = "select count(1) from chat_message where session_id = ? and user_id = ?";
    const totalCount = await queryCount(sql, [sessionId, store$1.getUserId()]);
    const { pageTotal, offset, limit } = getPageOffset(pageNo, totalCount);
    const params = [sessionId, store$1.getUserId()];
    sql = "select * from chat_message where session_id = ? and user_id = ?";
    if (maxMessageId) {
      sql = sql + "and message_id <=?";
      params.push(maxMessageId);
    }
    params.push(offset);
    params.push(limit);
    sql = sql + "order by message_id desc limit ?,?";
    const dataList = await queryAll(sql, params);
    resolve({
      dataList,
      pageTotal,
      pageNo
    });
  });
};
const saveMessage = (data) => {
  data.userId = store$1.getUserId();
  return insertOrReplace("chat_message", data);
};
const updateMessage = (data, paramData) => {
  paramData.userId = store$1.getUserId();
  return update("chat_message", data, paramData);
};
const saveMessageBatch = (chatMessageList) => {
  return new Promise(async (resolve, reject) => {
    const chatSessionCountMap = {};
    chatMessageList.forEach((item) => {
      let contactId = item.contactType == 1 ? item.contactId : item.sendUserId;
      let noReadCount = chatSessionCountMap[contactId];
      if (!noReadCount) {
        chatSessionCountMap[contactId] = 1;
      } else {
        chatSessionCountMap[contactId] = noReadCount + 1;
      }
    });
    for (let item in chatSessionCountMap) {
      await updateNoReadCount({ contactId: item, noReadCount: chatSessionCountMap[item] });
    }
    chatMessageList.forEach(async (item) => {
      await saveMessage(item);
    });
    resolve();
  });
};
const selectByMessageId = (messageId) => {
  let sql = "select * from chat_message where message_id = ? and user_id =?";
  const params = [messageId, store$1.getUserId()];
  return queryOne(sql, params);
};
const windowManage = {};
const saveWindow = (id, window) => {
  windowManage[id] = window;
};
const getWindow = (id) => {
  return windowManage[id];
};
const delWindow = (id) => {
  delete windowManage[id];
};
const fs = require("fs");
const fse = require("fs-extra");
const NODE_ENV$3 = process.env.NODE_ENV;
const path = require("path");
const { app, ipcMain, shell } = require("electron");
const { exec } = require("child_process");
const FormData = require("form-data");
const axios = require("axios");
const moment = require("moment");
moment.locale("zh-cn", {});
const { dialog } = require("electron");
const express = require("express");
const expressServer = express();
const cover_image_suffix = "_cover.png";
const image_suffix = ".png";
const ffprobePath = "/assets/ffprobe.exe";
const ffmpegPath = "/assets/ffmpeg.exe";
const mkdirs = (dir) => {
  if (!fs.existsSync(dir)) {
    const parentDir = path.dirname(dir);
    if (parentDir !== dir) {
      mkdirs(parentDir);
    }
    fs.mkdirSync(dir);
  }
};
const getResourcesPath = () => {
  let resourcesPath = app.getAppPath();
  if (NODE_ENV$3 !== "development") {
    resourcesPath = path.dirname(app.getPath("exe")) + "/resources";
  }
  return resourcesPath;
};
const getFFprobePath = () => {
  return path.join(getResourcesPath(), ffprobePath);
};
const getFFmegPath = () => {
  return path.join(getResourcesPath(), ffmpegPath);
};
const saveFile2Local = async (messageId, filePath, fileType) => {
  return new Promise(async (resolve, reject) => {
    let ffmpegPath2 = getFFmegPath();
    let savePath = await getLocalFilePath("chat", false, messageId);
    let coverPath = null;
    fs.copyFileSync(filePath, savePath);
    if (fileType != 2) {
      let command = `${getFFprobePath()} -v error -select_streams v:0 -show_entries stream=codec_name "${filePath}"`;
      let result = await execCommand(command);
      result = result.replaceAll("\r\n", "");
      result = result.substring(result.indexOf("=") + 1);
      let codec = result.substring(0, result.indexOf("["));
      if ("hevc" === codec) {
        command = `${ffmpegPath2}  -y -i "${filePath}" -c:v libx264 -crf 20 "${savePath}"`;
        await execCommand(command);
      }
      coverPath = savePath + cover_image_suffix;
      command = `${ffmpegPath2} -i "${savePath}" -y -vframes 1 -vf "scale=min(170\\,iw*min(170/iw\\,170/ih)):min(170\\,ih*min(170/iw\\,170/ih))" "${coverPath}"`;
      await execCommand(command);
    }
    uploadFile(messageId, savePath, coverPath);
    resolve();
  });
};
const uploadFile = (messageId, savePath, coverPath) => {
  const formData = new FormData();
  formData.append("messageId", messageId);
  formData.append("file", fs.createReadStream(savePath));
  if (coverPath) {
    formData.append("cover", fs.createReadStream(coverPath));
  }
  const url = `${getDomain()}/api/chat/uploadFile`;
  const token = store$1.getUserData("token");
  const config = { headers: { "Content-Type": "multipart/form-data", "token": token } };
  axios.post(url, formData, config).then((response) => {
  }).catch((error) => {
    console.error("文件上传成功失败", error);
  });
};
const createCover = (filePath) => {
  return new Promise(async (resolve, reject) => {
    let ffmpegPath2 = getFFmegPath();
    let avatarPath = await getLocalFilePath("avatar", false, store$1.getUserId() + "_temp");
    let command = `${ffmpegPath2} -i "${filePath}" "${avatarPath}" -y`;
    await execCommand(command);
    let coverPath = await getLocalFilePath("avatar", false, store$1.getUserId() + "_temp_cover");
    command = `${ffmpegPath2} -i "${filePath}" -y -vframes 1 -vf "scale=min(60\\,iw*min(60/iw\\,60/ih)):min(60\\,ih*min(60/iw\\,60/ih))" "${coverPath}"`;
    await execCommand(command);
    resolve({
      avatarStream: fs.readFileSync(avatarPath),
      coverStream: fs.readFileSync(coverPath)
    });
  });
};
const execCommand = (command) => {
  return new Promise((resolve, reject) => {
    exec(command, (error, stdout, stderr) => {
      console.log("ffmpeg命令:", command);
      if (error) {
        console.error("执行命令失败", error);
      }
      console.log("ffmpeg命令:", command, stdout);
      resolve(stdout);
    });
  });
};
const getDomain = () => {
  return NODE_ENV$3 !== "development" ? store$1.getData("prodDomain") : store$1.getData("devDomain");
};
const FILE_TYPE_CONTENT_TYPE = {
  "0": "image/",
  "1": "video/",
  "2": "application/octet-stream"
};
const checkFile = () => {
  ipcMain.on("checkFile", async (e, fileId) => {
    const localPath = await getLocalFilePath("chat", false, fileId);
    if (!fs.existsSync(localPath)) {
      await downloadFile(fileId, false, localPath, "chat");
    }
    e.sender.send("checkFileCallback", { fileId, status: 1 });
  });
};
let server = null;
const startLocalServer = (serverPort) => {
  server = expressServer.listen(serverPort, () => {
    console.log("本地服务在 http://127.0.0.1:" + serverPort + "开启");
  });
};
const closeLocalServer = () => {
  server.close();
};
const getLocalFilePath = async (partType, showCover, fileId) => {
  return new Promise(async (resolve, reject) => {
    let localFolder = store$1.getUserData("localFileFolder");
    let localPath = null;
    if (partType == "avatar") {
      localFolder = localFolder + "/avatar/";
      if (!fs.existsSync(localFolder)) {
        mkdirs(localFolder);
      }
      localPath = localFolder + fileId + image_suffix;
    } else if (partType == "chat") {
      let messageInfo = await selectByMessageId(fileId);
      const month = moment(Number.parseInt(messageInfo.sendTime)).format("YYYYMM");
      localFolder = localFolder + "/" + month;
      if (!fs.existsSync(localFolder)) {
        mkdirs(localFolder);
      }
      let fileSuffix = messageInfo.fileName;
      fileSuffix = fileSuffix.substring(fileSuffix.lastIndexOf("."));
      localPath = localFolder + "/" + fileId + fileSuffix;
    } else if (partType == "tmp") {
      localFolder = localFolder + "/tmp/";
      if (!fs.existsSync(localFolder)) {
        mkdirs(localFolder);
      }
      localPath = localFolder + "/" + fileId;
    } else {
      localPath = localFolder + "/" + fileId;
    }
    if (showCover) {
      localPath = localPath + cover_image_suffix;
    }
    resolve(localPath);
  });
};
expressServer.get("/file", async (req, res) => {
  let { partType, fileType, fileId, showCover, forceGet } = req.query;
  if (!partType || !fileId) {
    res.send("请求参数错误");
    return;
  }
  showCover = showCover == void 0 ? false : Boolean(showCover);
  const localPath = await getLocalFilePath(partType, showCover, fileId);
  if (!fs.existsSync(localPath) || forceGet == "true") {
    if (forceGet == "true" && partType == "avatar") {
      await downloadFile(fileId, true, localPath + cover_image_suffix, partType);
    }
    await downloadFile(fileId, showCover, localPath, partType);
  }
  if (forceGet == "true" && partType == "avatar") {
    getWindow("main").webContents.send("reloadAvatar", fileId);
  }
  const fileSuffix = localPath.substring(localPath.lastIndexOf(".") + 1);
  let contentType = FILE_TYPE_CONTENT_TYPE[fileType] + fileSuffix;
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader("Content-Type", contentType);
  if (showCover || fileType != "1") {
    fs.createReadStream(localPath).pipe(res);
    return;
  }
  let stat = fs.statSync(localPath);
  let fileSize = stat.size;
  let range = req.headers.range;
  if (range) {
    let parts = range.replace(/bytes=/, "").split("-");
    let start = parseInt(parts[0], 10);
    let end = parts[1] ? parseInt(parts[1], 10) : start + 999999;
    end = end > fileSize - 1 ? fileSize - 1 : end;
    let chunksize = end - start + 1;
    let stream = fs.createReadStream(localPath, {
      start,
      end
    });
    let head = {
      "Content-Range": `bytes ${start}-${end}/${fileSize}`,
      "Accept-Ranges": "bytes",
      "Content-Length": chunksize,
      "Content-Type": "video/mp4"
    };
    res.writeHead(206, head);
    stream.pipe(res);
  } else {
    let head = {
      "Content-Length": fileSize,
      "Content-Type": "video/mp4"
    };
    res.writeHead(200, head);
    fs.createReadStream(localPath).pipe(res);
  }
});
const downloadFile = (fileId, showCover, savePath, partType) => {
  showCover = showCover + "";
  let url = `${getDomain()}/api/chat/downloadFile`;
  const token = store$1.getUserData("token");
  return new Promise(async (resolve, reject) => {
    const config = { responseType: "stream", headers: { "Content-Type": "multipart/form-data", "token": token } };
    let response = await axios.post(url, {
      fileId,
      showCover
    }, config);
    const folder = savePath.substring(0, savePath.lastIndexOf("/"));
    mkdirs(folder);
    const stream = fs.createWriteStream(savePath);
    if (response.headers["content-type"] == "application/json") {
      let resourcesPath = path.join(app.getAppPath(), "/");
      if (NODE_ENV$3 !== "development") {
        resourcesPath = path.join(path.dirname(app.getPath("exe")), "/resources/");
      }
      if (partType == "avatar") {
        fs.createReadStream(resourcesPath + "assets/user.png").pipe(stream);
      } else {
        fs.createReadStream(resourcesPath + "assets/404.png").pipe(stream);
      }
    } else {
      response.data.pipe(stream);
    }
    stream.on("finish", () => {
      stream.close();
      resolve();
    });
  });
};
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
    title: "保存文件",
    // 对话框标题
    defaultPath: fileName
  };
  let result = await dialog.showSaveDialog(options);
  if (result.canceled || result.filePath == "") {
    return;
  }
  const filePath = result.filePath;
  fs.copyFileSync(localPath, filePath);
};
const changeLocalFolder = async () => {
  let settingInfo = await selectSettingInfo(store$1.getUserId());
  const sysSetting = JSON.parse(settingInfo.sysSetting);
  let localFileFolder = sysSetting.localFileFolder;
  const options = {
    properties: ["openDirectory"],
    defaultPath: localFileFolder
  };
  let result = await dialog.showOpenDialog(options);
  if (result.canceled) {
    return;
  }
  if (localFileFolder !== result.filePaths[0]) {
    const userId2 = store$1.getUserId();
    getWindow("main").webContents.send("copyingCallback");
    await fse.copy(localFileFolder + "/" + userId2, result.filePaths[0] + "/" + userId2);
  }
  sysSetting.localFileFolder = result.filePaths[0] + "\\";
  const sysSettingJson = JSON.stringify(sysSetting);
  await updateSysSetting(sysSettingJson);
  store$1.setUserData("localFileFolder", sysSetting.localFileFolder + store$1.getUserId());
  getWindow("main").webContents.send("getSysSettingCallback", sysSettingJson);
};
const openLocalFolder = async () => {
  let settingInfo = await selectSettingInfo(store$1.getUserId());
  const sysSetting = JSON.parse(settingInfo.sysSetting);
  const localFileFolder = sysSetting.localFileFolder;
  if (!fs.existsSync(localFileFolder)) {
    mkdirs(localFileFolder);
  }
  shell.openPath("file:///" + localFileFolder);
};
const downloadUpdate = async (id, fileName) => {
  let url = `${store$1.getData("domain")}/api/update/download`;
  const token = store$1.getUserData("token");
  const config = {
    responseType: "stream",
    headers: { "Content-Type": "multipart/form-data", "token": token },
    onDownloadProgress(progress) {
      const loaded = progress.loaded;
      getWindow("main").webContents.send("updateDownloadCallback", loaded);
    }
  };
  const response = await axios.post(url, { id }, config);
  const localFile = await getLocalFilePath(null, false, fileName);
  const stream = fs.createWriteStream(localFile);
  response.data.pipe(stream);
  stream.on("finish", async () => {
    stream.close();
    const command = `${localFile}`;
    execCommand(command);
  });
};
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
};
const os = require("os");
const userDir = os.homedir();
const updateContactNoReadCount = ({ userId: userId2, noReadCount }) => {
  return new Promise(async (resolve, reject) => {
    let sql = null;
    if (noReadCount) {
      sql = "update user_setting set contact_no_read = contact_no_read+? where user_id = ?";
    } else {
      noReadCount = 0;
      sql = "update user_setting set contact_no_read = ? where user_id = ?";
    }
    await run(sql, [noReadCount, userId2]);
    resolve();
  });
};
const addUserSetting = async (userId2, email) => {
  let sql = "select max(server_port) server_port from user_setting";
  let { serverPort } = await queryOne(sql, []);
  if (serverPort == null) {
    serverPort = 10240;
  } else {
    serverPort++;
  }
  const sysSetting = {
    localFileFolder: userDir + "\\.easychat\\fileStorage\\"
  };
  sql = "select * from user_setting where user_id =?";
  const userInfo = await queryOne(sql, [userId2]);
  let resultServerPort = null;
  let localFileFolder = sysSetting.localFileFolder + userId2;
  if (userInfo) {
    await update("user_setting", { "email": email }, { "userId": userId2 });
    resultServerPort = userInfo.serverPort;
    localFileFolder = JSON.parse(userInfo.sysSetting).localFileFolder + userId2;
  } else {
    await insertOrIgnore("user_setting", {
      userId: userId2,
      email,
      sysSetting: JSON.stringify(sysSetting),
      contactNoRead: 0,
      serverPort
    });
    resultServerPort = serverPort;
  }
  startLocalServer(resultServerPort);
  store$1.setUserData("localServerPort", resultServerPort);
  store$1.setUserData("localFileFolder", localFileFolder);
};
const selectSettingInfo = (userId2) => {
  let sql = "select  * from  user_setting where user_id = ?";
  return queryOne(sql, [userId2]);
};
const updateSysSetting = (sysSetting) => {
  const data = {
    sysSetting
  };
  const paramData = {
    userId: store$1.getUserId()
  };
  return update("user_setting", data, paramData);
};
const loadLocalUser = async () => {
  let sql = "select email from user_setting where email is not null";
  return queryAll(sql, []);
};
const NODE_ENV$2 = process.env.NODE_ENV;
let ws = null;
let maxReConnectTimes = null;
let lockReconnect = false;
let wsUrl = null;
let sender = null;
let needReconnect = null;
const initWs = (config, _sender) => {
  wsUrl = `${NODE_ENV$2 !== "development" ? store$1.getData("prodWsDomain") : store$1.getData("devWsDomain")}?token=${config.token}`;
  sender = _sender;
  needReconnect = true;
  maxReConnectTimes = 5;
  createWs();
};
const closeWs = () => {
  needReconnect = false;
  ws.close();
};
const createWs = () => {
  if (wsUrl == null) {
    return;
  }
  ws = new WebSocket(wsUrl);
  ws.onopen = function(params) {
    console.log("客户端连接成功");
    ws.send("heart beat");
    maxReConnectTimes = 5;
  };
  ws.onmessage = async function(e) {
    let mainWindow = getWindow("main");
    if (!mainWindow.isFocused()) {
      mainWindow.flashFrame(true);
    }
    console.log("收到服务器消息", e.data);
    const message = JSON.parse(e.data);
    const leaveGroupUserId = message.extendData;
    const messageType = message.messageType;
    switch (messageType) {
      case 0:
        await saveOrUpdateChatSessionBatch4Init(message.extendData.chatSessionList);
        await saveMessageBatch(message.extendData.chatMessageList);
        await updateContactNoReadCount({ userId: store$1.getUserId(), noReadCount: message.extendData.applyCount });
        sender.send("reciveMessage", { messageType: message.messageType });
        break;
      case 4:
        await updateContactNoReadCount({ userId: store$1.getUserId(), noReadCount: 1 });
        sender.send("reciveMessage", { messageType: message.messageType });
        break;
      case 6:
        updateMessage({ status: message.status }, { messageId: message.messageId });
        sender.send("reciveMessage", message);
        break;
      case 10:
        updateGroupName(message.contactId, message.extendData);
        sender.send("reciveMessage", message);
        break;
      case 7:
        sender.send("reciveMessage", message);
        closeWs();
        break;
      case 1:
      case 3:
      case 9:
      case 2:
      case 5:
      case 8:
      case 11:
      case 12:
        if (message.sendUserId === store$1.getUserId() && message.contactType == 1) {
          break;
        }
        const sessionInfo = {};
        if (message.extendData && typeof message.extendData === "object") {
          Object.assign(sessionInfo, message.extendData);
        } else {
          Object.assign(sessionInfo, message);
          if (message.contactType == 0 && messageType != 1) {
            sessionInfo.contactName = message.sendUserNickName;
          }
          sessionInfo.lastReceiveTime = message.sendTime;
        }
        if (messageType == 9 || messageType == 12 || messageType == 11) {
          sessionInfo.memberCount = message.memberCount;
        }
        console.log("sessionInfo", sessionInfo);
        await saveOrUpdate4Message(store$1.getUserData("currentSessionId"), sessionInfo);
        await saveMessage(message);
        const dbSessionInfo = await selectUserSessionByContactId(message.contactId);
        message.extendData = dbSessionInfo;
        if (messageType == 11 && leaveGroupUserId == store$1.getUserId()) {
          break;
        }
        sender.send("reciveMessage", message);
        break;
    }
  };
  ws.onclose = function(evt) {
    console.log("关闭客户端连接准备重连");
    reconnect("onclose");
  };
  ws.onerror = function(evt) {
    console.log("连接失败了准备重连");
    reconnect("onerror");
  };
  const reconnect = (type) => {
    if (!needReconnect) {
      console.log("链接断开无须重连");
      return;
    }
    if (ws != null) {
      ws.close();
    }
    if (lockReconnect) {
      return;
    }
    console.log(type + "准备重连");
    lockReconnect = true;
    if (maxReConnectTimes > 0) {
      console.log("准备重连，剩余重连次数" + maxReConnectTimes, (/* @__PURE__ */ new Date()).getTime());
      maxReConnectTimes--;
      setTimeout(function() {
        createWs();
        lockReconnect = false;
      }, 5e3);
    } else {
      console.log("TCP连接已超时");
    }
  };
  setInterval(() => {
    if (ws != null && ws.readyState == 1) {
      ws.send("heart beat");
    }
  }, 1e3 * 5);
};
const NODE_ENV$1 = process.env.NODE_ENV;
const onLoginOrRegister = (callback) => {
  electron.ipcMain.on("loginOrRegister", (e, isLogin) => {
    callback(isLogin);
  });
};
const onLoginSuccess = (callback) => {
  electron.ipcMain.on("openChat", async (e, config) => {
    store$1.initUserId(config.userId);
    store$1.setUserData("token", config.token);
    addUserSetting(config.userId, config.email);
    callback(config);
    initWs(config, e.sender);
  });
};
const onSetLocalStore = () => {
  electron.ipcMain.on("setLocalStore", (e, { key, value }) => {
    store$1.setData(key, value);
  });
};
const onGetLocalStore = () => {
  electron.ipcMain.on("getLocalStore", (e, key) => {
    e.sender.send("getLocalStoreCallback", store$1.getData(key));
  });
};
const onReLogin = (callback) => {
  electron.ipcMain.on("reLogin", (e, data) => {
    callback();
    e.sender.send("reLogin");
    closeWs();
    closeLocalServer();
  });
};
const winTitleOp = (callback) => {
  electron.ipcMain.on("winTitleOp", (e, data) => {
    callback(e, data);
  });
};
const onLoadChatMessage = () => {
  electron.ipcMain.on("loadChatMessage", async (e, data) => {
    const result = await selectMessageList(data);
    e.sender.send("loadChatMessage", result);
  });
};
const onLoadSessionData = () => {
  electron.ipcMain.on("loadSessionData", async (e) => {
    console.log("开始查询session");
    const result = await selectUserSessionList();
    e.sender.send("loadSessionDataCallback", result);
  });
};
const onSetSessionSelect = () => {
  electron.ipcMain.on("setSessionSelect", async (e, { contactId, sessionId }) => {
    console.log("设置选中的会话", sessionId);
    if (sessionId) {
      store$1.setUserData("currentSessionId", sessionId);
      readAll(contactId);
    } else {
      store$1.deleteUserData("currentSessionId");
    }
  });
};
const onLoadContactApply = () => {
  electron.ipcMain.on("loadContactApply", async (e) => {
    const userId2 = store$1.getUserId();
    let result = await selectSettingInfo(userId2);
    let contactNoRead = 0;
    if (result != null) {
      contactNoRead = result.contactNoRead;
    }
    e.sender.send("loadContactApplyCallback", contactNoRead);
  });
};
const onUpdateContactNoReadCount = () => {
  electron.ipcMain.on("updateContactNoReadCount", async (e) => {
    await updateContactNoReadCount({ userId: store$1.getUserId() });
  });
};
const onAddLocalMessage = () => {
  electron.ipcMain.on("addLocalMessage", async (e, data) => {
    await saveMessage(data);
    if (data.messageType == 5) {
      await saveFile2Local(data.messageId, data.filePath, data.fileType);
      const updateInfo = {
        status: 1
      };
      await updateMessage(updateInfo, { messageId: data.messageId });
    }
    data.lastReceiveTime = data.sendTime;
    updateSessionInfo4Message(store$1.getUserData("currentSessionId"), data);
    e.sender.send("addLocalCallback", { status: 1, messageId: data.messageId });
  });
};
const onSaveAs = () => {
  electron.ipcMain.on("saveAs", async (e, data) => {
    saveAs(data);
  });
};
checkFile();
const onCreateCover = () => {
  electron.ipcMain.on("createCover", async (e, localFilePath) => {
    const stream = await createCover(localFilePath);
    e.sender.send("createCoverCallback", stream);
  });
};
const onGetSettingInfo = () => {
  electron.ipcMain.on("getSysSetting", async (e) => {
    const userId2 = store$1.getUserId();
    let result = await selectSettingInfo(userId2);
    let sysSetting = result.sysSetting;
    e.sender.send("getSysSettingCallback", sysSetting);
  });
};
const onChangeLocalFolder = () => {
  electron.ipcMain.on("changeLocalFolder", async (e) => {
    changeLocalFolder();
  });
};
const onOpenLocalFolder = () => {
  electron.ipcMain.on("openLocalFolder", async (e) => {
    openLocalFolder();
  });
};
const onDownloadUpdate = () => {
  electron.ipcMain.on("downloadUpdate", async (e, { id, fileName }) => {
    downloadUpdate(id, fileName);
  });
};
const onOpenUrl = () => {
  electron.ipcMain.on("openUrl", async (e, { url }) => {
    electron.shell.openExternal(url);
  });
};
const onSaveClipBoardFile = () => {
  electron.ipcMain.on("saveClipBoardFile", async (e, file) => {
    const result = await saveClipBoardFile(file);
    console.log("result", result);
    e.sender.send("saveClipBoardFileCallback", result);
  });
};
const onOpenNewWindow = () => {
  electron.ipcMain.on("newWindow", (e, config) => {
    openWindow(config);
  });
};
const onLoadLocalUser = () => {
  electron.ipcMain.on("loadLocalUser", async (e) => {
    let userList = await loadLocalUser();
    e.sender.send("loadLocalUserCallback", userList);
  });
};
const onDelChatSession = () => {
  electron.ipcMain.on("delChatSession", (e, contactId) => {
    delChatSession(contactId);
  });
};
const onTopChatSession = () => {
  electron.ipcMain.on("topChatSession", (e, { contactId, topType }) => {
    topChatSession(contactId, topType);
  });
};
const onReloadChatSession = () => {
  electron.ipcMain.on("reloadChatSession", async (e, { contactId }) => {
    await updateStatus(contactId);
    const chatSessionDataList = await selectUserSessionList();
    e.sender.send("reloadChatSessionCallback", { contactId, chatSessionDataList });
  });
};
const openWindow = ({ windowId, title = "EasyChat", path: path2, width = 960, height = 720, data }) => {
  const localServerPort = store$1.getUserData("localServerPort");
  data.localServerPort = localServerPort;
  let newWindow = getWindow(windowId);
  if (!newWindow) {
    newWindow = new electron.BrowserWindow({
      icon,
      width,
      height,
      //380
      fullscreenable: false,
      fullscreen: false,
      maximizable: false,
      autoHideMenuBar: true,
      resizable: true,
      titleBarStyle: "hidden",
      frame: true,
      transparent: true,
      hasShadow: false,
      webPreferences: {
        preload: path$1.join(__dirname, "../preload/index.js"),
        sandbox: false,
        contextIsolation: false
      }
    });
    saveWindow(windowId, newWindow);
    newWindow.setMinimumSize(600, 484);
    if (utils.is.dev && process.env["ELECTRON_RENDERER_URL"]) {
      newWindow.loadURL(`${process.env["ELECTRON_RENDERER_URL"]}/index.html#${path2}`);
    } else {
      newWindow.loadFile(path$1.join(__dirname, `../renderer/index.html`), { hash: `${path2}` });
    }
    if (NODE_ENV$1 === "development") {
      newWindow.webContents.openDevTools();
    }
    newWindow.on("ready-to-show", () => {
      console.log("设置title", title);
      newWindow.setTitle(title);
      newWindow.show();
    });
    newWindow.once("show", () => {
      setTimeout(() => {
        newWindow.webContents.send("pageInitData", data);
      }, 500);
    });
    newWindow.on("closed", () => {
      console.log("关闭窗口");
      delWindow(windowId);
    });
  } else {
    newWindow.show();
    newWindow.setSkipTaskbar(false);
    newWindow.webContents.send("pageInitData", data);
  }
};
const NODE_ENV = process.env.NODE_ENV;
const login_width = 300;
const login_height = 370;
const register_height = 490;
function createWindow() {
  const mainWindow = new electron.BrowserWindow({
    icon,
    width: login_width,
    height: login_height,
    //380
    show: false,
    fullscreenable: false,
    fullscreen: false,
    maximizable: false,
    autoHideMenuBar: true,
    //隐藏菜单
    resizable: false,
    //自动拖动大小
    frame: true,
    // 创建无边框窗口，没有窗口的某些部分（例如工具栏、控件等）
    transparent: true,
    //创建一个完全透明的窗口
    hasShadow: false,
    titleBarStyle: "hidden",
    webPreferences: {
      preload: path$1.join(__dirname, "../preload/index.js"),
      sandbox: false,
      contextIsolation: false
    }
  });
  saveWindow("main", mainWindow);
  if (NODE_ENV === "development") {
    mainWindow.webContents.openDevTools();
  }
  mainWindow.on("ready-to-show", () => {
    mainWindow.show();
    mainWindow.setTitle("EasyChat");
  });
  mainWindow.once("focus", () => mainWindow.flashFrame(false));
  mainWindow.on("close", (e) => {
    mainWindow.hide();
  });
  mainWindow.webContents.setWindowOpenHandler((details) => {
    electron.shell.openExternal(details.url);
    return { action: "deny" };
  });
  if (utils.is.dev && process.env["ELECTRON_RENDERER_URL"]) {
    mainWindow.loadURL(process.env["ELECTRON_RENDERER_URL"]);
  } else {
    mainWindow.loadFile(path$1.join(__dirname, "../renderer/index.html"));
  }
  const tray = new electron.Tray(icon);
  const contextMenu = [
    {
      label: "退出EasyChat",
      click: function() {
        electron.app.exit();
      }
    }
  ];
  const menu = electron.Menu.buildFromTemplate(contextMenu);
  tray.setToolTip("EasyChat");
  tray.setContextMenu(menu);
  tray.on("click", () => {
    mainWindow.setSkipTaskbar(false);
    mainWindow.show();
  });
  onSetLocalStore();
  onGetLocalStore();
  onLoginSuccess((config) => {
    contextMenu.splice(0);
    contextMenu.push({
      label: "退出EasyChat",
      click: function() {
        electron.app.exit();
      }
    });
    mainWindow.setResizable(true);
    mainWindow.setSize(850, 800);
    mainWindow.center();
    mainWindow.setMaximizable(true);
    mainWindow.setMinimumSize(800, 600);
    if (config.admin) {
      contextMenu.unshift({
        label: "管理后台",
        click: function() {
          openWindow({
            windowId: "admin",
            title: "管理后台",
            path: `/admin`,
            width: config.screenWidth * 0.8,
            height: config.screenHeight * 0.8,
            data: {
              token: config.token
            }
          });
        }
      });
    }
    contextMenu.unshift({
      label: "用户：" + config.nickName,
      click: function() {
      }
    });
    tray.setContextMenu(electron.Menu.buildFromTemplate(contextMenu));
  });
  onLoginOrRegister((isLogin) => {
    mainWindow.setResizable(true);
    if (isLogin) {
      mainWindow.setSize(login_width, login_height);
    } else {
      mainWindow.setSize(login_width, register_height);
    }
    mainWindow.setResizable(false);
  });
  onReLogin(() => {
    mainWindow.setResizable(true);
    mainWindow.setMinimumSize(login_width, login_height);
    mainWindow.setSize(login_width, login_height);
    mainWindow.center();
    mainWindow.setResizable(false);
  });
  winTitleOp((e, { action, data }) => {
    const webContents = e.sender;
    const win = electron.BrowserWindow.fromWebContents(webContents);
    switch (action) {
      case "close": {
        if (data.closeType == 0) {
          win.close();
        } else {
          win.setSkipTaskbar(true);
          win.hide();
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
    }
  });
  onLoadChatMessage();
  onLoadSessionData();
  onOpenNewWindow();
  onSetSessionSelect();
  onLoadContactApply();
  onUpdateContactNoReadCount();
  onAddLocalMessage();
  onCreateCover();
  onSaveAs();
  onGetSettingInfo();
  onChangeLocalFolder();
  onOpenLocalFolder();
  onDownloadUpdate();
  onOpenUrl();
  onSaveClipBoardFile();
  onLoadLocalUser();
  onDelChatSession();
  onTopChatSession();
  onReloadChatSession();
}
electron.app.whenReady().then(() => {
  utils.electronApp.setAppUserModelId("com.electron");
  electron.app.on("browser-window-created", (_, window) => {
    utils.optimizer.watchWindowShortcuts(window);
  });
  createWindow();
  electron.app.on("activate", function() {
    if (electron.BrowserWindow.getAllWindows().length === 0)
      createWindow();
  });
});
electron.app.on("window-all-closed", () => {
  if (process.platform !== "darwin") {
    electron.app.quit();
  }
});
electron.app.commandLine.appendSwitch("wm-window-animations-disabled");
