#!/bin/bash

#remove any previous trust material
rm *.jks *.csr *.pem

printf "[INFO] Generating key pair ..."
keytool -genkeypair -noprompt -alias jks-localhost -dname "CN=localhost, OU=Unknown, O=Unknown, L=Rio de Janeiro, S=RJ, C=BR" -keystore keystore.jks -storepass changeit -keypass changeit -keyalg RSA -keysize 4096 -validity 3650 -storetype JKS -sigalg SHA256withRSA

printf "\n[INFO] Generating certificate signing request ..."
keytool -certreq -alias jks-localhost -file localhost.csr -keystore keystore.jks -storepass changeit
	
printf "\n[INFO] Generating certificate ..."
keytool -gencert -alias jks-localhost -keystore keystore.jks -storepass changeit -keypass changeit -keyalg RSA -keysize 4096 -validity 365 -storetype JKS -sigalg SHA256withRSA -infile localhost.csr -outfile localhost.pem -rfc

sleep 1

printf "\n[INFO] Importing certificate to keystore ... "
keytool -importcert -alias cert-localhost -trustcacerts -noprompt -file localhost.pem -keystore keystore.jks -storepass changeit

mv keystore.jks src/main/resources
mv localhost.csr src/main/resources
mv localhost.pem src/main/resources