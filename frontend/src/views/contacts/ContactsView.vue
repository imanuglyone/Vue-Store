<template>
  <section class="page-shell contacts-view">
    <header class="page-header">
      <div>
        <p class="eyebrow">Контакты</p>
        <h1 class="page-title">Свяжитесь с нами</h1>
        <p class="page-subtitle">
          Мы готовы помочь с подбором, статусом заказа или корпоративными запросами.
        </p>
      </div>
      <div class="page-actions">
        <span class="pill">Пн-Пт 10:00-20:00</span>
      </div>
    </header>

    <div class="info-grid">
      <div class="glass-card info-card">
        <h3>Телефон</h3>
        <p>+7 (999) 123-45-67</p>
        <p class="muted">Звонки принимаем ежедневно.</p>
      </div>
      <div class="glass-card info-card">
        <h3>Email</h3>
        <p>info@vue-store.ru</p>
        <p class="muted">Ответим в течение 1 часа.</p>
      </div>
      <div class="glass-card info-card">
        <h3>Шоурум</h3>
        <p>Москва, ул. Арбат, 12</p>
        <p class="muted">Посещение по предварительной записи.</p>
      </div>
    </div>

    <div class="glass-card contact-form">
      <h3>Напишите нам</h3>
      <form class="contact-grid" @submit.prevent="submitForm">
        <div class="form-field">
          <label for="contactName" class="form-label">Имя</label>
          <input
            id="contactName"
            class="form-input"
            type="text"
            placeholder="Ваше имя"
            v-model="name"
            required
          >
        </div>
        <div class="form-field">
          <label for="contactEmail" class="form-label">Email</label>
          <input
            id="contactEmail"
            class="form-input"
            type="email"
            placeholder="example@mail.ru"
            v-model="email"
            required
          >
        </div>
        <div class="form-field contact-message">
          <label for="contactMessage" class="form-label">Сообщение</label>
          <textarea
            id="contactMessage"
            class="form-textarea"
            rows="4"
            placeholder="Как мы можем помочь?"
            v-model="message"
            required
          ></textarea>
        </div>
        <button class="btn btn-primary" :disabled="isSending">Отправить</button>
      </form>
      <div v-if="success" class="alert-box">
        Спасибо! Мы получили ваше сообщение.
      </div>
      <div v-if="error" class="alert-box">
        {{ error }}
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { supportService } from '@/services/support.service'

const name = ref('')
const email = ref('')
const message = ref('')
const isSending = ref(false)
const success = ref(false)
const error = ref<string | null>(null)

const submitForm = async () => {
  isSending.value = true
  success.value = false
  error.value = null
  try {
    await supportService.sendContact({
      name: name.value,
      email: email.value,
      message: message.value
    })
    success.value = true
    name.value = ''
    email.value = ''
    message.value = ''
  } catch (err: any) {
    error.value = err.response?.data?.message || 'Не удалось отправить сообщение'
  } finally {
    isSending.value = false
  }
}
</script>
