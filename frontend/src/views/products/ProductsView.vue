<template>
  <section class="page-shell products-view">
    <header class="page-header">
      <div>
        <p class="eyebrow">Каталог</p>
        <h1 class="page-title">Каталог товаров</h1>
        <p class="page-subtitle">
          Премиальная подборка техники и аксессуаров, отобранная для безупречного повседневного опыта.
        </p>
      </div>
      <div class="page-actions">
        <span class="pill">Товаров: {{ filteredProducts.length }}</span>
        <button class="btn btn-secondary" @click="resetFilters">Сбросить фильтры</button>
      </div>
    </header>

    <div v-if="isLoading" class="glass-card empty-state">
      Загрузка каталога...
    </div>
    <div v-else-if="error" class="glass-card empty-state">
      {{ error }}
    </div>
    <div v-else class="section-grid two-col">
      <aside class="glass-card filter-panel">
        <h3 class="panel-title">Фильтры</h3>
        <div class="filter-group">
          <p class="filter-label">Категория</p>
          <div class="chip-group">
            <button
              class="chip"
              :class="{ active: selectedCategory === 'all' }"
              @click="selectedCategory = 'all'"
            >
              Все
            </button>
            <button
              v-for="category in categories"
              :key="category"
              class="chip"
              :class="{ active: selectedCategory === category }"
              @click="selectedCategory = category"
            >
              {{ category }}
            </button>
          </div>
        </div>
        <div class="filter-group">
          <p class="filter-label">Ценовой сегмент</p>
          <div class="chip-group">
            <button
              class="chip"
              :class="{ active: selectedPrice === 'all' }"
              @click="selectedPrice = 'all'"
            >
              Любая цена
            </button>
            <button
              v-for="segment in priceSegments"
              :key="segment.id"
              class="chip"
              :class="{ active: selectedPrice === segment.id }"
              @click="selectedPrice = segment.id"
            >
              {{ segment.label }}
            </button>
          </div>
        </div>
        <div class="filter-group">
          <p class="filter-label">Сервис</p>
          <div class="filter-list">
            <span class="filter-item">Гарантия 24 месяца</span>
            <span class="filter-item">Бесплатная доставка</span>
            <span class="filter-item">Персональный менеджер</span>
          </div>
        </div>
      </aside>

      <div class="product-grid">
        <article
          v-for="product in filteredProducts"
          :key="product.id"
          class="product-card glass-card"
        >
          <div class="product-media">
            <img
              :src="resolveImageUrl(product.imageUrl)"
              :alt="product.name"
              class="product-image"
            >
          </div>
          <div class="product-info">
            <div class="product-meta">
              <span class="pill">{{ product.category }}</span>
              <span class="product-stock">В наличии: {{ product.stock }}</span>
            </div>
            <h3 class="product-title">{{ product.name }}</h3>
            <p class="product-description">{{ product.description }}</p>
            <div class="product-footer">
              <span class="product-price">{{ product.price }} руб.</span>
              <button
                @click="addToCart(product)"
                class="btn btn-primary"
              >
                В корзину
              </button>
            </div>
          </div>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart.store'
import { productService } from '@/services/product.service'
import type { Product } from '@/types'
import { resolveImageUrl } from '@/utils/image'

const cartStore = useCartStore()
const products = ref<Product[]>([])
const isLoading = ref(true)
const error = ref<string | null>(null)
const selectedCategory = ref('all')
const selectedPrice = ref('all')
const route = useRoute()
const router = useRouter()

const priceSegments = [
  { id: 'up-10000', label: 'До 10 000', min: 0, max: 10000 },
  { id: '10000-30000', label: '10 000 - 30 000', min: 10000, max: 30000 },
  { id: '30000-70000', label: '30 000 - 70 000', min: 30000, max: 70000 },
  { id: '70000+', label: '70 000+', min: 70000, max: Number.MAX_SAFE_INTEGER }
]

const categories = computed(() => {
  const set = new Set(products.value.map(product => product.category))
  return Array.from(set)
})

const filteredProducts = computed(() => {
  let result = [...products.value]

  if (selectedCategory.value !== 'all') {
    result = result.filter(product => product.category === selectedCategory.value)
  }

  if (selectedPrice.value !== 'all') {
    const segment = priceSegments.find(entry => entry.id === selectedPrice.value)
    if (segment) {
      result = result.filter(product => product.price >= segment.min && product.price < segment.max)
    }
  }

  return result
})

const resetFilters = () => {
  selectedCategory.value = 'all'
  selectedPrice.value = 'all'
}

const loadProducts = async () => {
  isLoading.value = true
  error.value = null
  try {
    products.value = await productService.getAll()
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Не удалось загрузить каталог'
  } finally {
    isLoading.value = false
  }
}

const addToCart = async (product: Product) => {
  await cartStore.addItem(product, 1)
}

const applyQueryFilters = () => {
  const category = typeof route.query.category === 'string' ? route.query.category : 'all'
  const price = typeof route.query.price === 'string' ? route.query.price : 'all'
  selectedCategory.value = category
  selectedPrice.value = price
}

watch([selectedCategory, selectedPrice], () => {
  router.replace({
    query: {
      ...route.query,
      category: selectedCategory.value === 'all' ? undefined : selectedCategory.value,
      price: selectedPrice.value === 'all' ? undefined : selectedPrice.value
    }
  })
})

onMounted(async () => {
  await loadProducts()
  applyQueryFilters()
})

watch(
  () => route.query,
  () => {
    applyQueryFilters()
  }
)
</script>
