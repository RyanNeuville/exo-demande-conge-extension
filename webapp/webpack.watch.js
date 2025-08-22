const path = require('path');
const {merge} = require('webpack-merge');
const webpackCommonConfig = require('./webpack.prod.js');

// the display name of the war
const app = 'demande-conge-extension-webapp';

// add the server path to your server location path
const exoServerPath = "/opt/exo";

let config = merge(webpackCommonConfig, {
    mode: 'development',
  output: {
    path: path.resolve(`${exoServerPath}/webapps/demande-conge-extension-webapp/`)
  },
  devtool: 'inline-source-map'
});

module.exports = config;