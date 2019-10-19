## Mini Brain Dump (there never was a decent readme, as it was a demo app)

This repository was meant for a set of demos and code samples for several presentations in 2019. We had the goal of creating a quiz game server and clients, and demonstrating it at our booth. As you might imagine, it's tough to get everything working properly, and so it never really launched. However, it was a good proving ground for API experimentation.

The instructions below will boot up the Spring GraphQL server and build the client apps in a Docker container, complete with an Apache facade for hosting it.  The old (and incomplete) NodeJS server is still available to review, and show some example calls (`servers/deprecated-esxpress-server`) and wiring.  Ignore the fact it has Redux - I was planning on somehow getting shared state going but Redux was a really bad idea on my part). Was hoping to somehow store the state using Redis but then fell back to Java and multithreading. Ahhhhh.)

Ken Rimple
Fall 2019


### Setup (for developers)
* Install Docker and start it up
* Install OpenJDK 11 or higher (if you want to debug Spring Boot server directly on your machine)
* Add `db` and `quizzo` aliases in your `/etc/hosts` (or `Windows\System32\Drivers\etc\hosts` file on Windoze) with
  ```
  127.0.0.1           localhost  db  quizzo
  ```
* Install NodeJS LTS version (10.x or current is 11.x, either should work fine)
* Install Yarn build tool for Node (`npm install -g yarn`)
* Install Angular CLI `npm install -g @angular/cli`
* Git clone the repo with `git clone git@github.com:chariotsolutions/graphql-quizzo.git`

### Execution (for developers)

#### To run the servers (except JS client ones, that’s below)
```
cd <root of project>
docker-compose up -d    (detaches after start)
```

#### To stop the servers
```
docker-compose down
```

Note: This runs the Java (Spring Boot) one, as well as the PostgreSQL one and the flyway migrations to set it up.

#### To run the clients (only the Angular one is semi-working atm):

##### Prereqs

* Have Node 10+ installed (I’m on 11.0.2)
* Install yarn with `npm install -g yarn`
* Build/start with `yarn start`
* Browse `http://localhost:4200` and you’ll get the UI

### To Debug using a GraphQL client

* Access `http://localhost:8080/graphql` with any GraphQL query tool - such as `Altair` or `GraphiQL`  standalone clients
* You must establish a JWT token. To do so, use this query:
  ```text
  # Query in query tool:
 
  mutation login($credentials: SignInCredentials){
    login(credentials: $credentials) 
  }

  # Variables in variables pane of query tool
  {
    "credentials": {
      "userName": "krimple",
      "password": "admin123"
    }
  }
  ```
* Now any other GraphQL query will work, provided you paste the JWT token as a header like this:
  ```
  Header name:  Authentication
  Header value:  Bearer <jwt token here>  (no angle brackets of course)
  ```
  GraphQL query tools have a "headers" tab where you can set this
  
* Example queries:

  ```
  query {
    gameStatus {
        gameMode
     currentQuestion {
       text
       options {
         key
         label
       }
     }
   }
  } 
  ```
  Using the Authentication token as mentioned above this should bring back the game status. See the Moderation Service in 
  the Angular client for more queries at `src/app/services/moderation.service.ts` in the `graphql-quizzo-client-angular` app 
  in the `apps` folder for more queries to experiment with. 
* Use the Altair query tool for best results.
  
#### To run the Java server app standalone (for debugging)
* To run the Java server directly, just start everything but the java server with `docker-compose down` (shuts down the other instance)
* Start up with `docker-compose up db flyway` (Only starts the database and migrations tool)
* Then cd to the `servers/quizzo-graphql-java-server` directory
* And do `./gradlew bootRun` (Manually boots the Java build tool Gradle, which launches the server.  Now you can hit CTRL-C and do it again to restart. Also if you run in IntelliJ you can debug it if you really want to)

