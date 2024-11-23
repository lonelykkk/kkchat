<template>
  <div>
    <div class="top-panel">
      <el-card>
        <el-form :model="searchForm" label-width="70px" label-position="right">
          <el-row>
            <el-col :span="5">
              <el-form-item label="靓号" label-width="40px">
                <el-input
                  class="password-input"
                  v-model="searchForm.userIdFuzzy"
                  clearable
                  placeholder="支持模糊搜索"
                  @keyup.native="loadDataList"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="邮箱">
                <el-input
                  class="password-input"
                  v-model="searchForm.emailFuzzy"
                  clearable
                  placeholder="支持模糊搜索"
                  @keyup.native="loadDataList"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4" :style="{ paddingLeft: '10px' }">
              <el-button type="success" @click="loadDataList()">查询 </el-button>
              <el-button type="primary" @click="editAccount()">新增靓号 </el-button>
            </el-col>
          </el-row>
        </el-form>
      </el-card>
    </div>
    <el-card class="table-data-card">
      <Table
        :columns="columns"
        :fetch="loadDataList"
        :dataSource="tableData"
        :options="tableOptions"
      >
        <template #slotAvatar="{ index, row }">
          <Avatar :width="50" :userId="row.userId" partType="avatar"> </Avatar>
        </template>

        <template #slotNickName="{ index, row }">
          {{ row.nickName }}
          <span v-if="row.sex == 0" class="iconfont icon-woman"></span>
          <span v-if="row.sex == 1" class="iconfont icon-man"></span>
        </template>

        <template #slotStatus="{ index, row }">
          <span style="color: red" v-if="row.status == 0">未使用</span>
          <span style="color: green" v-else>已使用</span>
        </template>

        <template #slotOnline="{ index, row }">
          <span style="color: green" v-if="!row.status || row.onlineType == 1">在线</span>
          <span style="color: #8a8a8a" v-else>离线</span>
        </template>

        <template #slotOperation="{ index, row }">
          <el-dropdown placement="bottom-end" trigger="click">
            <span class="iconfont icon-more"> </span>
            <template #dropdown>
              <el-dropdown-item @click="editAccount(row)" v-if="row.status == 0"
                >修改</el-dropdown-item
              >
              <el-dropdown-item @click="delAccount(row)">删除</el-dropdown-item>
            </template>
          </el-dropdown>
        </template>
      </Table>
    </el-card>
    <BeautyAccountEdit ref="beautyAccountEditRef" @reload="loadDataList"></BeautyAccountEdit>
  </div>
</template>
<script setup>
import BeautyAccountEdit from './BeautyAccountEdit.vue'
import { getCurrentInstance, nextTick, ref } from 'vue'

const { proxy } = getCurrentInstance()

const tableData = ref({})
const tableOptions = {}
const columns = [
  {
    label: '邮箱',
    prop: 'email'
  },
  {
    label: '靓号',
    prop: 'userId'
  },
  {
    label: '状态',
    prop: 'status',
    scopedSlots: 'slotStatus'
  },
  {
    label: '操作',
    prop: 'operation',
    scopedSlots: 'slotOperation'
  }
]

const searchForm = ref({})

const loadDataList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize
  }
  Object.assign(params, searchForm.value)
  let result = await proxy.Request({
    url: proxy.Api.loadBeautyAccount,
    params: params
  })
  if (!result) {
    return
  }
  Object.assign(tableData.value, result.data)
}

//删除用户
const delAccount = (data) => {
  proxy.Confirm({
    message: `确定要删除邮箱【${data.email}】对应的靓号吗？`,
    okfun: async () => {
      let result = await proxy.Request({
        url: proxy.Api.delBeautAccount,
        params: {
          id: data.id
        }
      })
      if (!result) {
        return
      }
      proxy.Message.success('删除成功')
      loadDataList()
    }
  })
}

const beautyAccountEditRef = ref()
const editAccount = (data) => {
  beautyAccountEditRef.value.showEdit(data)
}
</script>
<style lang="scss" scoped>
.icon-man {
  color: #2cb6fe;
}
.icon-woman {
  color: #fb7373;
}
</style>
