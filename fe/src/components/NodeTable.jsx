/* @flow */
import React from 'react';

import {Link } from 'react-router-dom';

import {Node} from './DrivePage.jsx';
import NodeFilters  from './NodeFilters.jsx';
import { Redirect } from 'react-router-dom';


type Props = {
    currentFolder: Node,
    requestDriveReload: () => void,
}

type State = {
    filteringFunction: (Node) => boolean,
    sortingFunction: (Node, Node) => number,
    requestRedirectRevision: number,
    showConfirmModal: boolean,
    toDelNodeGid: string,
    showShareModal: boolean,
    nodeToShare: any
}

export default class NodeTable extends React.Component<Props, State> {
    // rename node
    requestRedirectUrl: string;

    constructor(props){
        super(props);
        this.requestRedirectUrl = null;
        this.state = {
            filteringFunction: (node) => {return true;},
            sortingFunction: (n1, n2) => {return n1.localCompare(n2.name)},
            requestRedirectRevision: 0,
            showConfirmModal: false,
            toDelNodeGid: null,
            showShareModal: false,
            nodeToShare: null,
        }
    }

    handleFilteringFunctionChange(newFunction){
        this.setState({
            filteringFunction: newFunction
        });
    }

    handleSortingFunctionChange(option, direction){
        let dir = direction == 'asc' ? 1 : -1;
        let f;
        if(option == 'name'){
            f = (a, b) => a.name.localCompare(b.name)
        }
        this.setState({
            sortingFunction: (n1, n2) => {return f(n1,n2) * dir;}
        });
    }

    renderTBody() {
        if(this.props.currentFolder == null){
            return <tbody />;
        }
        let trs = [];
        let nodes = this.props.currentFolder.folders.concat(this.props.currentFolder.documents).concat(this.props.currentFolder.blobs);
        for(let n of nodes){
            if(this.state.filteringFunction(n)){
                let gotoButtonFunction = () => {
                    this.requestRedirectUrl = '/doc/' + n.gid;
                    let rev = this.state.requestRedirectRevision + 1;
                    this.setState({
                        requestRedirectRevision: rev
                    });
                }
                let deleteButtonFunction = () => {
                    this.setState({
                        showConfirmModal: true,
                        toDelNodeGid: n.gid
                    });
                }
                let shareButtonFunction = () => {
                    this.setState({
                        showShareModal: true,
                        nodeToShare: n
                    });
                }
                let iconClassName = "state-icon glyphicon glyphicon-" + (n.type == 'folder' ? 'folder-open' : 'book');
                let mockDate = this.plzMockDate()
                trs.push(<tr>
                    <td>{n.name} <span style={{float:"right"}}><i className={iconClassName}></i></span></td>
                    <td>{mockDate.modified}</td>
                    <td>{mockDate.viewed}</td>
                    <td style={{width: "220px"}}>
                        <button type="button" className="btn btn-default btn-sm" onClick={gotoButtonFunction}>View</button>
                        <button type="button" className="btn btn-danger btn-sm" disabled={n.role === 'read'}onClick={deleteButtonFunction}>Delete</button>
                        <button type="button" className="btn btn-primary btn-sm " onClick={shareButtonFunction}>Share details</button>
                    </td>
                    </tr>);
            }
        }
        return <tbody>{trs}</tbody>;
    }

    plzMockDate(){
        let now = 1526587831000;
        let randDayBackNo1 = Math.random() * 10;
        let randDayBackNo2 = Math.random() < 0.1 ? randDayBackNo1 : (randDayBackNo1 - Math.random() * 10);

        randDayBackNo1 *= 1000 * 60 * 60 * 24;
        randDayBackNo2 *= 1000 * 60 * 60 * 24;

        let v = new Date(now - randDayBackNo2);
        let m = new Date(now - randDayBackNo1);


        return {
            modified: m.getDate() + '/0' + m.getMonth() + '/' + m.getFullYear(),
            viewed: v.getDate() + '/0' + v.getMonth() + '/' + v.getFullYear()
        }
    }


    renderConfirmModal(){
        return (<ConfirmModal
            show={this.state.showConfirmModal}
            nodeGid={this.state.toDelNodeGid}
            requestTreeReload={() => this.props.requestDriveReload()}
            modalClosed={() => this.setState({showConfirmModal: false})}
            content='this is test content' />);
    }

