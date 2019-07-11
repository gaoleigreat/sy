FROM java:8
VOLUME /temp
ADD  user-service-impl-0.0.1-SNAPSHOT.jar user-service.jar
RUN bash -c 'touch /user-service.jar'
ENTRYPOINT ["java","-jar","/user-service.jar"]