<template>
  <footer class="app-footer mt-auto py-4 bg-dark text-white">
    <div class="container">
      <div class="row">
        <div class="col-md-4">
          <h5>Магазин Vue</h5>
          <p>Лучшие товары по доступным ценам</p>
        </div>
        <div class="col-md-4">
          <h5>Контакты</h5>
          <p>Email: info@vue-store.ru</p>
          <p>Телефон: +7 (999) 123-45-67</p>
        </div>
        <div class="col-md-4">
          <h5>Быстрые ссылки</h5>
          <ul class="list-unstyled">
            <li><router-link to="/" class="text-white text-decoration-none">Главная</router-link></li>
            <li><router-link to="/products" class="text-white text-decoration-none">Каталог</router-link></li>
            <li><router-link to="/cart" class="text-white text-decoration-none">Корзина</router-link></li>
          </ul>
        </div>
      </div>
      <hr class="text-white">
      <p class="text-center mb-0">&copy; 2024 Vue Store. Все права защищены.</p>
    </div>
  </footer>
</template>

<script setup lang="ts">
</script>

<style scoped>
.app-footer {
  margin-top: auto;
}
</style>