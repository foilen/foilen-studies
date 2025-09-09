import axios from "axios";
import {toast} from "react-toastify";

export class Service {
    constructor() {
        this.axiosInstance = axios.create({
            baseURL: '',
        })
    }

    googleAnalyticsGet() {
        return this.axiosInstance.get('/appDetails/googleAnalytics')
    }

    userGet() {
        return this.axiosInstance.get('/user')
    }

    userIsLoggedIn() {
        return this.axiosInstance.get('/user/isLoggedIn')
    }

    multiplicationRandom(form) {
        return this.axiosInstance.post('/multiplication/random', form)
    }

    multiplicationScores() {
        return this.axiosInstance.get('/multiplication/scores')
    }

    multiplicationTrack(form) {
        return this.axiosInstance.post('/multiplication/track', form)
    }

    verbList() {
        return this.axiosInstance.get('/verb/')
    }

    verbGet(verbId) {
        return this.axiosInstance.get(`/verb/${verbId}`)
    }

    verbSave(form) {
        return this.axiosInstance.post(`/verb/`, form)
    }

    verbDelete(verbId) {
        return this.axiosInstance.delete(`/verb/${verbId}`)
    }

    wordListList() {
        return this.axiosInstance.get('/wordList/')
    }

    wordListSave(form) {
        return this.axiosInstance.post(`/wordList/`, form)
    }

    wordListGet(id) {
        return this.axiosInstance.get(`/wordList/${id}`)
    }

    wordListDelete(wordListId) {
        return this.axiosInstance.delete(`/wordList/${wordListId}`)
    }

    wordListBulkSplit(form) {
        return this.axiosInstance.post('/wordList/bulkSplit', form)
    }

    wordListCopy(form) {
        return this.axiosInstance.post('/wordList/copy', form)
    }

    wordListRandom(form) {
        return this.axiosInstance.post('/wordList/random', form)
    }

    wordListTrack(form) {
        return this.axiosInstance.post('/wordList/track', form)
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

export function failuresToToast(context, promiseProvider, showSuccess = true, successOnAction) {

    return new Promise((resolve) => {

        let execute = () => {
            promiseProvider()
                .then((response) => {

                    if (response.data.success === undefined || response.data.success) {
                        if (showSuccess) {
                            toast.success(`[${context}] Done`)
                        }
                        if (successOnAction) {
                            successOnAction(response.data)
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
