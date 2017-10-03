# Tasker, a task runner
[![Build Status](https://img.shields.io/travis/opsxcq/tasker.svg?style=plastic)](https://travis-ci.org/opsxcq/tasker)
[![Coverage](https://img.shields.io/codecov/c/github/opsxcq/tasker.svg?style=plastic)](https://codecov.io/gh/opsxcq/tasker/branch/master)
[![Docker Pulls](https://img.shields.io/docker/pulls/strm/tasker.svg?style=plastic)](https://hub.docker.com/r/strm/tasker/)

Tasker is a task runner, plain and simple. Define tasks, schedule them, and that's it !

Give it a try ! Just create the following `docker-compose.yml` file

```yml
version: "2"

services:
    tasker:
        image: strm/tasker
        volumes:
            - "/var/run/docker.sock:/var/run/docker.sock"
        environment:
             configuration: |
                 schedule:
                     - every: minute
                       task: hello
                 tasks:
                     docker:
                         - name: hello
                           image: debian:jessie
                           script:
                               - echo Hello world from Tasker
```

And that's it, now you have a task, running inside docker every minute, using the `debian:jessie` image (and bash inside this image), to run the script defined inside `script` element.

# Configuration

Configuration is segmented, the concept is, you first configure your tasks, and then you configure what will activate them. Then just map your configuration file to `/application.yml` in the container, or if you run it outside docker, map it to a file called `application.yml` in the same folder that you are running the application from.

Here is an example with several constructions:

```yml
config:
    global-environment:
        - http_proxy="http://yourproxy:8080"

schedules:
  - every: 5 minutes
    task: test
  - every: Saturday
    task: backup
  - cron: 0 0 13 * * *
    task: test

tasks:
  docker:
    - name: backup
      image: debian:jessie
      script-strict: true
      script:
        - echo Running the backup
      environment:
        - TEST=environment variable value    
      volumes:
        - someVolume:/whatWouldBeMappedInTheContainer
```

# Tasks

At this moment there is only one kind of task, `docker` tasks. Those tasks ran inside docker containers, plain and simple as that. More task executers will be implemented in a near future, watch this repository to be sure you get the updates !

# Docker tasks

Configurations

 * `image` - the image in the very same format as it is expressed for docker, in `repo/image:tag` for images in the default repository, of `server/repo/image:tag` for images residing somewhere else.
 * `environment` - Define environment varibales to be used in the task execution, they follow the same pattern that you use in `docker-compose.yml` file, a list of `variable=value`.
 * `volumes` - An array, just like you map `volumes` in your `docker-compose.yml`.
 * `ports` - An array, just like you map `ports` in your `docker-compose.yml`.

 * `network` - If you wish to attach or use any other network that you have. If the desired network doesn't exist, it will be created.

Configurations regarding container life cicle:

 * `keepContainerAfterExecution` - Keep container after it executes, won't delete it, *WARNING* it can leave a lot of trash. USE IT FOR DEBUGING ONLY !
 * `always-pull` - A boolean (true/false) property, when it's true, Tasker will pull a newer image version updating it if there is a newer one available.
 * `reuse-container` - If the container doesn't exist, create it, if there is already a container with that name, reuse the container.
 
## Entrypoint and Arguments
 
You can pass parameters to your task and set the entrypoint of the image as you pass in docker command line. Example:

```yml
tasks:
  docker:
    - name: hello
      image: debian:jessie
      entrypoint: /bin/bash
      arguments: -c,echo Aloha world
```

The example above execute `/bin/bash` passing as argument `-c echo Aloha world`. Comma can be used to separate arguments in an inline array. It uses a YML structure, so you can rewrite it as you wish. For example, you can declare the parameter array as a list:

```yml
tasks:
  docker:
    - name: hello
      image: debian:jessie
      entrypoint: /bin/bash
      arguments:
        - -c
        - echo green bar
```

Both examples will produce the very same result.


## Script directive

`script` directive is a facilitator to setting the `entrypoint` to `/bin/sh` and pass as arguments as a list of commands. It's syntax is just a list of commands like:

```yml
    - name: helloScriptStrict
      image: debian:jessie
      script:
        - echo Hello from Docker
```

Commands will be executed sequentially, no matter the result. To enable the strict mode, where the next command will **only** be executed if the previous command was successful (exit status 0), you can use the `script-strict` property. For example

```yml
    - name: helloScriptStrict
      image: debian:jessie
      script-strict: true
      script:
        - echo This is the first line
        - echo This second line will only be executed if the above command properly runs
```

## Docker images and pulls

Tasker uses the concept of lazy loading, in other words, it will pull your image when it will run at the very first time. You can use `always-pull=true` in your docker task definition if you want to keep updating the local image everytime your task runs.

## Other considerations about docker tasks

 * Tasker can run in a swarm or single instance, but it will schedule tasks to run in the current node.
 * You can set `DOCKER_HOST` environment variable to this image, to make it run the scheduled tasks in a remote docker host.
 * `secrets` aren't available at the moment
 
# Scheduler

Scheduler configuration is an important aspect in Tasker. The scheduler is responsible to what it's name suggests, **schedule** tasks to be executed. It is defined in `schedule` section of the configuration file. Example

```yml
schedule:
  - every: 10 minutes
    task: test
  - cron: 00 00 11 * * *
    task: test
```

`schedule` is an array of schedules. There are two ways to schedule an task. One is by giving it an interval using `every` property or `cron`. You can name a schedule with the `name` attribute, but it isn't mandatory, you can have anonymous schedules if you prefer.


## Every

Every is a representation for a more simple understanding, like `every: minute`, is far more readable, but won't give you the same power as the `cron` directive. The `every` parameters can be:

 * Minute - Every minute inside **one hour** period. For example, `every: 10 minutes`, will result in 6 executions, at 00, 10, 20, 30, 40 and 50. Every minute scheduled task will start it's first execution at the first minute of the hour (00).
 * Hour - Every hour inside **one day** period. For example, `every: 6 hours` will result in 4 executions, at 00, 06, 12 and 18. Every hour scheduled task will start it's execution at the very first hour of the day (00 - midnight).
 * Weekday - Every weekday as in the list (Monday, Tuesday, Wednesday, Thursday, Friday, Saturday and Sunday). The task will be executed at the first minute of the first hour of the respective day. For example `every: monday`.
  * Year - Will be executed in the very first second, of the first minute, of the first day, of the first month of the year.
 
## Cron
 
 `cron` is another way to set the expected trigger to your scheduled task. It obey the standard `unix` cron format, but with the exception that it has one more field. Is a field representing `seconds`, and is the first field, from `00` to `59` are the accepted values. 
 
# `config` aka Configuration of the Configuration section
 
 Configuration is the place where additional components of Tasker can be configured.
 
  * `global-environment` - A list of environment variables that will be assigned to all tasks. Note that this variables are overwritten by `environment` variables section of each task in case of a conflit.
  
# Notifications

Is possible to configure Tasker to notify other components when tasks finish. Notifications are triggered *after* the task is completed. Notifications are defined in the `notify` section of the configuration file, separated for each sub-type of notification, for example:

```
notify:
  email:
    - name: notifyEmailTest
      task: helloNotifyEmail
      when: always
      server: mail.gmail.com
      subject: Email subject
      sender: myemail@gmail.com
```

Notifications need to be linked to a task, so a valid `task` should be informed.

 * `name` - A name to identify the notification.
 * `task` - Which task will trigger this notification
 * `when` - When the trigger will run. There are three scenarios, 
   * `always` - Is the *default* behavior, it will trigger the notification every time that the task runs.
   * `on-success` - Will trigger the notification only if the task is successful.
   * `on-error` - Will trigger the notification only if the task isn't successful.



## E-mail notifications
  
In Tasker, e-mail notifications are a sub-category of notifications. E-mail notifications are configured with `email:` field, inside the `notify` section. Bellow a simple example configuration using Gmail:

```
notify:
  email:
    - name: notifyEmailTest
      task: helloNotifyEmail
      server: mail.gmail.com
      subject: Email subject
      sender: myemail@gmail.com
```

A complete example bellow

```
notify:
  email:
    - name: notifyEmailTest
      task: helloNotifyEmail
      server: mail.gmail.com
      port: 9925
      subject: Email subject
      sender: myemail@gmail.com
```

Configuration parameters regarding the connection to the e-mail server:

 * `server` - Remote server where the email server is running. Default `localhost`.
 * `port` - Remote port where the server is listening to. Default `587`.
 * `username` - Username that will be used in the authentication with the server.
 * `password` - Password for the given username.
 * `protocol` - Protocol to use for e-mail transfer. Default `smtp`.
 * `starttls` - Use TLS or not. Default is `true`.
 * `debug` - Enable a more verbose output of the e-mail transfer process, use it only for troubleshooting. Default `false`.
 * `validate-server-on-configuration-load` - Validate the e-mail configuration when Tasker startup, and abort it if isn't possible to connect to the e-mail server. Default `true`.
 
Configuration parameters regarding the e-mail to be sent:

 * `subject` - The subject of the e-mail.
 * `sender` - The `FROM` field in the e-mail.
 * `recipients` - A list of e-mail addresses that will receive the e-mail.
 * `content` - The content of the e-mail.
  
  
# Logging

If you want to suppress some of the logging information, you can add the following to your configuration

```yaml
logging:
  level:
    ROOT: WARN
    org.springframework.web: WARN
```