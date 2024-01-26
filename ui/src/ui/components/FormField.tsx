import React, { PropsWithChildren, useState } from 'react'
import { IconButton, InputAdornment, Select, TextField } from '@mui/material'
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff'
import VisibilityIcon from '@mui/icons-material/Visibility'

const FieldSxProps = {
  backgroundColor: '#262F37',
  borderRadius: 2,
  input: { color: 'white' },
  color: 'white',
  textAlign: 'left',
  '& #password-input-helper-text': {
    color: 'red',
  },
}

interface FormFieldProps {
  id: string
  type: React.HTMLInputTypeAttribute
  onChange: (
    e: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>
  ) => void
  error?: boolean
  helperText?: string
}

export const FormField = ({
  id,
  type,
  onChange,
  error,
  helperText,
}: FormFieldProps) => {
  const [showPassword, setShowPassword] = useState(false)
  const isPassword = type === 'password'

  return (
    <TextField
      size="small"
      required
      id={id}
      type={isPassword && showPassword ? 'text' : type}
      sx={FieldSxProps}
      onChange={onChange}
      error={error}
      helperText={helperText}
      InputProps={{
        endAdornment: isPassword && (
          <InputAdornment position="end">
            <IconButton
              aria-label="toggle password visibility"
              onClick={() => setShowPassword(!showPassword)}
              edge="end"
            >
              {showPassword ? <VisibilityOffIcon /> : <VisibilityIcon />}
            </IconButton>
          </InputAdornment>
        ),
      }}
    />
  )
}

interface SelectFieldProps
  extends PropsWithChildren<Pick<FormFieldProps, 'id'>> {
  onChange: (event: any, child: React.ReactNode) => void
  value: number
}

export const SelectField = ({
  id,
  value,
  onChange,
  children,
}: SelectFieldProps) => {
  return (
    <Select
      size="small"
      id={id}
      required
      color="info"
      sx={FieldSxProps}
      value={value}
      onChange={onChange}
    >
      {children}
    </Select>
  )
}
