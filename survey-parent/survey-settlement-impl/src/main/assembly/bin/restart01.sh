#! /bin/sh

# 停止 服务
./stop.sh
echo 'stop service'
sleep 5s
# 备份jar
time=`date +%Y%m%d%H%M%s`
backup_path=../backup/server/$time
mkdir -p $backup_path
cp ./*.jar $backup_path
echo 'backup jar to  $backup_path'
# 替换新jar包
\cp ../new/*.jar ./
echo 'replace jar '
# 启动framework
./framework.sh
echo 'start framework'
sleep 3s
# 启动 base
./base.sh
echo 'start base'
sleep 3s
# 启动 service
./service.sh
echo 'start service'
