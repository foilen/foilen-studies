# Web UI

For the official build, it will automatically compile it when going `./gradlew build`.

For development, you can run `npm run watch` and then run the Java application.
It will automatically compile when any file in the webui changes.

# Install and configure

- Copy the files from sample_config and set your own values
- Put those files in the working directory of the application
- Run the application

# Authentication

Users log in with their email, either:

- with a password (that they can set in the "Mot de passe" page once logged in)
- with a one-time code that is sent by email (the user is created on the first code request)

Configure the SMTP server with the `spring.mail.*` properties and the sender with `app.mailFrom` (see
`sample_config/application.properties`).

Users that existed before the switch from Microsoft (Azure) have no email. To link one, set its `email` (lowercase) in
its `userDetails` document in MongoDB.

### Local mail server

Run `./mailpit-start.sh` (needs Docker) to start a [Mailpit](https://mailpit.axllent.org/) catch-all and set
`spring.mail.host: localhost` and `spring.mail.port: 1026` in `application.properties`. Read the captured emails (login
codes) at http://localhost:8025/. Stop it with `./mailpit-stop.sh`.
