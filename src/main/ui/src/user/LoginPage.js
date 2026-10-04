import React, {useState} from "react";
import {actionWhenEnterKey, sendFormAndHandleResponse, setFormValue} from "../common/Forms";
import ErrorResults from "../common/ErrorResults";

function LoginPage({onLoggedIn}) {
    const [activeTab, setActiveTab] = useState('password')
    const [codeStep, setCodeStep] = useState('request')
    const [form, setForm] = useState({})
    const [formResult, setFormResult] = useState(null)

    const selectTab = (tab) => {
        setActiveTab(tab)
        setCodeStep('request')
        setFormResult(null)
    }

    const login = () => sendFormAndHandleResponse('Connexion',
        () => window.service.userLogin({email: form.email, password: form.password}),
        setFormResult, onLoggedIn)

    const requestCode = () => sendFormAndHandleResponse('Envoi du code',
        () => window.service.userLoginWithCodeRequest({email: form.email}),
        setFormResult, () => setCodeStep('verify'))

    const verifyCode = () => sendFormAndHandleResponse('Connexion',
        () => window.service.userLoginWithCode({email: form.email, code: form.code}),
        setFormResult, onLoggedIn)

    return (
        <div className="row justify-content-center">
            <div className="col-12 col-md-6 col-lg-4">
                <h2>Connexion</h2>

                <ul className="nav nav-tabs mb-3">
                    <li className="nav-item">
                        <button type="button" className={`nav-link ${activeTab === 'password' ? 'active' : ''}`}
                                onClick={() => selectTab('password')}>Mot de passe
                        </button>
                    </li>
                    <li className="nav-item">
                        <button type="button" className={`nav-link ${activeTab === 'code' ? 'active' : ''}`}
                                onClick={() => selectTab('code')}>Code par courriel
                        </button>
                    </li>
                </ul>

                <ErrorResults formResult={formResult}/>

                <div className="mb-3">
                    <label htmlFor="loginEmail">Courriel</label>
                    <input type="text" className="form-control" id="loginEmail" autoComplete="username"
                           value={form.email || ''} disabled={activeTab === 'code' && codeStep === 'verify'}
                           onChange={(e) => setFormValue(form, setForm, 'email', e.target.value)}
                           onKeyDown={(e) => actionWhenEnterKey(e, activeTab === 'password' ? login : requestCode)}/>
                </div>

                {activeTab === 'password' && (<>
                    <div className="mb-3">
                        <label htmlFor="loginPassword">Mot de passe</label>
                        <input type="password" className="form-control" id="loginPassword"
                               autoComplete="current-password"
                               value={form.password || ''}
                               onChange={(e) => setFormValue(form, setForm, 'password', e.target.value)}
                               onKeyDown={(e) => actionWhenEnterKey(e, login)}/>
                    </div>
                    <button type="button" className="btn btn-primary" onClick={login}>Se connecter</button>
                </>)}

                {activeTab === 'code' && codeStep === 'request' && (
                    <button type="button" className="btn btn-primary" onClick={requestCode}>Envoyer le code</button>
                )}

                {activeTab === 'code' && codeStep === 'verify' && (<>
                    <p>Un code a été envoyé à votre courriel.</p>
                    <div className="mb-3">
                        <label htmlFor="loginCode">Code</label>
                        <input type="text" className="form-control" id="loginCode" autoComplete="one-time-code"
                               value={form.code || ''}
                               onChange={(e) => setFormValue(form, setForm, 'code', e.target.value)}
                               onKeyDown={(e) => actionWhenEnterKey(e, verifyCode)}/>
                    </div>
                    <button type="button" className="btn btn-primary" onClick={verifyCode}>Valider le code</button>
                </>)}
            </div>
        </div>
    )
}

export default LoginPage
