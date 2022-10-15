import './WordItem.css'

/**
 * Display a word
 *
 * props.word: The word to display
 *
 * props.onSpeakTextChange: (optional) The action to do when changing
 * props.onDelete: (optional) The action to do when deleting
 *
 */
function WordItem(props) {

    const word = props.word

    return (
        <div className={`word score-${word.score}`}>
            {props.onDelete && <button className="btn btn-danger float-end" onClick={e => props.onDelete(e)}>X</button>}
            <div><strong>Mot:</strong> {word.word}</div>
            {props.onSpeakTextChange &&
                <div>
                    <input type="text" className="form-control" value={word.speakText.text}
                           onChange={e => props.onSpeakTextChange(e)}/>
                </div>
                ||
                <div><strong>Texte dit:</strong> {word.speakText.text}</div>
            }
        </div>
    )
}

export default WordItem
