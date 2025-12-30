<template>
  <div class="product-detail-container">
    <el-page-header @back="goBack" title="返回列表" content="商品详情" class="page-header"></el-page-header>
    
    <div class="content" v-if="product">
      <el-row :gutter="40">
        <el-col :span="10">
          <img :src="product.img || 'https://via.placeholder.com/400'" class="product-image">
        </el-col>
        <el-col :span="14">
          <h1 class="product-name">{{ product.name }}</h1>
          <h3 class="product-title">{{ product.title }}</h3>
          
          <div class="price-box">
            <span class="label">价格：</span>
            <span class="price">￥{{ product.price }}</span>
          </div>
          
          <div class="info-item">
            <span class="label">分类：</span>
            <el-tag>{{ product.category || '未分类' }}</el-tag>
          </div>
          
          <div class="info-item">
            <span class="label">库存：</span>
            <span>{{ product.stock }}</span>
          </div>

          <!-- Seckill Info -->
          <div v-if="seckillActivity" class="seckill-info">
            <el-divider></el-divider>
            <div class="seckill-price-box">
                <span class="label">秒杀价：</span>
                <span class="seckill-price">￥{{ seckillActivity.seckillPrice }}</span>
            </div>
            <div class="seckill-status">
                <el-tag v-if="seckillActivity.timeStatus === 0" type="info">秒杀未开始</el-tag>
                <el-tag v-else-if="seckillActivity.timeStatus === 1" type="danger">秒杀进行中</el-tag>
                <el-tag v-else type="warning">秒杀已结束</el-tag>
                <span class="stock-info">剩余库存: {{ seckillActivity.stockCount }}</span>
            </div>
            <div class="seckill-action" style="margin-top: 20px;">
                <el-button 
                    type="danger" 
                    size="large" 
                    :disabled="seckillActivity.timeStatus !== 1 || seckillActivity.stockCount <= 0 || hasPurchased"
                    @click="handleSeckill"
                >
                    {{ hasPurchased ? '已购买' : (seckillActivity.stockCount > 0 ? '立即抢购' : '已售罄') }}
                </el-button>
            </div>
          </div>

          <el-divider></el-divider>
          
          <div class="detail-section">
            <h3>商品详情</h3>
            <p>{{ product.detail || '暂无详情描述' }}</p>
          </div>
        </el-col>
      </el-row>
    </div>
    <div v-else class="loading">
      <el-empty description="加载中或未找到商品"></el-empty>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getSeckillList, executeSeckill, getOrderList } from '../api/seckill'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const product = ref(null)
const seckillActivity = ref(null)
const user = ref(JSON.parse(localStorage.getItem('user') || '{}'))
const hasPurchased = ref(false)

onMounted(async () => {
  const goodsId = route.params.id
  try {
    const list = await getSeckillList()
    // Find activity by goodsId
    const foundActivity = list.find(item => item.goods.id == goodsId)
    
    if (foundActivity) {
      seckillActivity.value = foundActivity
      product.value = foundActivity.goods
      
      // Check if user has purchased
      if (user.value && user.value.id) {
        try {
            const orderRes = await getOrderList()
            const orders = orderRes.data || orderRes || []
            // Check if any order matches the current activityId
            const order = orders.find(o => o.activityId === foundActivity.id)
            if (order) {
                hasPurchased.value = true
            }
        } catch (e) {
            console.error('Failed to check purchase status', e)
        }
      }

    } else {
        // Fallback Mock
        product.value = {
            id: goodsId,
            name: 'Mock Product ' + goodsId,
            title: 'This is a mock product title',
            img: 'https://via.placeholder.com/400',
            price: 999.00,
            stock: 100,
            category: 'Mock Category',
            detail: 'This is a mock detail description because the product was not found in the loaded list.'
        }
    }
  } catch (e) {
    console.error(e)
  }
})

const handleSeckill = async () => {
    if (!seckillActivity.value) return
    
    // Check Login
    if (!user.value || !user.value.id) {
        ElMessage.warning('请先登录')
        router.push('/login')
        return
    }

    try {
        const res = await executeSeckill(seckillActivity.value.id)
        const orderId = res.data || res 
        hasPurchased.value = true
        
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
            router.push(`/pay/${orderId}`)
        }).catch((action) => {
            if (action === 'cancel') {
                ElMessage.info('您可以稍后在“我的订单”中进行支付')
            }
        })
    } catch (e) {
        console.error(e)
    }
}

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.product-detail-container {
  padding: 40px;
  max-width: 1200px;
  margin: 0 auto;
}
.page-header {
  margin-bottom: 40px;
}
.product-image {
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.product-name {
  font-size: 32px;
  color: #303133;
  margin-bottom: 15px;
}
.product-title {
  font-size: 18px;
  color: #909399;
  font-weight: normal;
  margin-bottom: 30px;
}
.price-box {
  background-color: #f0f9eb;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 30px;
}
.label {
  color: #606266;
  font-size: 16px;
  margin-right: 15px;
}
.price {
  font-size: 24px;
  color: #F56C6C;
  font-weight: bold;
}
.info-item {
  margin-bottom: 20px;
  font-size: 16px;
}
.seckill-info {
  margin-top: 30px;
}
.seckill-price-box {
  margin-bottom: 20px;
}
.seckill-price {
    color: #F56C6C;
    font-size: 36px;
    font-weight: bold;
}
.seckill-status {
  margin-bottom: 25px;
}
.stock-info {
    margin-left: 20px;
    color: #909399;
    font-size: 16px;
}
.detail-section {
  padding: 20px 0;
}
.detail-section h3 {
  margin-bottom: 20px;
  font-size: 20px;
  border-left: 4px solid #409EFF;
  padding-left: 10px;
}
.detail-section p {
  line-height: 1.8;
  color: #606266;
  font-size: 16px;
}
</style>
