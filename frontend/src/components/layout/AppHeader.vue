<template>
  <header class="app-header">
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow">
      <div class="container">
        <router-link to="/" class="navbar-brand">
          🛍️ Vue Store
        </router-link>
        
        <div class="navbar-collapse">
          <ul class="navbar-nav me-auto">
            <li class="nav-item">
              <router-link to="/" class="nav-link">
                Главная
              </router-link>
            </li>
            <li class="nav-item">
              <router-link to="/products" class="nav-link">
                Каталог
              </router-link>
            </li>
            <li class="nav-item">
              <router-link to="/cart" class="nav-link">
                Корзина <span class="badge bg-danger">{{ cartItemsCount }}</span>
              </router-link>
            </li>
          </ul>
          
          <div class="d-flex">
            <div class="dropdown">
              <button class="btn btn-outline-light dropdown-toggle" type="button">
                👤 {{ user?.name || 'Гость' }}
              </button>
              <ul class="dropdown-menu dropdown-menu-end">
                <li><router-link to="/login" class="dropdown-item">Войти</router-link></li>
                <li><router-link to="/register" class="dropdown-item">Регистрация</router-link></li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </nav>
  </header>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth.store'
import { useCartStore } from '@/stores/cart.store'

const authStore = useAuthStore()
const cartStore = useCartStore()

const user = computed(() => authStore.user)
const cartItemsCount = computed(() => cartStore.totalItems)
</script>

<style scoped>
.app-header {
  position: sticky;
  top: 0;
  z-index: 1000;
}

.navbar-brand {
  font-weight: bold;
  font-size: 1.5rem;
}
</style>