/* @flow */
import React from 'react';

import {Link } from 'react-router-dom';

import { DriveFolder } from './DrivePage.jsx';

import './../../../vendor/bootstrap-treeview.min.js';
import './../../../vendor/bootstrap-treeview.min.css';

type Props = {
    driveRoot: DriveFolder,
    onCheckoutFolder: (string) => void,
}

type State = {

}

export default class TreeExplorer extends React.Component<Props, State> {
    constructor(props){
        super(props);
    }

    

    componentDidUpdate(prevProps, prevState) {
        if(this.props.driveRoot != null){
            this.reloadTreeView(this.props.driveRoot);
        }
    }

    reloadTreeView(rootNode){
        console.log('reloadinTreeView')
        if(!rootNode) {
            console.log('node is null')
            return;
        }
        let reloaded = this.doReloadTree(rootNode);
        console.log('reloaded:', JSON.stringify(reloaded))
        $('#treeview-container').treeview({
            data: reloaded.nodes,
            onNodeSelected: (event, data) => {
                this.props.onCheckoutFolder(data.gid);
            },
        });
    }

    doReloadTree(node: Node){
        if(node.type == 'document'){
            return {
                text: node.name,
                gid: node.gid,
                icon: "glyphicon glyphicon-book",
                selectable: false,
                }
        } else if(node.type == 'folder'){
            let children = [];
            let nodeChildren = node.documents.concat(node.folders).concat(node.blobs);
            for(let n of nodeChildren){
                children.push(this.doReloadTree(n));
            }
            return {
                text: node.name,
                gid: node.gid,
                icon: "glyphicon glyphicon-folder-open",
                selectable: true,
                nodes: children,
            }
        } // TODO - else blob
    }

    render() {
        return (<div className="col-md-4 sidebar pre-scrollable">
            {this.props.driveRoot == null ?
                <p>Loading ... </p>
                :
                <div id="treeview-container" />}
            </div>)
    }
}

// https://github.com/jonmiles/bootstrap-treeview