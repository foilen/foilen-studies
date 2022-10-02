import React, {useEffect, useState} from "react";
import {autoRetry} from "../service";

function VocabularyPage() {

    const [user, setUser] = useState({})

    useEffect(() => {
        autoRetry('Get User Details', () => window.service.getUser(), 5000).then(response => {
            setUser(response.data.item)
        })
    }, [])

    return (
        <div className="row">
            <div className="col">

                <h2>Vocabulary</h2>

                TODO Vocabulary
            </div>
        </div>
    );
}

export default VocabularyPage
