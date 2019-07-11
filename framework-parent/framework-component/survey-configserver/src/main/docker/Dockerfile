FROM java:8
VOLUME /temp
ADD  survey-config-0.0.1-SNAPSHOT.jar survey-config.jar
RUN bash -c 'touch /survey-config.jar'
ENTRYPOINT ["java","-jar","/survey-config.jar"]
EXPOSE 48021