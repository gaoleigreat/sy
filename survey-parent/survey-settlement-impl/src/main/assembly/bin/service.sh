#! /bin/sh




services=" survey-user-impl  survey-project-impl survey-report-impl survey-settlement-impl survey-file-impl  "



for service in $services
do
    service_name=`ls | grep $service`
    echo "`date +'%F %T'` [INFO] start $service_name"
    java -jar  -Dspring.profiles.active=dev  -server -Xms256m -Xmx256m $service_name > /dev/null 2>&1 &
    sleep 12s
done
