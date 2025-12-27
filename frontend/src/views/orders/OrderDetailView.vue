<template>
  <section class="page-shell order-detail-view">
    <header class="page-header">
      <div>
        <p class="eyebrow">Заказ</p>
        <h1 class="page-title">Заказ #{{ order?.id }}</h1>
        <p class="page-subtitle">
          Статус: <span class="text-gradient">{{ order ? formatStatus(order.status) : '' }}</span>
        </p>
      </div>
      <div class="page-actions">
        <router-link to="/orders" class="btn btn-secondary">Назад к списку</router-link>
      </div>
    </header>

    <div v-if="isLoading" class="glass-card empty-state">
      Загрузка заказа...
    </div>
    <div v-else-if="error" class="glass-card empty-state">
      {{ error }}
    </div>
    <div v-else-if="order" class="order-detail-grid">
      <div class="glass-card order-detail-card">
        <h3>Состав заказа</h3>
        <div class="order-items">
          <div v-for="item in order.items" :key="item.productName" class="order-item">
            <span>{{ item.productName }}</span>
            <span class="muted">{{ item.quantity }} × {{ item.productPrice }} руб.</span>
          </div>
        </div>
        <div class="order-footer">
          <strong>Итого: {{ order.totalAmount }} руб.</strong>
          <span class="muted">Дата: {{ formatDate(order.createdAt) }}</span>
        </div>
      </div>

      <div class="glass-card order-detail-card">
        <h3>Отслеживание</h3>
        <div class="order-meta">
          <p class="muted">Получатель: {{ order.fullName }}</p>
          <p class="muted">Телефон: {{ order.phone }}</p>
          <p class="muted">Адрес: {{ order.address }}</p>
          <p class="muted">Оплата: {{ paymentLabel }}</p>
        </div>
        <ol class="order-tracking">
          <li :class="{ active: isStatusAtLeast('CREATED') }">Заказ создан</li>
          <li :class="{ active: isStatusAtLeast('PAID') }">Оплата подтверждена</li>
          <li :class="{ active: isStatusAtLeast('SHIPPED') }">Передан в доставку</li>
          <li :class="{ active: isStatusAtLeast('COMPLETED') }">Доставлен</li>
        </ol>
        <div class="order-actions">
          <button
            v-if="order.status === 'CREATED'"
            class="btn btn-secondary"
            :disabled="isCanceling"
            @click="cancelOrder"
          >
            Отменить заказ
          </button>
        </div>
        <p class="muted">
          Для уточнения статуса свяжитесь с поддержкой через страницу контактов.
        </p>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { orderService } from '@/services/order.service'
import type { Order } from '@/types'

const route = useRoute()
const order = ref<Order | null>(null)
const isLoading = ref(true)
const error = ref<string | null>(null)
const isCanceling = ref(false)

const loadOrder = async () => {
  isLoading.value = true
  error.value = null
  try {
    const id = Number(route.params.id)
    order.value = await orderService.getOrder(id)
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Не удалось загрузить заказ'
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

const statusOrder = ['CREATED', 'PAID', 'SHIPPED', 'COMPLETED']

const isStatusAtLeast = (status: string) => {
  if (!order.value) return false
  return statusOrder.indexOf(order.value.status) >= statusOrder.indexOf(status)
}

const paymentLabel = computed(() => {
  if (!order.value) return ''
  if (order.value.paymentMethod === 'CASH') {
    return 'Наличные при получении'
  }
  const last4 = order.value.paymentLast4 ? ` **** ${order.value.paymentLast4}` : ''
  return `Карта${last4}`
})

onMounted(loadOrder)

const cancelOrder = async () => {
  if (!order.value) return
  isCanceling.value = true
  try {
    order.value = await orderService.cancelOrder(order.value.id)
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Не удалось отменить заказ'
  } finally {
    isCanceling.value = false
  }
}
</script>
