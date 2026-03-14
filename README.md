# EGA Backend

Backend Spring Boot d’une application bancaire (gestion clients/comptes/transactions, authentification JWT, dashboard, PDF et e-mail).

## ✅ Ce qui a été fait

- Mise en place d’une documentation projet claire et centralisée.
- Ajout des instructions d’installation et d’exécution (local + Docker).
- Ajout d’un aperçu des endpoints REST disponibles.
- Ajout des informations de configuration essentielles (DB, JWT, e-mail).
- Ajout des commandes de build et de test.

## Stack technique

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Spring Security + JWT
- MySQL
- Maven
- Docker / Docker Compose

## Fonctionnalités principales

- Authentification administrateur (`/auth/signin`) avec JWT
- Gestion des administrateurs
- Gestion des clients
- Gestion des comptes
- Transactions: dépôt, retrait, virement
- Génération de relevés PDF
- Envoi d’e-mails (bienvenue, reset mot de passe, relevés)
- Dashboard (`/dashboard`)

## Structure du projet

```text
src/main/java/com/example/EGA
├── controller     # Endpoints REST
├── service        # Logique métier
├── repository     # Accès BD (JPA)
├── entity         # Entités
├── dto            # Objets de transfert
├── security       # JWT + sécurité Spring
├── cron           # Tâches planifiées
└── model          # Enums
```

## Prérequis

- Java 17+
- Maven 3.9+
- MySQL 8 (si exécution sans Docker)

## Configuration

Configuration principale:

- `src/main/resources/application.properties`

Variables recommandées par environnement:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `SPRING_MAIL_USERNAME`
- `SPRING_MAIL_PASSWORD`

> ⚠️ Éviter de versionner des secrets en clair. Préférer des variables d’environnement ou un gestionnaire de secrets.

## Lancer le projet

### Local

```bash
./mvnw spring-boot:run
```

API disponible sur `http://localhost:8080`.

### Docker Compose

```bash
docker compose up --build
```

Services:

- API: `http://localhost:8080`
- MySQL: `localhost:3307`

## Build & tests

```bash
./mvnw clean package
./mvnw test
```

## Sécurité

- `/auth/**` est accessible sans token.
- Toutes les autres routes nécessitent: `Authorization: Bearer <token>`.

## Endpoints (aperçu)

### Auth (`/auth`)

- `POST /auth/signin`
- `POST /auth/signup`
- `GET /auth/all`
- `PUT /auth/update/{id}`
- `PUT /auth/update-password/{id}`
- `PUT /auth/update-me`
- `PUT /auth/change-password`
- `DELETE /auth/delete/{id}`

### Clients

- `GET /client`
- `GET /client/{id}`
- `GET /client/{id}/comptes`
- `POST /client/ajouter`
- `PUT /client/modifier/{id}`
- `PUT /client/supprimer/{id}`

### Comptes

- `GET /compte`
- `GET /compte/{id}`
- `POST /compte/ajouter`
- `PUT /compte/modifier/{id}`
- `PUT /compte/supprimer/{id}`

### Transactions (`/transaction`)

- `GET /transaction/all`
- `GET /transaction/compte/{num}`
- `GET /transaction/compte/{num}/periode`
- `POST /transaction/deposer`
- `POST /transaction/retirer`
- `POST /transaction/virement`
- `GET /transaction/releve/pdf/{numeroCompte}`
- `GET /transaction/releveperiod/pdf`
- `POST /transaction/releve/envoyer/{numeroCompte}`

### Dashboard

- `GET /dashboard`
