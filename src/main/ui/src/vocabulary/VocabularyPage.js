import React, {useEffect, useState} from "react";
import {autoRetry} from "../service";
import lodash from "lodash";
import playImage from "./play.png";
import {updateFormValue} from "../common/Forms";

function VocabularyPage() {

    // Initial details
    const [wordLists, setWordLists] = useState([])

    // Main game
    const [selectedWordList, setSelectedWordList] = useState(null)
    const [wordsToFind, setWordsToFind] = useState(null)
    const [currentWordIdx, setCurrentWordIdx] = useState(null)

    const [userAnswer, setUserAnswer] = useState({
        answer: ''
    })

    // Progression
    const [score, setScore] = useState(null)

    const [transition, setTransition] = useState(false)
    const [correction, setCorrection] = useState(null)
    const [wrongAnswers, setWrongAnswers] = useState(null)

    // End of game
    const [finalScore, setFinalScore] = useState(null)

    useEffect(() => {
        autoRetry('Get Word Lists', () => window.service.wordListList(), 5000).then(response => {
            setWordLists(response.data.items)
        })
    }, [])

    function chooseList(wordList) {
        setSelectedWordList(wordList)

        autoRetry('Get Words', () => window.service.wordList(wordList.id), 5000).then(response => {
            startGame(response.data.items);
        })
    }

    function startGame(words) {
        words = lodash.shuffle(words)
        setWordsToFind(words)
        changeCurrentWord(0, words)
        setScore({
            success: 0,
            fails: 0,
            total: words.length,
        })
        setWrongAnswers([])
        setFinalScore(null)
    }

    function changeCurrentWord(idx, items) {
        if (items === undefined) {
            items = wordsToFind;
        }
        setCurrentWordIdx(idx)
        setUserAnswer({answer: ''})
        setTransition(false)
        setCorrection(null)
        let currentWord = items[idx]
        playMp3(currentWord.speakText.cacheId)
    }

    function playMp3(speakTextCacheId) {
        new Audio(`/speakText/${speakTextCacheId}`).play()
    }

    function validateAnswer() {
        let currentWord = wordsToFind[currentWordIdx]
        let expected = currentWord.word
        let actual = userAnswer.answer

        setTransition(true)
        let nextScore = {...score};
        let nextWrongAnswers = [...wrongAnswers]
        if (expected === actual) {
            // TODO Happy sound
            ++nextScore.success
            setCorrection({
                isSuccess: true,
                message: 'Bravo!',
            })
        } else {
            // TODO Sad sound
            ++nextScore.fails
            setCorrection({
                isSuccess: false,
                message: expected,
            })
            nextWrongAnswers = [...nextWrongAnswers, currentWord]
            setWrongAnswers(nextWrongAnswers)
        }
        setScore(nextScore)

        // Timer and go to next or show summary
        setTimeout(() => {
            let nextWordIdx = currentWordIdx + 1
            if (nextWordIdx === wordsToFind.length) {
                // Show summary
                setWordsToFind(null)
                setFinalScore({
                    ...nextScore,
                    wrongAnswers: nextWrongAnswers,
                })
            } else {
                changeCurrentWord(nextWordIdx)
            }
        }, 3000)

    }

    return (
        <div className="row">
            <div className="col">

                <h2>Vocabulaire</h2>

                { /* List the word lists */}
                {!selectedWordList &&
                    <div>
                        <p>Choisi une liste</p>
                        <ul> {wordLists.map(wordList =>
                            <li key={wordList.id}>
                                {wordList.name} ({wordList.wordIds.length} mots)
                                <button className="btn btn-success"
                                        onClick={() => chooseList(wordList)}>Démarrer</button>
                            </li>
                        )}
                        </ul>
                    </div>
                }

                { /* The main game */}
                {wordsToFind &&
                    <div>
                        {wordsToFind.map((wordToFind, wIdx) =>
                            <div key={wordToFind.id}>
                                {wIdx === currentWordIdx &&
                                    <div>
                                        <div className="row">
                                            <div className="col-12">
                                                <img src={playImage} alt="Dire le mot"
                                                     onClick={() => playMp3(wordToFind.speakText.cacheId)}/>
                                            </div>
                                        </div>
                                        <hr/>
                                        <div className="row">
                                            <div className="col-11 col-sm-4 col-md-2">
                                                <input type="text" className="form-control"
                                                       name="answer"
                                                       value={userAnswer.answer}
                                                       onChange={(e) => updateFormValue(e, userAnswer, setUserAnswer)}
                                                />
                                            </div>
                                            <div className="col-1 col-sm-1 col-md-1">
                                                <button className="btn btn-primary"
                                                        disabled={transition}
                                                        onClick={() => validateAnswer()}>OK
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                }
                            </div>
                        )}

                        { /* Show if the current answer is good */}
                        {correction && <>
                            <hr/>

                            <div className="row">
                                <div className="col-12">
                                    <p className={`${correction.isSuccess ? 'text-bg-success' : 'text-bg-danger'}`}>{correction.message}</p>
                                </div>
                            </div>
                        </>
                        }

                        <hr/>

                        { /* Show the progression */}
                        <div className="row">
                            <div className="col-12">
                                Progression:
                                <span className="text-bg-success">{score.success}</span> ;
                                <span className="text-bg-danger">{score.fails}</span> ;
                                {score.success + score.fails} / {score.total}
                            </div>
                        </div>

                    </div>
                }

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

                                    <button className="btn btn-success"
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
    );
}

export default VocabularyPage
