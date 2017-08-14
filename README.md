# Tasker, a task runner

# Config file spec

```

config:

notifiers:

schedules:
  every:
    5-minutes:
      - blacklist-generator
  hourly:
    - blacklist-generator
  daily:
    - blacklist-generator
  weekly:
    - blacklist-generator
  montly:
    - blacklist-generator

listeners:

events:

tasks:
  blacklist-generator:
    executer: docker
    image: strm/task-blacklist
    reuse: true # Create a container and resue it instead of creating a new one
    
```

# Configuration

##Tasks

###Docker tasks

Configurations

 * `image` - the image in the very same format as it is expressed for docker, in `repo/image:tag` for images in the default repository, of `server/repo/image:tag` for images residing somewhere else.
 * `reuse` - Default is `false`, if set to `true`, the container will be created only once, and then, reused for every execution 
 
 ####Entrypoint and Arguments
 
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