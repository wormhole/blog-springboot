#!/bin/bash

redis-server /etc/redis/redis.conf
/etc/init.d/mysql start
catalina.sh run