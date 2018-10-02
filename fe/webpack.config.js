const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const HtmlWebpackPluginConfig = new HtmlWebpackPlugin({
  template: './src/index.html',
  filename: 'index.html',
  inject: 'body'
})
const fs = require('fs');
const APP_VERSION = fs.readFileSync("VERSION", { encoding: 'utf8' });
console.log('app version is: ' + APP_VERSION)

const DefinePlugin = new webpack.DefinePlugin({
  CONFIG_APP_VERSION: JSON.stringify(APP_VERSION + "-dev"),
});

const { execSync } = require('child_process');
let hostIp = ('' + execSync("ip r | awk '/default/{print $3}'")).trim();

 // node@8.0.0
 // npm@6.1.0

var webpackConfig = {
  entry: './src/index.jsx',
  output: {
    path: '/dist',
    filename: 'bundle.js'
  },
  devtool: 'source-map',
  module: {
    loaders: [
      { test: /\.(js|jsx)$/, loader: 'babel-loader', exclude: /node_modules/ },
      { test: /\.json$/, loader: 'json-loader'},
      { test: /\.css$/, loader: "style-loader!css-loader?importLoaders=1" },
      { test: /\.(scss)$/, use: [
          { loader: 'style-loader' }, 
          { loader: 'css-loader' }, 
          { loader: 'postcss-loader', options: {
              plugins: function () { // post css plugins, can be exported to postcss.config.js
              return [
                  require('precss'),
                  require('autoprefixer')
                ];
              }
            }}, 
          { loader: 'sass-loader'}
        ]}
    ],
  },
  devServer: {
    host: "0.0.0.0",
    port: 3000,
    historyApiFallback: true, 
    contentBase: './',
  },
  plugins: [
    HtmlWebpackPluginConfig,
    DefinePlugin 
    ]
}

module.exports = webpackConfig