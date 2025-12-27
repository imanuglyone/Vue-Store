<template>
  <section class="page-shell auth-view">
    <div class="auth-card glass-card">
      <div class="auth-header">
        <p class="eyebrow">Вход</p>
        <h1 class="page-title">С возвращением</h1>
        <p class="page-subtitle">Войдите, чтобы продолжить покупки в премиальном каталоге.</p>
      </div>

      <form class="auth-form" @submit.prevent="handleLogin">
        <div class="form-field">
          <label for="email" class="form-label">Email</label>
          <input
            type="email"
            id="email"
            v-model="email"
            class="form-input"
            required
            placeholder="example@mail.ru"
          >
        </div>

        <div class="form-field">
          <label for="password" class="form-label">Пароль</label>
          <input
            type="password"
            id="password"
            v-model="password"
            class="form-input"
            required
            placeholder="Введите пароль"
          >
        </div>

        <div v-if="authStore.error" class="alert-box">
          {{ authStore.error }}
        </div>

        <button
          type="submit"
          class="btn btn-primary w-100"
          :disabled="authStore.isLoading"
        >
          <span v-if="authStore.isLoading" class="spinner"></span>
          <span v-else>Войти</span>
        </button>
      </form>

      <div class="auth-footer">
        <p>
          Еще нет аккаунта?
          <router-link to="/register" class="link">Создать аккаунт</router-link>
        </p>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store'

const router = useRouter()
const authStore = useAuthStore()

const email = ref('test@example.com')
const password = ref('password')

const handleLogin = async () => {
  const success = await authStore.login(email.value, password.value)
  if (success) {
    router.push('/')
  }
}
</script>
