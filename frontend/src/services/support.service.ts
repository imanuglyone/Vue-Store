import api from './api.service'

export interface ContactRequest {
  name: string
  email: string
  message: string
}

export const supportService = {
  async sendContact(request: ContactRequest): Promise<void> {
    await api.post('/support/contact', request)
  }
}
