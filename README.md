# NewsProject - News Reader

> - 從聯合新聞網下載新聞(即時/股市)，並存入MongoDB
> - 擷取頁面 https://udn.com/news/breaknews/1/11#breaknews

### MongoDB
MongoDB Info: dbHost, dbName, account, password

### Run Program
```Shell
> java newsproject.newsreader.ReadNews dbHost dbName account password
```

### Run in Docker
- Dockerfile
```
FROM openjdk:8-jdk-alpine
RUN apk add -U tzdata
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY target/libs libs/
ENTRYPOINT ["java","-jar","app.jar","dbHost","dbName","account","password"]
```
- Build and Run
```Shell
> mvn clean package
> docker build -t news-reader .
> docker run --rm -e "TZ=Asia/Taipei" news-reader
```
