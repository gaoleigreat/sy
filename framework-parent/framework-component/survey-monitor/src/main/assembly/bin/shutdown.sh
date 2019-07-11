#!/usr/bin/env bash
VERSION="0.0.1-SNAPSHOT";
APPLICATION="api-service-${VERSION}"
APPLICATION_JAR="${APPLICATION}.jar";
PID=$(ps -ef | grep "${APPLICATION_JAR}" | grep -v grep | awk '{ print $2 }')
KILL -9 ${PID}

