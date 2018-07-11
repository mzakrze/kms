/* @flow */
import React from 'react';
import ReactDOM from 'react-dom';

type Props = {

}

class App extends React.Component<Props> {

    render() {
        return (<p> Hello World </p>);
    }
}

ReactDOM.render(<App />, document.getElementById('react-root') || document.createElement("div"));