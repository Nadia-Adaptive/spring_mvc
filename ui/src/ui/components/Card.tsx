import { PropsWithChildren } from 'react'
import {
  Button,
  Divider,
  Grid,
  List,
  SxProps,
  Theme,
  Typography,
} from '@mui/material'

export const Item = ({
  title,
  onClick,
  selected,
  style,
}: {
  title: string
  onClick?: () => void
  selected?: boolean
  style?: SxProps<Theme>
}) => {
  return (
    <Button
      sx={{
        borderRadius: 2,
        backgroundColor: selected ? '#13CC02' : '#262F37',
        '&:hover': {
          backgroundColor: selected
            ? 'rgb(19, 204, 2, 0.7)'
            : 'rgb(38, 47, 55, 0.7)',
        },
        padding: 0,
        ...style,
      }}
      color="inherit"
      onClick={onClick}
    >
      <Typography padding={1}>{title}</Typography>
    </Button>
  )
}

export const Card = ({
  title,
  footer,
  children,
}: PropsWithChildren<{
  title: string
  footer?: React.ReactNode
}>) => {
  return (
    <Grid
      item
      xs={3.9}
      style={{
        display: 'flex',
        flexDirection: 'column',
        borderRadius: 8,
        padding: 8,
        gap: 8,
        backgroundColor: '#131D26',
      }}
    >
      <Typography variant="h5" textAlign="left" fontWeight={600} padding={2}>
        {title}
      </Typography>

      <Divider color="#262F37" sx={{ marginX: 2 }} />

      <List
        style={{
          padding: 4,
          flexGrow: 1,
          height: '0px',
          display: 'flex',
          flexDirection: 'column',
          overflow: 'auto',
          gap: 8,
        }}
      >
        {children}
      </List>

      {footer}
    </Grid>
  )
}