    renderShareModal() {
        return (<ShareModal
            show={this.state.showShareModal}
            modalClosed={() => this.setState({showShareModal: false})}
            node={this.state.nodeToShare} />);
    }

    render() {
        if(this.requestRedirectUrl != null){
            let url = this.requestRedirectUrl;
            return <Redirect to={url} />
        }
        return (
            <div id="example_wrapper" className="dataTables_wrapper form-inline dt-bootstrap">
               <div className="row">
                  <NodeFilters
                    onFilteringFunctionChange={this.handleFilteringFunctionChange.bind(this)} />
               </div>
               <hr />
               <div className="row">
                  <div className="col-sm-12">
                     <table id="example" className="table table-striped table-bordered dataTable" style={{width: "100%"}} role="grid" aria-describedby="example_info">
                        <thead>
                           <tr role="row">
                              <th rowSpan="1" colSpan="1">Name<span style={{float:"right"}}><i className="state-icon glyphicon glyphicon-sort"></i></span> </th>
                              <th rowSpan="1" colSpan="1">Last modified<span style={{float:"right"}}><i className="state-icon glyphicon glyphicon-sort"></i></span> </th>
                              <th rowSpan="1" colSpan="1">Last viewed<span style={{float:"right"}}><i className="state-icon glyphicon glyphicon-sort"></i></span> </th>
                           </tr>
                        </thead>
                        {this.renderTBody()}
                        {this.renderConfirmModal()}
                        {this.renderShareModal()}
                     </table>
                  </div>
               </div>
            </div>);
        }
}









type ConfirmModalProps = {
    nodeGid: string,
    requestTreeReload: () => void,
    show: boolean,
    modalClosed: () => void,
    content: any
}
type ConfirmModalState = {
}
class ConfirmModal extends React.Component<ConfirmModalProps, ConfirmModalState> {

    constructor(props){
        super(props)
    }

    componentDidMount(){
        $('#confirm-modal').on('hidden.bs.modal', this.props.modalClosed)
    }

    componentWillReceiveProps(nextProps){
        if(false == this.props.show && nextProps.show){
            $('#confirm-modal').modal('toggle');
        }
    }

    handleDelete(){
        api.deleteNodeCascade(this.props.nodeGid)
            .done((data, status, resp) => {
                $('#confirm-modal').modal('toggle');
                this.props.requestTreeReload();
            });
    }

    handleCancel(){
        $('#confirm-modal').modal('toggle');
    }

    render() {
        return (
            <div className="modal fade" id="confirm-modal">
            <div className="modal-dialog">
              <div className="modal-content">
                <div className="modal-header">
                  <h4 className="modal-title">Please confirm</h4>
                  <button type="button" className="close" data-dismiss="modal">&times;</button>
                </div>
                <div className="modal-body">
                    {this.props.content}
                    <hr/>
                    <button className="btn btn-primary" onClick={this.handleDelete.bind(this)}>Delete</button>
                    <button className="btn btn-primary" onClick={this.handleCancel.bind(this)}>Cancel</button>
                </div>
              </div>
            </div>
          </div>
        );
    }

}




type ShareModalProps = {
    show: boolean,
    modalClosed: () => void,
    node: string,
}
type ShareModalState = {
}
class ShareModal extends React.Component<ShareModalProps, ShareModalState> {

    constructor(props){
        super(props)
    }

    componentDidMount(){
        $('#share-modal').on('hidden.bs.modal', this.props.modalClosed);
        $('#users-collapse').collapse("show")
    }

    componentWillReceiveProps(nextProps){
        if(false == this.props.show && nextProps.show){
            $('#share-modal').modal('toggle');
            
            let usersRead = $('#share-users-read-input');
            let usersWrite = $('#share-users-write-input');
            let usersAdmin = $('#share-users-admin-input');
            let teamsRead = $('#share-teams-read-input');
            let teamsWrite = $('#share-teams-write-input');
            let teamsAdmin = $('#share-teams-admin-input');

            usersRead.tagsinput('removeAll');
            usersWrite.tagsinput('removeAll');
            usersAdmin.tagsinput('removeAll');
            teamsRead.tagsinput('removeAll');
            teamsWrite.tagsinput('removeAll');
            teamsAdmin.tagsinput('removeAll');

            if(nextProps.node != null){
                for(let o of nextProps.node.owners){
                    if(o.accessType == 'user'){
                        if(o.scope == 'admin'){
                            usersAdmin.tagsinput('add', o.email);}
                        else if(o.scope == 'read_write'){
                            usersWrite.tagsinput('add', o.email);}
                        else if(o.scope == 'read'){
                            usersRead.tagsinput('add', o.email); }
                    } else if(o.accessType == 'team'){
                        if(o.scope == 'admin'){
                            teamsAdmin.tagsinput('add', o.name);}
                        else if(o.scope == 'read_write'){
                            teamsWrite.tagsinput('add', o.name); }
                        else if(o.scope == 'read'){
                            teamsRead.tagsinput('add', o.name);}
                    }
                }
            }
        }
    }

