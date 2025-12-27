<template>
  <section class="page-shell checkout-view">
    <header class="page-header">
      <div>
        <p class="eyebrow">Оформление</p>
        <h1 class="page-title">Оформление заказа</h1>
        <p class="page-subtitle">
          Проверьте данные и подтвердите покупку. Заказ появится в разделе “Заказы”.
        </p>
      </div>
      <div class="page-actions">
        <router-link to="/cart" class="btn btn-secondary">Назад в корзину</router-link>
      </div>
    </header>

    <div v-if="cartStore.items.length === 0" class="glass-card empty-state">
      <h2>Корзина пуста</h2>
      <p>Добавьте товары в каталоге и возвращайтесь для оформления.</p>
      <router-link to="/products" class="btn btn-primary">Перейти в каталог</router-link>
    </div>

    <div v-else class="checkout-grid">
      <div class="glass-card checkout-card">
        <h3>Контактные данные</h3>
        <form class="checkout-form" @submit.prevent="submitOrder">
          <div class="form-field">
            <label class="form-label" for="fullName">Имя и фамилия</label>
            <input
              id="fullName"
              class="form-input"
              type="text"
              v-model="fullName"
              required
              placeholder="Иван Петров"
            >
          </div>
          <div class="form-field">
            <label class="form-label" for="phone">Телефон</label>
            <input
              id="phone"
              class="form-input"
              type="tel"
              v-model="phone"
              required
              placeholder="+7 (999) 123-45-67"
            >
          </div>
          <div class="form-field">
            <label class="form-label" for="address">Адрес доставки</label>
            <input
              id="address"
              class="form-input"
              type="text"
              v-model="address"
              required
              placeholder="Город, улица, дом"
            >
          </div>
          <div class="form-field">
            <label class="form-label" for="comment">Комментарий</label>
            <textarea
              id="comment"
              class="form-textarea"
              rows="3"
              v-model="comment"
              placeholder="Пожелания по доставке"
            ></textarea>
          </div>
          <div class="form-field">
            <label class="form-label" for="paymentMethod">Способ оплаты</label>
            <select id="paymentMethod" class="form-input" v-model="paymentMethod" required>
              <option value="CARD">Карта</option>
              <option value="CASH">Наличные при получении</option>
            </select>
          </div>
          <div class="form-field" v-if="paymentMethod === 'CARD'">
            <label class="form-label" for="paymentLast4">Последние 4 цифры карты</label>
            <input
              id="paymentLast4"
              class="form-input"
              type="text"
              v-model="paymentLast4"
              maxlength="4"
              placeholder="1234"
              required
            >
          </div>
          <button class="btn btn-primary w-100" :disabled="isSubmitting">
            <span v-if="isSubmitting" class="spinner"></span>
            <span v-else>Подтвердить заказ</span>
          </button>
        </form>
      </div>

      <aside class="glass-card checkout-summary">
        <h3>Ваш заказ</h3>
        <div class="order-items">
          <div v-for="item in cartStore.items" :key="item.productId" class="order-item">
            <span>{{ item.name }}</span>
            <span class="muted">{{ item.quantity }} × {{ item.price }} руб.</span>
          </div>
        </div>
        <div class="summary-row">
          <span>Товаров</span>
          <span>{{ cartStore.totalItems }}</span>
        </div>
        <div class="summary-row">
          <span>Сумма</span>
          <span>{{ cartStore.totalPrice }} руб.</span>
        </div>
        <div class="summary-divider"></div>
        <div v-if="error" class="alert-box">{{ error }}</div>
        <div v-if="success" class="alert-box">Заказ оформлен. Переходим в список заказов...</div>
      </aside>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart.store'
import { orderService } from '@/services/order.service'

const cartStore = useCartStore()
const router = useRouter()

const fullName = ref('')
const phone = ref('')
const address = ref('')
const comment = ref('')
const paymentMethod = ref('CARD')
const paymentLast4 = ref('')
const isSubmitting = ref(false)
const error = ref<string | null>(null)
const success = ref(false)

const submitOrder = async () => {
  isSubmitting.value = true
  error.value = null
  success.value = false
  try {
    await orderService.createOrder({
      fullName: fullName.value,
      phone: phone.value,
      address: address.value,
      paymentMethod: paymentMethod.value,
      paymentLast4: paymentMethod.value === 'CARD' ? paymentLast4.value : undefined
    })
    await cartStore.fetchCart()
    success.value = true
    setTimeout(() => {
      router.push('/orders')
    }, 800)
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Не удалось оформить заказ'
  } finally {
    isSubmitting.value = false
  }
}
</script>
