FROM java:8
VOLUME /temp
ADD  auth-service-impl-0.0.1-SNAPSHOT.jar auth-service.jar
RUN bash -c 'touch /auth-service.jar'
ENTRYPOINT ["java","-jar","/auth-service.jar"]
