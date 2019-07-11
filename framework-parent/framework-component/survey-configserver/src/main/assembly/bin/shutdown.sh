#!/usr/bin/env bash
VERSION="0.0.1-SNAPSHOT";
APPLICATION="api-service-${VERSION}"
APPLICATION_JAR="${APPLICATION}.jar";
PID=$(ps -ef | grep "${APPLICATION_JAR}" | grep -v grep | awk '{ print $2 }')
if [ "$PID" == "" ]; then
    echo "===app process not exists or stop success"
else
    KILL -9 ${PID}
    echo "app killed success"
fi


