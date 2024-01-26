import { Grid } from '@mui/material'
import Button from '@mui/material/Button'
import { orangeBgColorStyle } from '../utils.ts'

const GRID_SPACING = 12

interface SegmentedControlItem {
  onClick: (value: any) => void
  label: string
}

const segmentControlBtnStyle = (selected: boolean) => ({
  color: 'white',
  flexGrow: 1,
  backgroundColor: selected ? orangeBgColorStyle.backgroundColor : '#262F37',
  '&:hover': {
    backgroundColor: selected
      ? orangeBgColorStyle['&:hover'].backgroundColor
      : 'rgba(38, 47, 55, 0.5)',
  },
  paddingY: 1,
})

export const SegmentedControl = ({
  selectedIndex,
  items,
  containerGap = 2,
}: {
  selectedIndex: number
  items: SegmentedControlItem[]
  containerGap?: number
}) => {
  return (
    <Grid container gap={containerGap} marginBottom={2}>
      {items.map(({ onClick, label }, index) => (
        <Grid item xs={GRID_SPACING / items.length - 0.1} display="flex">
          <Button
            sx={segmentControlBtnStyle(selectedIndex === index)}
            onClick={() => onClick(index)}
          >
            {label}
          </Button>
        </Grid>
      ))}
    </Grid>
  )
}
