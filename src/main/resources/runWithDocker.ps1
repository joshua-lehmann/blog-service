docker network create blog-nw
docker run -d --name keycloak --network blog-nw -e KEYCLOAK_ADMIN = admin -e KEYCLOAK_ADMIN_PASSWORD = admin -e KC_HTTP_PORT = 8180 -e KC_HOSTNAME_URL = http://keycloak:51956 -p 51956:51956 quay.io/keycloak/keycloak:22.0.1 --import-realm



docker run -d -p 8080:8080 --env KEYCLOAK_ADMIN=admin --env KEYCLOAK_ADMIN_PASSWORD=admin --name keycloak-auth -v /src/main/resources/quarkusrealm.json:/opt/keycloak/data/import/realm.json quay.io/keycloak/keycloak:18.0 start-dev --import-realm



docker run --name keycloak --network blog-nw -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -e KC_HTTP_PORT=8180 -e KC_HOSTNAME_URL=http://keycloak:8180 -p 51956:8180 -d -v /src/main/resources/quarkusrealm.json:/opt/keycloak/data/import/realm.json quay.io/keycloak/keycloak:22.0.1 start-dev --import-realm