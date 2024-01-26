import {
  Button,
  CircularProgress,
  Divider,
  FormControl,
  FormLabel,
  Grid,
  Link,
  TextField,
  Typography,
} from '@mui/material'
import { grey } from '@mui/material/colors'
import { useContext, useState } from 'react'
import { login } from '../../../service/login.ts'
import { AuthContext } from '../../AuthProvider.tsx'
import { SignInState } from '../../../service/types.ts'

export const Login = () => {
  const { setUser } = useContext(AuthContext)

  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')

  const [signInState, setSignInState] = useState(SignInState.SIGNED_OUT)
  const hasError = signInState === SignInState.SIGN_ERROR

  const onSubmit = async () => {
    if (!username || !password) {
      console.error('Username and password are required.')
      return
    }

    setSignInState(SignInState.SIGNING_IN)

    try {
      const user = await login(username, password)
      setUser(user)
    } catch (e) {
      setSignInState(SignInState.SIGN_ERROR)
    }
  }

  return (
    <Grid
      container
      sx={{
        background: grey[600],
        borderRadius: 2,
        marginTop: 10,
        boxShadow: '4px 4px 3px #616161\n',
      }}
      margin="auto"
      width="20%"
      padding={2}
      spacing={1}
    >
      <Grid item xs={12}>
        <Typography
          variant="h6"
          color={grey[900]}
          fontWeight={800}
          textAlign="left"
        >
          Log in to your account
        </Typography>
      </Grid>

      <Grid item xs={12}>
        <Divider />
      </Grid>

      <Grid item xs={12}>
        <FormControl
          sx={{ gap: 1, textAlign: 'left', display: 'flex', marginTop: 2 }}
        >
          <FormLabel sx={{ fontWeight: 800, color: grey[900] }}>
            USERNAME
          </FormLabel>
          <TextField
            size={'small'}
            required
            id="username-input"
            color={'info'}
            onChange={e => setUsername(e.target.value)}
            error={hasError}
          />

          <FormLabel sx={{ fontWeight: 800, color: grey[900] }}>
            PASSWORD
          </FormLabel>
          <TextField
            size={'small'}
            required
            id="pasword-input"
            type={'password'}
            color={'info'}
            onChange={e => setPassword(e.target.value)}
            error={hasError}
          />

          <Button
            variant={'contained'}
            sx={{ marginTop: 4, marginBottom: 4 }}
            onClick={onSubmit}
          >
            {signInState === SignInState.SIGNING_IN ? (
              <CircularProgress />
            ) : (
              'Log in'
            )}
          </Button>
        </FormControl>
      </Grid>

      {hasError && (
        <Grid item xs={12}>
          <Typography variant="body1" color={'red'}>
            Error logging in
          </Typography>
        </Grid>
      )}

      <Grid item xs={12}>
        <Typography variant="body1" color={'black'}>
          Don't have an account?
        </Typography>
        <Typography variant="body1" color={'black'} component={Link}>
          Contact an admin to create one for you
        </Typography>
      </Grid>
    </Grid>
  )
}
