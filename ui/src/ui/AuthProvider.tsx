import { createContext, PropsWithChildren, useMemo, useState } from 'react'
import { User } from '../service/types.ts'

type UserContext = {
  setUser: (value?: User) => void
  user?: User
}

export const AuthContext = createContext<UserContext>({
  setUser: (_?: User) => {},
  user: undefined,
})

export const AuthProvider = ({ children }: PropsWithChildren) => {
  const [user, setUser] = useState<User>()

  const value = useMemo(() => ({ user, setUser }), [user])

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
