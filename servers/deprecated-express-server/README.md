# Game server written in GraphQL Yoga


## Installation

yarn install

## Running

yarn start           ## for runtime
yarn dev             ## uses nodemon for live reload of server

CORS headers are emitted.

## Sample query (hit http://localhost:4000 to try it)

```
query {
  getQuizzes {
    id
    name
    questions {
      id
      text
      choices {
        key
        text
        correct
      }
    }
  }
}
```

