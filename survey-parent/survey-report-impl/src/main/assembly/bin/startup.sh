#!/usr/bin/env bash
cd `dirname $0`
BASE_PATH=`dirname $0`
echo  "dirname $0======$BASEPATH"
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`
CONF_DIR=$DEPLOY_DIR/conf

LOGS_DIR=$DEPLOY_DIR/logs
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi
STDOUT_FILE=$LOGS_DIR/stdout.log

LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
MAIN_DIR=$DEPLOY_DIR/main
MAIN_JARS=`ls $MAIN_DIR|grep .jar|awk '{print "'$MAIN_DIR'/"$0}'|tr "\n" ":"`


echo  " ====>$MAIN_JARS ====\c"

JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true "
JAVA_MEM_OPTS=" -server -Xms512m -Xmx512m -XX:PermSize=128m -XX:SurvivorRatio=2 -XX:+UseParallelGC "


echo -e "Starting ...\c"
echo  "$JAVA_OPTS  ==1== $JAVA_MEM_OPTS ==2====$CONF_DIR:$LIB_JARS:$MAIN_JARS   ==3==== $STDOUT_FILE"

nohup java $JAVA_OPTS $JAVA_MEM_OPTS  -classpath $CONF_DIR:$LIB_JARS:$MAIN_JARS com.lego.survey.ApiSurveyApplication > $STDOUT_FILE 2>&1 &

echo  "Finish to start,begin monitor start status!"

echo "START SUCCESS!"
PID=`ps -f | grep java | grep "$DEPLOY_DIR" | awk '{print $2}'`
echo "PID: $PID"
echo "STDOUT: $STDOUT_FILE"