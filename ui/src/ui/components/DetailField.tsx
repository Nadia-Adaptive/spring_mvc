import { SxProps, Typography } from '@mui/material'

export const DetailField = ({
  field,
  text,
  style,
}: {
  field: string
  text: string
  style?: SxProps
}) => {
  return (
    <Typography
      component="div"
      textAlign="left"
      paddingY={2}
      paddingX={1}
      bgcolor="#262F37"
      display="inline-flex"
      sx={style}
    >
      <Typography fontWeight={800}>{field}</Typography>
      <Typography>: {text}</Typography>
    </Typography>
  )
}
