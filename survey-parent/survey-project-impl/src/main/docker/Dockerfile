FROM java:8
VOLUME /temp
ADD  project-service-impl-0.0.1-SNAPSHOT.jar project-service.jar
RUN bash -c 'touch /project-service.jar'
ENTRYPOINT ["java","-jar","/project-service.jar"]