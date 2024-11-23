<template>
  <div>
    <div class="top-panel">
      <el-card>
        <el-form :model="searchForm" label-width="70px" label-position="right">
          <el-row>
            <el-col :span="5">
              <el-form-item label="发布日期" label-width="70px">
                <el-date-picker
                  v-model="searchFormData.createTimeRange"
                  type="daterange"
                  range-separator="~"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  value-format="YYYY-MM-DD"
                  @change="loadDataList"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="4" :style="{ paddingLeft: '10px' }">
              <el-button type="success" @click="loadDataList()">查询 </el-button>
              <el-button type="primary" @click="showEdit()">发布版本 </el-button>
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
        <template #slotUpdateDesc="{ index, row }">
          <div v-for="(item, num) in row.updateDescArray">{{ num + 1 }}、{{ item }}</div>
        </template>

        <template #fileTypeSlot="{ index, row }">
          <div v-if="row.fileType == 0">本地文件</div>
          <div v-if="row.fileType == 1">{{ row.outerLink }}</div>
        </template>

        <template #slotStatus="{ index, row }">
          <div style="color: #f56c6c" v-if="row.status == 0">未发布</div>
          <div style="color: #f7ba2a" v-if="row.status == 1">灰度发布</div>
          <div style="color: #529b2e" v-if="row.status == 2">全网发布</div>
        </template>

        <template #slotOperation="{ index, row }">
          <el-dropdown placement="bottom-end" trigger="click">
            <span class="iconfont icon-more"> </span>
            <template #dropdown>
              <el-dropdown-item @click="showEdit(row)" v-if="row.status == 0"
                >修改</el-dropdown-item
              >
              <el-dropdown-item @click="updatePost(row)">发布</el-dropdown-item>
              <el-dropdown-item @click="del(row)" v-if="row.status == 0">删除</el-dropdown-item>
            </template>
          </el-dropdown>
        </template>
      </Table>
    </el-card>
    <UpdateEdit ref="updateEditRef" @reload="loadDataList"></UpdateEdit>

    <UpdatePost ref="updatePostRef" @reload="loadDataList"></UpdatePost>
  </div>
</template>
<script setup>
import UpdateEdit from './UpdateEdit.vue'
import UpdatePost from './UpdatePost.vue'
import { getCurrentInstance, nextTick, onMounted, ref } from 'vue'
const { proxy } = getCurrentInstance()

const searchFormData = ref({})
const tableData = ref({})
const tableOptions = {}
const columns = [
  {
    label: '版本',
    prop: 'version',
    width: 120
  },
  {
    label: '更新内容',
    prop: 'updateDesc',
    scopedSlots: 'slotUpdateDesc',
    width: 200
  },
  {
    label: '发布时间',
    prop: 'createTime',
    width: 180
  },
  {
    label: '文件类型',
    prop: 'fileType',
    scopedSlots: 'fileTypeSlot'
  },
  {
    label: '状态',
    prop: 'status',
    scopedSlots: 'slotStatus',
    width: 80
  },
  {
    label: '操作',
    prop: 'operation',
    scopedSlots: 'slotOperation',
    width: 80
  }
]

const searchForm = ref({})

const loadDataList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize
  }
  if (searchFormData.value.createTimeRange) {
    params.createTimeStart = searchFormData.value.createTimeRange[0]
    params.createTimeEnd = searchFormData.value.createTimeRange[1]
  }
  delete params.createTimeRange
  Object.assign(params, searchForm.value)
  let result = await proxy.Request({
    url: proxy.Api.loadUpdateDataList,
    params: params
  })
  if (!result) {
    return
  }
  tableData.value = result.data
}

const updateEditRef = ref()
const showEdit = (data) => {
  updateEditRef.value.showEdit(data)
}

//删除用户
const del = (data) => {
  proxy.Confirm({
    message: `确定要删除【${data.version}】吗？`,
    okfun: async () => {
      let result = await proxy.Request({
        url: proxy.Api.delUpdate,
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

//发布更新
const updatePostRef = ref()
const updatePost = (data) => {
  updatePostRef.value.showEdit(data)
}
</script>
<style lang="scss" scoped>
</style>
