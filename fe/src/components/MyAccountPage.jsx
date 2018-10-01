/* @flow */
import React from 'react';
import ReactDOM from 'react-dom';
import { HashRouter as Router, Route, Switch, withRouter, Redirect } from 'react-router-dom';


import { beforeSendRequest } from './../util.jsx';


const api = {
  checkCredentials: (spaceName, password) => {
    let data = {spaceName, password};
    return $.ajax({
      url: '/api/space/status',
      type: "POST",
      data: JSON.stringify(data),
      async: false,
      headers: {"Content-Type": "application/json"},
      beforeSend: beforeSendRequest,
  });
  }
}

type Props = {

}

type State = {
  tabSelected: string,
}

export default class DrivePage extends React.Component<Props> {

    constructor(props){
        super(props);
        this.state = {
          tabSelected: 'space',
          currentSpace: 'TODO',
          spaceForm: {},
          accountForm: {},
          gitForm: {},
        }   
    }

    renderSpaceSettings() {
      return (<form className="form-horizontal">
      <fieldset>
      
      <legend>Space settings</legend>
      
      Current space: {this.state.currentSpace}

      <div className="form-group">
        <label className="col-md-8 control-label" for="spaceName">Space name</label>  
        <div className="col-md-8">
        <input id="spaceName" name="spaceName" className="form-control input-md" type="text" onChange={ev => formChanged('currSpace', '', ev.target.value)}/>
        </div>
      </div>
      <div className="form-group">
        <label className="col-md-8 control-label" for="password">Password for the space</label>  
        <div className="col-md-8">
        <input id="password" name="password" className="form-control input-md" type="password" onChange={ev => handlePasswordChanged(ev.target.value)}/>
        </div>
      </div>
      
      <button onClick={() => {
        api.checkCredentials(spaceName, password)
        .done((data, status, dunno) => {
          if(status == 200){
            alert('changed space to: ' + data)
          }
        })
        .catch((response) => {
          if(response.status == 403){
            alert('invalid data');
          }
        })
      }} type="button">
        Change space
      </button>


      <hr/>
      Create new space

      <div className="form-group">
        <label className="col-md-8 control-label" for="spaceName">Space name</label>  
        <div className="col-md-8">
        <input id="spaceName" name="spaceName" className="form-control input-md" type="text" onChange={ev => handleFormChanged(ev.target.value)}/>
        </div>
      </div>
      <div className="form-group">
        <label className="col-md-8 control-label" for="password">Password for the space</label>  
        <div className="col-md-8">
        <input id="password" name="password" className="form-control input-md" type="password" onChange={ev => handlePasswordChanged(ev.target.value)}/>
        </div>
      </div>
      <div className="form-group">
        <label className="col-md-8 control-label" for="password">Retype password</label>  
        <div className="col-md-8">
        <input id="password" name="password" className="form-control input-md" type="password" onChange={ev => handlePasswordChanged(ev.target.value)}/>
        </div>
      </div>


      <hr/>
      <button onClick={() => {alert('not implemented')}} type="button">
        I forgot my spaces, remind me via email
      </button>
      
      </fieldset>
      </form>);
    }

    renderAccountSettings() {

    }

    renderGitSettings() {

    }

    render(){
        return (<div className="container">
          <div className="row">
            <aside className="col-sm-6 col-md-3 card  my-4">
              <h3>Settings</h3>
              <hr />
              <ul>
                  <li><a onClick={() => this.changeSelectedTag('space')}>
                          <span className="fa fa-paint-brush "></span>
                          Space
                      </a>
                  </li>
                  <li>
                      <a onClick={() => this.changeSelectedTag('account')}> 
                          <span className="fa fa-gear"></span>
                          Account settings
                      </a>
                  </li>
                  <li> 
                      <a onClick={() => this.changeSelectedTag('git')}>
                          <span className="fa fa-wrench"></span>
                          Git integration
                      </a>
                  </li>
              </ul>
            </aside>
          {this.state.tabSelected == 'space' ?
            this.renderSpaceSettings() : 
            this.state.tabSelected == 'account' ?
            this.renderAccountSettings() : 
            this.state.tabSelected == 'git' ?
            this.renderGitSettings() :
            <div>:(</div>
          }





        </div>
    </div>);
    }
}