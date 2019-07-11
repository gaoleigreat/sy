set JAVA_OPTS=" -Djava.awt.headless=true -Djava.net.preferIPv4Stack=true "
set JAVA_MEM_OPTS=" -server -Xms512m -Xmx512m -XX:PermSize=128m -XX:SurvivorRatio=2 -XX:+UseParallelGC "

java -jar  ../boot/settlement-service-impl-0.0.1-SNAPSHOT.jar  --spring.config.location=../conf/bootstrap.yml
