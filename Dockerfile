FROM tomcat:latest

MAINTAINER StdUtil

WORKDIR /

ARG JAR_FILE

RUN apt-get update && apt-get install -y redis-server mysql-client mysql-server
RUN /etc/init.d/mysql start &&\
mysql -e "update mysql.user set password=PASSWORD('19960821') where user='root';" &&\
mysql -e "update mysql.user set plugin='mysql_native_password' where user='root';"
RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/${JAR_FILE} /usr/local/tomcat/webapps/blog.war
COPY run.sh /
COPY server.xml /usr/local/tomcat/conf/

EXPOSE 80

CMD ["bash","run.sh"]