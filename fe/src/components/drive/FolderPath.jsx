/* @flow */
import React from 'react';

import {Link } from 'react-router-dom';

import {Node} from './DrivePage.jsx';


type Props = {
    pathToCurrFolder: array<Node>,
    onCheckoutFolder: (string) => void,
}

type State = {

}

export default class FolderPath extends React.Component<Props, State> {
    render() {
        let components = [];
        for(let f of this.props.pathToCurrFolder){
            components.push(
                <a style={{fontSize: '20px'}}onClick={() => this.props.onCheckoutFolder(f.gid)}>{f.name}</a>
            )
        }
        return (
        <nav aria-label="breadcrumb">
        <ol className="breadcrumb">
            <li className="breadcrumb-item">My drive</li>
            {components.map(e => 
                <li className="breadcrumb-item">{e}</li>
            )}
        </ol>
      </nav>
      )

    }
}