<template>
  <section class="page-shell admin-view">
    <header class="page-header">
      <div>
        <p class="eyebrow">Администрирование</p>
        <h1 class="page-title">Админ-панель</h1>
        <p class="page-subtitle">
          Управляйте товарами и отслеживайте заказы.
        </p>
      </div>
    </header>

    <div v-if="!isAdmin" class="glass-card empty-state">
      <h2>Доступ запрещен</h2>
      <p>Эта страница доступна только администратору.</p>
    </div>

    <div v-else class="admin-grid">
      <div class="glass-card admin-card">
        <h3>Товары</h3>
        <div class="admin-list">
          <button
            v-for="product in products"
            :key="product.id"
            class="admin-list-item"
            :class="{ active: selectedProduct?.id === product.id }"
            @click="selectProduct(product)"
          >
            <span>{{ product.name }}</span>
            <span class="muted">{{ product.price }} руб.</span>
          </button>
        </div>
      </div>

      <div class="glass-card admin-card">
        <h3>{{ selectedProduct ? 'Редактирование товара' : 'Новый товар' }}</h3>
        <form class="admin-form" @submit.prevent="saveProduct">
          <div class="form-field">
            <label class="form-label">Название</label>
            <input class="form-input" v-model="form.name" required>
          </div>
          <div class="form-field">
            <label class="form-label">Описание</label>
            <textarea class="form-textarea" rows="3" v-model="form.description" required></textarea>
          </div>
          <div class="form-field">
            <label class="form-label">Категория</label>
            <input class="form-input" v-model="form.category" required>
          </div>
          <div class="form-field">
            <label class="form-label">Цена</label>
            <input class="form-input" type="number" min="0" v-model.number="form.price" required>
          </div>
          <div class="form-field">
            <label class="form-label">Остаток</label>
            <input class="form-input" type="number" min="0" v-model.number="form.stock" required>
          </div>
          <div class="form-field">
            <label class="form-label">Картинка</label>
            <input class="form-input" type="file" @change="handleFile">
            <input class="form-input" v-model="form.imageUrl" placeholder="/uploads/..." required>
          </div>
          <div class="admin-actions">
            <button class="btn btn-primary" :disabled="isSaving">
              {{ selectedProduct ? 'Сохранить' : 'Создать' }}
            </button>
            <button
              v-if="selectedProduct"
              type="button"
              class="btn btn-secondary"
              @click="resetForm"
            >
              Новый товар
            </button>
            <button
              v-if="selectedProduct"
              type="button"
              class="btn btn-secondary"
              @click="removeProduct"
            >
              Удалить
            </button>
          </div>
          <div v-if="error" class="alert-box">{{ error }}</div>
        </form>
      </div>

      <div class="glass-card admin-card">
        <h3>Заказы</h3>
        <div v-if="orders.length === 0" class="muted">Заказов пока нет.</div>
        <div v-else class="admin-orders">
          <div v-for="order in orders" :key="order.id" class="admin-order">
            <div>
              <strong>Заказ #{{ order.id }}</strong>
              <p class="muted">{{ order.fullName }} · {{ order.totalAmount }} руб.</p>
            </div>
            <select class="form-input" v-model="order.status" @change="updateStatus(order)">
              <option value="CREATED">Создан</option>
              <option value="PAID">Оплачен</option>
              <option value="SHIPPED">Передан в доставку</option>
              <option value="COMPLETED">Завершен</option>
              <option value="CANCELED">Отменен</option>
            </select>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useAuthStore } from '@/stores/auth.store'
import { adminService } from '@/services/admin.service'
import type { Order, Product } from '@/types'

const authStore = useAuthStore()
const isAdmin = computed(() => authStore.isAdmin)

const products = ref<Product[]>([])
const orders = ref<Order[]>([])
const selectedProduct = ref<Product | null>(null)
const isSaving = ref(false)
const error = ref<string | null>(null)

const form = reactive({
  name: '',
  description: '',
  price: 0,
  imageUrl: '',
  category: '',
  stock: 0
})

const loadData = async () => {
  if (!isAdmin.value) return
  products.value = await adminService.getProducts()
  orders.value = await adminService.getAllOrders()
}

const selectProduct = (product: Product) => {
  selectedProduct.value = product
  form.name = product.name
  form.description = product.description
  form.price = product.price
  form.imageUrl = product.imageUrl
  form.category = product.category
  form.stock = product.stock
}

const resetForm = () => {
  selectedProduct.value = null
  form.name = ''
  form.description = ''
  form.price = 0
  form.imageUrl = ''
  form.category = ''
  form.stock = 0
}

const handleFile = async (event: Event) => {
  const input = event.target as HTMLInputElement
  if (!input.files || input.files.length === 0) return
  error.value = null
  try {
    const url = await adminService.uploadImage(input.files[0])
    form.imageUrl = url
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Не удалось загрузить картинку'
  }
}

const saveProduct = async () => {
  isSaving.value = true
  error.value = null
  try {
    if (selectedProduct.value) {
      const updated = await adminService.updateProduct(selectedProduct.value.id, { ...form })
      const index = products.value.findIndex(item => item.id === updated.id)
      if (index >= 0) products.value[index] = updated
    } else {
      const created = await adminService.createProduct({ ...form })
      products.value.push(created)
      resetForm()
    }
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Не удалось сохранить товар'
  } finally {
    isSaving.value = false
  }
}

const removeProduct = async () => {
  if (!selectedProduct.value) return
  await adminService.deleteProduct(selectedProduct.value.id)
  products.value = products.value.filter(item => item.id !== selectedProduct.value?.id)
  resetForm()
}

const updateStatus = async (order: Order) => {
  await adminService.updateOrderStatus(order.id, order.status)
}

onMounted(loadData)
</script>
