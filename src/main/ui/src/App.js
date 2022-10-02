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
import Footer from "./layout/Footer";

function Page() {
    return <div className="Page container-fluid"><Outlet/></div>;
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
                            <Route path="vocabulary" element={<VocabularyPage/>}/>
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
        </Router>
    );
}

export default App;
