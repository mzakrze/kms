const merge = require('webpack-merge');
const UglifyJSPlugin = require('uglifyjs-webpack-plugin');
const common = require('./webpack.config.js');

// TODO - sprawdź flow

module.exports = merge(common, {
    plugins: [
        new UglifyJSPlugin()
    ]
});