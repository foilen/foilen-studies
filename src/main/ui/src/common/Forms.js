import {toast} from "react-toastify";

export function actionWhenEnterKey(event, actionCallback) {
    if (event.key === 'Enter') {
        actionCallback()
    }
}

export function sendFormAndHandleResponse(context, promiseProvider, setFormResult, successOnAction) {
    setFormResult(null)

    promiseProvider()
        .then(response => {
            const formResult = response.data
            if (formResult.success) {
                toast.success(`[${context}] done`)
                if (formResult.globalWarnings) {
                    for (let warning of formResult.globalWarnings) {
                        toast.warning(`[${context}] ${warning}`)
                    }
                }
                successOnAction(formResult)
            } else {
                toast.error(`[${context}] Problem`)
                setFormResult(formResult)
            }
        })
        .catch(error => {
            const formResult = {
                error: {
                    timestamp: new Date().toISOString(),
                    message: error ? error.toString() : 'No error message',
                }
            }
            toast.error(`[${context}] Problem`)
            setFormResult(formResult)
        })
}

export function setFormValue(form, setForm, fieldName, newValue, callback) {
    let newForm = {...form}
    newForm[fieldName] = newValue
    setForm(newForm)

    if (callback) {
        callback()
    }
}

export function updateFormValue(event, form, setForm) {
    const target = event.target
    const value = target.type === 'checkbox' ? target.checked : target.value
    const name = target.name

    let newForm = {...form}
    newForm[name] = value
    setForm(newForm)

    return newForm
}
