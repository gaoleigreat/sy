FROM java:8
VOLUME /temp
ADD  monitor-admin-0.0.1-SNAPSHOT monitor-admin.jar
RUN bash -c 'touch monitor-admin.jar'
ENTRYPOINT ["java","-jar","/monitor-admin.jar"]