import { defineStore } from "pinia";
export const useGlobalInfoStore = defineStore('globalInfo', {
    state: () => {
        return {
            globalInfo: {},
        }
    },
    actions: {
        setInfo(key, value) {
            this.globalInfo[key] = value;
        },
        getInfo(key) {
            return this.globalInfo[key]
        }
    }
})