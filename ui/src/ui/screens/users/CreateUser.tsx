import { Card, Item } from '../../components/Card.tsx'
import { FormLabel } from '@mui/material'
import { useContext, useState } from 'react'
import { User, UserDetails } from '../../../service/types.ts'
import { postUser } from '../../../service/users.ts'
import { FormField } from '../../components/FormField.tsx'
import { AuthContext } from '../../AuthProvider.tsx'
import { orangeBgColorStyle } from '../../utils.ts'

const initialDetails = {
  username: '',
  password: '',
  firstName: '',
  lastName: '',
  organisation: '',
}

// TODO: Add validation
export const CreateUser = ({
  onCreate,
}: {
  onCreate: (data: User) => void
}) => {
  const { user } = useContext(AuthContext)
  const [userDetails, setUserDetails] = useState<UserDetails>(initialDetails)
  const [repeatPassword, setRepeatPassword] = useState<string>('')
  const passwordError = Boolean(
    repeatPassword &&
      userDetails.password &&
      repeatPassword !== userDetails.password
  )

  const createUser = async () => {
    try {
      const res = await postUser(userDetails, user?.token ?? '')
      onCreate(res)
    } catch (e) {
      console.log(e)
    }
  }

  return (
    <Card
      title="User fields"
      footer={
        <Item
          title="Create User"
          style={orangeBgColorStyle}
          onClick={createUser}
        />
      }
    >
      <FormLabel sx={{ color: 'white', textAlign: 'left' }}>Username</FormLabel>
      <FormField
        id="username-input"
        type="text"
        onChange={e =>
          setUserDetails({
            ...userDetails,
            username: e.target.value,
          })
        }
      />

      <FormLabel sx={{ color: 'white', textAlign: 'left' }}>Password</FormLabel>
      <FormField
        id="pasword-input"
        type="password"
        onChange={e =>
          setUserDetails({
            ...userDetails,
            password: e.target.value,
          })
        }
      />

      <FormLabel sx={{ color: 'white', textAlign: 'left' }}>
        Repeat Password
      </FormLabel>
      <FormField
        id="pasword-input"
        type="password"
        onChange={e => setRepeatPassword(e.target.value)}
        error={passwordError}
        helperText={passwordError ? "Passwords don't match." : ''}
      />

      <FormLabel sx={{ color: 'white', textAlign: 'left' }}>
        First Name
      </FormLabel>
      <FormField
        id="firstName-input"
        type="text"
        onChange={e =>
          setUserDetails({
            ...userDetails,
            firstName: e.target.value,
          })
        }
      />

      <FormLabel sx={{ color: 'white', textAlign: 'left' }}>
        Last Name
      </FormLabel>
      <FormField
        id="lastName-input"
        type="text"
        onChange={e =>
          setUserDetails({
            ...userDetails,
            lastName: e.target.value,
          })
        }
      />

      <FormLabel sx={{ color: 'white', textAlign: 'left' }}>
        Organisation
      </FormLabel>
      <FormField
        id="organisation-input"
        type="text"
        onChange={e =>
          setUserDetails({
            ...userDetails,
            organisation: e.target.value,
          })
        }
      />
    </Card>
  )
}
