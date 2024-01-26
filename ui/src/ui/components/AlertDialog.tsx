import Button from '@mui/material/Button'
import Dialog from '@mui/material/Dialog'
import DialogActions from '@mui/material/DialogActions'
import DialogContent from '@mui/material/DialogContent'
import DialogContentText from '@mui/material/DialogContentText'
import DialogTitle from '@mui/material/DialogTitle'

export interface DialogState {
  title?: string
  content?: string
  open: boolean
  handleClose?: (agree: boolean) => void
  twoActions?: boolean
}

export const AlertDialog = ({
  title = '',
  content = '',
  open,
  handleClose,
  twoActions = true,
}: DialogState) => {
  return (
    <Dialog open={open} onClose={handleClose}>
      <DialogTitle>{title}</DialogTitle>
      <DialogContent>
        <DialogContentText>{content}</DialogContentText>
      </DialogContent>
      <DialogActions>
        {twoActions && (
          <Button onClick={() => handleClose?.(false)}>Dismiss</Button>
        )}
        <Button onClick={() => handleClose?.(true)}>Confirm</Button>
      </DialogActions>
    </Dialog>
  )
}
