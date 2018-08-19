/* @flow */
import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Route, Switch } from 'react-router-dom'
import WelcomeUnknownPage from './components/WelcomeUnknownPage.jsx'
import DrivePage from './components/drive/DrivePage.jsx'
import MyAccountPage from './components/MyAccountPage.jsx'
import Header from './components/Header.jsx'
import * as util from './util.jsx';


const api = {
    getCurrentUser: () => {
        return $.ajax({
            url: '/api/user/current',
            type: "GET",
            beforeSend: util.beforeSendRequest,
            async: true,
            headers: {"Content-Type": "application/json"},
        });
    },
    goTryMode: () => {
        return $.ajax({
            url: '/api/user/try_mode',
            type: "POST",
            async: true,
            headers: {"Content-Type": "application/json"},
        });
    },
    attemptLogin: (email: string, password: string) => {
        return $.ajax({
            url: '/api/user/login',
            type: "POST",
            data: JSON.stringify({email, password}),
            async: true,
            headers: {"Content-Type": "application/json"},
        });
    },
    attemptRegister: (email: string, password: string) => {
        return $.ajax({
            url: '/api/user/register',
            type: "POST",
            data: JSON.stringify({email, password}),
            async: true,
            headers: {"Content-Type": "application/json"},
        }); 
    }
}

type Props = {

}

export type CurrentUser = {
    gid: string,
    isInTryMode: boolean,
    anonymous: boolean,
}

class App extends React.Component<Props> {

    constructor(props){
        super(props);
        this.state = {
            currentUser: {
                anonymous: true,
            }
        }
    }

    handleLogin(email, password) {
        api.attemptLogin(email, password)
            .done((data, status, resp) => {
                let jwt = resp.getResponseHeader('Authentication');
                localStorage.setItem('JwtAuthentication', jwt);
                this.setState({
                    currentUser: data
                })
            });
    }

    handleRegister(email, password) {
        api.attemptRegister(email, password)
            .done((data, status, resp) => {
                let jwt = resp.getResponseHeader('Authentication');
                localStorage.setItem('JwtAuthentication', jwt);
                this.setState({
                    currentUser: data
                })
            });
    }

    handleTryMode() {
        api.goTryMode()
            .done((data, status, resp) => {
                this.setState({
                    currentUser: data
                })
            });
    }

    handleLogout() {
        localStorage.removeItem('JwtAuthentication');
        window.location = '/'
    }

    componentDidMount(){
        api.getCurrentUser()
            .done((data, status, resp) => {
                this.setState({
                    currentUser: data
                })
            })
    }

    render() {
        if(this.state.currentUser == null || this.state.currentUser.anonymous) {
            if(document.location.pathname != '/' && document.location.pathname != ''){
                window.location = '/';
            }
            return <WelcomeUnknownPage 
                attemptLogin={this.handleLogin.bind(this)}
                attemptRegister={this.handleRegister.bind(this)}
                attemptGoTryMode={this.handleTryMode.bind(this)} />
        }
        return (
            <BrowserRouter>
                <div>
                    <Route path="/my-account" render={this.renderMyAccountPage.bind(this)} />
                    <Route exact path="/" render={this.renderDrivePage.bind(this)} />
                    <Route path="/drive" render={this.renderDrivePage.bind(this)} />
                </div>
        </BrowserRouter>);
    }

    renderDrivePage(routerProps) {
        // strona domyślna - zmień url
        if(routerProps.location.pathname != '/drive'){
            routerProps.history.push('drive')
        }
        return (<div>
            <Header />
            <DrivePage />
            </div>);
    }

    renderMyAccountPage(routerProps){
        return (<div>
            <Header />
            <MyAccountPage />
            </div>);
    }
}

ReactDOM.render(<App />, document.getElementById('react-root') || document.createElement("div"));