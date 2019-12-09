#!/bin/bash

#remove any previous trust material
rm *.jks *.cer

printf "[INFO] Generating self-signed certificate for domain 1 ..."
keytool -genkey -alias mykey -dname "CN=localhost, OU=Unknown, O=Unknown, L=Rio de Janeiro, S=RJ, C=BR" -noprompt -keyalg RSA -keysize 2048 -sigalg SHA256withRSA -validity 365 -keypass changeit -keystore identity.jks -storepass changeit
keytool -export -alias mykey -file root.cer -keystore identity.jks -storepass changeit
keytool -import -alias mykey -trustcacerts -noprompt -file root.cer -keystore truststore.jks -storepass changeit

mv identity.jks src/main/resources
mv truststore.jks src/main/resources
mv root.cer src/main/resources
