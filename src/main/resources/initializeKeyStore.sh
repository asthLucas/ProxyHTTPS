#!/bin/sh

# remove any previous trust material and redirects stderr output to /dev/null
rm *.jks *.cer 2> /dev/null

printf "[INFO] Generating self-signed certificate for test ...\n"
keytool -genkey -alias mykey -dname "CN=test, OU=Unknown, O=Unknown, L=Rio de Janeiro, S=RJ, C=BR" -noprompt -keyalg RSA -keysize 2048 -sigalg SHA256withRSA -validity 365 -keypass changeit -keystore identity.jks -storepass changeit
keytool -export -alias mykey -file root.cer -keystore identity.jks -storepass changeit
keytool -import -alias mykey -trustcacerts -noprompt -file root.cer -keystore truststore.jks -storepass changeit

printf "\n[INFO] Generating self-signed certificate for domain1.test ...\n"
keytool -genkey -alias mykey1 -dname "CN=domain1.test, OU=Unknown, O=Unknown, L=Rio de Janeiro, S=RJ, C=BR" -noprompt -keyalg RSA -keysize 2048 -sigalg SHA256withRSA -validity 365 -keypass changeit -keystore identity1.jks -storepass changeit
keytool -export -alias mykey1 -file root1.cer -keystore identity1.jks -storepass changeit
keytool -import -alias mykey1 -trustcacerts -noprompt -file root1.cer -keystore truststore1.jks -storepass changeit

printf "\n[INFO] Generating self-signed certificate for domain2.test ...\n"
keytool -genkey -alias mykey2 -dname "CN=domain2.test, OU=Unknown, O=Unknown, L=Rio de Janeiro, S=RJ, C=BR" -noprompt -keyalg RSA -keysize 2048 -sigalg SHA256withRSA -validity 365 -keypass changeit -keystore identity2.jks -storepass changeit
keytool -export -alias mykey2 -file root2.cer -keystore identity2.jks -storepass changeit
keytool -import -alias mykey2 -trustcacerts -noprompt -file root2.cer -keystore truststore2.jks -storepass changeit

printf "\n[INFO] Generating self-signed certificate for proxy.test ...\n"
keytool -genkey -alias mykey3 -dname "CN=proxy.test, OU=Unknown, O=Unknown, L=Rio de Janeiro, S=RJ, C=BR" -noprompt -keyalg RSA -keysize 2048 -sigalg SHA256withRSA -validity 365 -keypass changeit -keystore identity3.jks -storepass changeit
keytool -export -alias mykey3 -file root3.cer -keystore identity3.jks -storepass changeit
keytool -import -alias mykey3 -trustcacerts -noprompt -file root3.cer -keystore truststore3.jks -storepass changeit

mv *.jks src/main/resources
mv *.cer src/main/resources
