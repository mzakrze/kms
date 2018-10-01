/* @flow */
import React from 'react';

import {Link, BrowserRouter as Router } from 'react-router-dom';

const api = {

}

type Props = {
	routerProps: any;
}

type State = {

}

export default class Header extends React.Component<Props, State> {
    redirectUrl: string; // {"drive" | "teams" | "my-account"}

	constructor(props: Props){
		super(props);
		this.redirectUrl = null;
		this.state = {
		}
	}

    componentDidMount() {

    }

	renderMyAccountInfo(){
		return (<p>
		    TODO
		    <hr />
		    <Link to='my-account'>Edit</Link>
		    </p>);
	}

    handleUserLogout() {
		localStorage.removeItem('JwtAuthentication');
        window.location = '/'
    }

	render(){
		return (
			<div className="header">
				<ul className="nav nav-bar pull-right">
                    <Link className="p-2 text-dark" to="/drive">Drive</Link>
                    <Link className="p-2 text-dark" to="/my-account">My account</Link>
                    <button className="btn btn-outline-primary" onClick={this.handleUserLogout}>Log out</button>
				</ul>
				<Link to="/">
					<h3>
					<img src={'./static/DokuWikiLogo.png'} width="50" height="50"/>
					KMS
					</h3>
				</Link>
                <hr />
			</div>);
	}
}