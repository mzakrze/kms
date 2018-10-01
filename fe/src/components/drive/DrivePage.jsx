/* @flow */
import React from 'react';

import {Link } from 'react-router-dom';
import DriveManagerHeader from './DriveManagerHeader.jsx';
import FolderPath from './FolderPath.jsx';
import NodeTable from './NodeTable.jsx';
import TreeExplorer from './TreeExplorer.jsx';
import {beforeSendRequest} from './../../util.jsx';

const api = {
    fetchDriveTree: () => {
        return $.ajax({
            url: '/api/drive/tree',
            type: "GET",
            async: false,
            beforeSend: beforeSendRequest,
        });
    }

}

export type DriveBlob = {
    name: string,
    gid: string,
}

export type DriveDocument = {
    name: string,
    gid: string,
    lastViewedTs: string,
    lastEditedTs: string,
}

export type DriveFolder = {
    name: string,
    gid: string,
    documents: array<DriveDocument>,
    folders: array<DriveFolder>,
    blobs: array<DriveBlob>,
}

type Props = {

}

type State = {
    driveRoot: DriveFolder,
    currentFolder: DriveFolder,
    pathToCurrFolder: array<DriveFolder>,
    filteringFunction: (any) => boolean, // DriveBlob | DriveDocument | DriveFolder
}

export default class DrivePage extends React.Component<Props, State> {

    constructor(props){
        super(props);
        this.state = {
            driveRoot: null,
            currentFolder: null,
            pathToCurrFolder: [],
            filteringFunction: () => true
        }
    }

    handleCurrentFolderChange(nodeGid){
        let currentFolder;
        let treeSearchStack = [];
        let searchTree = (node) => {
            treeSearchStack.push(node);
            if(node.gid == nodeGid){
                currentFolder = node;
                return true;
            }
            for(let n of node.folders){
                if(searchTree(n)){
                    return true;
                }
            }
            treeSearchStack.pop();
            return false;
        }
        let success = searchTree(this.state.driveRoot);
        if(success == false) {
            console.error('Tree search failed')
        }
        this.setState({
            currentFolder: currentFolder,
            pathToCurrFolder: treeSearchStack
        })
    }

    handleFilteringFunctionChange(newFunction: any){
        this.setState({
            filteringFunction: newFunction
        })
    }

    componentDidMount(){
        this.reloadDrive();
    }

    reloadDrive(){
        api.fetchDriveTree()
            .done((data, status, resp) => {
                let currentFolderGid = this.state.currentFolder == null ? null : this.state.currentFolder.gid;
                if(currentFolderGid == null){
                    this.setState({
                        driveRoot: data.root,
                        currentFolder: data.root,
                        pathToCurrFolder: [data.root],
                    })
                } else {
                    let currentFolder = null;
                    // znajdź po gid'dzie zapamiętując ścieżkę
                    let path = [];
                    let findByGidStoringPath = (folder, path) => {
                        path.push(folder);
                        if(folder.gid == currentFolderGid) {
                            return true;
                        }
                        for(let f of folder.folders){
                            let success = findByGidStoringPath(f, path);
                            if(success){
                                return true;
                            }
                        }
                        path.pop();
                        return false;
                    }
                    let success = findByGidStoringPath(data.root, path);
                    if(success == false){
                        // folder na którym znajdował się użytkownik już nie istnieje
                        // nowym jest od teraz root
                        path = [data.root];
                        currentFolder = data.root;
                    }
                    this.setState({
                        driveRoot: data.root,
                        currentFolder: currentFolder,
                        pathToCurrFolder: path,
                    })   
                }
            });
    }

    searchByName(node: Node, name: string){
        if(node == null){
            return null;
        }
        if(node.name == name){
            return node;
        }
        if(node.children == null) return null;
        for(let child of node.children){
            let r = this.searchByName(child, name);
            if(r != null){
                return r;
            }
        }
        return null;
    }

    // deprecated
    searchByGid(node: Node, gid: string){
        if(node == null){
            return null;
        }
        if(node.gid == gid){
            return node;
        }
        if(node.children == null) return null;
        for(let child of node.children){
            let r = this.searchByGid(child, gid);
            if(r != null){
                return r;
            }
        }
        return null;
    }

    render() {
        console.log('driveRoot:', this.state.driveRoot)
        let newNodeParentFolder = this.state.currentFolder;
        return (<div>
            <nav className="navbar navbar-default navbar-static-top">
                <div className="container-fluid">
                    <div className="collapse navbar-collapse">
                        <div className="nav navbar-nav navbar-left">
                            <h3>My drive</h3>
                        </div>
                        <ul className="nav navbar-nav navbar-right">
                            <DriveManagerHeader
                                newNodeParentFolder={newNodeParentFolder}
                                requestDriveReload={this.reloadDrive.bind(this)} />
                        </ul>
                    </div>
                </div>
            </nav>
            <div className="container-fluid main-container">
                <TreeExplorer
                    driveRoot={this.state.driveRoot}
                    onCheckoutFolder={this.handleCurrentFolderChange.bind(this)}/>
                <div className="col-md-8 content">
                    <div className="panel panel-default">
                        <div className="panel-heading">
                            <FolderPath
                                pathToCurrFolder={this.state.pathToCurrFolder}
                                onCheckoutFolder={this.handleCurrentFolderChange.bind(this)} />
                        </div>
                        <div className="panel-body">
                            <NodeTable
                                currentFolder={this.state.currentFolder}
                                requestDriveReload={this.reloadDrive.bind(this)}
                                filteringFunction={this.state.filteringFunction} />
                        </div>
                    </div>
                </div>
                </div>
            </div>);
    }
}