FROM node:6

RUN npm install webpack -g

WORKDIR /usr/frontend_app/

COPY package.json .

RUN npm config set registry http://registry.npmjs.org/ && npm install

RUN ["apt-get", "update"]
RUN ["apt-get", "install", "-y", "vim"]

