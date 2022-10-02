import axios from "axios";
import {toast} from "react-toastify";

export class Service {
    constructor() {
        this.axiosInstance = axios.create({
            baseURL: 'http://localhost:8080',
        })
    }

    getUser() {
        return this.axiosInstance.get('/user')
    }

}

export function autoRetry(context, promiseProvider, delayInSec) {

    return new Promise((resolve) => {

        let execute = () => {
            promiseProvider()
                .then((response) => resolve(response))
                .catch((e) => {
                    toast.warn(`[${context}] Problem getting the response. Will retry in ${delayInSec} seconds. ${e}`)
                    setTimeout(execute, delayInSec * 1000)
                })
        }

        execute()

    })

}

export function failuresToToast(context, promiseProvider, showSuccess = true) {

    return new Promise((resolve) => {

        let execute = () => {
            promiseProvider()
                .then((response) => {

                    if (response.data.success === undefined || response.data.success) {
                        if (showSuccess) {
                            toast.success(`[${context}] Done`)
                        }
                        resolve(response)
                    } else {
                        console.log('ERROR', context, response.data)
                        let errors = []
                        if (response.data.error) {
                            const error = response.data.error
                            errors.push(`${error.timestamp} - ${error.uniqueId} - ${error.message}`)
                        }
                        if (response.data.globalErrors) {
                            for (const globalError of response.data.globalErrors) {
                                errors.push(globalError)
                            }
                        }
                        if (response.data.validationErrorsByField) {
                            for (const fieldName in response.data.validationErrorsByField) {
                                for (const error of response.data.validationErrorsByField[fieldName]) {
                                    errors.push(`(${fieldName}) ${error}`)
                                }
                            }
                        }

                        for (const error of errors) {
                            toast.error(`[${context}] ${error}`, {
                                autoClose: 20000,
                            })
                        }
                    }

                })
                .catch((e) => {
                    toast.error(`[${context}] Problem getting the response. ${e}`, {
                        autoClose: 20000,
                    })
                })
        }

        execute()

    })

}
