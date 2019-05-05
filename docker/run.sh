#!/bin/bash

redis-server &
/etc/init.d/mysql start
/usr/local/tomcat/bin/catalina.sh run