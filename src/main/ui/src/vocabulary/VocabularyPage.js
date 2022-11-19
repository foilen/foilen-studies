import React, {useEffect, useState} from "react";
import {autoRetry} from "../service";
import lodash from "lodash";
import {NavLink} from "react-router-dom";
import "./VocabularyPage.css"
import {toast} from "react-toastify";
import AskWordsSection from "../common/AskWordsSection";

function VocabularyPage() {

    // Initial details
    const [wordLists, setWordLists] = useState([])

    // Main game
    const [wordsToFind, setWordsToFind] = useState(null)

    // End of game
    const [finalScore, setFinalScore] = useState(null)

    useEffect(() => {
        autoRetry('Get Word Lists', () => window.service.wordListList(), 5000).then(response => {
            let items = response.data.items
            if (!items) {
                items = []
            }

            items.push({
                id: null,
                name: 'Tous les mots',
                wordIds: [],
            })
            for (const item of items) {
                item.choice = {
                    selected: false,
                    anyScoreCount: item.wordIds ? item.wordIds.length : 0,
                    noScoreCount: 0,
                    badScoreCount: 0,
                    averageScoreCount: 0,
                    goodScoreCount: 0,
                }
            }
            setWordLists(items)
        })
    }, [])

    function toggleSelect(wordListIdx) {
        let nextWordLists = [...wordLists]
        wordLists[wordListIdx].choice.selected = !wordLists[wordListIdx].choice.selected
        setWordLists(nextWordLists)
    }

    function deleteWordList(wordList) {
        if (window.confirm(`Êtes-vous sûr de vouloir effacer la liste ${wordList.name}?`)) {
            autoRetry('Delete Word List', () => window.service.wordListDelete(wordList.id), 5000).then(() => {
                let nextWordLists = [...wordLists]
                lodash.remove(nextWordLists, {id: wordList.id})
                setWordLists(nextWordLists)
            })
        }
    }

    function changeScoreCount(wordListIdx, fieldName, value) {
        let nextWordLists = [...wordLists]
        wordLists[wordListIdx].choice[fieldName] = value
        setWordLists(nextWordLists)
    }

    function startWithParameters() {
        const selectedWordLists = wordLists.filter(wl => wl.choice.selected)
        const form = {
            parameters: selectedWordLists.map(wl => ({
                wordListId: wl.id,
                anyScoreCount: wl.choice.anyScoreCount,
                noScoreCount: wl.choice.noScoreCount,
                badScoreCount: wl.choice.badScoreCount,
                averageScoreCount: wl.choice.averageScoreCount,
                goodScoreCount: wl.choice.goodScoreCount,
            }))
        }

        // Get the random list
        autoRetry('Get Words', () => window.service.wordListRandom(form), 5000).then(response => {
            if (response.data.items) {
                startGame(response.data.items);
            } else {
                toast.error("Aucun mot trouvé")
            }
        })
    }

    function startGame(words) {
        words = lodash.shuffle(words)
        setWordsToFind(words)
        setFinalScore(null)
    }

    function onAnswer(currentWord, currentAnswer, isSuccess) {

        const trackForm = {
            wordId: currentWord.id,
            answer: currentAnswer,
            success: isSuccess,
        }

        // Send tracking
        autoRetry('Send tracking', () => window.service.wordListTrack(trackForm), 5000)
    }

    function onCompletion(gameFinalScore) {
        setWordsToFind(null)
        setFinalScore(gameFinalScore)
    }

    return (
        <div className="row">
            <div className="col col-sm-6 col-md-4 col-lg-4">

                <h2>Vocabulaire</h2>

                { /* List the word lists */}
                {!wordsToFind && !finalScore &&
                    <div>
                        <div className="float-end">
                            <NavLink to="/vocabulary/create" className="btn btn-success">Créer</NavLink>
                        </div>
                        <p>Choisi une liste</p>
                        {wordLists.map((wordList, wordListIdx) =>
                            <div className="wordList" key={wordList.id}>
                                <input className="form-check-input" type="checkbox"
                                       checked={wordList.choice.selected}
                                       onChange={() => toggleSelect(wordListIdx)}
                                />
                                {wordList.name} {wordList.scores && <span>({wordList.scores.total} mots)</span>}

                                {wordList.id && <>
                                    <NavLink to={`/vocabulary/${wordList.id}`}
                                             className="btn btn-outline-primary">Éditer</NavLink>
                                    <button className="btn btn-danger float-end"
                                            onClick={() => deleteWordList(wordList)}>X
                                    </button>
                                </>
                                }

                                {wordList.choice.selected &&
                                    <>
                                        <div className="row">
                                            <label className="col-form-label col">N'importe quel mot</label>
                                            <div className="col">
                                                <input type="number" className="form-control"
                                                       value={wordList.choice.anyScoreCount}
                                                       onChange={e => changeScoreCount(wordListIdx, 'anyScoreCount', e.target.value)}
                                                />
                                            </div>
                                        </div>

                                        <div className="row">
                                            <label className="col-form-label col">Mot sans score</label>
                                            <div className="col">
                                                <input type="number" className="form-control"
                                                       value={wordList.choice.noScoreCount}
                                                       onChange={e => changeScoreCount(wordListIdx, 'noScoreCount', e.target.value)}
                                                />
                                            </div>
                                        </div>

                                        <div className="row">
                                            <label className="col-form-label col">Mot avec un mauvais score</label>
                                            <div className="col">
                                                <input type="number" className="form-control"
                                                       value={wordList.choice.badScoreCount}
                                                       onChange={e => changeScoreCount(wordListIdx, 'badScoreCount', e.target.value)}
                                                />
                                            </div>
                                        </div>


                                        <div className="row">
                                            <label className="col-form-label col">Mot avec un score moyen</label>
                                            <div className="col">
                                                <input type="number" className="form-control"
                                                       value={wordList.choice.averageScoreCount}
                                                       onChange={e => changeScoreCount(wordListIdx, 'averageScoreCount', e.target.value)}
                                                />
                                            </div>
                                        </div>


                                        <div className="row">
                                            <label className="col-form-label col">Mot avec un bon score</label>
                                            <div className="col">
                                                <input type="number" className="form-control"
                                                       value={wordList.choice.goodScoreCount}
                                                       onChange={e => changeScoreCount(wordListIdx, 'goodScoreCount', e.target.value)}
                                                />
                                            </div>
                                        </div>
                                    </>
                                }

                                {wordList.scores &&
                                    <div className="progress">
                                        <div className="progress-bar bg-danger" role="progressbar"
                                             style={{width: wordList.scores.badPercentage + '%'}}>{wordList.scores.bad}</div>
                                        <div className="progress-bar bg-warning" role="progressbar"
                                             style={{width: wordList.scores.averagePercentage + '%'}}>{wordList.scores.average}</div>
                                        <div className="progress-bar bg-success" role="progressbar"
                                             style={{width: wordList.scores.goodPercentage + '%'}}>{wordList.scores.good}</div>
                                    </div>
                                }

                            </div>
                        )}

                        <button className="btn btn-primary"
                                onClick={() => startWithParameters()}
                                disabled={wordLists.every(wl => !wl.choice.selected)}
                        >
                            Démarrer
                        </button>
                    </div>
                }

                { /* The main game */}
                {wordsToFind && <AskWordsSection
                    wordsToFind={wordsToFind}
                    onAnswer={onAnswer}
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
                                    <h3>Mots problématiques</h3>
                                    <ul> {finalScore.wrongAnswers.map(word =>
                                        <li key={word.id}>{word.word}</li>
                                    )}
                                    </ul>

                                    <button className="btn btn-danger"
                                            onClick={() => startGame(finalScore.wrongAnswers)}>
                                        Démarrer avec juste les mots problématiques
                                    </button>
                                </div>
                            </div>
                        }
                    </div>
                }

            </div>
        </div>
    )
}

export default VocabularyPage
