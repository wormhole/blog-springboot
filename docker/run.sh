#!/bin/bash

redis-server &
service mysql start
/usr/local/tomcat/bin/catalina.sh run