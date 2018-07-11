const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const HtmlWebpackPluginConfig = new HtmlWebpackPlugin({
  template: './src/index.html',
  filename: 'index.html',
  inject: 'body'
})
const fs = require('fs')

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
    port: 3000,
    historyApiFallback: true, 
    contentBase: './',
    proxy: { // proxy URLs to backend development server
      '/api/**': {
        target: 'http://localhost:8080'
      },
    },
  },
  plugins: [
    HtmlWebpackPluginConfig,
    ]
}

module.exports = webpackConfig