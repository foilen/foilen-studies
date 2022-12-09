import './WordItem.css'

/**
 * Display a word
 *
 * props.prefix: (optional) A prefix to display
 * props.word: The word to display
 *
 * props.onWordChange: (optional) The action to do when changing
 * props.onPrefixChange: (optional) The action to do when changing
 * props.onSpeakTextChange: (optional) The action to do when changing
 * props.onDelete: (optional) The action to do when deleting
 *
 */
function WordItem(props) {

    const word = props.word

    return (
        <div className={`word score-${word.score}`}>
            {props.onDelete && <button className="btn btn-danger float-end" onClick={e => props.onDelete(e)}>X</button>}

            {props.onPrefixChange &&
                <div>
                    <input type="text" className="form-control" value={props.prefix}
                           onChange={e => props.onPrefixChange(e)}/>
                </div>
                ||
                props.prefix && <div>{props.prefix} </div>
            }

            {props.onWordChange &&
                <div>
                    <input type="text" className="form-control" value={word.word}
                           onChange={e => props.onWordChange(e)}/>
                </div>
                ||
                <div><strong>Mot:</strong> {word.word}</div>
            }

            {props.onSpeakTextChange &&
                <div>
                    <input type="text" className="form-control" value={word.speakText.text}
                           onChange={e => props.onSpeakTextChange(e)}/>
                </div>
                ||
                word.speakText && <div><strong>Texte dit:</strong> {word.speakText.text}</div>
            }
        </div>
    )
}

export default WordItem
