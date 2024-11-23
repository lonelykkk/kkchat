
const fs = require('fs');
const sqlite3 = require('sqlite3').verbose();
const os = require('os');
const NODE_ENV = process.env.NODE_ENV
// 获取当前用户的家目录  
import { add_tables, add_indexes, alter_tables } from "./Tables"
const userDir = os.homedir();
const dbFolder = userDir + (NODE_ENV === "development" ? "/.easychatdev/" : "/.easychat/");
if (!fs.existsSync(dbFolder)) {
    fs.mkdirSync(dbFolder);
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
            const field = fieldList.some(row => row.name === item.field);
            if (!field) {
                await run(item.sql, []);
            }
        }
        resolve();
    });
}

const toCamelCase = (str) => {
    return str.replace(/_([a-z])/g, function (match, p1) {
        return String.fromCharCode(p1.charCodeAt(0) - 32);
    });
}

const convertDbObj2BizObj = (data) => {
    if (!data) {
        return null;
    }
    const bizData = {};
    for (let item in data) {
        bizData[toCamelCase(item)] = data[item];
    }
    return bizData;
}
//所有表字段和属性对应关系
const globalColumnsMap = {};

//新增，修改，删除
const run = (sql, params) => {
    //console.log(`执行的sql:${sql},params:${params}`);
    return new Promise((resolve, reject) => {
        const stmt = db.prepare(sql);
        stmt.run(params, function (err, row) {
            if (err) {
                console.error(`执行的sql:${sql},params:${params},执行失败:${err}`);
                reject("查询数据库失败");
            }
            console.log(`执行的sql:${sql},params:${params}执行记录数:${this.changes}`);
            resolve(this.changes);
        });
        stmt.finalize();
    }).catch(error => {
        console.error(error);
    })
}
const queryCount = (sql, params) => {
    return new Promise((resolve, reject) => {
        const stmt = db.prepare(sql);
        stmt.get(params, function (err, row) {
            console.log(`执行的sql:${sql},params:${params},row:${row}`);
            if (err) {
                console.error(err);
                //reject("查询数据库失败");
                resolve(0);
            }
            resolve(Array.from(Object.values(row))[0]);
        });
        stmt.finalize();
    })
}

//查询单个
const queryOne = (sql, params) => {
    return new Promise((resolve, reject) => {
        const stmt = db.prepare(sql);
        stmt.get(params, function (err, row) {
            if (err) {
                console.error(err);
                // reject("查询数据库失败");
                resolve({});
            }
            resolve(convertDbObj2BizObj(row));
            console.log(`执行的sql:${sql},params:${params},row:${JSON.stringify(row)}`);
        });
        stmt.finalize();
    })
}

//查询所有
const queryAll = (sql, params) => {
    return new Promise((resolve, reject) => {
        const stmt = db.prepare(sql);
        stmt.all(params, function (err, row) {
            if (err) {
                console.error(err);
                //reject("查询数据库失败");
                resolve([]);
            }
            row.forEach((item, index) => {
                row[index] = convertDbObj2BizObj(item);
            })
            console.log(`执行的sql:${sql},params:${params},row:${JSON.stringify(row)}`);
            resolve(row);
        });
        stmt.finalize();
    })
}


const insert = (sqlPrefix, tableName, data) => {
    const columnsMap = globalColumnsMap[tableName];
    const dbColumns = [];
    const params = [];
    for (let item in data) {
        if (data[item] != undefined && columnsMap[item] != undefined) {
            dbColumns.push(columnsMap[item]);
            params.push(data[item]);
        }
    }
    const preper = '?'.repeat(dbColumns.length).split("").join(",");
    const sql = `${sqlPrefix} ${tableName}(${dbColumns.join(",")})values(${preper})`;
    return run(sql, params);
}

const insertOrReplace = (tableName, data) => {
    return insert("insert or replace into", tableName, data);
}

const insertOrIgnore = (tableName, data) => {
    return insert("insert or ignore into", tableName, data);
}


const update = (tableName, data, paramData) => {
    const columnsMap = globalColumnsMap[tableName];
    const dbColumns = [];
    const params = [];
    const whereColumns = [];
    for (let item in data) {
        if (data[item] != undefined && columnsMap[item] != undefined) {
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
    const sql = `update ${tableName} set ${dbColumns.join(",")} ${whereColumns.length > 0 ? ' where ' : ' '} ${whereColumns.join(" and ")}`;
    return run(sql, params);
}

//初始化获取所有字段列
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
}

const init = () => {
    db.serialize(async () => {
        await createTable();
        initTableColumnsMap();
    });
}

init();

export {
    run,
    queryOne,
    queryCount,
    queryAll,
    insertOrReplace,
    insertOrIgnore,
    update,
    insert
};