import React from "react";
import emailImage from "./email.png";
import githubImage from "./github.png";

function HomePage() {

    return (<>
        <div className="row">
            <div className="col">
                <h2>Bienvenue sur ce site pour jeunes étudiants</h2>
                <p>Choisissez une activité dans le menu supérieur.</p>
            </div>
        </div>

        <div className="row">
            <div className="col col-sm-8 col-md-5 col-xl-4 col-xxl-3">
                <div className="card">
                    <div className="card-body">
                        <h5 className="card-title">Compte Microsoft</h5>
                        <p className="card-text">
                            Vous allez devoir vous connecter à un compte Microsoft (compte pour vous connecter à
                            votre Windows 10/11, Hotmail, Outlook, travail ou école).
                            Cela va permettre d'obtenir seulement un identifiant unique pour conserver vos listes et
                            statistiques.
                            Nous n'obtiendrons pas votre nom, courriel ou toute autre information permettant de
                            savoir qui vous êtes ou permettre de vous contacter.
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <div className="row">
            <div className="col col-sm-8 col-md-5 col-xl-4 col-xxl-3">
                <div className="card">
                    <div className="card-body">
                        <h5 className="card-title">Présentation du module de vocabulaire (1 min)</h5>
                        <p className="card-text">
                            <iframe width="560" height="315" src="https://www.youtube.com/embed/4IDzIyXSKwA"
                                    title="YouTube video player" frameBorder="0"
                                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                                    allowFullScreen></iframe>
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <div className="row">
            <div className="col col-sm-8 col-md-5 col-xl-4 col-xxl-3">
                <div className="card">
                    <div className="card-body">
                        <h5 className="card-title">Module de vocabulaire (11 min)</h5>
                        <p className="card-text">
                            <iframe width="560" height="315" src="https://www.youtube.com/embed/gMr52iWrY38"
                                    title="YouTube video player" frameBorder="0"
                                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                                    allowFullScreen></iframe>
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <div className="row">
            <div className="col col-sm-8 col-md-5 col-xl-4 col-xxl-3">
                <div className="card">
                    <div className="card-body">
                        <h5 className="card-title">Logiciel libre</h5>
                        <p className="card-text">
                            <img src={githubImage} className="float-end" alt="GitHub"/>
                            Ce logiciel est à sources libres. Vous pouvez consulter le code et participer à son
                            amélioration.
                        </p>
                        <a href="https://github.com/foilen/foilen-studies" className="btn btn-primary"
                           target="_blank">GitHub</a>
                    </div>
                </div>
            </div>
        </div>

        <div className="row">
            <div className="col col-sm-8 col-md-5 col-xl-4 col-xxl-3">
                <div className="card">
                    <div className="card-body">
                        <h5 className="card-title">Contacter</h5>
                        <p className="card-text">
                            <img src={emailImage} className="float-end" alt="Courriel"/>
                            Si vous avez des questions, des problèmes ou des suggestions, n'hésitez pas à me contacter.
                        </p>
                        <a href="mailto:simon@foilen.com?subject=Foilen études" className="btn btn-primary"
                           target="_blank">Courriel</a>
                    </div>
                </div>
            </div>
        </div>

    </>)
}

export default HomePage
