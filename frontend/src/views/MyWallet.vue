<template>
  <div class="container">
    <el-page-header @back="router.back()" title="返回" content="我的钱包" class="page-header"></el-page-header>
    <el-card class="box-card">
        <div class="balance-box">
            <h3>账户余额</h3>
            <div class="balance">￥{{ user.balance || '0.00' }}</div>
            <div class="recharge-section">
                <el-input-number v-model="rechargeAmount" :min="1" :precision="2" :step="100" label="充值金额" size="large"></el-input-number>
                <el-button type="primary" size="large" @click="handleTopUp" :loading="loading" style="margin-left: 20px;">充值</el-button>
            </div>
        </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getUserInfo, recharge } from '../api/seckill'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const user = ref({})
const rechargeAmount = ref(100)
const loading = ref(false)

onMounted(async () => {
    loadInfo()
})

const loadInfo = async () => {
    try {
        const res = await getUserInfo()
        user.value = res.data || res
    } catch (e) {
        console.error(e)
    }
}

const handleTopUp = async () => {
    try {
        await ElMessageBox.confirm(`确定要充值 ￥${rechargeAmount.value} 吗？`, '充值确认', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'info'
        })
        
        loading.value = true
        await recharge(rechargeAmount.value)
        ElMessage.success('充值成功')
        await loadInfo()
    } catch (e) {
        if (e !== 'cancel') {
            ElMessage.error(e.message || '充值失败')
        }
    } finally {
        loading.value = false
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
.balance-box {
    text-align: center;
    padding: 60px 40px;
}
.balance-box h3 {
    margin-bottom: 20px;
    color: #606266;
}
.balance {
    font-size: 56px;
    color: #f56c6c;
    margin: 30px 0 50px;
    font-weight: bold;
}
.recharge-section {
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>
