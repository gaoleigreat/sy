FROM java:8
VOLUME /temp
ADD  survey-eureka-0.0.1-SNAPSHOT.jar survey-eureka.jar
RUN bash -c 'touch /survey-eureka.jar'
ENTRYPOINT ["java","-jar","/survey-eureka.jar"]
EXPOSE 48011