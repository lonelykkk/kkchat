const Store = require('electron-store')
const store = new Store();

let userId = null;

const initUserId = (_userId) => {
    userId = _userId;
}

const setData = (key, value) => {
    store.set(key, value);
}

const getData = (key) => {
    return store.get(key);
}

const setUserData = (key, value) => {
    setData(userId + key, value);
}

const getUserData = (key) => {
    return getData(userId + key);
}

const getUserId = () => {
    return userId;
}

const deleteUserData = (key) => {
    store.delete(userId + key)
}
export default {
    initUserId,
    setData,
    getData,
    setUserData,
    getUserData,
    getUserId,
    deleteUserData,
}
