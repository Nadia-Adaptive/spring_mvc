import { config } from '../config.ts'

const getHeaders = async (token?: string) => {
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  }

  if (token) {
    headers.Authorization = `Bearer ${token}`
  }

  return headers
}

export enum ApiMethod {
  get = 'GET',
  post = 'POST',
  put = 'PUT',
  delete = 'DELETE',
}

type ApiRequest<P> = {
  method: ApiMethod
  path: string
  body?: P
  token?: string
}

export const doApiRequest = async <P, R>({
  method,
  path,
  body,
  token,
}: ApiRequest<P>): Promise<R> => {
  const url = `${config.BE_API}/${path}`
  const headers = await getHeaders(token)

  let response = await fetch(url, {
    method,
    headers,
    body: body ? JSON.stringify(body) : undefined,
  })

  if (response.status === 401) {
    console.error('Unauthorized request')
    console.error(response)
  }

  // NO_CONTENT response (204) has no data in it, but its successful
  if (response.status === 204) return response as R

  const data = await response.json()

  // We can get successful responses 200 (OK) and 201 (CREATED)
  if (response.status !== 200 && response.status !== 201) {
    const errorMessage = `${response.status}: ${(data as Error).message}`
    throw new Error(errorMessage)
  }

  return data as R
}
