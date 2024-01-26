import { User } from './types.ts'
import { ApiMethod, doApiRequest } from './utils.ts'

enum UsersEndpoints {
  GetAll = 'users',
}

export const getAllUsers = async (token: string): Promise<User[]> => {
  return await doApiRequest({
    method: ApiMethod.get,
    path: UsersEndpoints.GetAll,
    token,
  })
}

export const postUser = async (token: string): Promise<User[]> => {
  return await doApiRequest({
    method: ApiMethod.post,
    path: UsersEndpoints.GetAll,
    token,
  })
}
