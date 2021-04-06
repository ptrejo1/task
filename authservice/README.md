# authservice
The userservice is backed by a postgres db, and the connection parameters can be found in the .env file.
The .env file is only included to make running the service easier, sensitive env values should not be
handled this way and instead be added through CI/CD or some secrets api, such as Kubernetes secrets,
when it comes time to deploy the service.

For this task I choose to use a self-signed certificate for serving HTTPS requests to provide a 
secure and encrypted connection. In a production setting, using something like nginx as a reverse 
proxy with CA signed certificates would be the way to go.

### Running authservice
The recommended way to run the service is through IntelliJ. The service comes with already setup run
configurations that are available to run in IntelliJ in the top right corner. 

Opening the service in IntelliJ should show a prompt to set up the gradle project. Once that is done, 
run the task `autheservice [generateJks]` that's located in the run configs dropdown to create a 
certificate. Then you should be able to run the service with `Run Local Server`.
The server will be listening at https://127.0.0.1:8443 with routes
- POST https://127.0.0.1:8443/api/v1/auth/signup
- POST https://127.0.0.1:8443/api/v1/auth/login

body for both being:
```
{
	"username": "foo",
	"password": "password1"
}
```

There is also a run configuration to run the tests named `Run Tests`, a running postgres server 
is necessary for these as well.
