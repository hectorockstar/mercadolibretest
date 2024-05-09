# mercadolibretest

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


## EndPoints Disponibles
- [ ] [Swagger (OpenAI)](http://localhost:1004/swagger-ui/index.html)


## Base de datos en Memoria

- [ ] [H2 Database Admin](http://localhost:1004/h2-console/)