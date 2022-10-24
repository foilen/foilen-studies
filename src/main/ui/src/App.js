import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import "bootstrap-icons/font/bootstrap-icons.css";
import 'react-toastify/dist/ReactToastify.css';
import {ToastContainer} from "react-toastify";
import React from "react";
import {HashRouter as Router, Outlet, Route, Routes} from "react-router-dom";
import Header from './layout/Header.js';
import HomePage from "./home/HomePage";
import VocabularyPage from "./vocabulary/VocabularyPage";
import VocabularyEditPage from "./vocabulary/VocabularyEditPage";
import Footer from "./layout/Footer";
import GoogleAnalytics from "./common/GoogleAnalytics";

function Page() {
    return <div className="Page"><Outlet/></div>;
}

function App() {
    return (
        <Router>
            <div className="App">
                <Header/>
                <div className="container-fluid">
                    <Routes>
                        <Route path="/" element={<Page/>}>
                            <Route path="" element={<HomePage/>}/>
                            <Route path="vocabulary/">
                                <Route path="" element={<VocabularyPage/>}/>
                                <Route path="create" element={<VocabularyEditPage/>}/>
                                <Route path=":wordListId" element={<VocabularyEditPage/>}/>
                            </Route>
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
