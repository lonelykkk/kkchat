const windowManage = {}

const saveWindow = (id, window) => {
    windowManage[id] = window;
}

const getWindow = (id) => {
    return windowManage[id];
}

const delWindow = (id) => {
    delete windowManage[id];
}


export {
    saveWindow,
    getWindow,
    delWindow,
};