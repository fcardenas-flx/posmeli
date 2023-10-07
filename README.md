# posmeli

Aplicación Backend que se conecta a Mercado Libre, con servicios que ayudan a sincronizar Inventario y venta de productos en tienda física.

Parámetros de Ejecución de Servicio:

--spring.profiles.active=qa
--server.port=8085
--PROJECT_SQL_SERVER=localhost
--PROJECT_SQL_PORT=3306
--PROJECT_SQL_DATABASE=lmb
--PROJECT_SQL_USER=root
--PROJECT_SQL_PASSWORD=1Qaz2wsx--
--java.awt.headless=false
--REDIS_SERVER=localhost
--REDIS_PORT=6379
--PROJECT_CACHE_TYPE=redis
--MAX_POOL_DB=10
--PROJECT_TIME_ZONE=America/Santo_Domingo

Ejecución de Servicio:

mvn clean compile spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=qa --server.port=8080 --PROJECT_SQL_SERVER=localhost --PROJECT_SQL_PORT=3306 --PROJECT_SQL_DATABASE=lmb --PROJECT_SQL_USER=root --PROJECT_SQL_PASSWORD=1Qaz2wsx-- --java.awt.headless=false --REDIS_SERVER=localhost --REDIS_PORT=6379 --PROJECT_CACHE_TYPE=redis --MAX_POOL_DB=10 --PROJECT_TIME_ZONE=America/Santo_Domingo"