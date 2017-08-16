# Tasker, a task runner

# Config file spec

```

config:

notifiers:

schedules:
  - every: 5 minutes
    task: test
  - cron

listeners:

events:

tasks:
  docker:
    - name: test
      image: debian:jessie
      
      reuse: true # Create a container and reuse it instead of creating a new one
    
```

# Configuration

## Tasks

### Docker tasks

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


#### Script directive

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

