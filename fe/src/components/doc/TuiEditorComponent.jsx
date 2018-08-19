/* @flow */
import React from 'react';

import Editor from 'tui-editor';

import 'codemirror/lib/codemirror.css' // codemirror
import 'tui-editor/dist/tui-editor.css'; // editor ui
import 'tui-editor/dist/tui-editor-contents.css'; // editor content
import 'highlight.js/styles/github.css'; // code block highlight

import { beforeSendRequest } from './../../util.jsx';

const api = {
    fetchDocumentByGid: (docGid) => {
        return $.ajax({
            url: '/api/doc/' + docGid,
            type: "GET",
            async: false,
            beforeSend: beforeSendRequest,
        });
    },

    persistDocContentAsync(docGid: string, content: string) {
        let data = {
            content: content,
        };
        return $.ajax({
            url: '/api/doc/content/' + docGid,
            type: "PUT",
            data: JSON.stringify(data),
            async: true,
            beforeSend: beforeSendRequest,
            headers: {"Content-Type": "application/json"},
        });
    }
}

type Props = {
  docGid: string,
  mode: string, // { 'edit' | 'view' },
  requestPersisContentRev: number,
  notifyIsSynchronized: (boolean) => void,
  titleFetched: (string) => void,
}

type State = {
  synchronized: boolean,
  gid: string,
  title: string,
}

export default class TuiEditorComponent extends React.Component<Props, State> {
    // http://nhnent.github.io/tui.editor/api/latest/
    editor: any;
    requestedPersisting: boolean;
    content: string;

    constructor(props: Props){
        super(props);

        this.state = {
            synchronized: false,
            title: null,
        }
        this.editor = null;
        this.content = null;
        this.requestedPersisting = false;
    }

    componentDidMount(){
        api.fetchDocumentByGid(this.props.docGid)
            .done((data, status, resp) => {
                if(this.editor){
                    this.editor.value = data.markdownContent;
                }
                this.setState({
                    title: data.title,
                    synchronized: true,
                });
                this.content = data.markdownContent;
                this.props.titleFetched(data.title);
            });
        this.createTuiEditor();
    }

    componentDidUpdate(prevProps: Props, prevState: State){
        if(prevProps.requestPersisContentRev != this.props.requestPersisContentRev){
            let content = this.editor.getValue();
            api.persistDocContentAsync(this.props.docGid, content)
                .done((data, status, resp) => {
                    this.props.notifyIsSynchronized(true);
                });
        }
        if(prevProps.mode !== this.props.mode){
            $('#tui-editor-id').empty();
            this.createTuiEditor();
        }
        if(prevState.gid == null && !!this.state.gid){
            $('#tui-editor-id').empty();
            this.createTuiEditor();
        }

    }

    requestPersistingDocument(){
        if(this.requestedPersisting == false){
            this.props.notifyIsSynchronized(false);
        }
        this.requestedPersisting = true;
    }


    createTuiEditor(){
        this.editor = Editor.factory({
            el: document.querySelector('#tui-editor-id'),
            viewer: this.props.mode == 'view',
            height: '500px',
            initialValue: this.content,
            initialEditType: 'wysiwyg',
        });

        var keyDownFunction = (a) => {
            //this.requestPersistingDocument();
        }

        this.editor.eventManager.events.get('keyMap').unshift(keyDownFunction.bind(this));
    }

    render(){
      return(
        <div>
          <div id="tui-editor-id" />
        </div>
      );
    }
}