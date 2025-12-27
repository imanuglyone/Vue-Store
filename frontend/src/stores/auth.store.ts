import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/services/api.service'
import { useCartStore } from '@/stores/cart.store'

export interface User {
  id: number
  email: string
  name: string
  role: 'ROLE_USER' | 'ROLE_ADMIN'
}

export const useAuthStore = defineStore('auth', () => {
  const user = ref<User | null>(null)
  const token = ref<string | null>(localStorage.getItem('token'))
  const isLoading = ref(false)
  const error = ref<string | null>(null)

  const isAuthenticated = computed(() => !!token.value)
  const isAdmin = computed(() => user.value?.role === 'ROLE_ADMIN')

  const login = async (email: string, password: string) => {
    isLoading.value = true
    error.value = null
    try {
      const response = await api.post('/auth/login', { email, password })
      token.value = response.data.token
      user.value = response.data.user
      localStorage.setItem('token', token.value)
      await useCartStore().fetchCart()
      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Не удалось выполнить вход'
      return false
    } finally {
      isLoading.value = false
    }
  }

  const register = async (userData: { name: string; email: string; password: string }) => {
    isLoading.value = true
    error.value = null
    try {
      const response = await api.post('/auth/register', userData)
      token.value = response.data.token
      user.value = response.data.user
      localStorage.setItem('token', token.value)
      await useCartStore().fetchCart()
      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Не удалось завершить регистрацию'
      return false
    } finally {
      isLoading.value = false
    }
  }

  const logout = () => {
    token.value = null
    user.value = null
    localStorage.removeItem('token')
    useCartStore().clearCart()
  }

  const fetchUser = async () => {
    if (!token.value) return
    try {
      const response = await api.get('/users/me')
      user.value = response.data
    } catch {
      logout()
    }
  }

  return {
    user,
    token,
    isLoading,
    error,
    isAuthenticated,
    isAdmin,
    login,
    register,
    logout,
    fetchUser
  }
})
