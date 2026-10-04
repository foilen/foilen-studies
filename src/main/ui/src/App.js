import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import "bootstrap-icons/font/bootstrap-icons.css";
import 'react-toastify/dist/ReactToastify.css';
import {ToastContainer} from "react-toastify";
import React, {useCallback, useEffect, useState} from "react";
import {HashRouter as Router, Navigate, Outlet, Route, Routes, useLocation} from "react-router-dom";
import Header from './layout/Header.js';
import HomePage from "./home/HomePage";
import VocabularyPage from "./vocabulary/VocabularyPage";
import VocabularyEditPage from "./vocabulary/VocabularyEditPage";
import VerbPage from "./verb/VerbPage";
import VerbEditPage from "./verb/VerbEditPage";
import Footer from "./layout/Footer";
import GoogleAnalytics from "./common/GoogleAnalytics";
import MultiplicationPage from "./multiplication/MultiplicationPage";
import DivisionPage from "./division/DivisionPage";
import LoginPage from "./user/LoginPage";
import ChangePasswordPage from "./user/ChangePasswordPage";

function Page({loggedIn, onLoggedIn}) {
    // The home page is public. The rest needs a login
    const location = useLocation()
    if (loggedIn === null) {
        return <div className="Page"></div>
    }
    if (loggedIn && location.pathname === '/login') {
        return <Navigate to="/" replace/>
    }
    if (!loggedIn && location.pathname !== '/') {
        return <div className="Page"><LoginPage onLoggedIn={onLoggedIn}/></div>
    }
    return <div className="Page"><Outlet/></div>;
}

function App() {
    const [loggedIn, setLoggedIn] = useState(null)

    const refreshLoggedIn = useCallback(() => {
        window.service.userCsrf()
            .then(() => window.service.userIsLoggedIn())
            .then((response) => setLoggedIn(response.data))
            .catch(() => setLoggedIn(false))
    }, [])
    useEffect(refreshLoggedIn, [refreshLoggedIn])

    const logout = () => {
        window.service.userLogout().finally(() => {
            window.location.hash = '#/'
            window.location.reload()
        })
    }

    return (
        <Router>
            <div className="App">
                <Header loggedIn={loggedIn} onLogout={logout}/>
                <div className="container-fluid">
                    <Routes>
                        <Route path="/" element={<Page loggedIn={loggedIn} onLoggedIn={refreshLoggedIn}/>}>
                            <Route path="" element={<HomePage/>}/>
                            <Route path="vocabulary/">
                                <Route path="" element={<VocabularyPage/>}/>
                                <Route path="create" element={<VocabularyEditPage/>}/>
                                <Route path=":wordListId" element={<VocabularyEditPage/>}/>
                            </Route>
                            <Route path="verb/">
                                <Route path="" element={<VerbPage/>}/>
                                <Route path="create" element={<VerbEditPage/>}/>
                                <Route path=":verbId" element={<VerbEditPage/>}/>
                            </Route>
                            <Route path="multiplication" element={<MultiplicationPage/>}/>
                            <Route path="division" element={<DivisionPage/>}/>
                            <Route path="login" element={<div/>}/>
                            <Route path="password" element={<ChangePasswordPage/>}/>
                        </Route>
                    </Routes>
                    <Footer/>
                </div>
            </div>
            <ToastContainer
                position="bottom-right"
                autoClose={5000}
                hideProgressBar={false}
                newestOnTop={false}
                closeOnClick
                rtl={false}
                pauseOnFocusLoss
                draggable
                pauseOnHover
            />
            <GoogleAnalytics/>
        </Router>
    );
}

export default App;
