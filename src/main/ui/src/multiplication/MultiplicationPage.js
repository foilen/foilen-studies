import React, {useEffect, useRef, useState} from "react";
import {actionWhenEnterKey, updateFormValue} from "../common/Forms";
import {autoRetry, failuresToToast} from "../service";

function MultiplicationPage() {

    const oneToTwelve = Array.from({length: 12}, (_, i) => i + 1)

    const [randomForm, setRandomForm] = useState({
        leftMax: 12,
        rightMax: 12,
        leftAlwaysSmaller: false,
        amount: 12 * 12,
    })

    const [scores, setScores] = useState(null)

    const [questions, setQuestions] = useState(null)
    const [nextQuestion, setNextQuestion] = useState(null)
    const [answer, setAnswer] = useState("")
    const answerInput = useRef();

    const [statusSuccess, setStatusSuccess] = useState(null)
    const [statusMessage, setStatusMessage] = useState("")

    const [score, setScore] = useState({
        success: 0,
        fails: 0,
        total: 0,
    })

    useEffect(() => {
        refreshScores()
    }, [])

    function refreshScores() {
        autoRetry('Get Scores', () => window.service.multiplicationScores(), 5).then(response => {
            if (response.data.success) {
                setScores(response.data.item.scores)
            }
        })
    }

    function updateFormValueAndCalculateAmount(e) {
        let newForm = updateFormValue(e, randomForm, setRandomForm)
        const leftMaxInt = parseInt(newForm.leftMax)
        const rightMaxInt = parseInt(newForm.rightMax)
        let smallest = leftMaxInt < rightMaxInt ? leftMaxInt : rightMaxInt
        let biggest = leftMaxInt > rightMaxInt ? leftMaxInt : rightMaxInt
        if (newForm.leftAlwaysSmaller) {
            let all = newForm.leftMax * newForm.rightMax
            for (let i = 2; i <= smallest; i++) {
                all -= (i - 1)
            }
            newForm.amount = all
        } else {
            newForm.amount = smallest * biggest + (biggest - smallest) * smallest
        }
        setRandomForm(newForm)
    }

    function startGame() {
        setScore({
            success: 0,
            fails: 0,
            total: randomForm.amount,
        })
        failuresToToast('Get Questions', () => window.service.multiplicationRandom(randomForm), false).then(response => {
            if (response.data.success) {
                setQuestions(response.data.questions)
                goToNextQuestion(0)
                setScores(null)
            }
        })
    }

    function goToNextQuestion(idx) {
        // Check if completed
        if (idx >= randomForm.amount) {
            setQuestions(null)
            setNextQuestion(null)
            setAnswer("")
            setStatusSuccess(null)
            setStatusMessage("")
            refreshScores()
            return
        }

        setNextQuestion(idx)
        setAnswer("")
        setStatusSuccess(null)
        setStatusMessage("")

        setTimeout(() => {
            answerInput.current.focus()
        }, 100)

    }

    function onAnswer() {

        // Check if answer is correct
        let question = questions[nextQuestion]
        let correctAnswer = question[0] * question[1];
        let correct = correctAnswer === parseInt(answer)

        // Send tracking
        autoRetry('Send tracking', () => window.service.multiplicationTrack({
            left: question[0],
            right: question[1],
            success: correct,
        }), 5)

        // Show good or bad during 3 seconds
        setStatusSuccess(correct)
        setStatusMessage(correct ? "Bravo!" : `La réponse était ${correctAnswer}`)
        setScore(prevState => ({
            ...prevState,
            success: correct ? prevState.success + 1 : prevState.success,
            fails: correct ? prevState.fails : prevState.fails + 1,
        }))
        setTimeout(() => {
            goToNextQuestion(nextQuestion + 1)
        }, correct ? 1000 : 3000)

    }

    function scoreColor(score) {
        // When null, gray
        if (score === null) {
            return "#f8f9fa"
        }
        // When 0/1, red
        if (score < 2) {
            return "#f8d7da"
        }
        // When 2/3, yellow
        if (score < 4) {
            return "#fff3cd"
        }
        // When 4/5, green
        return "#d4edda"
    }

    return (
        <div className="row">
            <div className="col col-sm-6 col-md-4 col-lg-4">

                <h2>Multiplication</h2>

                {!questions && <>
                    <p>Choisi les paramètres du jeu</p>
                    <div className="row">
                        <div className="col-4">
                            <input type="number" className="form-control"
                                   autoComplete="off"
                                   name="leftMax"
                                   value={randomForm.leftMax}
                                   min={1} max={12}
                                   onChange={e => updateFormValueAndCalculateAmount(e)}
                            />
                        </div>
                        <div className="col-1">
                            X
                        </div>
                        <div className="col-4">
                            <input type="number" className="form-control"
                                   autoComplete="off"
                                   name="rightMax"
                                   value={randomForm.rightMax}
                                   min={1} max={12}
                                   onChange={e => updateFormValueAndCalculateAmount(e)}
                            />
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-1">
                            <input type="checkbox" className="form-check-input"
                                   name="leftAlwaysSmaller"
                                   checked={randomForm.leftAlwaysSmaller}
                                   onChange={e => updateFormValueAndCalculateAmount(e)}
                            />
                        </div>
                        <div className="col-11">
                            <label htmlFor="leftAlwaysSmaller">Gauche toujours plus petit</label>
                        </div>
                    </div>

                    <div className="row">
                        <div className="col-4">
                            <input type="number" className="form-control"
                                   autoComplete="off"
                                   name="amount"
                                   value={randomForm.amount}
                                   min={1} max={144}
                                   onChange={e => updateFormValue(e, randomForm, setRandomForm)}
                            />
                        </div>
                        <div className="col-8">
                            <label htmlFor="amount">Nombre de questions</label>
                        </div>
                    </div>

                    <button className="btn btn-primary float-end"
                            onClick={() => startGame()}
                    >Commencer
                    </button>

                </>}

                {questions && <>
                    <p>Question</p>
                    <div className="row">
                        <div className="col-3">
                            <p>{questions[nextQuestion][0]} x {questions[nextQuestion][1]} =</p>
                        </div>
                        <div className="col-9">
                            <input type="number" className="form-control"
                                   autoComplete="off"
                                   ref={answerInput}
                                   value={answer}
                                   disabled={statusSuccess !== null}
                                   onChange={e => setAnswer(e.target.value)}
                                   onKeyUp={e => actionWhenEnterKey(e, () => onAnswer())}
                            />
                        </div>
                    </div>
                    <div className="row">
                        <div className="col-12">
                            {statusSuccess !== null &&
                                <p className={`${statusSuccess ? 'text-success' : 'text-danger'}`}>{statusMessage}</p>}

                            <button className="btn btn-primary float-end"
                                    disabled={statusSuccess !== null}
                                    onClick={e => onAnswer()}
                            >Suivant
                            </button>
                        </div>
                    </div>
                    { /* Show the progression */}
                    <div className="row">
                        <div className="col-12">
                            Progression:
                            <span className="text-bg-success">{score.success}</span> ;
                            <span className="text-bg-danger">{score.fails}</span> ;
                            {score.success + score.fails} / {score.total}
                        </div>
                    </div>
                </>}

                <div className="clearfix"></div>

                {scores && <div className="row">
                    <div className="col-12">
                        <h3>Scores</h3>
                        <table className="table">
                            <tbody>
                            <tr>
                                <th></th>
                                {oneToTwelve.map(i => <th key={i}>{i}</th>)}
                            </tr>
                            {oneToTwelve.map(x => <tr key={x}>
                                <th>{x}</th>
                                {oneToTwelve.map(y => <td key={y}
                                                          style={{backgroundColor: scoreColor(scores[x - 1][y - 1])}}>
                                    {(x) * (y)}
                                </td>)}
                            </tr>)}
                            </tbody>
                        </table>
                    </div>
                </div>}

            </div>
        </div>
    )
}

export default MultiplicationPage
