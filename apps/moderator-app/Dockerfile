FROM node:11 as devseed

FROM devseed as toolinstaller

RUN npm install -g @angular/cli yarn

FROM toolinstaller as dependencies-installed

RUN mkdir /app

WORKDIR /app

COPY package.json .

COPY yarn.lock .

RUN yarn install

FROM dependencies-installed

COPY . .

RUN yarn build-prod


