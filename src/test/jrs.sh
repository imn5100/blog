#! /bin/bash
cp -f /usr/local/jetty/webapps/root.war /usr/myfile/blog_bak.war;
\cp -rf  /usr/myfile/jettyTmp/webapp/static/userImages/* /usr/myfile/userImagesBak
sh /usr/local/jetty/bin/jetty.sh  stop;
cp -f /usr/myfile/root.war /usr/local/jetty/webapps/root.war;
sh /usr/local/jetty/bin/jetty.sh start
\cp -rf  /usr/myfile/userImagesBak/* /usr/myfile/jettyTmp/webapp/static/userImages/
