FROM java:8
ADD target/springboot.jar  springboot.jar
ENTRYPOINT ["java","-jar","springboot.jar"]