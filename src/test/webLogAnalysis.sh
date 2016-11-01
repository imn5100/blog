#! /bin/bash
lastday=$(date -d last-day +%Y%m%d)
lastday_nginxlogs='access_nginx.log-'$lastday'.gz'
logfile='/data/wwwlogs/'$lastday_nginxlogs
final_logfile='/usr/myfile/logs/access-'$lastday'.log'
final_file='/usr/myfile/logs/access-'$lastday'.html'
cp_command="cp -f $logfile /usr/myfile/logs/"
gz_command="gunzip /usr/myfile/logs/$lastday_nginxlogs >$final_logfile"
goaccess_command="goaccess -f $final_logfile -a >$final_file"
eval $cp_command
eval $gz_command
eval $goaccess_command
