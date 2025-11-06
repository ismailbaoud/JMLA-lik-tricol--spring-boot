# Multi-stage build pour Railway
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copier tous les projets
COPY . .

# Build basé sur SERVICE_NAME
ARG SERVICE_NAME=auth
RUN if [ "$SERVICE_NAME" = "auth" ]; then \
        cd Authontification && mvn clean package -DskipTests; \
    elif [ "$SERVICE_NAME" = "produits" ]; then \
        cd "Gestion des produits-spring-boot" && mvn clean package -DskipTests; \
    elif [ "$SERVICE_NAME" = "fournisseurs" ]; then \
        cd Gestion-des-Fournisseurs-spring-core && mvn clean package -DskipTests; \
    elif [ "$SERVICE_NAME" = "commandes" ]; then \
        cd "gestion des commmendsFournisseur spring boot" && mvn clean package -DskipTests; \
    elif [ "$SERVICE_NAME" = "mouvementstock" ]; then \
        cd MouvementStock && mvn clean package -DskipTests; \
    fi

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Installer bash pour le script start.sh
RUN apk add --no-cache bash

# Copier le projet compilé
COPY --from=builder /app /app

# Copier le script de démarrage
COPY start.sh /app/start.sh
RUN chmod +x /app/start.sh

# Variables d'environnement par défaut
ENV SERVICE_NAME=auth
ENV SPRING_PROFILES_ACTIVE=prod

# Exposer le port
EXPOSE 8080

# Démarrer l'application
CMD ["/app/start.sh"]

