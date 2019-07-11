#!/usr/bin/env bash
VERSION="0.0.1-SNAPSHOT";
APPLICATION="api-service-${VERSION}"
APPLICATION_JAR="${APPLICATION}.jar";
cd `dirname $0`
BASE_PATH=`dirname $0`
echo  "dirname $0======$BASE_PATH"
BIN_DIR=`pwd`
cd ../backup
BACKUP_DIR=`pwd`
echo "dirname  ==========${BACKUP_DIR}"
cp ../boot/${APPLICATION_JAR} ./${APPLICATION}.jar


