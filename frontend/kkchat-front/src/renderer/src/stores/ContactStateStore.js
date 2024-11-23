import { defineStore } from "pinia";
export const useContactStateStore = defineStore('contactStateInfo', {
    state: () => {
        return {
            contactReload: null,
            delContactId: null,
        }
    },
    actions: {
        setContactReload(state) {
            this.contactReload = state;
        },
        delContact(delContactId) {
            this.delContactId = delContactId;
        }
    }
})