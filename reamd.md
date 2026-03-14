# EGA Backend

API backend Spring Boot pour une application bancaire (gestion des clients, comptes, transactions, dashboard et authentification JWT).

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

- Authentification administrateur avec JWT (`/auth/signin`)
- Gestion des administrateurs (`/auth/signup`, mise à jour, suppression, reset mot de passe)
- Gestion des clients (CRUD logique)
- Gestion des comptes (CRUD logique)
- Opérations de transaction:
  - dépôt
  - retrait
  - virement
- Génération de relevés PDF
- Envoi d’e-mails (welcome, reset mot de passe, relevés)
- Endpoint dashboard synthétique (`/dashboard`)

## Structure du projet

```text
src/main/java/com/example/EGA
├── controller     # Endpoints REST
├── service        # Logique métier
├── repository     # Accès base de données (JPA)
├── entity         # Entités métier
├── dto            # Objets de transfert
├── security       # JWT + configuration Spring Security
├── cron           # Tâches planifiées
└── model          # Enums / modèles
```

## Prérequis

- Java 17+
- Maven 3.9+
- MySQL 8 (si exécution locale hors Docker)

## Configuration

Le projet lit sa configuration principale depuis:

- `src/main/resources/application.properties`

Variables sensibles à définir par environnement:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `SPRING_MAIL_USERNAME`
- `SPRING_MAIL_PASSWORD`

> ⚠️ Recommandation: ne pas versionner de secrets réels en clair, et privilégier des variables d’environnement ou un coffre de secrets.

## Lancer en local (sans Docker)

```bash
./mvnw spring-boot:run
```

Application disponible sur:

- `http://localhost:8080`

## Lancer avec Docker Compose

```bash
docker compose up --build
```

Services:

- API: `http://localhost:8080`
- MySQL: `localhost:3307` (container `mysql-ega`)

## Build et tests

```bash
./mvnw clean package
./mvnw test
```

## Sécurité

- Les routes `/auth/**` sont publiques.
- Les autres routes nécessitent un token JWT valide dans l’en-tête `Authorization: Bearer <token>`.

## Endpoints REST (aperçu)

### Authentification et admins (`/auth`)

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

## Notes

- Ce fichier donne une vue d’ensemble orientée démarrage rapide.
- Pour les schémas JSON exacts des requêtes/réponses, se référer aux DTO et contrôleurs du dossier `src/main/java/com/example/EGA`.
