<template>
  <div id="app" class="dark-theme main-layout">
    <header class="store-header">
      <div class="store-title-wrapper">
        <div class="title-border"></div>
        <h1 class="store-title">
          <router-link to="/" class="store-title-link">Vue Store</router-link>
        </h1>
      </div>
    </header>

    <nav class="main-navigation">
      <router-link to="/products" class="nav-item" active-class="active">
        <i class="bi bi-grid-3x3-gap"></i>
        Каталог товаров
      </router-link>
      <router-link to="/cart" class="nav-item" active-class="active">
        <i class="bi bi-cart3"></i>
        Корзина
        <span v-if="cartItemsCount > 0" class="cart-badge">{{ cartItemsCount }}</span>
      </router-link>
      <router-link to="/orders" class="nav-item" active-class="active">
        <i class="bi bi-receipt"></i>
        Заказы
      </router-link>
      <router-link v-if="!isAuthenticated" to="/login" class="nav-item" active-class="active">
        <i class="bi bi-box-arrow-in-right"></i>
        Вход
      </router-link>
      <router-link v-if="!isAuthenticated" to="/register" class="nav-item" active-class="active">
        <i class="bi bi-person-plus"></i>
        Регистрация
      </router-link>
      <router-link v-if="isAuthenticated" to="/profile" class="nav-item" active-class="active">
        <i class="bi bi-person-circle"></i>
        Профиль
      </router-link>
      <router-link v-if="isAdmin" to="/admin" class="nav-item" active-class="active">
        <i class="bi bi-shield-lock"></i>
        Админ
      </router-link>
      <button v-if="isAuthenticated" class="nav-item nav-button" @click="handleLogout">
        <i class="bi bi-box-arrow-right"></i>
        Выйти
      </button>
    </nav>

    <div class="quick-links-bar">
      <router-link to="/products" class="quick-link">
        <i class="bi bi-lightning-charge"></i>
        Хиты продаж
      </router-link>
      <router-link to="/products?category=electronics" class="quick-link">
        <i class="bi bi-cpu"></i>
        Электроника
      </router-link>
      <router-link to="/products?category=fashion" class="quick-link">
        <i class="bi bi-shop"></i>
        Мода
      </router-link>
      <router-link to="/help" class="quick-link">
        <i class="bi bi-question-circle"></i>
        Помощь
      </router-link>
      <router-link to="/contacts" class="quick-link">
        <i class="bi bi-telephone"></i>
        Контакты
      </router-link>
    </div>

    <main class="content-wrapper">
      <div class="centered-content">
        <router-view v-slot="{ Component }">
          <transition name="page-transition" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </main>

    <footer class="main-footer">
      <div class="footer-content">
        <p>(c) 2024 Vue Store. Все права защищены.</p>
        <p>Email: info@vue-store.ru | Телефон: +7 (999) 123-45-67</p>
        <div class="mt-3">
          <router-link to="/products" class="quick-link mx-2">Каталог товаров</router-link>
          <router-link to="/cart" class="quick-link mx-2">Корзина</router-link>
          <router-link to="/login" class="quick-link mx-2">Вход</router-link>
        </div>
      </div>
    </footer>

    <Notifications />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store'
import { useCartStore } from '@/stores/cart.store'

const router = useRouter()
const authStore = useAuthStore()
const cartStore = useCartStore()

const cartItemsCount = computed(() => cartStore.totalItems)
const isAuthenticated = computed(() => authStore.isAuthenticated)
const isAdmin = computed(() => authStore.isAdmin)

onMounted(() => {
  if (authStore.token) {
    authStore.fetchUser()
    cartStore.fetchCart()
  }
})

const handleLogout = () => {
  authStore.logout()
  router.push('/')
}
</script>

<style>
@import '@/assets/main.css';

.dark-theme {
  background-color: var(--bg-primary);
  color: var(--text-primary);
  min-height: 100vh;
}

.page-enter-active {
  animation: fadeInUp 0.5s ease;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
