FROM node:10-alpine

RUN npm install --global nodemon babel-cli redux-devtools yarn concurrently

# RUN npm uninstall sqlite3

# RUN npm install sqlite3
RUN mkdir -p /home/node/app/keys
run mkdir -p /home/node/app/node_modules
RUN chown -R node /home/node/app/keys
RUN chown -R node /home/node/app/node_modules
RUN chown -R node /home/node/app

USER node
WORKDIR /home/node/app

COPY ./package.json ./
COPY ./tsconfig.json ./
COPY ./tslint.json ./
COPY ./yarn.lock ./
COPY ./.babelrc ./
RUN pwd
COPY ./keys/* ./keys/

RUN yarn




