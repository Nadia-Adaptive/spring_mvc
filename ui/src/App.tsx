import './App.css'
import { Login } from './ui/screens/landing/Login.tsx'
import { Banner } from './ui/components/Banner.tsx'
import { Divider } from '@mui/material'
import { useContext } from 'react'
import { AuthContext } from './ui/AuthProvider.tsx'
import { UserManagement } from './ui/screens/users/UserManagement.tsx'

function App() {
  const { user } = useContext(AuthContext)

  if (!user) {
    return <Login />
  }

  return (
    <>
      <Banner />
      <Divider sx={{ marginTop: 2 }} color={'#ffffff'} />
      {user.admin && <UserManagement />}
    </>
  )
}

export default App
