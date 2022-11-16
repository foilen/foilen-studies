import React from 'react';
import {createRoot} from 'react-dom/client';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {Service} from "./service";

window.service = new Service()

// Redirect if the user is not logged in and not on the home page
let userIsLoggedIn = true

function redirectIfNotLoggedIn() {
    if (userIsLoggedIn) {
        return
    }

    const onTheMainPage = window.location.hash === '' || window.location.hash === '#/'
    if (onTheMainPage) {
        // Check later
        setTimeout(redirectIfNotLoggedIn, 500)
    } else {
        console.log('User is not logged in. Must log in.')
        const hashWithoutLeadingHash = window.location.hash.substring(1)
        window.location.href = '/user/needsLogin?hash=' + hashWithoutLeadingHash
    }
}

window.service.userIsLoggedIn().then((response) => {
    userIsLoggedIn = response.data
    redirectIfNotLoggedIn()
})


const container = document.getElementById('root');
const root = createRoot(container);

root.render(
    <React.StrictMode>
        <App/>
    </React.StrictMode>
)

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
