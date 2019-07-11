FROM java:8
VOLUME /temp
ADD  settlement-service-impl-0.0.1-SNAPSHOT.jar settlement-service.jar
RUN bash -c 'touch /settlement-service.jar'
ENTRYPOINT ["java","-jar","/settlement-service.jar"]