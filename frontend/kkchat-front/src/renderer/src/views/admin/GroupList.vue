<template>
  <div>
    <div class="top-panel">
      <el-card>
        <el-form :model="searchForm" label-width="80px" label-position="right">
          <el-row>
            <el-col :span="5">
              <el-form-item label="群组ID" label-width="55px">
                <el-input
                  class="password-input"
                  v-model="searchForm.groupId"
                  clearable
                  @keyup.native="loadDataList"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="群名称">
                <el-input
                  class="password-input"
                  v-model="searchForm.groupNameFuzzy"
                  clearable
                  placeholder="支持模糊搜索"
                  @keyup.native="loadDataList"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="群主UID">
                <el-input
                  class="password-input"
                  v-model="searchForm.groupOwnerId"
                  clearable
                  @keyup.native="loadDataList"
                >
                </el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4" :style="{ paddingLeft: '10px' }">
              <el-button type="success" @click="loadDataList()">查询 </el-button>
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
          <AvatarBase :width="50" :userId="row.groupId" partType="avatar"> </AvatarBase>
        </template>

        <template #slotGroupName="{ index, row }">
          {{ row.groupName }}({{ row.groupId }})
        </template>
        <template #slotGroupOwnerNickName="{ index, row }">
          {{ row.groupOwnerNickName }}({{ row.groupOwnerId }})
        </template>

        <template #slotJoinType="{ index, row }">
          <div>{{ row.joinType == 0 ? '直接加入' : '管理员同意后加入' }}</div>
        </template>
        <template #slotStatus="{ index, row }">
          <div>
            <span v-if="row.status == 0" style="color: red">已解散</span>
            <span v-if="row.status == 1" style="color: green">正常</span>
          </div>
        </template>
        <template #slotOperation="{ index, row }">
          <div class="row-op-panel">
            <a href="javascript:void(0)" @click="dissolutionGroup(row)" v-if="row.status == 1"
              >解散</a
            >
          </div>
        </template>
      </Table>
    </el-card>
  </div>
</template>
<script setup>
import AvatarBase from '@/components/AvatarBase.vue'
import { getCurrentInstance, nextTick, ref } from 'vue'
const { proxy } = getCurrentInstance()
const tableData = ref({})
const tableOptions = {}
const columns = [
  {
    label: '头像',
    prop: 'groupId',
    width: 70,
    scopedSlots: 'slotAvatar'
  },
  {
    label: '群名称',
    prop: 'groupName',
    scopedSlots: 'slotGroupName'
  },
  {
    label: '群主',
    prop: 'groupOwnerNickName',
    scopedSlots: 'slotGroupOwnerNickName'
  },
  {
    label: '群员',
    prop: 'memberCount',
    width: 200
  },
  {
    label: '创建时间',
    prop: 'createTime',
    width: 200
  },
  {
    label: '加入方式',
    prop: 'joinType',
    width: 150,
    scopedSlots: 'slotJoinType'
  },
  {
    label: '状态',
    prop: 'status',
    width: 150,
    scopedSlots: 'slotStatus'
  },
  {
    label: '操作',
    prop: 'operation',
    width: 80,
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
    url: proxy.Api.loadGroup,
    params: params
  })
  if (!result) {
    return
  }
  Object.assign(tableData.value, result.data)
}

const dissolutionGroup = (data) => {
  proxy.Confirm({
    message: `确认要解散群组【${data.groupName}】吗？`,
    okfun: async () => {
      let result = await proxy.Request({
        url: proxy.Api.adminDissolutionGroup,
        params: {
          groupId: data.groupId
        }
      })
      if (!result) {
        return
      }
      proxy.Message.success('解散成功')
      loadDataList()
    }
  })
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
