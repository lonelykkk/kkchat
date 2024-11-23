<template>
  <div class="search-item">
    <Avatar :userId="data.contactId" :showDetail="false"> </Avatar>
    <div class="contact-info">
      <div class="contact-name" v-html="data.contactName"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance, nextTick } from 'vue'
const { proxy } = getCurrentInstance()

const props = defineProps({
  data: {
    type: Object
  }
})
</script>

<style lang="scss" scoped>
.search-item {
  padding: 10px;
  display: flex;
  align-items: center;
  .contact-info {
    margin-left: 10px;
    :deep(.highlight) {
      color: red;
    }
    .last-message {
      color: #999999;
    }
  }
  &:hover {
    background: #ededed;
  }
}
</style>
