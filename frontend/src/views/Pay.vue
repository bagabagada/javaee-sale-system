<template>
  <div class="container">
    <el-page-header @back="router.back()" title="返回" content="订单支付" class="page-header"></el-page-header>
    <el-card class="box-card" v-if="order">
        <div class="pay-info">
            <p>订单编号：{{ order.orderNo }}</p>
            <p>支付金额：<span class="price">￥{{ order.payAmount }}</span></p>
            <el-divider></el-divider>
            <p>钱包余额：￥{{ user.balance }}</p>
            
            <div class="actions">
                <el-button type="primary" size="large" @click="handlePay" :loading="paying">确认支付</el-button>
            </div>
        </div>
    </el-card>
    <el-empty v-else description="加载中..."></el-empty>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getOrderList, getUserInfo, payOrder } from '../api/seckill'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const orderId = route.params.id
const order = ref(null)
const user = ref({})
const paying = ref(false)

onMounted(async () => {
    // 1. Get User Info for balance
    try {
        const userRes = await getUserInfo()
        user.value = userRes.data || userRes
    } catch (e) {
        console.error(e)
    }
    
    // 2. Get Order Detail (using list for now)
    try {
        const listRes = await getOrderList()
        const list = listRes.data || listRes
        order.value = list.find(o => o.id.toString() === orderId)
    } catch (e) {
        console.error(e)
    }
})

const handlePay = async () => {
    if (user.value.balance < order.value.payAmount) {
        ElMessage.error('余额不足，请充值')
        return
    }
    paying.value = true
    try {
        await payOrder(orderId)
        ElMessage.success('支付成功')
        router.push('/orders')
    } catch (e) {
        ElMessage.error(e.message || '支付失败')
    } finally {
        paying.value = false
    }
}
</script>

<style scoped>
.container {
    padding: 40px;
    max-width: 800px;
    margin: 0 auto;
}
.page-header {
    margin-bottom: 30px;
}
.pay-info {
    padding: 30px;
}
.pay-info p {
    margin-bottom: 20px;
    font-size: 16px;
    color: #606266;
}
.price {
    color: #f56c6c;
    font-size: 28px;
    font-weight: bold;
    margin-left: 10px;
}
.actions {
    margin-top: 40px;
    text-align: center;
}
</style>
