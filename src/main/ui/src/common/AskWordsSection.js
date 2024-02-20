import playImage from "./play.png";
import React, {useEffect, useState} from "react";

/**
 * Ask each word one after the other.
 *
 * props.wordsToFind: The words to ask one at a time
 *
 * props.onAnswer: (optional) The action to do for each answer (currentWord, currentAnswer, isSuccess)
 * props.onCompletion: The action to do when everything was answered (finalScore)
 *
 */
function AskWordsSection(props) {

    // Main game
    const [currentWordIdx, setCurrentWordIdx] = useState(0)

    const [userAnswer, setUserAnswer] = useState({
        answer: ''
    })

    // Progression
    const [transition, setTransition] = useState(false)
    const [correction, setCorrection] = useState(null)
    const [score, setScore] = useState({
        success: 0,
        fails: 0,
        total: props.wordsToFind.length,
    })
    const [wrongAnswers, setWrongAnswers] = useState([])

    // Start
    useEffect(() => {
        console.log('Start from the top')
        changeCurrentWord(0)
    }, [props.wordsToFind])

    function playMp3(speakTextCacheId) {
        new Audio(`/speakText/${speakTextCacheId}`).play()
    }

    function updateAnswer(e) {
        let value = e.target.value.toLowerCase()
        setUserAnswer(prevState => ({...prevState, answer: value}))
    }

    function changeCurrentWord(idx) {
        setCurrentWordIdx(idx)
        setUserAnswer({answer: ''})
        setTransition(false)
        setCorrection(null)
        let currentWord = props.wordsToFind[idx]
        playMp3(currentWord.speakText.cacheId)
    }

    function validateAnswer() {
        let currentWord = props.wordsToFind[currentWordIdx]
        let expected = currentWord.word
        let actual = userAnswer.answer.trim()

        setTransition(true)
        let nextScore = {...score};
        let nextWrongAnswers = [...wrongAnswers]

        let isSuccess = expected === actual;
        if (isSuccess) {
            ++nextScore.success
            setCorrection({
                isSuccess: true,
                message: 'Bravo!',
            })
        } else {
            ++nextScore.fails
            setCorrection({
                isSuccess: false,
                message: expected,
            })
            nextWrongAnswers = [...nextWrongAnswers, currentWord]
            setWrongAnswers(nextWrongAnswers)
        }
        setScore(nextScore)

        // Send event
        if (props.onAnswer) {
            props.onAnswer(currentWord, actual, isSuccess)
        }

        // Timer and go to next or show summary
        setTimeout(() => {
            let nextWordIdx = currentWordIdx + 1
            if (nextWordIdx === props.wordsToFind.length) {
                // Show summary
                const finalScore = {
                    ...nextScore,
                    wrongAnswers: nextWrongAnswers,
                }
                props.onCompletion(finalScore)
            } else {
                changeCurrentWord(nextWordIdx)
            }
        }, 3000)

    }

    return (
        <div>
            {props.wordsToFind.map((wordToFind, wIdx) =>
                <div key={wordToFind.id}>
                    {wIdx === currentWordIdx &&
                        <div>
                            <div className="row">
                                <div className="col-12 col-sm-5 col-md-3">
                                    <img src={playImage} alt="Dire le mot"
                                         width="150px" height="150px"
                                         className="mx-auto d-block"
                                         onClick={() => playMp3(wordToFind.speakText.cacheId)}/>
                                </div>
                            </div>
                            <hr/>
                            <div className="row">

                                {wordToFind.prefix && <div className="col-1">
                                    {wordToFind.prefix}
                                </div>}

                                <div className="col-10">
                                    <input type="text" className="form-control"
                                           autoComplete="off"
                                           name="answer"
                                           value={userAnswer.answer}
                                           onChange={(e) => updateAnswer(e, userAnswer, setUserAnswer)}
                                    />
                                </div>

                                <div className="col-1">
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
                    <div className="progress">
                        <div className="progress-bar bg-danger" role="progressbar"
                             style={{width: score.fails * 100 / score.total + '%'}}>{score.fails}</div>
                        <div className="progress-bar bg-success" role="progressbar"
                             style={{width: score.success * 100 / score.total + '%'}}>{score.success}</div>
                    </div>
                </div>
            </div>

        </div>
    )
}

export default AskWordsSection
