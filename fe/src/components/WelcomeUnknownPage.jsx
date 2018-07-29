/* @flow */
import React from 'react';
import ReactDOM from 'react-dom';
import { HashRouter as Router, Route, Switch, withRouter, Redirect } from 'react-router-dom';



type Props = {
    attemptRegister: (string, string) => void,
    attemptLogin: (string, string) => void,
    attemptGoTryMode: () => void,
}

export default class WelcomeUnknownPage extends React.Component<Props> {

    constructor(props){
        super(props);
        this.state = {
            login_email: '',
            login_password: '',
            register_email: '',
            register_password: '',
            register_password_confirm: ''
        }   
    }

    handleFormChanged(key, value) {
        let s = this.state;
        s[key] = value;
        this.setState(s);
    }

    handleLogin(ev) {
        ev.preventDefault();
        // TODO - validate
        this.props.attemptLogin(this.state.login_email, this.state.login_password);
    }

    handleRegister(ev) {
        ev.preventDefault();
        // TODO - validate
        this.props.attemptRegister(this.state.register_email, this.state.register_password);
    }

    render(){
        let hasError = false; // TODO
        return (<div className="container">	
            <div className="row">
            <ul className="nav nav-tabs">
                <li className="active">
                    <a href="#login" data-toggle="tab">Login</a>
                </li>
                <li className="">
                    <a href="#register" data-toggle="tab">Register</a>
                </li>
            </ul>
            
            <div className="tab-content ">
                <div className="tab-pane active" id="login">
                    <h3>Login</h3>
                    <form className="form" onSubmit={this.handleLogin.bind(this)}>
                         <div className={"form-group" + (hasError ? " has-error" : "")}>
                            <label className="control-label" htmlFor="login_email_input">Email</label>
                            <input type="text" className="form-control" placeholder="Email" onChange={(ev) => this.handleFormChanged('login_email', ev.target.value)} id="login_email_input" />

                            <label className="control-label" htmlFor="login_password_input">Password</label>
                            <input type="password" className="form-control" onChange={(ev) => this.handleFormChanged('login_password', ev.target.value)} id="login_password_input" placeholder="Password" />

                            <span style={{visibility: hasError ? "" : "hidden"}}className="help-block">Email or password is incorrect</span>
                        </div>
                        <button type="submit" className="btn btn-primary">Log in</button>
                    </form>
                </div>
                <div className="tab-pane" id="register">
                    <h3>Register</h3>
                    <form className="form" onSubmit={this.handleRegister.bind(this)}>
                         <div className={"form-group" + (hasError ? " has-error" : "")}>
                            <label className="control-label" htmlFor="register_email_input">Email</label>
                            <input type="text" className="form-control" placeholder="Email" onChange={(ev) => this.handleFormChanged('register_email', ev.target.value)} id="register_email_input" />

                            <label className="control-label" htmlFor="register_password_input">Password</label>
                            <input type="password" className="form-control" onChange={(ev) => this.handleFormChanged('register_password', ev.target.value)} id="register_password_input" placeholder="Password" />

                            <label className="control-label" htmlFor="register_password_confirm_input">Password confirm</label>
                            <input type="password" className="form-control" onChange={(ev) => this.handleFormChanged('register_password_confirm', ev.target.value)} id="register_password_confirm_input" placeholder="Password confirm" />

                            <span style={{visibility: hasError ? "" : "hidden"}}className="help-block">Email or password is incorrect</span>
                        </div>
                        <button type="submit" className="btn btn-primary">Register</button>
                    </form>
                </div>
            </div>
            <hr />
            <div>
                <button onClick={this.props.attemptGoTryMode}>Or, try now for free</button>
            </div>
            </div>
        </div>);
    }
}