# ProxyHTTPS
This is an example of a reverse proxy that uses HTTPS on both the proxy and the server behind it.

## Using  an IDE
To run on a development environment you must first generate the certificates by running the [initializeKeyStore.sh](https://github.com/asthLucas/ProxyHTTPS/blob/UndertowSNI/src/main/resources/initializeKeyStore.sh) script, located on the src/main/resources folder. After that you can start the application using the Main class. On Eclipse, which was used to make this example you main simply right-click the Main class and select 'run as  -> Java Application'

## Running in a container
To run the application in a container just run the [run.sh](https://github.com/asthLucas/ProxyHTTPS/blob/UndertowSNI/run.sh) script, it will create the Docker image and start the container by itself.


