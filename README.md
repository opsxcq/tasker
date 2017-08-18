# Tasker, a task runner

# Config file spec

```

config:

notifiers:

schedules:
  - every: 5 minutes
    task: test
  - cron: 00 11,16 * * *
    task: test

listeners:

events:

tasks:
  docker:
    - name: test
      image: debian:jessie
      
      reuse: true # Create a container and reuse it instead of creating a new one
    
```

# Configuration

# Tasks

## Docker tasks

Configurations

 * `image` - the image in the very same format as it is expressed for docker, in `repo/image:tag` for images in the default repository, of `server/repo/image:tag` for images residing somewhere else.
 * `reuse` - Default is `false`, if set to `true`, the container will be created only once, and then, reused for every execution 
 
 #### Entrypoint and Arguments
 
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


### Script directive

`script` directive is a facilitator to setting the `entrypoint` to `/bin/sh` and pass as arguments as a list of commands. It's syntax is just a list of commands like:

```
    - name: helloScriptStrict
      image: debian:jessie
      script:
        - echo green bar
        - echo green barbar
```

Commands will be executed sequentially, no matter the result. To enable the strict mode, where the next command will **only** be executed if the previous command was successful (exit status 0), you can use the `script-strict` property. For example

```
    - name: helloScriptStrict
      image: debian:jessie
      script-strict: true
      script:
        - echo green bar
        - echo green barbar
```

# Scheduler

Scheduler configuration is an important aspect in Tasker. The scheduler is responsible to what it's name suggests, **schedule** tasks to be executed. It is defined in `schedule` section of the configuration file. Example

```
schedule:
  - every: 10 minutes
    task: test
  - cron: 00 11,16 * * *
    task: test
```

`schedule` is an array of schedules. There are two ways to schedule an task. One is by giving it an interval using `every` property.

 * Minute - Every minute inside **one hour** period. For example, `every: 10 minutes`, will result in 6 executions, at 00, 10, 20, 30, 40 and 50. Every minute scheduled task will start it's first execution at the first minute of the hour (00).
 * Hour - Every hour inside **one day** period. For example, `every: 6 hours` will result in 4 executions, at 00, 06, 12 and 18. Every hour scheduled task will start it's execution at the very first hour of the day (00 - midnight).
 * Weekday - Every weekday as in the list (Monday, Tuesday, Wednesday, Thursday, Friday, Saturday and Sunday). The task will be executed at the first minute of the first hour of the respective day.
 
 `cron` is another way to set the expected trigger to your scheduled task. It obey the standard `unix` cron format.