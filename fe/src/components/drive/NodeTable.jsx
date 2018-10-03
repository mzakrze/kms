/* @flow */
import React from 'react';

import {Link } from 'react-router-dom';

import {Node} from './DrivePage.jsx';
import NodeFilters  from './NodeFilters.jsx';
import { Redirect } from 'react-router-dom';
import { beforeSendRequest } from './../../util.jsx';
const api = {
    deleteNodeCascade: (nodeGid:string) => {
        return $.ajax({
            url: '/api/drive/remove/' + nodeGid,
            type: "DELETE",
            async: false,
            headers: {"Content-Type": "application/json"},
            beforeSend: beforeSendRequest,
        });
    }
}

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
                let iconClassName = "state-icon glyphicon glyphicon-" + (n.type == 'folder' ? 'folder-open' : 'book');
                let mockDate = this.plzMockDate()
                trs.push(<tr>
                    <td>{n.name} <span style={{float:"right"}}><i className={iconClassName}></i></span></td>
                    <td>{mockDate.modified}</td>
                    <td>{mockDate.viewed}</td>
                    <td style={{width: "220px"}}>
                        <button type="button" className="btn btn-default btn-sm" onClick={gotoButtonFunction}>View</button>
                        <button type="button" className="btn btn-danger btn-sm" disabled={n.role === 'read'}onClick={deleteButtonFunction}>Delete</button>
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

