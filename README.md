# contenedorbcp
Bootcamp reto contenedor


# 1-Dockerfile y subir a dockerhub

https://github.com/FelipeAyalaT/contenedorbcp.git

Ubicarse en carpeta: /dockerhub
files:
Dockerfile

Comandos Utilizados:
docker build . -t bcpretodockerhub
docker run -d -p 1603:8080 bcpretodockerhub

validar con:
http://localhost:1603/actuator

Para subir a dockerhub renombramos con el usuario:

docker tag bcpretodockerhub:latest felipeayala/bcpretodockerhub

docker images

REPOSITORY                     TAG       IMAGE ID       CREATED          SIZE
bcpretodockerhub               latest    2bc82cc3d7cf   11 minutes ago   327MB
felipeayala/bcpretodockerhub   latest    2bc82cc3d7cf   11 minutes ago   327MB

Subimos a Dockerhub:
docker push felipeayala/bcpretodockerhub

URL:
https://hub.docker.com/repository/docker/felipeayala/bcpretodockerhub


# 2- usando docker-compose
Este escenario está ejecutando el build del Dockerfile, no estamos usando de dockerhub. Se está usando una base de datos mongodb para guardar los datos.

https://github.com/FelipeAyalaT/contenedorbcp.git

Ubicarse en carpeta: /dockercompose
files:
	Dockerfile
	docker-compose.yml
  src\main\resources\application.yml
	
Comandos:
Detenemos el contenedor iniciado para evitar conflictos del puerto.

docker stop (ID_PARA_DETENER_bcpretodockerhub)

Ejecutamos el docker-compose con una de las dps opciones:
docker-compose up
docker-compose up -d

Si se ejecuta en backgroud usar el comando para ver el log:
docker-compose logs -f apiblog


Para validar Usar URL:
http://localhost:1603/swagger-ui.html

Iniciar creando un author.


Gracias.
