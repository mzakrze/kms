/* @flow */
import React from 'react';
import ReactDOM from 'react-dom';

import * as api from './api.jsx';

type Props = {

}

class App extends React.Component<Props> {

    constructor(props){
        super(props);
        this.state = {
            login: 'test',
            password: '123456',
            loginResponse: null,
            title: 'title1',
            description: 'description1',
            toDelete: null,
            myTasks: []
        }
    }

    handleInputChanged(key, val){
        let s = this.state;
        s[key] = val;
        this.setState(s);
    }

    renderLogin(){
        let handleLogin = () => {
            let login = this.state.login;
            let password = this.state.password;
            $.ajax({
                url: '/api/v1/user/login',
                type: "POST",
                data: JSON.stringify({login, password}),
                async: true,
                headers: {"Content-Type": "application/json"},
            }).done((data, status, resp) => {
                let jwt = resp.getResponseHeader('Authentication');
                localStorage.setItem('JwtAuthentication', jwt);
            })
        }
        let handleLogout = () => {
            localStorage.removeItem('JwtAuthentication');
        }
        return [
            <input onChange={(ev) => this.handleInputChanged('login', ev.target.value)} placeholder="login" defaultValue={this.state.login}/>,
            <input onChange={(ev) => this.handleInputChanged('password', ev.target.value)} placeholder="password" defaultValue={this.state.password}/>,
            <button onClick={handleLogin}>Login</button>,
            <button onClick={handleLogout}>Logout</button>,
            <div>
                {this.state.loginResponse}
            </div>
        ];
    }

    renderFetchMyTasks(){
        let handleUpdate = () => {
            api.fetchMyTasks()
                .done((data, status, resp) => {
                    this.setState({
                        myTasks: data
                    })
                })
        }
        return [
            <button onClick={handleUpdate}> update </button>,
            <p>{JSON.stringify(this.state.myTasks)}</p>
        ];
    }

    renderCreateNewTask(){
        let handleCreate = () => {
            api.createNewTask(this.state.title, this.state.description);
        }
        return [
            <input onChange={(ev) => this.handleInputChanged('title', ev.target.value)} placeholder="title" defaultValue={this.state.title}/>,
            <input onChange={(ev) => this.handleInputChanged('description', ev.target.value)} placeholder="description" defaultValue={this.state.description}/>,
            <button onClick={handleCreate}>create</button>,
        ];
    }

    renderDelete(){
        let handleDelete = () => {
            api.deleteTask(this.state.toDelete)
        }
        return [
            <input onChange={(ev) => this.handleInputChanged('toDelete', ev.target.value)} placeholder="toDelete" defaultValue={this.state.toDelete}/>,
            <button onClick={handleDelete}>delete</button>,
        ];
    }

    render() {
        return (<div>
            {this.renderLogin()}
            <hr />
            {this.renderFetchMyTasks()}
            <hr />
            {this.renderCreateNewTask()}
            <hr />
            {this.renderDelete()}
            </div>)
        return (<p> Hello World </p>);
    }
}

ReactDOM.render(<App />, document.getElementById('react-root') || document.createElement("div"));