<template>
  <section class="page-shell cart-view">
    <header class="page-header">
      <div>
        <p class="eyebrow">Корзина</p>
        <h1 class="page-title">Ваши покупки</h1>
        <p class="page-subtitle">
          Проверьте состав заказа, выберите удобную доставку и подтвердите покупку.
        </p>
      </div>
      <div class="page-actions">
        <span class="pill">Позиций: {{ cartStore.totalItems }}</span>
      </div>
    </header>

    <div v-if="cartStore.items.length === 0" class="glass-card empty-state">
      <h2>Корзина пуста</h2>
      <p>Добавьте товары в каталоге, и здесь появятся ваши избранные позиции.</p>
      <router-link to="/products" class="btn btn-primary">Перейти в каталог</router-link>
    </div>

    <div v-else class="cart-layout">
      <div class="cart-list">
        <div
          v-for="item in cartStore.items"
          :key="item.productId"
          class="glass-card cart-item"
        >
          <div class="cart-item-media">
            <img
              :src="resolveImageUrl(item.imageUrl)"
              :alt="item.name"
              class="cart-item-image"
            >
          </div>
          <div class="cart-item-info">
            <h3 class="cart-item-title">{{ item.name }}</h3>
            <p class="cart-item-meta">{{ item.price }} руб.</p>
            <button class="link-button" @click="removeItem(item.productId)">Удалить</button>
          </div>
          <div class="cart-item-qty">
            <button
              class="qty-btn"
              @click="updateQuantity(item.productId, item.quantity - 1)"
              :disabled="item.quantity <= 1"
            >
              -
            </button>
            <input
              type="number"
              v-model.number="item.quantity"
              @change="updateQuantity(item.productId, item.quantity)"
              class="qty-input"
              min="1"
            >
            <button
              class="qty-btn"
              @click="updateQuantity(item.productId, item.quantity + 1)"
            >
              +
            </button>
          </div>
          <div class="cart-item-total">
            {{ item.lineTotal }} руб.
          </div>
        </div>
      </div>

      <aside class="glass-card cart-summary">
        <h3>Итого</h3>
        <div class="summary-row">
          <span>Товаров</span>
          <span>{{ cartStore.totalItems }}</span>
        </div>
        <div class="summary-row">
          <span>Сумма</span>
          <span>{{ cartStore.totalPrice }} руб.</span>
        </div>
        <div class="summary-divider"></div>
        <router-link to="/checkout" class="btn btn-primary w-100">Оформить заказ</router-link>
        <router-link to="/products" class="btn btn-secondary w-100">Продолжить покупки</router-link>
      </aside>
    </div>
  </section>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useCartStore } from '@/stores/cart.store'
import { resolveImageUrl } from '@/utils/image'

const cartStore = useCartStore()

const updateQuantity = async (productId: number, quantity: number) => {
  await cartStore.updateQuantity(productId, quantity)
}

const removeItem = async (productId: number) => {
  await cartStore.removeItem(productId)
}

onMounted(() => {
  cartStore.fetchCart()
})
</script>
