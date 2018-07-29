/* @flow */
import React from 'react';
import ReactDOM from 'react-dom';
import { HashRouter as Router, Route, Switch, withRouter, Redirect } from 'react-router-dom';
import WelcomeUnknownPage from './components/WelcomeUnknownPage.jsx'
import { beforeSend } from './api_util.jsx';

const api = {
    getCurrentUser: () => {
        return $.ajax({
            url: '/api/v1/user/current',
            type: "GET",
            beforeSend: beforeSend,
            async: true,
            headers: {"Content-Type": "application/json"},
        });
    },
    goTryMode: () => {
        return $.ajax({
            url: '/api/v1/user/try_mode',
            type: "POST",
            async: true,
            headers: {"Content-Type": "application/json"},
        });
    },
    attemptLogin: (email: string, password: string) => {
        return $.ajax({
            url: '/api/v1/user/login',
            type: "POST",
            data: JSON.stringify({email, password}),
            async: true,
            headers: {"Content-Type": "application/json"},
        });
    },
    attemptRegister: (email: string, password: string) => {
        return $.ajax({
            url: '/api/v1/user/register',
            type: "POST",
            data: JSON.stringify({email, password}),
            async: true,
            headers: {"Content-Type": "application/json"},
        }); 
    }
}

type Props = {

}

type CurrentUser = {
    gid: string,
    isInTryMode: boolean,
}

class App extends React.Component<Props> {

    constructor(props){
        super(props);
        this.state = {
            currentUser: null,
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
        if(this.state.currentUser == null || this.state.currentUser.gid == null) {
            return <WelcomeUnknownPage 
                attemptLogin={this.handleLogin.bind(this)}
                attemptRegister={this.handleRegister.bind(this)}
                attemptGoTryMode={this.handleTryMode.bind(this)} />
        }

        return (<div>
                <p> you are logged in {JSON.stringify(this.state.currentUser)} xd :) </p>
                <button onClick={this.handleLogout.bind(this)}>Logout</button>
                </div>);

        // return (<Router>
        //     <Switch>
        //         <Route exact path="/" render={this.renderEmptyPage.bind(this)} />
        //         <Route path="/doc/:docGid" render={this.renderDocumentEditorPage.bind(this)} />
        //         <Route path="/drive" render={this.renderDrivePage.bind(this)} />
        //         <Route path="/teams" render={this.renderTeamsPage.bind(this)} />
        //         <Route path="/my-account" render={this.renderMyAccountPage.bind(this)} />
        //     </Switch>
        // </Router>);
    }
}

ReactDOM.render(<App />, document.getElementById('react-root') || document.createElement("div"));