#! /bin/sh




services="survey-configserver survey-eureka"



for service in $services
do
    service_name=`ls | grep $service`
    echo "`date +'%F %T'` [INFO] start $service_name"
    java -jar  -server -Xms1024m -Xmx1024m $service_name > /dev/null 2>&1 &
    sleep 12s
done
