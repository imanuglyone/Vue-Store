<template>
  <section class="page-shell orders-view">
    <header class="page-header">
      <div>
        <p class="eyebrow">Заказы</p>
        <h1 class="page-title">Отслеживание заказов</h1>
        <p class="page-subtitle">
          История покупок и текущий статус доставки в одном месте.
        </p>
      </div>
      <div class="page-actions">
        <span class="pill">Всего: {{ orders.length }}</span>
      </div>
    </header>

    <div v-if="isLoading" class="glass-card empty-state">
      Загрузка заказов...
    </div>
    <div v-else-if="error" class="glass-card empty-state">
      {{ error }}
    </div>
    <div v-else-if="orders.length === 0" class="glass-card empty-state">
      <h2>Заказов пока нет</h2>
      <p>Выберите товары в каталоге и оформите первый заказ.</p>
      <router-link to="/products" class="btn btn-primary">Перейти в каталог</router-link>
    </div>
    <div v-else class="orders-grid">
      <article v-for="order in orders" :key="order.id" class="glass-card order-card">
        <div class="order-header">
          <div>
            <h3>Заказ #{{ order.id }}</h3>
            <p class="muted">Дата: {{ formatDate(order.createdAt) }}</p>
          </div>
          <span class="pill">{{ formatStatus(order.status) }}</span>
        </div>
        <div class="order-items">
          <div v-for="item in order.items" :key="item.productName" class="order-item">
            <span>{{ item.productName }}</span>
            <span class="muted">{{ item.quantity }} × {{ item.productPrice }} руб.</span>
          </div>
        </div>
        <div class="order-footer">
          <strong>Итого: {{ order.totalAmount }} руб.</strong>
          <span class="muted">Статус: {{ formatStatus(order.status) }}</span>
        </div>
        <div class="order-actions">
          <router-link :to="`/orders/${order.id}`" class="btn btn-secondary">Детали заказа</router-link>
          <button
            v-if="order.status === 'CREATED'"
            class="btn btn-secondary"
            :disabled="isCanceling === order.id"
            @click="cancelOrder(order.id)"
          >
            Отменить
          </button>
        </div>
      </article>
    </div>
  </section>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { orderService } from '@/services/order.service'
import type { Order } from '@/types'

const orders = ref<Order[]>([])
const isLoading = ref(true)
const error = ref<string | null>(null)
const isCanceling = ref<number | null>(null)

const loadOrders = async () => {
  isLoading.value = true
  error.value = null
  try {
    orders.value = await orderService.getOrders()
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Не удалось загрузить заказы'
  } finally {
    isLoading.value = false
  }
}

const formatDate = (value: string) => {
  const date = new Date(value)
  return new Intl.DateTimeFormat('ru-RU', {
    dateStyle: 'medium',
    timeStyle: 'short'
  }).format(date)
}

const formatStatus = (status: Order['status']) => {
  const map: Record<Order['status'], string> = {
    CREATED: 'Создан',
    PAID: 'Оплачен',
    SHIPPED: 'Передан в доставку',
    COMPLETED: 'Завершен',
    CANCELED: 'Отменен'
  }
  return map[status] || status
}

onMounted(loadOrders)

const cancelOrder = async (id: number) => {
  isCanceling.value = id
  try {
    await orderService.cancelOrder(id)
    await loadOrders()
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Не удалось отменить заказ'
  } finally {
    isCanceling.value = null
  }
}
</script>
