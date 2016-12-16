#! /bin/bash
cp  root.war blog_rollback.war
mv  blog_bak.war root.war
cp -f /usr/local/jetty/webapps/root.war /usr/myfile/blog_bak.war;
\cp -rf  /usr/myfile/jettyTmp/webapp/static/userImages/* /usr/myfile/userImagesBak
sh /usr/local/jetty/bin/jetty.sh  stop;
cp -f /usr/myfile/root.war /usr/local/jetty/webapps/root.war;
sh /usr/local/jetty/bin/jetty.sh start
path=/usr/myfile/jettyTmp/webapp/static/userImages/
if  [ -d $path ];then
 \cp -rf  /usr/myfile/userImagesBak/* $path
else
 mkdir $path
 \cp -rf  /usr/myfile/userImagesBak/* $path
fi
