export function unique(items, idFunction) {
    const ids = new Set()
    const uniqueItems = []

    items.forEach(item => {
        const id = idFunction(item)
        if (!ids.has(id)) {
            ids.add(id)
            uniqueItems.push(item)
        }
    })

    return uniqueItems
}
