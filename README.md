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