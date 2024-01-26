import { PropsWithChildren, ReactNode } from 'react'
import { Grid, Typography } from '@mui/material'

export const Wrapper = ({
  title,
  banner,
  children,
}: PropsWithChildren<{ title: string; banner?: ReactNode }>) => {
  return (
    <>
      <Typography variant="h6" textAlign="left" padding={2} fontWeight={800}>
        {title}
      </Typography>

      {banner}

      <Grid container flex={1} gap={2}>
        {children}
      </Grid>
    </>
  )
}
