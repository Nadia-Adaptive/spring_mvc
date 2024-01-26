enum CCY {
    dollars = '$',
}

const formatPrice = (price?: number) => {
    return `${price?.toString()} ${CCY.dollars}`
}

const formatQty = (qty?: number) => {
    return `${qty?.toString()} lots`
}

const orangeBgColorStyle = {
    backgroundColor: 'rgba(250, 141, 0, 0.5)',
    '&:hover': {
        backgroundColor: 'rgba(250, 141, 0, 0.2)',
    },
}

export {formatPrice, formatQty, orangeBgColorStyle}