    handleCancel(){
        $('#share-modal').modal('toggle');
    }

    handleSubmit(ev){
        ev.preventDefault();

        let usersRead = $('#share-users-read-input').tagsinput('items');
        let usersWrite = $('#share-users-write-input').tagsinput('items');
        let usersAdmin = $('#share-users-admin-input').tagsinput('items');
        let teamsRead = $('#share-teams-read-input').tagsinput('items');
        let teamsWrite = $('#share-teams-write-input').tagsinput('items');
        let teamsAdmin = $('#share-teams-admin-input').tagsinput('items');

        let data = {
            usersRead: usersRead,
            usersWrite: usersWrite,
            usersAdmin: usersAdmin,
            teamsRead: teamsRead,
            teamsWrite: teamsWrite,
            teamsAdmin: teamsAdmin
        }

        api.upsertNodeShares(this.props.node.gid, data)
            .done((data, status, resp) => {
                $('#share-modal').modal('toggle');
            });
    }

    shareSelectorTypeChanged(id){
        let s = '#' + id + '-collapse';
        let h = '#' + (id == 'users' ? 'teams' : 'users') + '-collapse';
        $(h).collapse('hide');
        $(s).collapse("show")
    }

    render() {
        return (
            <div className="modal fade" id="share-modal">
            <div className="modal-dialog">
              <div className="modal-content">
                <div className="modal-header">
                  <h4 className="modal-title">Share options</h4>
                  <button type="button" className="close" data-dismiss="modal">&times;</button>
                </div>
                <div className="modal-body">
                    <form className="form-share" onSubmit={this.handleSubmit.bind(this)}>
                            <div className="btn-group" data-toggle="buttons">
                                <label className="btn btn-primary active" onClick={() => this.shareSelectorTypeChanged('users')} >
                                    <input type="radio" name="options" id="users" /> Share with users
                                </label>
                                <label className="btn btn-primary" onClick={() => this.shareSelectorTypeChanged('teams')}>
                                    <input type="radio" name="options" id="teams"  /> Share with teams
                                </label>
                            </div>

                            <div className="collapse indent" id="users-collapse">
                                <h3> Share with users </h3>
                                <label className="control-label" htmlFor={"share-users-read-input"}>Read permissions</label>
                                <input type="text" className="form-control" id={"share-users-read-input"} data-role="tagsinput" /> <br/>

                                <label className="control-label" htmlFor={"share-users-write-input"}>Write permissions</label>
                                <input type="text" className="form-control" id={"share-users-write-input"} data-role="tagsinput" /> <br/>

                                <label className="control-label" htmlFor={"share-users-admin-input"}>Admin permissions</label>
                                <input type="text" className="form-control" id={"share-users-admin-input"} data-role="tagsinput" /> <br/>
                            </div>

                            <div className="collapse indent" id="teams-collapse">
                                <h3> Share with teams </h3>
                                <label className="control-label" htmlFor={"share-teams-read-input"}>Read permissions</label>
                                <input type="text" className="form-control" id={"share-teams-read-input"} data-role="tagsinput" /> <br/>

                                <label className="control-label" htmlFor={"share-teams-write-input"}>Write permissions</label>
                                <input type="text" className="form-control" id={"share-teams-write-input"} data-role="tagsinput" /> <br/>

                                <label className="control-label" htmlFor={"share-teams-admin-input"}>Admin permissions</label>
                                <input type="text" className="form-control" id={"share-teams-admin-input"} data-role="tagsinput" /> <br/>
                            </div>
                        <hr/>
                        <button className="btn btn-primary" disabled={this.props.node ? this.props.node.role == 'read' : false} type="submit">Save</button>
                        <button className="btn btn-primary" type="button" onClick={this.handleCancel.bind(this)}>Cancel</button>
                    </form>
                </div>
              </div>
            </div>
          </div>
        );
    }

}