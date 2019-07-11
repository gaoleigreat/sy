FROM java:8
VOLUME /temp
ADD  survey-gateway-0.0.1-SNAPSHOT.jar survey-gateway.jar
RUN bash -c 'touch survey-gateway.jar'
ENTRYPOINT ["java","-jar","/survey-gateway.jar"]