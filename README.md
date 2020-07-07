Buy Together
Requirements
Java 8+
Lombok Plugin
Build
Navigate to root directory
$ mvn clean install -Pdev
$ mvn clean install -Pprod
Run MySQL DB
docker run --name cortex-mysql -p 3308:3306 -e MYSQL_ROOT_PASSWORD=7a22v3zC2Ycv8PGa -e MYSQL_DATABASE=cortex_db -e MYSQL_USER=cortex -e MYSQL_PASSWORD=7a22v3zC2Ycv8PGa -d mysql:8.0
Run
Navigate to root directory
$ java -jar target/cortex-backend-1.0.0.jar
Swagger
http://{host}:{port}/swagger-ui.html