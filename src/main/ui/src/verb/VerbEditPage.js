import {NavLink, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {updateFormValue} from "../common/Forms";
import {failuresToToast} from "../service";
import WordItem from "../vocabulary/WordItem";

function VerbEditPage() {

    const {verbId} = useParams()

    const [form, setForm] = useState({
        name: '',
        verbLines: [],
    })

    useEffect(() => {
        if (verbId) {
            failuresToToast('Get existing verb', () => window.service.verbGet(verbId), false).then(response => {
                if (!response.data.item) {
                    response.data.item = []
                }
                setForm(response.data.item)
            })
        } else {
            setForm({
                name: '',
                verbLines: [
                    {
                        pronoun: 'je',
                        word: ''
                    }, {
                        pronoun: 'tu',
                        word: ''
                    }, {
                        pronoun: 'il',
                        word: ''
                    }, {
                        pronoun: 'nous',
                        word: ''
                    }, {
                        pronoun: 'vous',
                        word: ''
                    }, {
                        pronoun: 'ils',
                        word: ''
                    },
                ],
            })
        }
    }, [verbId])

    function save() {
        failuresToToast('Save', () => window.service.verbSave(form)).then(() => {
            window.location = '#/verb'
        })
    }

    function updatePronoun(idx, e) {
        const nextVerbLines = form.verbLines.map((verbLine, vlIdx) => {
            if (vlIdx === idx) {
                verbLine.pronoun = e.target.value
            }
            return verbLine
        })
        setForm({...form, verbLines: nextVerbLines})
    }

    function updateWord(idx, e) {
        const nextVerbLines = form.verbLines.map((verbLine, vlIdx) => {
            if (vlIdx === idx) {
                verbLine.word = e.target.value
            }
            return verbLine
        })
        setForm({...form, verbLines: nextVerbLines})
    }

    return (
        <div className="row">
            <div className="col col-sm-6 col-md-4 col-lg-4">

                <h2>Verbe</h2>

                <input type="text" className="form-control"
                       autoComplete="off"
                       name="name"
                       placeholder="Nom (ex: Avoir - Présent)"
                       value={form.name}
                       onChange={(e) => updateFormValue(e, form, setForm)}
                />

                {form.verbLines.map((verbLine, vlIdx) =>
                    <WordItem key={vlIdx}
                              prefix={verbLine.pronoun} word={verbLine}
                              onPrefixChange={e => updatePronoun(vlIdx, e)}
                              onWordChange={e => updateWord(vlIdx, e)}
                    />
                )}

                <hr/>

                <NavLink to="/verb" className="btn btn-outline-primary">Annuler</NavLink>

                <button className="btn btn-success float-end"
                        onClick={() => save()}
                >Sauvegarder
                </button>
            </div>
        </div>
    )
}

export default VerbEditPage
