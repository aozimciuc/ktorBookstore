## Sample Ktor application

This is a sample Ktor application that demonstrates how to use the Ktor framework to build a simple web application.
Routing, templating, and authentication are covered in this sample.
Data is stored in a MongoDB database.

### Running the application
```shell
./gradlew run
```

### Testing the application
```shell
./gradlew test
```

### Building the application
```shell
./gradlew clean build
```

### Command line arguments
The application accepts the following command line arguments:
- `-Dktor.deplyment.port` The port on which the application will run. Default is `8080`.
- `-Dmongodb.uri` The URI of the MongoDB database. Default is `mongodb://localhost:27017`.

### Endpoints
- `/html` Home page
- `/html/books` List of books (requires authentication). Books are hardcoded and stored in a MongoDB database every time the application starts.

### Authentication
The application uses a simple form-based authentication mechanism. 
Username and password are hardcoded in the application. The application uses a session to store the authentication state.
Login is `admin` and password is `admin`.
Session is stored in a cookie only. No CSRF protection is implemented. No password hashing is implemented.

