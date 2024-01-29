import { LoginRequest, User } from './types.ts'
import { ApiMethod, doApiRequest } from './utils.ts'

enum AuthEndpoints {
  login = 'auth/login',
  logout = 'auth/logout',
}

export const login = async (
  username: string,
  password: string
): Promise<User> => {
  const body: LoginRequest = {
    username,
    password,
  }

  return await doApiRequest({
    method: ApiMethod.post,
    body,
    path: AuthEndpoints.login,
  })
}

export const logout = async (token: string) => {
  return await doApiRequest({
    method: ApiMethod.post,
    path: AuthEndpoints.logout,
    token,
  })
}
