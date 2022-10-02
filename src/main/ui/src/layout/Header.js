import React from "react";
import {NavLink} from "react-router-dom";

function Header() {
    return (
        <nav className="navbar navbar-expand-lg bg-light">
            <div className="container-fluid">
                <a className="navbar-brand" href="#">Foilen - Études</a>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        <li className="nav-item">
                            <NavLink to="/"
                                     className={({isActive}) => isActive ? "nav-link active" : "nav-link"}>Accueil</NavLink>
                        </li>
                        <li className="nav-item">
                            <NavLink to="/vocabulary"
                                     className={({isActive}) => isActive ? "nav-link active" : "nav-link"}>Vocabulaire</NavLink>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    )
}

export default Header;