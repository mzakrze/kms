/* @flow */
import React from 'react';

import {Link } from 'react-router-dom';
import {Node} from './DrivePage.jsx';

import { beforeSendRequest } from './../util.jsx';

const api = {
    createDocument: (parentFolderGid, title, creationType) => {
        let data = {
            type: 'document',
            name: title,
            parentGid: parentFolderGid
        }
        return $.ajax({
            url: '/api/drive/create',
            type: "POST",
            data: JSON.stringify(data),
            async: false,
            headers: {"Content-Type": "application/json"},
            beforeSend: beforeSendRequest,
        });
    },

    createFolder: (parentFolderGid, name) => {
        let data = {
            type: 'folder',
            name: name,
            parentGid: parentFolderGid
        }
        return $.ajax({
            url: '/api/drive/create',
            type: "POST",
            data: JSON.stringify(data),
            async: false,
            headers: {"Content-Type": "application/json"},
            beforeSend: beforeSendRequest,
        });
    },

    uploadBlob: () => {
        // TODO - implement me
    }
}

type Props = {
    requestDriveReload: () => void,
    newNodeParentFolder: Node,
}

type State = {
    showNewDocModal: boolean,
    showNewFolderModal: boolean,
}

export default class DriveManagerHeader extends React.Component<Props, State> {

    constructor(props){
        super(props)
        this.state = {
            showNewDocModal: false,
            showNewFolderModal: false,
        }
    }

    handleRequestReloadDrive(){
        this.props.requestDriveReload();
    }

    render() {
        return (
            <div>
                <button onClick={() => this.setState({showNewDocModal: true})} className="btn btn-primary">New document</button>
                <button onClick={() => this.setState({showNewFolderModal: true})} className="btn btn-primary">New Folder</button>
                <NewNodeModal
                    type='doc'
                    show={this.state.showNewDocModal}
                    folder={this.props.newNodeParentFolder}
                    modalClosed={() => this.setState({showNewDocModal: false})}
                    requestReloadDrive={this.handleRequestReloadDrive.bind(this)} />
                <NewNodeModal
                    type='folder'
                    show={this.state.showNewFolderModal}
                    folder={this.props.newNodeParentFolder}
                    modalClosed={() => this.setState({showNewFolderModal: false})}
                    requestReloadDrive={this.handleRequestReloadDrive.bind(this)} />
            </div>
        );
    }
}

type NewNodeModalProps = {
    type: string, // { 'doc' | 'folder' }
    show: boolean,
    folder: any,
    modalClosed: () => void,
    requestReloadDrive: () => void,
}
type NewNodeModalState = {

}
class NewNodeModal extends React.Component<NewNodeModalProps, NewNodeModalState> {
    documentCreationTemplate: string;
    documentTitle: string;
    type: string;
    formTitle: string;
    FolderName: string;

    constructor(props) {
        super(props);

        this.type = props.type;
        this.id = 'new-' + props.type + '-modal';
        this.formTitle = "New " + (props.type == 'folder' ? "folder" : "document");
    }

    componentDidMount(){
        $('#' + this.id).on('hidden.bs.modal', this.props.modalClosed)
    }

    onSubmit(ev: any) {

        if(this.type == 'doc'){
            let parentFolderGid = this.props.folder.gid;
            let title = this.documentTitle;
            let creationType = this.documentCreationTemplate;
            api.createDocument(parentFolderGid, title, creationType);
            this.props.requestReloadDrive();
        } else if(this.type == 'folder'){
            let parentFolderGid = this.props.folder.gid;
            let name = this.FolderName;
            api.createFolder(parentFolderGid, name)
                .done((data, status, resp) => {
                    this.props.requestReloadDrive();
                });
        }

        $('#' + this.id).modal('toggle');
    }

    componentWillReceiveProps(nextProps){
        if(false == this.props.show && nextProps.show){
            $('#' + this.id).modal('toggle');
        }
    }

    renderNewDocModalBody(){
        return (<form className="form" onSubmit={this.onSubmit.bind(this)}>
                     <div className={"form-group"}>
                        <label className="control-label" htmlFor="new-doc-title-input">Document title</label>
                        <input type="text" className="form-control" placeholder="Title ..." onChange={(ev) => {this.documentTitle = ev.target.value;}} id="login-email-input" />
                        <hr />
                        <div className="btn-group" data-toggle="buttons" >
                          <label className="btn btn-primary active">
                            <input type="radio" name="options" id="option1" onClick={() => this.documentCreationTemplate = 'empty'} /> Empty
                          </label>
                          <label className="btn btn-primary">
                            <input type="radio" name="options" id="option2" onClick={() => this.documentCreationTemplate = 'from-example'} /> From example
                          </label>
                        </div>
                    </div>
                    <hr />
                    <button type="submit" className="btn btn-primary">Create</button>
                    <button onClick={() => $('#' + this.id).modal('toggle')} type="button" className="btn btn-primary">Cancel</button>
                </form>);
    }

    renderNewFolderModalBody(){
        return (<form className="form" onSubmit={this.onSubmit.bind(this)}>
                     <div className={"form-group"}>
                        <label className="control-label" htmlFor="new-folder-title-input">Folder name</label>
                        <input type="text" className="form-control" placeholder="Name ..." onChange={(ev) => {this.FolderName = ev.target.value;}} id="new-folder-name-input" />
                        <hr />
                    </div>
                    <hr />
                    <button type="submit" className="btn btn-primary">Create</button>
                    <button onClick={() => $('#' + this.id).modal('toggle')} type="button" className="btn btn-primary">Cancel</button>
                </form>);
    }

    renderWhere(){
        return <p> {JSON.stringify(this.props.folder)} </p>;
    }

    render() {
        return ( <div className="modal fade" id={this.id} data-backdrop={"false"}>
            <div className="modal-dialog">
              <div className="modal-content">
                <div className="modal-header">
                  <h4 className="modal-title">{this.formTitle}</h4>
                  <button type="button" className="close" data-dismiss="modal">&times;</button>
                </div>
                <div className="modal-body">
                    {this.type == 'folder' ?
                        this.renderNewFolderModalBody()
                        :
                        this.renderNewDocModalBody()}
                </div>
              </div>
            </div>
          </div>);
    }
}