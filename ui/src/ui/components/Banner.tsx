import { AppBar, Button, SvgIcon, Toolbar, Typography } from '@mui/material'
import auctionHouse from '../../assets/icons/home.svg?react'
import { useContext } from 'react'
import { AuthContext } from '../AuthProvider.tsx'
import { logout } from '../../service/login.ts'

export const Banner = () => {
  const { user, setUser } = useContext(AuthContext)

  const onLogout = async () => {
    try {
      await logout(user?.token ?? '')
      setUser(undefined)
    } catch (e) {
      console.error('Error logging out.')
    }
  }

  return (
    <AppBar
      position="static"
      style={{ background: '#262F37', borderRadius: 3 }}
    >
      <Toolbar variant="dense" sx={{ gap: 1 }}>
        <SvgIcon component={auctionHouse} style={{ fill: '#262F37' }}></SvgIcon>
        <Typography
          variant="h6"
          fontWeight={500}
          noWrap
          component="div"
          sx={{ flexGrow: 1, textAlign: 'left' }}
        >
          AUCTION HOUSE
        </Typography>
        {user && (
          <>
            <Typography>{user.username}</Typography>
            <Button onClick={onLogout}>log out</Button>
          </>
        )}
      </Toolbar>
    </AppBar>
  )
}
