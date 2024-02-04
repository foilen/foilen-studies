import React, {useEffect, useState} from "react";
import {autoRetry} from "../service";
import lodash from "lodash";
import {NavLink} from "react-router-dom";
import AskWordsSection from "../common/AskWordsSection";

function VerbPage() {

    // Initial details
    const [verbs, setVerbs] = useState([])

    // Main game
    const [verbToFind, setVerbToFind] = useState(null)

    // End of game
    const [finalScore, setFinalScore] = useState(null)

    useEffect(() => {
        autoRetry('Get Verbs', () => window.service.verbList(), 5).then(response => {
            let items = response.data.items
            if (!items) {
                items = []
            }

            setVerbs(items)
        })
    }, [])

    function deleteVerb(verb) {
        if (window.confirm(`Êtes-vous sûr de vouloir effacer le verbe ${verb.name}?`)) {
            autoRetry('Delete Verb', () => window.service.verbDelete(verb.id), 5).then(() => {
                let nextVerbs = [...verbs]
                lodash.remove(nextVerbs, {id: verb.id})
                setVerbs(nextVerbs)
            })
        }
    }

    function startGame(verb) {
        let transformedVerb = {...verb}
        transformedVerb.verbLines.forEach(verbLine => verbLine.prefix = verbLine.pronoun)
        setVerbToFind(transformedVerb)
        setFinalScore(null)
    }

    function onCompletion(gameFinalScore) {
        setVerbToFind(null)
        setFinalScore(gameFinalScore)
    }

    return (
        <div className="row">
            <div className="col col-sm-6 col-md-4 col-lg-4">

                <h2>Verbes</h2>

                { /* List the verbs lists */}
                {!verbToFind && !finalScore &&
                    <div>
                        <div className="float-end">
                            <NavLink to="/verb/create" className="btn btn-success">Créer</NavLink>
                        </div>
                        <p>Choisi une liste</p>
                        {verbs.map((verb) =>
                            <div className="wordList" key={verb.id}>
                                {verb.name}

                                {verb.id && <>
                                    <NavLink to={`/verb/${verb.id}`}
                                             className="btn btn-outline-primary">Éditer</NavLink>

                                    <button className="btn btn-primary"
                                            onClick={() => startGame(verb)}
                                    >
                                        Démarrer
                                    </button>

                                    <button className="btn btn-danger float-end"
                                            onClick={() => deleteVerb(verb)}>X
                                    </button>
                                </>
                                }

                            </div>
                        )}

                    </div>
                }

                { /* The main game */}
                {verbToFind && <AskWordsSection
                    wordsToFind={verbToFind.verbLines}
                    onCompletion={onCompletion}
                />}

                { /* The summary view */}
                {finalScore &&
                    <div>
                        <div className="row">
                            <div className="col-12">
                                <h3>Résultats</h3>
                                <p className="text-bg-success">Réussis: {finalScore.success}</p>
                                <p className="text-bg-danger">Ratés: {finalScore.fails}</p>
                                <p>Total: {finalScore.total}</p>
                                <p>Score: {(finalScore.success) / finalScore.total * 100} %</p>
                            </div>
                        </div>

                        {finalScore.wrongAnswers.length > 0 &&
                            <div className="row">
                                <div className="col-12">
                                    <h3>Problématiques</h3>
                                    <ul> {finalScore.wrongAnswers.map(verbLine =>
                                        <li key={verbLine.id}>{verbLine.pronoun} {verbLine.word}</li>
                                    )}
                                    </ul>
                                </div>
                            </div>
                        }

                        <button className="btn btn-primary"
                                onClick={() => setFinalScore(null)}>
                            Retour aux verbes
                        </button>

                    </div>
                }

            </div>
        </div>
    )
}

export default VerbPage
