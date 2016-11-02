#! /bin/bash
lastday=$(date +%Y%m%d)
lastday_nginxlogs='access_nginx.log-'$lastday'.gz'
logfile='/data/wwwlogs/'$lastday_nginxlogs
final_logfile='/usr/myfile/logs/access-'$lastday'.log'
final_file='/usr/myfile/logs/html/access-'$lastday'.html'
cp_command="cp -f $logfile /usr/myfile/logs/"
gz_command="gunzip -c /usr/myfile/logs/$lastday_nginxlogs >$final_logfile"
goaccess_command="goaccess -f $final_logfile -a >$final_file"
echo $cp_command
echo $gz_command
echo $goaccess_command
eval $cp_command
eval $gz_command
eval $goaccess_command

#赋予权限避免 Permission denied 错误
#chown root:root /usr/myfile/weblog_analysis.sh
#crontab -e 添加 任务。每天7:30执行 将日志输出到wla.log
#30 7 * * * sh  /usr/myfile/weblog_analysis.sh >/usr/myfile/wla.log 2>&1



