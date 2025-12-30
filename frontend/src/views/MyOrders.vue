<template>
  <div class="container">
    <el-page-header @back="router.back()" title="返回" content="我的订单" class="page-header"></el-page-header>
    <el-card class="box-card">
      <el-table :data="orders" style="width: 100%">
        <el-table-column prop="orderNo" label="订单编号" width="220" />
        <el-table-column prop="createTime" label="创建时间" width="180">
            <template #default="scope">
                {{ new Date(scope.row.createTime).toLocaleString() }}
            </template>
        </el-table-column>
        <el-table-column prop="payAmount" label="金额">
            <template #default="scope">
                ￥{{ scope.row.payAmount }}
            </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button 
                v-if="scope.row.status === 0" 
                type="primary" 
                size="small" 
                @click="goToPay(scope.row.id)"
            >
                去支付
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getOrderList } from '../api/seckill'

const router = useRouter()
const orders = ref([])

onMounted(async () => {
  try {
    const res = await getOrderList()
    orders.value = res.data || res
  } catch (e) {
    console.error(e)
  }
})

const getStatusType = (status) => {
    if (status === 0) return 'warning'
    if (status === 1) return 'success'
    return 'info'
}

const getStatusText = (status) => {
    if (status === 0) return '未支付'
    if (status === 1) return '已支付'
    if (status === -1) return '已取消'
    return '未知'
}

const goToPay = (id) => {
    router.push(`/pay/${id}`)
}
</script>

<style scoped>
.container {
    padding: 40px;
    max-width: 1200px;
    margin: 0 auto;
}
.page-header {
    margin-bottom: 30px;
}
</style>
