import api from './api.service'
import type { Order, Product } from '@/types'

export interface ProductRequest {
  name: string
  description: string
  price: number
  imageUrl: string
  category: string
  stock: number
}

export const adminService = {
  async getProducts(): Promise<Product[]> {
    const response = await api.get('/admin/products')
    return response.data
  },

  async createProduct(payload: ProductRequest): Promise<Product> {
    const response = await api.post('/admin/products', payload)
    return response.data
  },

  async updateProduct(id: number, payload: ProductRequest): Promise<Product> {
    const response = await api.put(`/admin/products/${id}`, payload)
    return response.data
  },

  async deleteProduct(id: number): Promise<void> {
    await api.delete(`/admin/products/${id}`)
  },

  async uploadImage(file: File): Promise<string> {
    const form = new FormData()
    form.append('file', file)
    const response = await api.post('/admin/products/upload', form)
    return response.data.url
  },

  async getAllOrders(): Promise<Order[]> {
    const response = await api.get('/admin/orders')
    return response.data
  },

  async updateOrderStatus(id: number, status: string): Promise<Order> {
    const response = await api.patch(`/admin/orders/${id}/status`, { status })
    return response.data
  }
}
