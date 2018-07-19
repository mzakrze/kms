const merge = require('webpack-merge');
const UglifyJSPlugin = require('uglifyjs-webpack-plugin');
const common = require('./webpack.config.js');

// TODO - wczytaj wersję z VERSION - wstaw jakoś do api

const appVersion = "this is app version";

// TODO - sprawdź flow

module.exports = merge(common, {
    plugins: [
        new UglifyJSPlugin(),
        new webpack.DefinePlugin({
            'APP_VERSION': appVersion
        }),
    ]
});