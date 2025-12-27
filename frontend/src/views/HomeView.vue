<template>
  <div class="home-page">
    <div class="welcome-section">
      <h2 class="welcome-title text-gradient">Добро пожаловать в Vue Store</h2>
      <p class="welcome-subtitle">
        Премиальный магазин техники и аксессуаров. Откройте коллекции с вниманием к деталям,
        сервису и идеальному пользовательскому опыту.
      </p>
      <div class="welcome-actions">
        <template v-if="isAuthenticated">
          <p class="welcome-meta">
            Здравствуйте, <strong>{{ userName }}</strong>. В корзине: {{ cartItemsCount }}.
          </p>
          <div class="home-actions">
            <router-link to="/orders" class="btn btn-primary">
              Отслеживать заказы
              <i class="bi bi-arrow-right"></i>
            </router-link>
            <router-link to="/cart" class="btn btn-secondary">
              Открыть корзину
            </router-link>
          </div>
        </template>
        <template v-else>
          <p class="welcome-meta">
            Войдите в аккаунт, чтобы сохранять корзину и отслеживать заказы.
          </p>
          <div class="home-actions">
            <router-link to="/login" class="btn btn-primary">
              Войти
              <i class="bi bi-arrow-right"></i>
            </router-link>
            <router-link to="/register" class="btn btn-secondary">
              Регистрация
            </router-link>
          </div>
        </template>
      </div>
    </div>

    <div class="features-grid">
      <div class="feature-card glass-card">
        <div class="feature-icon">
          <i class="bi bi-grid-3x3-gap"></i>
        </div>
        <h3 class="feature-title">Каталог премиум-товаров</h3>
        <p class="feature-description">
          Отобранные бренды, стильные устройства и аксессуары, которые подчеркивают ваш статус.
          Более 1000 позиций с проверенной репутацией.
        </p>
        <router-link to="/products" class="btn btn-primary mt-3">
          Перейти в каталог
          <i class="bi bi-arrow-right"></i>
        </router-link>
      </div>

      <div class="feature-card glass-card">
        <div class="feature-icon">
          <i class="bi bi-cart3"></i>
        </div>
        <h3 class="feature-title">Корзина и быстрый заказ</h3>
        <p class="feature-description">
          В вашей корзине: <strong class="text-gradient">{{ cartItemsCount }}</strong><br>
          Сохраняйте подборки, оформляйте заказ в один клик и получайте персональные предложения.
        </p>
        <router-link to="/cart" class="btn btn-primary mt-3">
          Перейти в корзину
          <i class="bi bi-arrow-right"></i>
        </router-link>
      </div>

      <div class="feature-card glass-card">
        <div class="feature-icon">
          <i class="bi bi-shield-lock"></i>
        </div>
        <template v-if="isAuthenticated">
          <h3 class="feature-title">Статус ваших заказов</h3>
          <p class="feature-description">
            Следите за каждым этапом доставки, просматривайте историю и получайте уведомления.
          </p>
          <router-link to="/orders" class="btn btn-primary mt-3">
            Перейти к заказам
            <i class="bi bi-arrow-right"></i>
          </router-link>
        </template>
        <template v-else>
          <h3 class="feature-title">Безопасный вход</h3>
          <p class="feature-description">
            Защищенный личный кабинет, мгновенный доступ к заказам и персональной истории покупок.
            Вся информация хранится в соответствии с требованиями безопасности.
          </p>
          <router-link to="/login" class="btn btn-primary mt-3">
            Войти в аккаунт
            <i class="bi bi-arrow-right"></i>
          </router-link>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useAuthStore } from '@/stores/auth.store'
import { useCartStore } from '@/stores/cart.store'

const authStore = useAuthStore()
const cartStore = useCartStore()
const cartItemsCount = computed(() => cartStore.totalItems)
const isAuthenticated = computed(() => authStore.isAuthenticated)
const userName = computed(() => authStore.user?.name || 'Гость')
</script>

<style scoped>
.home-page {
  animation: fadeInUp 0.8s ease;
}

.welcome-section {
  text-align: center;
  margin-bottom: 3rem;
  padding: 0 1rem;
}

.welcome-title {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  font-weight: 700;
}

.welcome-subtitle {
  font-size: 1.2rem;
  color: var(--text-secondary);
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.6;
}

.welcome-actions {
  margin-top: 1.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.welcome-meta {
  color: var(--text-secondary);
  font-size: 1rem;
}

.home-actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 0.75rem;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-top: 3rem;
}

.feature-card {
  text-align: center;
  padding: 2rem;
  transition: all var(--transition-normal);
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 1.5rem;
  background: var(--accent-gradient);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  display: inline-block;
}

.feature-title {
  font-size: 1.5rem;
  margin-bottom: 1rem;
  color: var(--text-primary);
  font-weight: 600;
}

.feature-description {
  color: var(--text-secondary);
  line-height: 1.6;
  margin-bottom: 1.5rem;
}

@media (max-width: 768px) {
  .welcome-title {
    font-size: 2rem;
  }

  .welcome-subtitle {
    font-size: 1rem;
  }

  .features-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
}
</style>
