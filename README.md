# Web UI

For the official build, it will automatically compile it when going `./gradlew build`.

For development, you can run `npm run watch` and then run the Java application. 
It will automatically compile when any file in the webui changes.

# Install and configure

- Copy the files from sample_config and set your own values
- Put those files in the working directory of the application
- Run the application

# Local Authentication for Development

For local testing, you can use a simple local authentication mechanism. This allows you to log in with a predefined user ID without needing to set up OAuth2 providers.

In `application.properties` put:
```
# Local authentication (for development only)
app.auth.local.enabled: true
app.auth.local.userId: local-user
```

You can change the local user ID by modifying the `app.auth.local.userId` property.

To disable local authentication and use OAuth2 instead, set `app.auth.local.enabled: false`.

**Note:** Local authentication should only be used for development and testing, not in production.
