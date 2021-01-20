# Cake Manager

A summer intern started on this project but never managed to get it finished. The developer assured us that some functionality is complete, but at the moment, accessing the `/cakes` endpoint
returns a `404` so getting this working should be the first priority.

## Requirements:
 * By accessing the root of the server (`/`) it should be possible to list the cakes currently in the system. This must be presented in an acceptable format for a human to read.
 * It must be possible for a human to add a new cake to the server.
 * By accessing an alternative endpoint (`/cakes`) with an appropriate client it must be possible to download a list of the cakes currently in the system as JSON data.
 * The `/cakes` endpoint must also allow new cakes to be created.

### Comments:
 * We feel like the software stack used by the original developer is quite outdated, it would be good to migrate the entire application to something more modern.
 * Would be good to change the application to implement proper client-server separation via REST API.

### Bonus points:
 * Tests
 * Authentication via OAuth2
 * Continuous Integration via any cloud CI system
 * Containerisation

## Solution

### Change Log
 * Renamed the table and columns defined in the `CakeEntity` class
 * Refactored legacy implementation to use [Spring Boot](https://spring.io/projects/spring-boot)
 * Created a REST controller with endpoints required to make a reasonably functional front-end (see below)

| METHOD   | HTTP URL                           | DESCRIPTION                                                                    |
|:--------:|------------------------------------|--------------------------------------------------------------------------------|
| `GET`    | `http://localhost:8080/cakes`      | Get all cakes                                                                  |
| `GET`    | `http://localhost:8080/cakes/{id}` | Get cake with specified `id`                                                   |
| `POST`   | `http://localhost:8080/cakes`      | Create new cake with details provided in Http Request body                     |
| `PUT`    | `http://localhost:8080/cakes/{id}` | Update cake with specified `id` with details provided in the Http Request body |
| `DELETE` | `http://localhost:8080/cakes/{id}` | Delete cake with specified `id`                                                |

 * Created a new `React` webapp with components for the various operations required
 * Replaced 'missing' image URLs in the gist used to load initial `CakeEntity` instances in the database
    * **Note**: 
      * I copied this to a new [gist](https://gist.githubusercontent.com/NormanLittle/db873d44bfdccb88f511276d6b9d1bfe/raw/25c1f8e58f2050b346eb8df039c722f27ae1b3df/cakes.json) and kept the original format in case handling this was relevant to the technical test.
 * Added integration tests for the REST API operations

### How To Run
 * Start the Spring Boot application
   * `mvn spring-boot:run`
 * Start the React webapp (from project root directory)
   * `cd src/main/webapp/ui`
   * `npm install`
   * `npm start`
    
The application can then be accessed from: `http://localhost:3000/`