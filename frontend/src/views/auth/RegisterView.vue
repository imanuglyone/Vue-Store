<template>
  <section class="page-shell auth-view">
    <div class="auth-card glass-card">
      <div class="auth-header">
        <p class="eyebrow">Регистрация</p>
        <h1 class="page-title">Новый аккаунт</h1>
        <p class="page-subtitle">Создайте профиль, чтобы получить персональные предложения.</p>
      </div>

      <form class="auth-form" @submit.prevent="handleRegister">
        <div class="form-field">
          <label for="name" class="form-label">Имя</label>
          <input
            type="text"
            id="name"
            v-model="name"
            class="form-input"
            required
            placeholder="Ваше имя"
          >
        </div>

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
            placeholder="Придумайте пароль"
          >
        </div>

        <div class="form-field">
          <label for="confirmPassword" class="form-label">Подтвердите пароль</label>
          <input
            type="password"
            id="confirmPassword"
            v-model="confirmPassword"
            class="form-input"
            required
            placeholder="Повторите пароль"
          >
        </div>

        <div v-if="authStore.error" class="alert-box">
          {{ authStore.error }}
        </div>

        <button
          type="submit"
          class="btn btn-primary w-100"
          :disabled="authStore.isLoading || password !== confirmPassword"
        >
          <span v-if="authStore.isLoading" class="spinner"></span>
          <span v-else>Создать аккаунт</span>
        </button>
      </form>

      <div class="auth-footer">
        <p>
          Уже зарегистрированы?
          <router-link to="/login" class="link">Войти</router-link>
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

const name = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')

const handleRegister = async () => {
  if (password.value !== confirmPassword.value) {
    authStore.error = 'Пароли не совпадают'
    return
  }

  const userData = {
    name: name.value,
    email: email.value,
    password: password.value
  }

  const success = await authStore.register(userData)
  if (success) {
    router.push('/')
  }
}
</script>
