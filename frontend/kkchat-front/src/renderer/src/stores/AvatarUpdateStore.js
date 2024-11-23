import { defineStore } from "pinia";
export const useAvatarInfoStore = defineStore('avatarInfo', {
    state: () => {
        return {
            avatarMap: {

            }
        }
    },
    actions: {
        setFoceReload(uid, foceReload) {
            this.avatarMap[uid] = foceReload
        },
        getFoceReload(uid) {
            return this.avatarMap[uid];
        }
    }
})