import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/services/api.service'
import type { CartItem, CartResponse, Product } from '@/types'

export const useCartStore = defineStore('cart', () => {
  const items = ref<CartItem[]>([])

  const totalItems = computed(() => {
    return items.value.reduce((total, item) => total + item.quantity, 0)
  })

  const totalPrice = computed(() => {
    return items.value.reduce((total, item) => total + item.lineTotal, 0)
  })

  const applyCart = (response: CartResponse) => {
    items.value = response.items
  }

  const fetchCart = async () => {
    const response = await api.get('/cart')
    applyCart(response.data)
  }

  const addItem = async (product: Product, quantity: number = 1) => {
    const response = await api.post('/cart/items', {
      productId: product.id,
      quantity
    })
    applyCart(response.data)
  }

  const updateQuantity = async (productId: number, quantity: number) => {
    const response = await api.patch(`/cart/items/${productId}`, { quantity })
    applyCart(response.data)
  }

  const removeItem = async (productId: number) => {
    const response = await api.delete(`/cart/items/${productId}`)
    applyCart(response.data)
  }

  const clearCart = () => {
    items.value = []
  }

  const getItemCount = (productId: number) => {
    const item = items.value.find(entry => entry.productId === productId)
    return item ? item.quantity : 0
  }

  return {
    items,
    totalItems,
    totalPrice,
    fetchCart,
    addItem,
    removeItem,
    updateQuantity,
    clearCart,
    getItemCount
  }
})
