FROM hub.c.163.com/library/java:8-alpine
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
ADD ./target/spring-mybatis-0.0.1-SNAPSHOT.jar /home/springboot/spring-mybatis-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java","-jar","/home/springboot/spring-mybatis-0.0.1-SNAPSHOT.jar"]
