import { Wrapper } from '../../components/Wrapper.tsx'
import { Card, Item } from '../../components/Card.tsx'
import { User } from '../../../service/types.ts'
import { useCallback, useContext, useEffect, useState } from 'react'
import { getAllUsers } from '../../../service/users.ts'
import { CreateUser } from './CreateUser.tsx'
import { AuthContext } from '../../AuthProvider.tsx'
import { orangeBgColorStyle } from '../../utils.ts'

enum Status {
  USER_SELECTED,
  CREATING_USER,
  IDLE,
}

export const UserManagement = () => {
  const { user } = useContext(AuthContext)
  const [users, setUsers] = useState<User[]>()

  const [status, setStatus] = useState(Status.IDLE)

  const refreshUsers = useCallback(() => {
    getAllUsers(user?.token ?? '').then(allUsers => {
      setUsers(allUsers)
    })
  }, [])

  useEffect(() => {
    refreshUsers()
  }, [])

  return (
    <Wrapper title="User Management">
      <Card
        title="Users"
        footer={
          <Item
            title="New User"
            style={orangeBgColorStyle}
            onClick={() =>
              setStatus(Status.CREATING_USER)
            }
          />
        }
      >
        {users?.map(user => (
          <Item
            title={user.username}
            key={user.id}
            onClick={() => setStatus(Status.USER_SELECTED)}
          />
        ))}
      </Card>

      {status === Status.CREATING_USER && (
        <CreateUser
          onCreate={() => refreshUsers()}
        />
      )}
    </Wrapper>
  )
}
