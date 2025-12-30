<template>
  <div class="home-container">
    <el-container>
      <el-header class="header">
        <div class="logo">在线秒杀系统</div>
        <div class="user-info">
          <el-dropdown v-if="user && user.id" @command="handleCommand">
            <span class="el-dropdown-link">
              {{ user.nickname }}
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="orders">我的订单</el-dropdown-item>
                <el-dropdown-item command="wallet">我的钱包</el-dropdown-item>
                <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <div v-else>
            <el-button type="text" @click="router.push('/login')">登录</el-button>
            <el-button type="text" @click="router.push('/register')">注册</el-button>
          </div>
        </div>
      </el-header>
      <el-main>  
        <!-- Category Filter -->
        <el-tabs v-model="activeCategory" class="category-tabs" @tab-click="handleCategoryClick">
          <el-tab-pane label="全部" name="All"></el-tab-pane>
          <el-tab-pane v-for="cat in categories" :key="cat" :label="cat" :name="cat"></el-tab-pane>
        </el-tabs>

        <el-row :gutter="20">
          <el-col :span="6" v-for="item in filteredList" :key="item.id" style="margin-bottom: 20px;">
            <el-card class="box-card" :body-style="{ padding: '0px' }">
              <img :src="item.goods.img || 'https://via.placeholder.com/300'" class="image" @click="goToDetail(item.goods.id)">
              <div style="padding: 14px;">
                <span class="goods-name" @click="goToDetail(item.goods.id)">{{ item.goods.name }}</span>
                <div class="bottom">
                  <div class="price">
                    <span class="seckill-price">￥{{ item.seckillPrice }}</span>
                    <span class="original-price">￥{{ item.goods.price }}</span>
                  </div>
                  <div class="status">
                    <el-tag v-if="item.timeStatus === 0" type="info">未开始</el-tag>
                    <el-tag v-else-if="item.timeStatus === 1" type="danger">进行中</el-tag>
                    <el-tag v-else type="warning">已结束</el-tag>
                  </div>
                  <div class="actions">
                    <el-button size="small" @click="goToDetail(item.goods.id)">查看详情</el-button>
                    <el-button 
                      type="primary" 
                      size="small"
                      :disabled="item.timeStatus !== 1 || item.stockCount <= 0" 
                      @click="openSeckillDialog(item)"
                    >
                      {{ item.stockCount > 0 ? '立即抢购' : '已售罄' }}
                    </el-button>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>

    <!-- Seckill Confirm Dialog -->
    <el-dialog
      v-model="showSeckillDialog"
      title="抢购确认"
      width="30%"
      center
      v-if="currentActivity"
    >
      <div class="dialog-content">
        <img :src="currentActivity.goods.img || 'https://via.placeholder.com/300'" class="dialog-image">
        <div class="dialog-info-row">
          <span class="dialog-name">{{ currentActivity.goods.name }}</span>
          <span class="dialog-price">￥{{ currentActivity.seckillPrice }}</span>
        </div>
        <div class="dialog-action-row">
          <el-button 
            type="danger" 
            size="large" 
            class="dialog-buy-btn"
            :disabled="currentActivity.stockCount <= 0"
            @click="handleSeckill(currentActivity.id)"
          >
            立即抢购
          </el-button>
          <span class="dialog-stock">剩余: {{ currentActivity.stockCount }}</span>
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getSeckillList, executeSeckill, payOrder } from '../api/seckill'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()
const user = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const seckillList = ref([])
const activeCategory = ref('All')
const categories = ref(['数码', '电脑', '游戏', '家居']) // Chinese categories

// Seckill Dialog State
const showSeckillDialog = ref(false)
const currentActivity = ref(null)

const openSeckillDialog = (item) => {
  if (!user.value || !user.value.id) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  currentActivity.value = item
  showSeckillDialog.value = true
}

const handleSeckill = async (activityId) => {
  try {
    // 1. Check Login
    if (!user.value || !user.value.id) {
        ElMessage.warning('请先登录')
        router.push('/login')
        return
    }

    // 2. Execute Seckill
    showSeckillDialog.value = false
    const res = await executeSeckill(activityId)
    const orderId = res.data || res // Handle potential response wrapper
    
    // 3. Show Success Dialog with Payment Option
    ElMessageBox.confirm(
        '抢购成功！订单已生成，请尽快支付。',
        '抢购成功',
        {
            confirmButtonText: '立即支付',
            cancelButtonText: '稍后支付',
            type: 'success',
            distinguishCancelAndClose: true
        }
    ).then(() => {
        // Confirm: Pay Now -> Go to Pay Page
        router.push(`/pay/${orderId}`)
    }).catch((action) => {
        // Cancel: Pay Later
        if (action === 'cancel') {
            ElMessage.info('您可以稍后在“我的订单”中进行支付')
        }
    })
  } catch (e) {
    console.error(e)
  }
}

const handleCommand = (command) => {
  if (command === 'logout') {
    logout()
  } else if (command === 'orders') {
    router.push('/orders')
  } else if (command === 'wallet') {
    router.push('/wallet')
  }
}

const logout = () => {
  localStorage.removeItem('user')
  localStorage.removeItem('token')
  user.value = {}
  router.push('/login')
}

onMounted(() => {
  loadSeckillList()
})

const loadSeckillList = async () => {
  try {
    const data = await getSeckillList()
    seckillList.value = data
  } catch (error) {
    console.error(error)
  }
}

const filteredList = computed(() => {
  if (activeCategory.value === 'All') return seckillList.value
  return seckillList.value.filter(item => {
      let cat = item.goods.category || 'Electronics'
      // Simple mapping for mock data
      if (cat === 'Electronics') cat = '数码'
      if (cat === 'Computers') cat = '电脑'
      if (cat === 'Gaming') cat = '游戏'
      if (cat === 'Home') cat = '家居'
      return cat === activeCategory.value
  })
})

const handleCategoryClick = (tab) => {
  // handled by v-model
}

const goToDetail = (goodsId) => {
  router.push(`/product/${goodsId}`)
}
</script>

<style scoped>
.home-container {
  height: 100vh;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #dcdfe6;
  padding: 0 20px;
  background-color: #fff;
}
.logo {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}
.user-info {
  display: flex;
  align-items: center;
}
.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
}
.category-tabs {
  margin-bottom: 10px;
}
.image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  display: block;
  cursor: pointer;
}
.goods-name {
  font-size: 16px;
  font-weight: bold;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: block;
  margin-bottom: 15px;
  cursor: pointer;
}
.bottom {
  margin-top: 15px;
  line-height: 12px;
}
.price {
  margin-bottom: 15px;
  display: flex;
  align-items: baseline;
}
.seckill-price {
  color: #F56C6C;
  font-size: 20px;
  font-weight: bold;
  margin-right: 10px;
}
.original-price {
  color: #999;
  text-decoration: line-through;
  font-size: 14px;
}
.status {
  margin-bottom: 15px;
}
.actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.dialog-content {
  text-align: center;
}
.dialog-image {
  width: 200px;
  height: 200px;
  object-fit: cover;
  margin-bottom: 20px;
  border-radius: 8px;
}
.dialog-info-row {
  margin-bottom: 25px;
}
.dialog-name {
  font-size: 18px;
  font-weight: bold;
  margin-right: 15px;
}
.dialog-price {
  color: #F56C6C;
  font-size: 24px;
  font-weight: bold;
}
.dialog-action-row {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
}
.dialog-stock {
  color: #909399;
  font-size: 14px;
}
</style>
