#! /bin/sh




services="survey-configserver "



for service in $services
do
    service_name=`ls | grep $service`
    echo "`date +'%F %T'` [INFO] start $service_name"
    java -jar  -server -Xms256m -Xmx256m $service_name > /dev/null 2>&1 &
    sleep 12s
done
