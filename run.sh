./src/main/resources/initializeKeyStore.sh

docker build -t proxy:0.0.1 .

docker run -d \
--net=host \
--add-host proxy.test:127.0.0.1 \
--add-host domain1.test:127.0.0.1 \
--add-host domain2.test:127.0.0.1 \
--add-host test:127.0.0.1 \
--name reverse-proxy proxy:0.0.1

