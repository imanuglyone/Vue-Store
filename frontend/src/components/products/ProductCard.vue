<template>
  <div class="product-card card h-100 shadow-sm">
    <div class="position-relative">
      <img 
        :src="resolveImageUrl(product.imageUrl)" 
        :alt="product.name"
        class="card-img-top product-image"
        @error="handleImageError"
      >
      <span v-if="product.stock <= 0" class="badge bg-danger position-absolute top-0 start-0 m-2">
        Нет в наличии
      </span>
      <span v-else-if="product.stock < 10" class="badge bg-warning position-absolute top-0 start-0 m-2">
        Мало осталось
      </span>
    </div>
    
    <div class="card-body d-flex flex-column">
      <div class="mb-2">
        <span class="badge bg-secondary">{{ product.category }}</span>
      </div>
      
      <h5 class="card-title mb-2">{{ product.name }}</h5>
      <p class="card-text text-muted small flex-grow-1">
        {{ truncateDescription(product.description) }}
      </p>
      
      <div class="d-flex justify-content-between align-items-center mt-3">
        <div>
          <h4 class="text-primary mb-0">{{ formatPrice(product.price) }}</h4>
          <small class="text-muted">за шт.</small>
        </div>
        
        <div class="d-flex gap-2">
          <router-link 
            :to="`/products/${product.id}`" 
            class="btn btn-outline-primary btn-sm"
          >
            Подробнее
          </router-link>
          
          <button 
            v-if="product.stock > 0"
            @click="addToCart"
            :disabled="addingToCart"
            class="btn btn-primary btn-sm"
          >
            <span v-if="addingToCart" class="spinner-border spinner-border-sm"></span>
            <i v-else class="bi bi-cart-plus"></i>
          </button>
        </div>
      </div>
      
      <div class="mt-2">
        <small class="text-muted">
          <i class="bi bi-box"></i> В наличии: {{ product.stock }} шт.
        </small>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart.store'
import type { Product } from '@/types'
import { resolveImageUrl } from '@/utils/image'

interface Props {
  product: Product
}

const props = defineProps<Props>()
const router = useRouter()
const cartStore = useCartStore()
const addingToCart = ref(false)

const truncateDescription = (text: string, length: number = 100): string => {
  if (text.length <= length) return text
  return text.substring(0, length) + '...'
}

const formatPrice = (price: number): string => {
  return new Intl.NumberFormat('ru-RU', {
    style: 'currency',
    currency: 'RUB'
  }).format(price)
}

const handleImageError = (event: Event) => {
  const img = event.target as HTMLImageElement
  img.src = '/placeholder-image.jpg'
}

const addToCart = async () => {
  addingToCart.value = true
  try {
    await cartStore.addItem(props.product, 1)
    // Можно добавить toast-уведомление
  } catch (error) {
    console.error('Ошибка добавления в корзину:', error)
  } finally {
    addingToCart.value = false
  }
}
</script>

<style scoped>
.product-card {
  transition: all 0.3s ease;
  border: 1px solid #e0e0e0;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0,0,0,0.1) !important;
  border-color: #0d6efd;
}

.product-image {
  height: 200px;
  object-fit: cover;
  background-color: #f8f9fa;
}

.card-title {
  font-weight: 600;
  color: #333;
  height: 48px;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.btn {
  border-radius: 20px;
  padding: 6px 16px;
}

.btn-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.btn-outline-primary {
  border: 2px solid #667eea;
  color: #667eea;
}

.btn-outline-primary:hover {
  background-color: #667eea;
  color: white;
}
</style>
