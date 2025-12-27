<template>
  <section class="page-shell profile-view">
    <header class="page-header">
      <div>
        <p class="eyebrow">Профиль</p>
        <h1 class="page-title">Личный кабинет</h1>
        <p class="page-subtitle">
          Управляйте учетной записью и отслеживайте заказы.
        </p>
      </div>
      <div class="page-actions">
        <button class="btn btn-secondary" @click="logout">Выйти</button>
      </div>
    </header>

    <div v-if="!isAuthenticated" class="glass-card empty-state">
      <h2>Нужен вход</h2>
      <p>Авторизуйтесь, чтобы увидеть данные профиля и историю заказов.</p>
      <router-link to="/login" class="btn btn-primary">Войти</router-link>
    </div>

    <div v-else class="profile-grid">
      <div class="glass-card profile-card">
        <h3>Профиль</h3>
        <div class="profile-meta">
          <span>Имя: {{ user?.name }}</span>
          <span>Email: {{ user?.email }}</span>
        </div>
      </div>

      <div class="glass-card profile-card">
        <h3>Последние заказы</h3>
        <div v-if="isLoading" class="muted">Загрузка...</div>
        <div v-else-if="orders.length === 0" class="muted">Заказов пока нет.</div>
        <div v-else class="order-items">
          <div v-for="order in orders.slice(0, 3)" :key="order.id" class="order-item">
            <span>Заказ #{{ order.id }}</span>
            <router-link :to="`/orders/${order.id}`" class="link">Детали</router-link>
          </div>
        </div>
        <router-link to="/orders" class="btn btn-primary mt-3">Все заказы</router-link>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useAuthStore } from '@/stores/auth.store'
import { orderService } from '@/services/order.service'
import type { Order } from '@/types'

const authStore = useAuthStore()
const orders = ref<Order[]>([])
const isLoading = ref(false)

const isAuthenticated = computed(() => authStore.isAuthenticated)
const user = computed(() => authStore.user)

const logout = () => {
  authStore.logout()
}

const loadOrders = async () => {
  if (!isAuthenticated.value) return
  isLoading.value = true
  try {
    orders.value = await orderService.getOrders()
  } finally {
    isLoading.value = false
  }
}

onMounted(loadOrders)
</script>
