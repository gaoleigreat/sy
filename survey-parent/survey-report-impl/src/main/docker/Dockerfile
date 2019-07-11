FROM java:8
VOLUME /temp
ADD  report-service-impl-0.0.1-SNAPSHOT.jar report-service.jar
RUN bash -c 'touch /report-service.jar'
ENTRYPOINT ["java","-jar","/report-service.jar"]