# Moderator / Admin front-end for Chariot Quizzo

## Setup Instructions

```
npm install -g yarn @angular/cli
yarn
```

## Configuration Instructions

### Assumptions

* Dev server is hosted on machine specified in proxy-conf.json 
* Proxy should expose /graphql to dev server
* Running server with yarn start will fire up the server and proxy all requests aimed at /graphql to the graphql port on the app server

### Docker commands

_Graciously lifted from https://mherman.org/blog/dockerizing-an-angular-app/ with help from Stack Oveflow_

#### Build 

```
docker build -t quizzo-angular-client-dev .
```

#### Run in Docker

```
docker run -d -v ${PWD}:/usr/src/app -v /usr/src/app/node_modules -p 4200:4200 --name quizzo-angular-client-dev-container quizzo-angular-client-dev
```

#### Test in Docker

```
docker exec --privileged -it quizzo-angular-client-dev-container  ng test --watch=false
```

### To Build for QA

```
ng build --configuration=qa
```

### QA URL

```
https://quizzo.rimpledev.com
```
