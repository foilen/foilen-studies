import {useEffect} from "react"

export function useInterval(theFunction, intervalInMs, depts = []) {

    useEffect(() => {
            const interval = setInterval(theFunction, intervalInMs)
            return () => clearInterval(interval)
        },  // eslint-disable-next-line
        depts)

}
