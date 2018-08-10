/* @flow */
import React from 'react';
import ReactDOM from 'react-dom';
import { HashRouter as Router, Route, Switch, withRouter, Redirect } from 'react-router-dom';
import { CurrentUser } from '../index.jsx';


type Props = {
    currentUser: any,
}

export default class Header extends React.Component<Props> {

    constructor(props){
        super(props);
        this.state = {
            
        }   
    }


    render(){
        return (<p> This is header </p>);
    }
}