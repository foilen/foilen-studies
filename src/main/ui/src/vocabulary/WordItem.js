import './WordItem.css'

/**
 * Display a word
 *
 * props.word: The word to display
 *
 * props.onDelete: (optional) The action to do when deleting
 */
function WordItem(props) {

    const word = props.word

    return (
        <div className="word">
            {props.onDelete && <button className="btn btn-danger float-end" onClick={e => props.onDelete(e)}>X</button>}
            <div><strong>Mot:</strong> {word.word}</div>
            <div><strong>Texte dit:</strong> {word.speakText.text}</div> { /* TODO Edit speakText */ }
        </div>
    )
}

export default WordItem
