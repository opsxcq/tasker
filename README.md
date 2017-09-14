# Tasker, a task runner

[![build status](http://git.thestorm.com.br:8080/strm/tasker/badges/master/build.svg)](http://git.thestorm.com.br:8080/strm/tasker/commits/master)
[![coverage report](http://git.thestorm.com.br:8080/strm/tasker/badges/master/coverage.svg)](http://git.thestorm.com.br:8080/strm/tasker/commits/master)

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
                      name: testEveryMinute
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

```
config:

notifiers:

schedules:
  - every: 5 minutes
    task: test
  - every: saturday
    task: backup
  - cron: 00 11,16 * * *
    task: test

listeners:

tasks:
  docker:
    - name: backup
      image debian:jessie
      script-strict: true
      script:
        - echo Backing up
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
 * `keepContainerAfterExecution` - Keep container after it executes, won't delete it, *WARNING* it can leave a lot of trash.
 * `volumes` - An array, just like you map `volumes` in your `docker-compose.yml`.
 
## Entrypoint and Arguments
 
You can pass parameters to your task and set the entrypoint of the image as you pass in docker command line. Example:

```
tasks:
  docker:
    - name: hello
      image: debian:jessie
      entrypoint: /bin/bash
      arguments: -c,echo Aloha world
```

The example above execute `/bin/bash` passing as argument `-c echo Aloha world`. Comma can be used to separate arguments in an inline array. It uses a YML structure, so you can rewrite it as you wish. For example, you can declare the parameter array as a list:

```
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

```
    - name: helloScriptStrict
      image: debian:jessie
      script:
        - echo Hello from Docker
```

Commands will be executed sequentially, no matter the result. To enable the strict mode, where the next command will **only** be executed if the previous command was successful (exit status 0), you can use the `script-strict` property. For example

```
    - name: helloScriptStrict
      image: debian:jessie
      script-strict: true
      script:
        - echo This is the first line
        - echo This second line will only be executed if the above command properly runs
```

# Scheduler

Scheduler configuration is an important aspect in Tasker. The scheduler is responsible to what it's name suggests, **schedule** tasks to be executed. It is defined in `schedule` section of the configuration file. Example

```
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
 * Weekday - Every weekday as in the list (Monday, Tuesday, Wednesday, Thursday, Friday, Saturday and Sunday). The task will be executed at the first minute of the first hour of the respective day.
 
## Cron
 
 `cron` is another way to set the expected trigger to your scheduled task. It obey the standard `unix` cron format, but with the exception that it has one more field. Is a field representing `seconds`, and is the first field, from `00` to `59` are the accepted values. 