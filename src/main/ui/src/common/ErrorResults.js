import React from "react";

function ErrorResults({formResult}) {
    if (!formResult) {
        return null
    }

    const errors = []
    if (formResult.error) {
        errors.push(formResult.error.message)
    }
    for (const globalError of formResult.globalErrors || []) {
        errors.push(globalError)
    }
    for (const fieldName in formResult.validationErrorsByField || {}) {
        for (const error of formResult.validationErrorsByField[fieldName]) {
            errors.push(`${fieldName} : ${error}`)
        }
    }
    if (errors.length === 0) {
        return null
    }

    return (
        <div className="alert alert-danger">
            {errors.map((error, i) => <div key={i}>{error}</div>)}
        </div>
    )
}

export default ErrorResults
