# Lyon_FindR

Application web permettant de répertorier, organiser et retrouver des lieux à Lyon selon leur catégorie, leur note et leurs favoris.

Le projet est développé avec **Angular** pour le frontend, **Spring Boot** pour le backend et **PostgreSQL** pour la base de données.

## Présentation

Lyon_FindR est un projet web réalisé pour mettre en pratique le développement d'une application full-stack.

L'application permet de gérer une collection de lieux et de les organiser pour faciliter leur consultation. Le frontend communique avec une API REST développée en Java avec Spring Boot. Les données sont stockées dans une base PostgreSQL.

Ce projet permet notamment de mettre en pratique la séparation des responsabilités entre le frontend, le backend et la base de données, ainsi que la création d'une API REST et la manipulation de données persistantes.

## Fonctionnalités

- **Gestion des lieux** : création, consultation et suppression.
- **Catégorisation** : classement des lieux par type.
- **Filtrage** : affichage des lieux selon une ou plusieurs catégories.
- **Tri dynamique** : classement par nom, note ou catégorie, en ordre croissant ou décroissant.
- **Favoris** : marquage des lieux favoris et consultation des favoris.
- **Notation** : attribution d'une note aux lieux.

### Catégories de lieux

Les catégories actuellement utilisées sont :

- `RESTAURANT`
- `LOISIR`
- `VISITE`
- `PLACE`
- `PARC`

## Technologies utilisées

### Frontend

- Angular
- TypeScript
- HTML
- CSS
- Angular Signals pour la gestion de l'état de l'interface
- `HttpClient` pour les appels HTTP vers l'API

### Backend

- Java
- Spring Boot
- Spring Web pour l'API REST
- Spring Data JPA pour l'accès aux données
- Lombok pour réduire le code répétitif des entités

### Base de données

- PostgreSQL

## Architecture du projet

Le projet est organisé en deux parties principales :

```text
Lyon_FindR/
├── README.md
├── backend/
│   ├── pom.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   └── com/Lyon_FindR/backend/
│           │       ├── controller/
│           │       ├── service/
│           │       ├── repository/
│           │       └── model/
│           └── resources/
│               └── application.properties
└── frontend/
    ├── package.json
    └── src/
        └── app/
            ├── components/
            └── services/
```

### Organisation du backend

Le backend suit une architecture en couches.

| Couche | Responsabilité |
|---|---|
| `controller` | Reçoit les requêtes HTTP et expose les endpoints de l'API. |
| `service` | Contient la logique de traitement et coordonne les opérations. |
| `repository` | Accède aux données avec Spring Data JPA. |
| `model` | Définit les entités et les énumérations utilisées par l'application. |

## Prérequis

Avant de lancer le projet, il faut disposer des éléments suivants :

- Java, dans une version compatible avec le projet Spring Boot.
- Maven, ou le Maven Wrapper si celui-ci est présent dans le backend.
- Node.js et npm, dans des versions compatibles avec le projet Angular.
- PostgreSQL.
- Git, si le projet doit être cloné depuis un dépôt.

Les versions exactes dépendent des configurations du projet (`pom.xml`, `package.json` et fichier de configuration Angular).

## Installation et lancement

### 1. Récupérer le projet

Si le projet est hébergé sur Git :

```bash
git clone <URL_DU_DEPOT>
cd Lyon_FindR
```

Remplacer `<URL_DU_DEPOT>` par l'adresse réelle du dépôt.

### 2. Configurer PostgreSQL

Créer une base de données dédiée au projet, par exemple :

```sql
CREATE DATABASE lyon_findr;
```

Configurer ensuite la connexion dans `backend/src/main/resources/application.properties`.

Exemple de configuration :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/lyon_findr
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
```

Les variables `DB_USERNAME` et `DB_PASSWORD` doivent être définies dans l'environnement d'exécution, ou remplacées par une configuration locale adaptée.

La configuration JPA ci-dessus est un exemple de développement. Le mode de création et de mise à jour du schéma doit être adapté à l'environnement utilisé.

### 3. Lancer le backend

Depuis le dossier du backend :

```bash
cd backend
```

Avec Maven :

```bash
mvn spring-boot:run
```

Le backend est supposé être accessible à l'adresse suivante si le port par défaut est conservé :

```text
http://localhost:8080
```

### 4. Lancer le frontend

Dans un autre terminal, depuis la racine du projet :

```bash
cd frontend
npm install
```

Puis lancer Angular :

```bash
npm start
```

Si le script `start` n'est pas défini dans `package.json`, utiliser la commande Angular disponible dans le projet, par exemple :

```bash
npx ng serve
```

Le frontend Angular est généralement accessible à l'adresse :

```text
http://localhost:4200
```

## API REST

Le backend expose des endpoints REST permettant de gérer les lieux.

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/places` | Récupère les lieux, avec filtres et tri facultatifs. |
| `GET` | `/place/{id}` | Récupère un lieu par son identifiant, si cet endpoint est implémenté. |
| `POST` | `/place` | Crée un lieu. |
| `PUT` | `/place/{id}` | Modifie un lieu, selon l'implémentation du contrôleur. |
| `DELETE` | `/place/{id}` | Supprime un lieu, selon l'implémentation du contrôleur. |

### Paramètres de filtrage et de tri

L'endpoint `GET /places` accepte les paramètres suivants :

| Paramètre | Description | Valeur par défaut |
|---|---|---|
| `categories` | Une ou plusieurs catégories de lieux. | Aucune catégorie imposée |
| `sortBy` | Champ utilisé pour le tri : `name`, `note` ou `category`. | `name` |
| `direction` | Sens du tri : `ASC` ou `DESC`. | `ASC` |

Les valeurs autorisées pour le tri correspondent à l'implémentation actuelle du service. Les catégories doivent correspondre aux valeurs de l'énumération `Category`.

## Exemples de requêtes

Les exemples suivants peuvent être testés avec Postman, une fois le backend lancé.

### Récupérer tous les lieux

```http
GET http://localhost:8080/places
```

### Filtrer par catégorie

```http
GET http://localhost:8080/places?categories=RESTAURANT
```

### Filtrer par plusieurs catégories

```http
GET http://localhost:8080/places?categories=PARC&categories=PLACE
```

### Trier par note décroissante

```http
GET http://localhost:8080/places?sortBy=note&direction=DESC
```

### Combiner filtrage et tri

```http
GET http://localhost:8080/places?categories=RESTAURANT&sortBy=note&direction=DESC
```

### Créer un lieu

Méthode : `POST`

URL :

```text
http://localhost:8080/place
```

Dans Postman, choisir **Body → raw → JSON**, puis envoyer :

```json
{
  "name": "Parc de la Tête d'Or",
  "category": "PARC",
  "note": 5,
  "isFavorite": true,
  "address": "Parc de la Tête d'Or, Lyon"
}
```

Le format exact de la réponse dépend de l'implémentation du contrôleur.


**Lyon_FindR** — Projet de développement web full-stack avec Angular, Spring Boot et PostgreSQL.
