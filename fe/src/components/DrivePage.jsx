/* @flow */
import React from 'react';
import ReactDOM from 'react-dom';
import { HashRouter as Router, Route, Switch, withRouter, Redirect } from 'react-router-dom';



type Props = {

}

export default class DrivePage extends React.Component<Props> {

    constructor(props){
        super(props);
        this.state = {
        }   
    }


    render(){
        return (<p> Hello, authorized user </p>);
    }
}