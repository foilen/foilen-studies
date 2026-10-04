import React, {useState} from "react";
import {actionWhenEnterKey, sendFormAndHandleResponse, setFormValue} from "../common/Forms";
import ErrorResults from "../common/ErrorResults";

function ChangePasswordPage() {
    const [form, setForm] = useState({})
    const [formResult, setFormResult] = useState(null)

    const save = () => sendFormAndHandleResponse('Mot de passe',
        () => window.service.userChangePassword(form),
        setFormResult, () => setForm({}))

    const field = (name, label, autoComplete) => (
        <div className="mb-3">
            <label htmlFor={name}>{label}</label>
            <input type="password" className="form-control" id={name} autoComplete={autoComplete}
                   value={form[name] || ''}
                   onChange={(e) => setFormValue(form, setForm, name, e.target.value)}
                   onKeyDown={(e) => actionWhenEnterKey(e, save)}/>
        </div>
    )

    return (
        <div className="row">
            <div className="col-12 col-md-6 col-lg-4">
                <h2>Mot de passe</h2>
                <p>Le mot de passe actuel n'est demandé que si vous en avez déjà un.</p>
                <ErrorResults formResult={formResult}/>
                {field('currentPassword', 'Mot de passe actuel', 'current-password')}
                {field('newPassword', 'Nouveau mot de passe', 'new-password')}
                {field('newPasswordConfirmation', 'Confirmation du nouveau mot de passe', 'new-password')}
                <button type="button" className="btn btn-primary" onClick={save}>Enregistrer</button>
            </div>
        </div>
    )
}

export default ChangePasswordPage
