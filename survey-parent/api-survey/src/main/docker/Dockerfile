FROM java:8
VOLUME /temp
ADD  api-survey-0.0.1-SNAPSHOT.jar api-survey.jar
RUN bash -c 'touch api-survey.jar'
ENTRYPOINT ["java","-jar","/api-survey.jar"]