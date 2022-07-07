#Instalatiehandleiding

In deze repository is de backend spring boot server van Hidrik Landlust te vinden.
Deze is ontwikkeld voor de opleiding backend developer van Novi Hogeschool.

##Inhoudsopgave

1. [Inleiding](#H1)
2. [Benodige programma's](#H2)
3. [Installatie instructie](#H3)
4. [Klaarmaken voor productie](#H4)
5. [Start applicatie](#H5)
6. [Endpoints](#H6)

## 1. Inleiding <div id="H1"></div>
De server is de backend van een projectbeheersysteem. De server is als RESTful API ontwikkeld.
In dit systeem kunnen projecten, accounts, opdrachten en componenten aangemaakt worden.
Vervolgens kunnen personen (accounts) aan projecten worden toegewezen en kunnen opdrachten bij projecten
worden gemaakt. Ook deze opdrachten kunnen aan personen gekoppeld worden. Als er componenten gebruikt
worden bij opdrachten, kunnen ook deze toegevoegd worden.

Eerst worden de benodigde programma's opgesomd. Vervolgens wordt benoemd hoe de programma's geïnstalleerd
moeten worden. Het kan zijn dat de applicatie meteen in productie moet, daarvoor moeten dingen uitgevoerd worden. Dit is
in hoofdstuk 4 te lezen. Als dit gedaan is, dan kan de applicatie gestart worden. Dit is te vinden in hoofdstuk 5.
Tot slot zijn in hoofdstuk 6 de endpoints te vinden met hierbij voorbeelden van data (te verzenden of te ontvangen).

## 2. Benodigde programma's <div id="H2"></div>
Om de applicatie te kunnen starten, zijn enkele programma's nodig. Deze zijn eerst opgesomd. Vervolgens zijn er enkele programma's
handig bij het doorontwikkelen van deze applicatie. Die zijn daaronder te vinden. Tot slot is weergegeven op welke besturingssystemen
de applicatie gestart kan worden.
### Applicaties t.b.v. productie:
- Java 
  - OpenJDK-17.0.2 
  - https://jdk.java.net/archive/
- PostgreSQL 
  - Versie 14.2 
  - https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
- Maven
  - https://maven.apache.org/download.cgi

### Applicaties t.b.v. ontwikkelen:
- Postman 
  - https://www.postman.com/downloads/
- Optioneel: IDE naar keuze
  - Voorbeeld: Jetbrains IntelliJ IDEA
  - Voorbeeld: Visual studio code

### Besturingssysteem
- Windows 7 or newer(64 bit)
- Linux (32 & 64 bit)
- Mac OS X (64 bit)


## 3. Installatie instructies <div id="H3"></div>
De programma's die benoemd zijn in het hoofdstuk hiervoor, moeten geïnstalleerd zijn. In dit hoofdstuk zijn de installatie instructies
te vinden van deze programma's. Bij het installeren van de PostgreSQL database moet wat ingesteld worden.

### 3.1 Java
In deze paragraaf is te vinden waar java gedownload kan worden. Zonder java kan de server niet starten.
Voor het ontwikkelen is openJDK-17.0.2 gebruikt.

Hieronder de downloadlink voor open-JDK. Kies versie 17.0.2 en selecteer het juiste besturingssysteem.
- https://jdk.java.net/archive/
- Bij windows: Download het bestand en pak het zip-bestand uit. Installeer vervolgens dit programma.
- Overig: Download het bestand en installeer het bestand.


### 3.2 PostgreSQL
Hieronder is te lezen waar postgreSQL gedownload kan worden en welke versie er benodigd is. PostgreSQL
is de database die gebruikt is. Vervolgens is te vinden hoe de database ingesteld kan worden bij de eerste keer opstarten.

Versie 14.2 is gebruikt voor het ontwikkelen. Hieronder de downloadlink. Download het bestand en
installeer deze.

https://www.enterprisedb.com/downloads/postgres-postgresql-downloads

##### Eerste keer opstarten

- Open PostgreSQL
  - Windows: Open het programma `SQL shell (psql)`. Deze kan gevonden worden door te zoeken in de taakbalk.
  - Linux: Open de terminal en type in: psql
- Bij server => druk enter
- Bij database => druk enter
- Bij poort => druk enter
- Bij username => voer in `postgres` en druk enter
- User aanmaken: Voer het volgende commando in en druk op enter om de root-user aan te maken:
  - `CREATE ROLE root LOGIN PASSWORD 'admin' NOINHERIT CREATEDB;`
  - Optioneel: Er kan ook voor een andere gebruiker gekozen worden.
    - Bij het aanmaken van de user kan `root` vervangen
      worden door een andere username.
    - Het wachtwoord kan veranderd worden door `admin` aan te passen.
    - Verander vervolgens in `application.properties` de volgende variabelen:
      - `spring.datasource.username={eigen user}`
      - `spring.datasource.password={eigen wachtwoord}`
- Database aanmaken: Voer het volgende commando in en druk op enter om de database aan te maken:
  - `CREATE DATABASE projectdata WITH ENCODING 'UTF8' LC_COLLATE='Dutch_Netherlands' LC_CTYPE='Dutch_Netherlands';`
  - Optioneel: De database naam kan ook veranderd worden. Verander `projectdata` naar een eigen naam.
    Let op: gebruik kleine letters!
  - Verander in `application.properties` de volgende variabele
  - `spring.datasource.url=jdbc:postgresql://localhost:5432/{eigen database naam}`
- Optioneel: Voer vervolgens het volgende commando in om de user `root` de eigenaar van de database te maken:
  - `ALTER DATABASE projectdata OWNER TO root;`

### 3.3 Maven
Maven moet ook geïnstalleerd worden. Maven zorgt ervoor dat dependencies geïnstalleerd kunnen worden en
kan testen uitvoeren.

Hieronder de downloadlink voor Maven. Daaronder staan de instructies per soort besturingssysteem

https://maven.apache.org/download.cgi

#### Windows
- Download de Binary zip archive
- Pak dit archief uit een gewenste locatie
- Voeg M2_HOME en MAVEN_HOME variabelen toe aan de windows environment via systeem eigenschappen.
De waarde van deze variabelen is de locatie waar Maven is uitgepakt.
- Klik vervolgens op Path in systeem variabelen en wijzig deze. Voeg hier het volgende toe: `%M2_HOME%\bin`
- Verifieer of het gelukt is door in CMD het volgende te typen: mvn -version

#### Linux
- Download de Binary tar.gz archive
- Creëer een map waarin maven opgeslagen kan worden. Bijvoorbeeld:`mkdir -p /usr/local/apache-maven/apache-maven-3.8.4`
- Pak vervolgens het archief uit op deze locatie.
- Voeg maven toe aan de Environment Path door eerst het bestand bashrc te openen. `nano ~/.bashrc`
- Voeg onderstaande regels toe aan het bestand:


    export M2_HOME=/usr/local/apache-maven/apache-maven-3.8.4
    export M2=$M2_HOME/bin
    export MAVEN_OPTS=-Xms256m -Xmx512m
    export PATH=$M2:$PATH`
- Sla het bestand op en herlaad het bestand: `source ~/.bashrc`
- Check of het gelukt is: `mvn -version`
- Voorbeeld van antwoord:


    Apache Maven 3.8.4 (81a9f75f19aa7275152c262bcea1a77223b93445; 2021-01-07T15:30:30+01:29)
    Maven home: /usr/local/apache-maven/apache-maven-3.8.4

    Java version: 1.8.0_75, vendor: Oracle Corporation

    Java home: /usr/local/java-current/jdk1.8.0_75/jre

### 3.4 Optioneel: Postman
De endpoints kunnen goed getest worden met Postman. Hieronder zijn de instructies te vinden voor het installeren van Postman

- Maak een account aan.
  - https://identity.getpostman.com/signup
- Download en installeer postman.
  - https://www.postman.com/downloads/
- Klik vervolgens op de kop hieronder om de collectie die behoord bij deze repository toe te voegen aan het aangemaakte account.

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/19114410-9a50fcb2-1124-41ca-bba4-be08cb65a12d?action=collection%2Ffork&collection-url=entityId%3D19114410-9a50fcb2-1124-41ca-bba4-be08cb65a12d%26entityType%3Dcollection%26workspaceId%3Dce474eb5-ca19-4bb7-be9f-d9cbf412aadd)

## 4. Klaarmaken voor productie  <div id="H4"></div>
In deze paragraaf is te lezen hoe de server productieklaar gemaakt kan worden.
Het kan namelijk zijn dat de database op een andere server draait dan de applicatie. Bij het ontwikkelen
worden de database-tabellen bij het opstarten aangemaakt en bij het stoppen verwijderd (create-drop). Dit is in productie **NIET** gewenst.

#### 4.1 **BELANGRIJK!** Database van create-drop => update
Voer onderstaande stappen uit. Als dit niet gedaan wordt, kan het zijn dat de database gewist wordt!
- Verander de volgende variabele in `application.properties` aan naar `validate`:
  - `spring.jpa.hibernate.ddl-auto=create-drop`
- Geeft deze een exceptie => voer stap A uit. Voer anders stap B uit.
- A: Als er nog geen tabellen in de database zijn aangemaak:
  - Let op: deze stap overschrijft alle bestaande data in de database! Bij twijfel => eerst navragen.
  - Verander de volgende variabele in `application.properties` naar `create` en start de server:
    - `spring.jpa.hibernate.ddl-auto=create-drop`
  - Stop de server.
  - Voer stap B uit.
- B: Verander de volgende variabele in `application.properties` aan naar `update`:
  - `spring.jpa.hibernate.ddl-auto=create-drop`

#### 4.2 Externe database:
- Pas de volgende variabele in `application.properties` aan:
  - Commando: `spring.datasource.url=jdbc:postgresql://{database ip/address}:{specifieke poort}/projectdata`
  - Voorbeeld: `spring.datasource.url=jdbc:postgresql://https://voorbeeld.nl:12345/projectdata`
  - Het kan ook zijn dat er geen gebruik gemaakt wordt van een specifieke poort omdat alles goed geforward wordt, in dit geval
geen poort aangeven.

## 5 Start applicatie <div id="H4"></div>
In dit hoofdstuk is te lezen hoe de applicatie gestart kan worden. Eerst is benoemd hoe de database gestart kan worden en
vervolgens is te lezen hoe m.b.v. Maven de applicatie gestart kan worden.

### 5.1 PostgreSQL start
Nu alles geïnstalleerd is, kan de database gestart worden. Hieronder is te lezen wat de volgorde is en
hoe het gestart moet worden.

- Open een terminal
  - Windows: cmd
  - Linux & Mac OS X: terminal
- Ga met de terminal naar de map waar PostgreSQL geïnstalleerd staat, ga vervolgens naar map 14 en daarna naar bin.
  - Command: `cd \{installatielocatie PostgreSQL}\14\bin`
  - Voorbeeld: `cd \PostgreSQL\14\bin`
- Start vervolgens de database door het volgende commando in te voeren.
  - Command: `pg_ctl.exe restart -D  "{installatielocatie PostgreSQL}\14\data"`
  - Voorbeeld: `pg_ctl.exe restart -D  "D:\PostgreSQL\14\data"`


Nu de database gestart en de juiste info aangemaakt is, kan de server gestart worden.

### 5.2 Start server
Hieronder is te lezen hoe de server gestart kan worden.
- Ga naar de map waar deze repository staat opgeslagen
- Open de terminal en start de java applicatie met het volgende commando:
  - Commando: `mvn spring-boot:run`
  - Opmerking: Dit moet vanuit de map waar de POM.xml ook staat opgeslagen.
- De server kan vervolgens gestopt worden door vanuit de terminal CRTL + C in te drukken.

De server is nu gestart op het volgende adres: https://localhost:8443/


## 6. Endpoints <div id="H5"></div>
In dit hoofdstuk zijn alle endpoints te vinden. Daarbij zijn voorbeelden van te ontvangen en verzenden data gevoegd.
De dataoverdracht gaat m.b.v. JSON objecten. De authorizatie gaat via bearer tokens. Bij alle endpoints behalve /authenticate moet
deze token meegestuurd worden. Aan de hand hiervan wordt bepaald of het om een `USER` of `ADMIN` gaat.

### 6.1 Authenticatie
- #### Login
  - Request: `POST`
  - Endpoint: `/authenticate`
  - Gebruiker: -
  - Requirements: geen
  - Body: 
    ```java
      {
      "username" : "landlust",
      "password" : "landlust"
      }
  - Response: `200 OK`
    ```java
      {
      "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsYW5kbHVzdCIsImV4cCI6MTY1NzIyNDM3OSwiaWF0IjoxNjU3MTg4Mzc5fQ.yGLPfZxrXeLX4C5impq7neOVz1-O5dwBJMMXNsURKxw"
      }


- #### Check status login
  - Request: `GET`
  - Endpoint: `/authenticated`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: geen
  - Response: `200 OK`
    ```java
    {
    "authorities": [{
            "authority": "ROLE_ADMIN"
        },
        {
            "authority": "ROLE_USER"
        }
    ],
    "details": {
        "remoteAddress": "0:0:0:0:0:0:0:1",
        "sessionId": null
    },
    "authenticated": true,
    "principal": {
        "password": "************",
        "username": "landlust",
        "authorities": [{
                "authority": "ROLE_ADMIN"
            },
            {
                "authority": "ROLE_USER"
            }
        ],
        "accountNonExpired": true,
        "accountNonLocked": true,
        "credentialsNonExpired": true,
        "enabled": true
    },
    "credentials": null,
    "name": "landlust"
    }



### 6.2 Projecten
- #### Ontvang alle projecten
  - Request: `GET`
  - Endpoint: `/projects`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: geen
    - Response: `200 OK`
    ```java
    [
    {
        "description": "test",
        "progressPercentage": 10,
        "deadline": "2022-10-01",
        "budget": 1000,
        "costs": 0,
        "id": 1,
        "projectCode": "PROJECT001"
    },
    {
        "description": "test",
        "progressPercentage": 20,
        "deadline": "2022-10-02",
        "budget": 2000,
        "costs": 0,
        "id": 2,
        "projectCode": "PROJECT002"
    },
    {
        "description": "test",
        "progressPercentage": 30,
        "deadline": "2022-10-03",
        "budget": 3000,
        "costs": 0,
        "id": 3,
        "projectCode": "PROJECT003"
    },
    {
        "description": "test",
        "progressPercentage": 40,
        "deadline": "2022-10-04",
        "budget": 4000,
        "costs": 0,
        "id": 4,
        "projectCode": "PROJECT004"
    },
    {
        "description": "test",
        "progressPercentage": 50,
        "deadline": "2022-10-05",
        "budget": 5000,
        "costs": 0,
        "id": 5,
        "projectCode": "PROJECT005"
    },
    {
        "description": "test",
        "progressPercentage": 60,
        "deadline": "2022-10-06",
        "budget": 6000,
        "costs": 0,
        "id": 6,
        "projectCode": "PROJECT006"
    }
    ]

- #### Ontvang één project
  - Request: `GET`
  - Endpoint: `/projects/{projectCode}`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: geen
  - Response: `200 OK`
    ```java
    {
    "description": "test",
    "progressPercentage": 10,
    "deadline": "2022-10-01",
    "budget": 1000,
    "costs": 0,
    "id": 1,
    "projectCode": "PROJECT001",
    "assignments": [{
        "id": 1,
        "hoursWorked": 0,
        "descriptionFinishedWork": "",
        "assignmentCode": "PROJECT001-1",
        "progressPercentage": 0
    }],
    "accounts": [{
        "id": 1,
        "firstName": "Hidrik",
        "lastName": "Landlust",
        "employeeFunction": "THE_BOSS"
    }]}

- #### Creëer project
  - Request: `POST`
  - Endpoint: `/projects`
  - Gebruiker: `ADMIN`
  - Requirements:
    - Deadline is nog niet verstreken
  - Body: **Form-data**
    - Verplicht:
      - projectCode (moet uniek zijn!)
      - description
    - Optional:
      - deadline (yyyy-mm-dd)
      - budget
  - Response: `201 CREATED`
    ```java
    {
    "description": "test",
    "progressPercentage": 0,
    "deadline": "2022-12-12",
    "budget": 1000,
    "costs": 0,
    "id": 7,
    "projectCode": "PROJECT010"
    }
      
- #### Update project info
  - Request: `PUT`
  - Endpoint: `/projects/{projectCode}`
  - Gebruiker: `ADMIN`
  - Requirements: 
    - Project moet bestaan
    - Deadline is nog niet verstreken
  - Body, minimaal één variabele.
    ```java
      {
      "description": "TEST",
      "deadline": "2022-12-12",
      "budget": 9999
      }
  - Response: `201 CREATED`

- #### Verwijder project
  - Request: `DELETE`
  - Endpoint: `/projects/{projectCode}`
  - Gebruiker: `ADMIN`
  - Requirements: Project moet bestaan.
  - Response: `202 ACCEPTED`
  
- #### Creëer een nieuwe opdracht en voeg deze toe aan een project
  - Request: `PUT`
  - Endpoint: `/projects/{projectCode}/assignments`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: 
    - Project moet bestaan
    - Deadline is nog niet verstreken
  - Body, `deadline` is optioneel.
    ```java
      {
      "description" : "Dit moet je doen etc.",
      "budget" : 10000,
      "deadline" : "2022-12-12"
      }
  - Response: `201 CREATED`


- #### Voeg een projectlid toe aan een project
  - Request: `PUT`
  - Endpoint: `/projects/{projectCode}/accounts/{accountId}`
  - Gebruiker: `ADMIN`
  - Requirements: 
    - Project bestaat
    - Account bestaat
    - Account is geen lid van Project
  - Response: `201 CREATED`


- #### Verwijder projectlid van project
  - Request: `DELETE`
  - Endpoint: `/projects/{projectCode}/accounts/{accountId}`
  - Gebruiker: `ADMIN`
  - Requirements: 
    - Project bestaat
    - Account bestaat
    - Account is lid van Project
  - Response: `200 OK`

### 6.3 Gebruikers
- #### Ontvang alle gebruikers
  - Request: `GET`
  - Endpoint: `/users`
  - Gebruiker: `ADMIN`
  - Requirements: -
  - Response: `200 OK`
    ```java
    [
    {
        "username": "landlust",
        "password": "***********",
        "enabled": true,
        "email": "landlust@hotmail.com",
        "account": {
            "id": 1,
            "firstName": "Hidrik",
            "lastName": "Landlust",
            "employeeFunction": "THE_BOSS"
        },
        "authorities": [
            {
                "username": "landlust",
                "authority": "ROLE_USER"
            },
            {
                "username": "landlust",
                "authority": "ROLE_ADMIN"
            }
        ]
    },
    {
        "username": "hidrik",
        "password": "***********",
        "enabled": true,
        "email": "hidrik@hotmail.com",
        "account": {
            "id": 2,
            "firstName": "Hidrik",
            "lastName": "Landlust",
            "employeeFunction": "Manager"
        },
        "authorities": [
            {
                "username": "hidrik",
                "authority": "ROLE_USER"
            }
        ]
    }
    ]

- #### Ontvang één gebruiker
  - Request: `GET`
  - Endpoint: `/users/{username}`
  - Gebruiker: `ADMIN`
  - Requirements:
    - Gebruiker bestaat
  - Response: `200 OK`
    ```java
    {
    "username": "hidrik",
    "password": "***********",
    "enabled": true,
    "email": "hidrik@hotmail.com",
    "account": {
        "id": 2,
        "firstName": "Hidrik",
        "lastName": "Landlust",
        "employeeFunction": "Manager"
    },
    "authorities": [
        {
            "username": "hidrik",
            "authority": "ROLE_USER"
        }
    ]
    }

- #### Creëer gebruiker
  - Request: `POST`
  - Endpoint: `/users`
  - Gebruiker: `ADMIN`
  - Requirements:
    - Gebruikersnaam bestaat not niet
    - Email bestaat nog niet
  - Body: Alle variabelen verplicht
    ```java
      {
      "username" : "hidrik",
      "password" : "landlust",
      "email" : "hidrik@hotmail.com",
      "account" : 
        {
        "firstName" : "Hidrik",
        "lastName" : "Landlust",
        "employeeFunction" : "Manager"
        }
      }

  - Response: `201 CREATED`
    ```java
    {
    "username": "hidrik",
    "password": "**********",
    "enabled": true,
    "email": "hidrik@hotmail.com",
    "account": {
        "id": 2,
        "firstName": "Hidrik",
        "lastName": "Landlust",
        "employeeFunction": "Manager"
    },
    "authorities": [
        {
            "username": "hidrik",
            "authority": "ROLE_USER"
        }
    ]}

- #### Verander wachtwoord of email
  - Request: `PUT`
  - Endpoint: `/users/{username}`
  - Gebruiker: `ADMIN`
  - Requirements:
    - Gebruiker bestaat
    - Nieuw email bestaat nog niet
  - Body: Eén van beide of beide
    ```java
      {"email" : "landlust@landlust.eu"}
    ```
    ```java
      {"password" : "landlust"}
    ```
  - Response: `202 ACCEPTED`

- #### Verwijder gebruiker
  - Request: `DELETE`
  - Endpoint: `/users/{username}`
  - Gebruiker: `ADMIN`
  - Requirements:
    - Gebruiker bestaat
  - Response: `202 ACCEPTED`

- #### Ontvang gebruiker machtigingen
  - Request: `GET`
  - Endpoint: `/users/{username}/authorities`
  - Gebruiker: `ADMIN`
  - Requirements:
    - Gebruiker bestaat
  - Body:
    ```java
      {
      "authority": "ROLE_ADMIN"
      }
  - Response: `200 OK`
    ```java
    [{
        "username": "hidrik",
        "authority": "ROLE_USER"
    }]

- #### Voeg machtiging toe aan gebruiker
  - Request: `PUT`
  - Endpoint: `/users/{username}/authorities`
  - Gebruiker: `ADMIN`
  - Requirements:
    - Gebruiker bestaat
    - Gebruiker heeft de machtiging nog niet
  - Body:
    ```java
      {
      "authority": "ROLE_ADMIN"
      }
  - Response: `202 ACCEPTED`

- #### Verwijdere machtiging van gebruiker
  - Request: `DELETE`
  - Endpoint: `/users/{username}/authorities`
  - Gebruiker: `ADMIN`
  - Requirements:
    - Gebruiker bestaat
    - Gebruiker heeft te verwijderen machtiging
  - Body:
    ```java
      {
      "authority": "ROLE_ADMIN"
      }
  - Response: `202 ACCEPTED`

### 6.4 Opdrachten
- #### Ontvang alle opdrachten
  - Request: `GET`
  - Endpoint: `/assignments`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: -
  - Response: `200 OK`
    ```java
    [
    {
        "id": 2,
        "hoursWorked": 0,
        "descriptionFinishedWork": "",
        "assignmentCode": "PROJECT001-2",
        "progressPercentage": 0
    },
    {
        "id": 1,
        "hoursWorked": 0,
        "descriptionFinishedWork": "",
        "assignmentCode": "PROJECT001-1",
        "progressPercentage": 0
    }
    ]

- #### Ontvang één opdracht
  - Request: `GET`
  - Endpoint: `/assignments/{id}`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: 
    - Opdracht met ID bestaat
  - Response: `200 OK`
    ```java
    {
    "description": "Dit moet je doen etc.",
    "progressPercentage": 0,
    "deadline": "2022-12-12",
    "budget": 10000,
    "costs": 5000,
    "id": 1,
    "hoursWorked": 0,
    "descriptionFinishedWork": "",
    "assignmentCode": "PROJECT001-1",
    "components": [
        {
            "id": 1,
            "description": "Dit is component met id 1",
            "manufacturer": "NOVI",
            "price": 1000,
            "stock": 95,
            "articleNumber": "ABC-DEF-GHI-JKL",
            "orderLink": "https://www.bestel.nl/"
        }
    ],
    "amountOfComponentById": {
        "1": 5
    },
    "project": {
        "description": "test",
        "progressPercentage": 10,
        "deadline": "2022-10-01",
        "budget": 1000,
        "costs": 5000,
        "id": 1,
        "projectCode": "PROJECT001"
    },
    "account": {
        "id": 1,
        "firstName": "Hidrik",
        "lastName": "Landlust",
        "employeeFunction": "THE_BOSS"
    }
    }

- #### Update opdracht
  - Request: `PUT`
  - Endpoint: `/assignments/{id}`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: 
    - AssignmentCode is nog niet in gebruik
    - Deadline is nog niet verstreken
  - Body:
    ```java
    {
    "description": "Dit moet je doen etc.",
    "progressPercentage": 0,
    "deadline": "2022-12-12",
    "budget": 10000,
    "hoursWorked": 0,
    "assignmentCode": "PROJECT001-2"
    }
  - Response: `200 OK`

- #### Verwijder opdracht
  - Request: `DELETE`
  - Endpoint: `/assignments/{ID}`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: 
    - Project bestaat
  - Response: `202 ACCEPTED`

- #### Voeg omschrijving van uitgevoerde werkzaamheden toe
  - Request: `PUT`
  - Endpoint: `/assignments/{id}/finishedWork`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: 
    - Opdracht bestaat
  - Body: Alle variabelen zijn verplicht!
  ```java
      {
      "hoursWorked" : 5,
      "descriptionFinishedWork" : "Ik heb dit en dit en dit gedaan bla bla bla",
      "progressPercentage" : 30
      }
  - Response: `200 OK`

- #### Voeg componenten toe aan opdracht
  - Request: `PUT`
  - Endpoint: `/assignments/{id}/components/{componentId}`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: 
    - Component bestaat
    - Opdracht bestaat
    - Amount is groter dan 0
  - Body:
    ```java
    {
    "amount" : 5
    }
  - Response: `200 OK`

- #### Verwijder componenten van de opdracht
  - Request: `DELETE`
  - Endpoint: `/assignments/{id}/components/{componentId}`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements:
    - Component is onderdeel van de opdracht
    - Opdracht bestaat
    - Amount is groter dan 0
  - Body:
    ```java
    {
    "amount" : 5
    }
  - Response: `202 ACCEPTED`

- #### Wijs een gebruiker toe aan een opdracht
  - Request: `GET`
  - Endpoint: `/assignments`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: 
    - Gebruiker bestaat
    - Opdracht bestaat
    - Opdracht heeft nog geen toegewezen gebruiker
  - Response: `200 OK`

- #### Verwijder een gebruiker van een opdracht
  - Request: `PUT`
  - Endpoint: `/assignments/{id}/accounts/{accountId}`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: 
    - Gebruiker bestaat
    - Opdracht bestaat
    - Gebruiker is toegezen aan de opdracht
  - Response: `200 OK`

### 6.5 Componenten
- #### Ontvang alle componenten
  - Request: `GET`
  - Endpoint: `/components`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: -
  - Response: `200 OK`
    ```java
    [
    {
        "id": 2,
        "description": "Dit component is bla bla bla",
        "manufacturer": "Novi",
        "price": 100,
        "stock": 0,
        "articleNumber": "abc-def-ghi",
        "orderLink": "www.bestel.nl",
        "fileName": null,
        "fileUrl": null,
        "assignments": []
    },
    {
        "id": 1,
        "description": "Dit is component met id 1",
        "manufacturer": "NOVI",
        "price": 1000,
        "stock": 100,
        "articleNumber": "ABC-DEF-GHI-JKL",
        "orderLink": "https://www.bestel.nl/",
        "fileName": "6ES75121CK010AB0_datasheet_en.pdf",
        "fileUrl": "uploads\\1\\6ES75121CK010AB0_datasheet_en.pdf",
        "assignments": []
    }
    ]

- #### Ontvang één component
  - Request: `GET`
  - Endpoint: `/components/{id}`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: 
    - Component bestaat
  - Response: `200 OK`
    ```java
    {
    "id": 1,
    "description": "Dit is component met id 1",
    "manufacturer": "NOVI",
    "price": 1000,
    "stock": 95,
    "articleNumber": "ABC-DEF-GHI-JKL",
    "orderLink": "https://www.bestel.nl/",
    "fileName": "6ES75121CK010AB0_datasheet_en.pdf",
    "fileUrl": "uploads\\1\\6ES75121CK010AB0_datasheet_en.pdf",
    "assignments": [
        {
            "id": 1,
            "hoursWorked": 0,
            "descriptionFinishedWork": "",
            "assignmentCode": "PROJECT001-1",
            "progressPercentage": 0
        }
    ]
    }

- #### Creëer component
  - Request: `POST`
  - Endpoint: `/components`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: -
  - Body: Description, Manufacturer, Price, ArticleNumber, OrderLink zijn verplichte velden
    ```java
    {
    "description" : "Dit component is bla bla bla",
    "manufacturer" : "Novi",
    "price" : 100,
    "articleNumber" : "abc-def-ghi",
    "orderLink" : "www.bestel.nl",
    "stock" : 1000
    }
  - Response: `201 CREATED`
    ```java
    {
    "id": 3,
    "description": "Dit component is bla bla bla",
    "manufacturer": "Novi",
    "price": 100,
    "stock": 1000,
    "articleNumber": "abc-def-ghi",
    "orderLink": "www.bestel.nl",
    "fileName": null,
    "fileUrl": null,
    "assignments": null
    }

- #### Update component
  - Request: `PUT`
  - Endpoint: `/components/{id}`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: 
    - Component bestaat
  - Body: Minimaal één veld ingevoerd
    ```java
    {
    "description" : "DIT COMPONENT IS BLA BLA BLA",
    "manufacturer" : "NOVI",
    "price" : 1000,
    "articleNumber" : "ABC-DEF-GHI",
    "orderLink" : "WWW.BESTEL.NL",
    "stock" : 15
    }
  - Response: `202 ACCEPTED`
    ```java
    {
    "id": 1,
    "description": "DIT COMPONENT IS BLA BLA BLA",
    "manufacturer": "NOVI",
    "price": 1000,
    "stock": 15,
    "articleNumber": "ABC-DEF-GHI",
    "orderLink": "WWW.BESTEL.NL",
    "fileName": "6ES75121CK010AB0_datasheet_en.pdf",
    "fileUrl": "uploads\\1\\6ES75121CK010AB0_datasheet_en.pdf",
    "assignments": [
        {
            "id": 1,
            "hoursWorked": 0,
            "descriptionFinishedWork": "",
            "assignmentCode": "PROJECT001-1",
            "progressPercentage": 0
        }
    ]
    }

- #### Delete component
  - Request: `GET`
  - Endpoint: `/components/{id}`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: 
    - Component bestaat
  - Response: `202 ACCEPTED`

- #### Ontvang toegevoegd bestand van component
  - Request: `GET`
  - Endpoint: `/components/{id}/file`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements:
    - Component bestaat
    - Component heeft een toegevoegd bestand
  - Response: `200 OK`
    - Stuurt een Resource toe (File + name + byte-data)

- #### Voeg bestand toe aan component
  - Request: `POST`
  - Endpoint: `/components/{id}/file`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: 
    - Component bestaat
    - Component heeft nog geen file
  - Body: `form-data`
    - file => MultipartFile. Dit is een file die toegevoegd is door een `form` in HTML.
  - Response: `200 OK` `Uploaded file successfully: {filename.extension}`

- #### Verander het toegevoegde bestand
  - Request: `PUT`
  - Endpoint: `/components/{id}/file`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements:
    - Component bestaat
    - Component heeft al een file toegewezen.
  - Body: `form-data`
    - file => MultipartFile. Dit is een file die toegevoegd is door een `form` in HTML.
  - Response: `200 OK` `Uploaded and updated file successfully: {filename.extension}`

- #### Verwijder het toegevoegde bestand
  - Request: `DELETE`
  - Endpoint: `/components/{id}/file`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: 
    - Component bestaat
    - Component heeft een bestand toegewezen.
  - Response: `200 OK` `Removed file from component with id 1`

### 6.6 Accounts
- #### Ontvang alle persoonsgegevens
  - Request: `GET`
  - Endpoint: `/accounts`
  - Gebruiker: `USER`, `ADMIN`
  - Requirements: -
  - Response: `200 OK`
    ```java
    [
    {
        "id": 1,
        "firstName": "Hidrik",
        "lastName": "Landlust",
        "employeeFunction": "THE_BOSS"
    },
    {
        "id": 2,
        "firstName": "Hidrik",
        "lastName": "Landlust",
        "employeeFunction": "Manager"
    }
    ]
- #### Ontvang persoonsgegevens van één persoon
  - Request: `GET`
  - Endpoint: `/accounts/{id}`
  - Gebruiker: `ADMIN`
  - Requirements:
    - Account bestaat
  - Response: `200 OK`
    ```java
    {
    "id": 1,
    "firstName": "Hidrik",
    "lastName": "Landlust",
    "employeeFunction": "THE_BOSS",
    "postalCode": "1234 AB",
    "streetName": "LaanStraatWeg",
    "houseNumber": 69,
    "city": "Monkeytown",
    "projects": [
        {
            "description": "test",
            "progressPercentage": 10,
            "deadline": "2022-10-01",
            "budget": 1000,
            "costs": 0,
            "createdOn": "2022-07-07T11:47:55.887+00:00",
            "updatedOn": "2022-07-07T11:47:55.887+00:00",
            "id": 1,
            "projectCode": "PROJECT001"
        }
    ],
    "assignments": [
        {
            "description": "Dit moet je doen etc.",
            "progressPercentage": 0,
            "deadline": "2022-12-12",
            "budget": 10000,
            "costs": 0,
            "createdOn": "2022-07-07T11:47:55.883+00:00",
            "updatedOn": "2022-07-07T11:47:58.812+00:00",
            "id": 1,
            "hoursWorked": 0,
            "descriptionFinishedWork": "",
            "assignmentCode": "PROJECT001-1",
            "amountOfComponentById": {}
        }
    ]
    }

- #### Update persoonsgegevens
  - Request: `PUT`
  - Endpoint: `/accounts/{id}`
  - Gebruiker: `ADMIN`
  - Requirements:
    - Account bestaat
  - Response: `200 OK`
    ```java
     {   
    "firstName": "Hidrik",
    "lastName": "Landlust",
    "employeeFunction": "Schoonmaker"
    }