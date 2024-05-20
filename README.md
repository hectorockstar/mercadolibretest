# mercadolibretest - Instrucciones

Te pedimos que desarrolles un servicio que:
- [ ] Dada una URL larga, mi servicio me tiene que devolver una URL corta.
- [ ] Dada una URL corta, mi servicio me tiene que devolver la URL larga original.
- [ ] Puedan obtenerse estadísticas de las URLs que utilizan este servicio.
- [ ] Puedan manejarse solicitudes a gran escala.
- [ ] El 90% de todas las solicitudes puedan responderse en menos de 10 ms.
- [ ] Puedan borrarse las URLs cortas necesarias.
- [ ] Y lógicamente, que el usuario navegue hacia la URL larga cuando ingresa una URL corta válida en su navegador :)

En MercadoLibre la escala es importante, así que piensa una solución que pueda escalar
hasta, al menos, 50.000 peticiones por segundo.

De igual forma, la creación de una URL corta debe ser un proceso que tarde poco (del orden
de 1 segundo).

# Ubicacion del repositorio
- [ ] https://github.com/hectorockstar/mercadolibretest
  - Para descargas: git clone https://github.com/hectorockstar/mercadolibretest.git

# Requerimientos de Arranque
- [ ] Java version 17
- [ ] Servidor Redis 7.2.4

# Opciones de arranque
- [ ] Arrancar Redis:
  - Desde windows: https://redis.io/docs/latest/operate/oss_and_stack/install/install-redis/install-redis-on-windows/
  - Desde Mac: https://redis.io/docs/latest/operate/oss_and_stack/install/install-redis/install-redis-on-mac-os/
  - El servidor de Redis debe correr en el puerto 6379 de tu ambiente local. 
- [ ] Desde tu IDE:
  - Configurar bootRun desde la clase "MercadolibretestApplication" metodo "main"
  - Run
- [ ] linea de comando:
  - ./gradlew clean build
  - Para Iniciar: java -jar build/libs/mercadolibretest-0.0.1-SNAPSHOT.jar com.mercadolibretestMercadolibretestApplication.java
- [ ] Arranque sin redis:
  - Si quiere ejecutar la aplicacion sin redis, o ejecutar sus test de forma independiente, primero debes configurar como variable de entorno el siguiente campo: "spring.profiles.active" con valor "dev". Esta variable esta ubicada en el archivo "application.properties" del classpath de la aplicacion.


## EndPoints Disponibles
Los endpoints que se disponibilizaran una vez que la aplicacion este en ejecucion, los podras verificar desde la siguiente documentacion confeccionada en Swagger, a continuancion te dejo el link:
- [ ] [Swagger (OpenAI)](http://localhost:1004/swagger-ui/index.html)


## Base de datos en Memoria
La base de datos de este proyecto es una H2, por lo tanto funciona en memoria y solo podras la verificar en tiempo de ejecucion de la aplicacion, por lo que a continuacion te dejo el link en el cual se disponibilizara:

- [ ] [H2 Database Admin](http://localhost:1004/h2-console/)