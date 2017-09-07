#!/bin/sh

if [ ! -z "$configuration" ]
then
  echo '[+] Using configuration from environment variable'
  echo "$configuration"
  echo "$configuration" > /application.yml
else
  if [ ! -f '/application.yml' ]
  then
    echo '[-] Missing /application.yml , please, configure this application before running it'
    exit -1
  fi
fi

java -Djava.security.egd=file:/dev/./urandom -jar /tasker.jar
