import api from './api.service'
import type { Product } from '@/types'

export const productService = {
  async getAll(): Promise<Product[]> {
    const response = await api.get('/products')
    return response.data
  },

  async getById(id: number): Promise<Product> {
    const response = await api.get(`/products/${id}`)
    return response.data
  }
}
