/* @flow */
import React from 'react';

import TuiEditorComponent from './TuiEditorComponent.jsx';
import {Link } from 'react-router-dom';

import { beforeSendRequest } from './../../util.jsx';


const api = {
    requestDownloadAsMarkdown: (docGid: string) => {
        return $.ajax({
            url: '/api/doc/download/' + docGid + '?download_as=raw_markdown',
            type: "GET",
            async: false,
            beforeSend: beforeSendRequest,
        });
    }
}

type Props = {
    docGid: string,
}

type State = {
    mode: string,
    title: string,
    synchronized: boolean,
    requestPersisContentRev: number,
}

export default class DocumentEditor extends React.Component<Props, State> {

    constructor(props: Props){
        super(props);
        this.state = {
            mode: 'view',
            title: null,
            synchronized: true,
            requestPersisContentRev: 0
        }
    }

    handleTitleFetched(title: string){
        this.setState({
            title: title
        });
    }

    handleNotifiedIsSynchronized(isSync: boolean){
        this.setState({
            synchronized: isSync
        });
    }

    render() {
        return (
            <div className="container h-100">
                <div className="panel panel-default">
                    <div className="panel-heading">
                        <h3 className="pull-left" contentEditable="true">{this.state.title}</h3>
                        <button className="btn btn-default pull-right"
                            onClick={() => {
                            api.requestDownloadAsMarkdown(this.props.docGid);
                        }}>Download as markdown</button>
                        {this.state.synchronized ?
                            (this.state.mode == 'view' ? <p></p> : <p className="pull-left" ></p>)
                            :
                            <p className="pull-left" >Synchronizing</p>}
                        <button className="btn btn-default pull-right"
                            onClick={() => {
                            let m = this.state.mode == 'view' ? "edit" : "view";
                            this.setState({mode: m});
                        }}> {this.state.mode == 'view' ? "Edit mode" : "View mode"}</button>
                        <button className="btn btn-default pull-right"
                            onClick={() => {
                                let rev = this.state.requestPersisContentRev + 1;
                                this.setState({requestPersisContentRev: rev});
                        }}>Save</button>
                        <div className="clearfix"></div>
                    </div>
                        <div className="panel-body">
                            <TuiEditorComponent
                                titleFetched={this.handleTitleFetched.bind(this)}
                                docGid={this.props.docGid}
                                mode={this.state.mode} 
                                notifyIsSynchronized={this.handleNotifiedIsSynchronized.bind(this)}
                                requestPersisContentRev={this.state.requestPersisContentRev} />
                    </div>
                </div>
            </div>
        );
   }
}