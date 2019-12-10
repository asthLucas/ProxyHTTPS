grep "127.0.0.1 proxy.test domain1.test domain2.test test" /etc/hosts
if [[ $? -ne 0 ]]; then 
  echo -e "\n127.0.0.1 proxy.test domain1.test domain2.test test" >> /etc/hosts 
fi

./src/main/resources/initializeKeyStore.sh

docker build -t proxy:0.0.1 .

docker run -d \
--net=host \
--add-host proxy.test:127.0.0.1 \
--add-host domain1.test:127.0.0.1 \
--add-host domain2.test:127.0.0.1 \
--add-host test:127.0.0.1 \
--name reverse-proxy proxy:0.0.1



