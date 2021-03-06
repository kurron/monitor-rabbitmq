#Overview
This is one project in a suite of cooperating projects that are used to simulate a real world application.  This
simulation is used to evaluate different application monitoring services as well as distributed logging solutions.

This application plays the role of a service that relies on RabbitMQ for message processing.  The messages are simply 
consumed and logged.  It is expected that the upstream services will produce the messages to be consumed.

#Prerequisites

* [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) installed and working
* [Git](https://git-scm.com/) installed and working
* [RabbitMQ](https://www.rabbitmq.com/) installed and working
* Development and testing was done on [Ubuntu Linux](http://www.ubuntu.com/) but other JVM compatible operating systems should work as well

#Building
The project uses [Gradle](http://gradle.org/) to manage builds and will install itself upon first build.  To initiate a build,
simply issue `./gradlew`.  After a moment or two, you should have a fully assembled application in `build/libs`.

#Installation
The application runs standalone and can be conveniently launched via `bin/launch.sh`.  The application should be listening at port 8500
for requests.

#Tips and Tricks

##Health Check
The application can respond to health checks and report back whether or not connectivity is available to its required services.  Run
`bin/check-health.sh` to check on the application's status. This can be used to verify the configuration settings for RabbitMQ and PostgreSQL are
correct.

##Use Docker
There is a [companion project](https://github.com/kurron/docker-monitor-rabbitmq) that wraps the application into a Docker container and is
probably a more convenient way to launch the application.

##Configuration
The application's configuration settings are controlled via `src/main/resources/config/application.yml`.  You can change the values and rebuild
the application, if desired.  A simpler way is to override those settings at application launch time.  For example:

```bash
$JAVA_HOME/bin/java -jar build/libs/monitor-postgresql-0.0.0-RELEASE.jar --server.port=1234 --spring.rabbitmq.host=192.168.1.10
```

#Troubleshooting

#License and Credits
This project is licensed under the [Apache License Version 2.0, January 2004](http://www.apache.org/licenses/).

#List of Changes
