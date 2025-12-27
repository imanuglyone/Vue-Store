import api from './api.service'
import type { Order } from '@/types'

export interface OrderCreateRequest {
  fullName: string
  phone: string
  address: string
  paymentMethod: string
  paymentLast4?: string
}

export const orderService = {
  async createOrder(payload: OrderCreateRequest): Promise<Order> {
    const response = await api.post('/orders', payload)
    return response.data
  },

  async getOrders(): Promise<Order[]> {
    const response = await api.get('/orders')
    return response.data
  },

  async getOrder(id: number): Promise<Order> {
    const response = await api.get(`/orders/${id}`)
    return response.data
  },

  async cancelOrder(id: number): Promise<Order> {
    const response = await api.patch(`/orders/${id}/cancel`)
    return response.data
  }
}
