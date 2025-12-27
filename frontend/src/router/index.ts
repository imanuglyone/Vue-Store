import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/HomeView.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/auth/LoginView.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/auth/RegisterView.vue')
    },
    {
      path: '/products',
      name: 'products',
      component: () => import('@/views/products/ProductsView.vue')
    },
    {
      path: '/cart',
      name: 'cart',
      component: () => import('@/views/cart/CartView.vue')
    },
    {
      path: '/checkout',
      name: 'checkout',
      component: () => import('@/views/checkout/CheckoutView.vue')
    },
    {
      path: '/orders',
      name: 'orders',
      component: () => import('@/views/orders/OrdersView.vue')
    },
    {
      path: '/orders/:id',
      name: 'order-detail',
      component: () => import('@/views/orders/OrderDetailView.vue')
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('@/views/profile/ProfileView.vue')
    },
    {
      path: '/admin',
      name: 'admin',
      component: () => import('@/views/admin/AdminView.vue')
    },
    {
      path: '/help',
      name: 'help',
      component: () => import('@/views/help/HelpView.vue')
    },
    {
      path: '/contacts',
      name: 'contacts',
      component: () => import('@/views/contacts/ContactsView.vue')
    }
  ]
})

export default router
