import {NavLink, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {toggleCheckbox, updateFormValue} from "../common/Forms";
import {autoRetry, failuresToToast} from "../service";
import WordItem from "./WordItem";
import {unique} from "../common/ArrayUtils";

function VocabularyEditPage() {

    const {wordListId} = useParams()

    const [form, setForm] = useState({
        name: '',
        words: [],
    })

    useEffect(() => {
        if (wordListId) {
            failuresToToast('Get existing list', () => window.service.wordListGet(wordListId), false).then(response => {
                if (!response.data.item.words) {
                    response.data.item.words = []
                }
                setForm(response.data.item)
            })
        }
    }, [wordListId])

    const [bulkForm, setBulkForm] = useState({
        all: '',
        acceptSpacesInWords: false,
    })

    function bulkAdd() {
        autoRetry('Bulk split', () => window.service.wordListBulkSplit(bulkForm), 5000).then(response => {
            addWords(response.data.items)
            setBulkForm({...bulkForm, all: ''})
        })
    }

    function addWords(newWords) {
        const nextWords = unique([...form.words, ...newWords], item => item.id)
        setForm({...form, words: nextWords})
    }

    function updateSpeakText(id, e) {
        const nextWords = form.words.map(word => {
            if (word.id === id) {
                word.speakText.text = e.target.value
            }
            return word
        })
        setForm({...form, words: nextWords})
    }

    function removeWord(id) {
        const nextWords = form.words.filter(word => word.id !== id)
        setForm({...form, words: nextWords})
    }

    function save() {
        // Check if the bulk form still have data in it and confirm if so
        if (bulkForm.all) {
            if (!window.confirm('Vous avez des mots non-ajoutés dans le champ du haut. Êtes-vous sûr de vouloir sauvegarder la liste sans ajouter ces mots?')) {
                return
            }
        }

        failuresToToast('Save', () => window.service.wordListSave(form)).then(() => {
            window.location = '#/vocabulary'
        })
    }

    return (
        <div className="row">
            <div className="col col-sm-6 col-md-4 col-lg-4">

                <h2>Ajouter un ou plusieurs mots d'un coup</h2>
                <p className="text-bg-info">Vous pouvez coller une liste de mots séparés par des espaces, des virgules,
                    des points et des retours de lignes et ce, tout en même temps. Les mots seront ensuite séparés et
                    gérables.</p>

                <div className="form-check">
                <input className="form-check-input" type="checkbox"
                       checked={bulkForm.acceptSpacesInWords}
                       onChange={() => toggleCheckbox(bulkForm, setBulkForm, 'acceptSpacesInWords')}/>
                    <label className="form-check-label">
                        Accepter les espaces dans les mots (ex: "parce que")
                    </label>
                </div>

                <textarea className="form-control" rows="10"
                          name="all"
                          value={bulkForm.all}
                          onChange={e => updateFormValue(e, bulkForm, setBulkForm)}
                ></textarea>

                <button className="btn btn-primary"
                        onClick={() => bulkAdd()}
                >Ajouter
                </button>

                <h2>Liste</h2>

                <input type="text" className="form-control"
                       autoComplete="off"
                       name="name"
                       placeholder="Nom de la liste"
                       value={form.name}
                       onChange={(e) => updateFormValue(e, form, setForm)}
                />

                {form.words.map(word => <WordItem key={word.id} word={word}
                                                  onSpeakTextChange={e => updateSpeakText(word.id, e)}
                                                  onDelete={() => removeWord(word.id)}/>
                )}

                <hr/>

                <NavLink to="/vocabulary" className="btn btn-outline-primary">Annuler</NavLink>

                <button className="btn btn-success float-end"
                        onClick={() => save()}
                >Sauvegarder
                </button>
            </div>
        </div>
    )
}

export default VocabularyEditPage
