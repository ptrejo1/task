# userservice
The userservice is backed by a postgres db, and the connection parameters can be found in the .env file.
The .env file is only included to make running the service easier, sensitive env values should not be 
handled this way and instead be added through CI/CD or some secrets api, such as Kubernetes secrets, 
when it comes time to deploy the service.

### Running userservice
The recommended way to run the service is through IntelliJ. The service comes with already setup run 
configurations that are available to run in IntelliJ in the top right corner. 

Opening the service in IntelliJ should show a prompt to Load Gradle Project.
Once that is done, you should be able to run the service with `Run Local Server`.
The server wil be listening at http://127.0.0.1:8000 with the routes
- GET http://127.0.0.1:8000/api/v1/user-info
- POST http://127.0.0.1:8000/api/v1/user-info, a convenience route for inserting data to retrieve, with body
```
{
    "email": "em",
    "firstName": "fn",
    "lastName": "ln"
}
```

There is also a run configuration to run the tests named `Run Tests`, a running postgres server
is necessary for these as well.
