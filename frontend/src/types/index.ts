export interface User {
  id: number
  email: string
  name: string
  role: 'ROLE_USER' | 'ROLE_ADMIN'
}

export interface Product {
  id: number
  name: string
  description: string
  price: number
  imageUrl: string
  category: string
  stock: number
  createdAt: string
}

export interface CartItem {
  productId: number
  name: string
  imageUrl: string
  price: number
  quantity: number
  lineTotal: number
}

export interface CartResponse {
  items: CartItem[]
  totalItems: number
  totalPrice: number
}

export interface Order {
  id: number
  status: 'CREATED' | 'PAID' | 'SHIPPED' | 'COMPLETED' | 'CANCELED'
  totalAmount: number
  createdAt: string
  items: OrderItem[]
  fullName: string
  phone: string
  address: string
  paymentMethod: string
  paymentLast4: string | null
  paymentStatus: string
}

export interface OrderItem {
  productName: string
  productPrice: number
  quantity: number
  lineTotal: number
}
