import { defineStore } from "pinia";
export const useSysSettingStore = defineStore('sysSetting', {
    state: () => {
        return {
            sysSetting: {},
        }
    },
    actions: {
        setSetting(config) {
            this.sysSetting = config;
        },
        getSetting() {
            return this.sysSetting
        },
    }
})