const apiBase = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'
const apiOrigin = apiBase.replace(/\/api\/?$/, '')

export const resolveImageUrl = (path?: string) => {
  if (!path) return '/placeholder-image.jpg'
  if (path.startsWith('http')) return path
  if (path.startsWith('/')) return `${apiOrigin}${path}`
  return `${apiOrigin}/uploads/${path}`
}
