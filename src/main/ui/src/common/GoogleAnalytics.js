import {useEffect} from "react";
import {autoRetry} from "../service";

function GoogleAnalytics() {

    useEffect(() => {
        autoRetry('Get Google Analytics id', () => window.service.googleAnalyticsGet(), 5000).then(response => {
            let googleAnalyticsId = response.data.item
            if (googleAnalyticsId) {
                console.log('Configure Google Analytics with id: ' + googleAnalyticsId)
                window.dataLayer = window.dataLayer || []

                function gtag() {
                    window.dataLayer.push(arguments)
                }

                gtag('js', new Date())
                gtag('config', googleAnalyticsId)
            }
        })
    }, [])

    return <></>
}

export default GoogleAnalytics
