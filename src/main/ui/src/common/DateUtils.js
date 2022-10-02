import {DateTime} from "luxon";

export function toIsoFullDate(date) {
    if (!date) {
        return '-'
    }

    const fullDate = DateTime.fromMillis(date)
    let formatted = fullDate.year + '-'
    formatted += (fullDate.month + '-').padStart(3, '0')
    formatted += (fullDate.day + ' ').padStart(3, '0')
    formatted += (fullDate.hour + ':').padStart(3, '0')
    formatted += (fullDate.minute + ':').padStart(3, '0')
    formatted += (fullDate.second + '').padStart(2, '0')

    return formatted
}

export function toIsoDate(date) {
    if (!date) {
        return '-'
    }

    return DateTime.fromMillis(date).toISODate()
}
