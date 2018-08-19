/* @flow */
import React from 'react';

import {Link } from 'react-router-dom';

import {Node} from './DrivePage.jsx';


type Props = {
    onFilteringFunctionChange: ((any) => boolean) => void,
}

type State = {
    onlyMine: boolean
}

export default class NodeFilters extends React.Component<Props, State> {

    constructor(props){
        super(props)
        this.state = {
            onlyMine: false
        }
    }

    reloadFilteringFunctionAndDoSetState(newState){
        let filters = [];
        if(newState.onlyMine){
            filters.push((node) => true); // TODO
        }
        let filteringFunction = (node) => {
            for(let f of filters){
                if(f(node) == false){
                    return false;
                }
            }
            return true;
        }
        console.log('reloadFilteringFunction, calling props.onFitlerginFunctionChange');
        this.props.onFilteringFunctionChange(filteringFunction);
        this.setState(newState);
    }

    handleChange(option, value){
        let newState = Object.assign({}, this.state);
        if(option == 'onlyMine'){
            newState['onlyMine'] = value
        }
        this.reloadFilteringFunctionAndDoSetState(newState)
    }

    render() {
        let b1,b2;
        if(this.state.b1){
            b1 = <button type="button" onClick={() => this.setState({b1: false})} className="btn btn-primary active" data-color="primary"><i className="state-icon glyphicon glyphicon-check"></i>&nbsp;Hide folders</button>
        } else {
             b1 = <button type="button" onClick={() => this.setState({b1: true})} className="btn btn-default" data-color="primary"><i className="state-icon glyphicon glyphicon-unchecked"></i>&nbsp;Hide folders</button>
        }

        if(this.state.b2){
            b2 = <button type="button" onClick={() => this.setState({b2: false})} className="btn btn-primary active" data-color="primary"><i className="state-icon glyphicon glyphicon-check"></i>&nbsp;Hide documents</button>
        } else {
             b2 = <button type="button" onClick={() => this.setState({b2: true})} className="btn btn-default" data-color="primary"><i className="state-icon glyphicon glyphicon-unchecked"></i>&nbsp;Hide documents</button>
        }

        let s1 = (<input type="text" className="form-control" placeholder="Search for snippets"/>)
        let s2 = (<div className="input-group-btn">
                    <div className="btn-group" role="group">
                        <div className="dropdown dropdown-lg">
                            <button type="button" className="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><span className="caret"></span></button>
                            <div className="dropdown-menu dropdown-menu-right" role="menu">
                                <form className="form-horizontal" role="form">
                                <div className="form-group">
                                    <label for="filter">Filter by</label>
                                    <select className="form-control">
                                        <option value="0" selected="">All Snippets</option>
                                        <option value="1">Featured</option>
                                        <option value="2">Most popular</option>
                                        <option value="3">Top rated</option>
                                        <option value="4">Most commented</option>
                                    </select>
                                </div>
                                <div className="form-group">
                                    <label for="contain">Author</label>
                                    <input className="form-control" type="text"/>
                                </div>
                                <div className="form-group">
                                    <label for="contain1">Has tag</label>
                                    <input className="form-control" type="text"/>
                                </div>
                                <div className="form-group">
                                    <label for="contain">Contains the words</label>
                                    <input className="form-control" type="text"/>
                                </div>
                                <button type="submit" className="btn btn-primary"><span className="glyphicon glyphicon-search" aria-hidden="true"></span></button>
                                </form>
                            </div>
                        </div>
                        <button type="button" className="btn btn-primary"><span className="glyphicon glyphicon-search" aria-hidden="true"></span></button>
                    </div>
                </div>);


            return (<div className="col-sm-6">
                <div className="input-group ">
                	<input type="text" className="form-control" placeholder="Search for..."/>
                      <span className="input-group-btn">
                        <button className="btn btn-search" type="button"><i className="fa fa-search fa-fw"></i> Search</button>
                      </span>
                </div>
                <br/>
            <span>{b1}</span>
            <span>{b2}</span>

            </div>)
    }
}