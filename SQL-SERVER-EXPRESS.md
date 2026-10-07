# Configurer SQL Server Express pour Poudlar2026

Ce guide explique comment installer et configurer SQL Server Express sous Windows, activer TCP/IP, se connecter avec SSMS et donner a l'application Spring Boot un acces limite a la base.

## 1. Installer SQL Server Express et SSMS

1. Installez **SQL Server Express** depuis le site Microsoft. Pendant l'installation, retenez le nom de l'instance, generalement `SQLEXPRESS`.
2. Installez **SQL Server Management Studio (SSMS)**. SSMS est l'outil graphique d'administration et peut etre installe separement du moteur SQL Server.
3. Dans les services Windows (`services.msc`), verifiez que **SQL Server (SQLEXPRESS)** est demarre.

## 2. Activer TCP/IP et fixer le port

Pour correspondre a la configuration JDBC de ce projet (`localhost:1433`), configurez l'instance pour ecouter sur le port TCP fixe `1433`.

1. Ouvrez **SQL Server Configuration Manager** correspondant a la version installee. Vous pouvez le rechercher dans le menu Demarrer; son executable porte un nom tel que `SQLServerManager16.msc`.
2. Allez dans **SQL Server Network Configuration > Protocols for SQLEXPRESS** (adaptez le nom si votre instance est differente).
3. Faites un clic droit sur **TCP/IP**, choisissez **Enable**, puis ouvrez **Properties**.
4. Dans l'onglet **IP Addresses**, allez au bas de la page, dans **IPAll** :
   - videz **TCP Dynamic Ports**;
   - mettez `1433` dans **TCP Port**.
5. Validez, puis redemarrez **SQL Server (SQLEXPRESS)** depuis Configuration Manager ou `services.msc`.

Le port `1433` doit etre libre sur la machine. Si une autre instance SQL Server l'utilise deja, choisissez un autre port fixe et reportez exactement ce port dans l'URL JDBC ci-dessous.

### Pare-feu Windows (connexion depuis une autre machine)

Si l'application et SQL Server tournent sur le meme ordinateur, `localhost` n'exige pas d'ouverture du pare-feu pour une connexion distante. Pour un client sur un autre ordinateur, creez une regle entrante Windows Defender Firewall autorisant **TCP 1433**, de preference uniquement depuis l'adresse IP ou le sous-reseau de confiance. N'exposez pas le port SQL Server directement sur Internet.

Avec un port fixe, la connexion utilise directement ce port. SQL Server Browser et UDP 1434 ne sont normalement pas necessaires.

## 3. Se connecter dans SSMS

Dans SSMS, dans **Connect > Database Engine** :

- **Server type** : `Database Engine`;
- **Server name** : `tcp:localhost,1433` pour la machine locale, ou `tcp:<adresse-ip-du-serveur>,1433` depuis un autre ordinateur;
- **Authentication** : Windows Authentication, ou SQL Server Authentication si vous avez active le mode mixte et cree un login SQL.

Cliquez sur **Connect**. Le prefixe `tcp:` et la virgule avant le port forcent l'utilisation de TCP/IP. Pour se connecter a une instance par son nom sans port fixe, le nom ressemble plutot a `NOM-DU-PC\SQLEXPRESS`; ce n'est pas l'URL configuree par defaut dans ce projet.

## 4. Creer la base et un login pour l'application

Connectez-vous avec un compte administrateur dans SSMS et ouvrez **New Query**. Le script suivant cree la base si elle n'existe pas, puis un login SQL dedie et un utilisateur dans cette base. Remplacez `REMPLACER_PAR_UN_MOT_DE_PASSE_LONG_ET_UNIQUE` par un vrai mot de passe avant execution; ne reutilisez pas le mot de passe administrateur `sa`.

```sql
IF DB_ID(N'PoudlardM2') IS NULL
    CREATE DATABASE [PoudlardM2];
GO

USE [master];
GO

IF SUSER_ID(N'poudlar_app') IS NULL
    CREATE LOGIN [poudlar_app]
    WITH PASSWORD = N'REMPLACER_PAR_UN_MOT_DE_PASSE_LONG_ET_UNIQUE',
         CHECK_POLICY = ON;
GO

USE [PoudlardM2];
GO

IF USER_ID(N'poudlar_app') IS NULL
    CREATE USER [poudlar_app] FOR LOGIN [poudlar_app];
GO

ALTER ROLE [db_datareader] ADD MEMBER [poudlar_app];
ALTER ROLE [db_datawriter] ADD MEMBER [poudlar_app];
GO
```

Selectionnez `PoudlardM2` dans le menu de base de donnees de SSMS et executez le script. Si votre base existe sous un autre nom, adaptez-le ici et dans l'URL JDBC.

Les roles `db_datareader` et `db_datawriter` autorisent la lecture et l'ecriture des donnees, sans donner les droits d'administration de `db_owner`. Executez les scripts de creation ou de modification des tables avec un compte administrateur/deploiement distinct, et non avec ce login applicatif.

Pour utiliser un login SQL, l'instance doit accepter le mode mixte :

1. Dans SSMS, clic droit sur le serveur > **Properties > Security**.
2. Selectionnez **SQL Server and Windows Authentication mode**.
3. Validez puis redemarrez l'instance SQL Server.

## 5. Configurer Poudlar2026

La configuration actuelle de `src/main/resources/application.properties` utilise SQL Server sur `localhost:1433`, la base `PoudlardM2` et le login `sa`. Remplacez le login et le mot de passe par le compte applicatif cree ci-dessus; ne stockez pas un vrai mot de passe dans un fichier versionne.

Une configuration recommandee utilise des variables d'environnement :

```properties
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.datasource.url=jdbc:sqlserver://${DB_HOST:localhost}:${DB_PORT:1433};databaseName=${DB_NAME:PoudlardM2};encrypt=false
spring.datasource.username=${DB_USERNAME:poudlar_app}
spring.datasource.password=${DB_PASSWORD}
```

Avant de lancer l'application, definissez au minimum le mot de passe dans l'environnement du processus. Dans PowerShell, pour le terminal courant :

```powershell
$env:DB_PASSWORD = "votre-mot-de-passe"
.\mvnw.cmd spring-boot:run
```

Pour un serveur distant, renseignez aussi `DB_HOST` avec son nom ou son adresse IP. En production, privilegiez une configuration de chiffrement TLS adaptee au certificat du serveur plutot que `encrypt=false`.

## 6. Creer la table Sortileges

Si l'entite `SortilegeEntity` est utilisee et que cette table n'existe pas encore, executez ce script une seule fois dans SSMS, sur la base `PoudlardM2`. Il suppose que la table `dbo.Sorciers` existe et que sa cle primaire est `ID`.

```sql
USE [PoudlardM2];
GO

IF OBJECT_ID(N'dbo.Sortileges', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.Sortileges (
        ID INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        nom NVARCHAR(100) NOT NULL,
        type_sortilege NVARCHAR(50) NOT NULL,
        sorcier_id INT NOT NULL,
        CONSTRAINT FK_Sortileges_Sorciers
            FOREIGN KEY (sorcier_id) REFERENCES dbo.Sorciers(ID)
    );
END;
GO
```

## 7. Depannage rapide

- **La connexion SSMS sur `tcp:localhost,1433` echoue** : verifiez que TCP/IP est active, que le port TCP fixe est bien `1433`, puis redemarrez l'instance.
- **Le port est deja utilise** : choisissez un autre port fixe libre et mettez-le dans `IPAll > TCP Port`, puis dans `DB_PORT` (ou l'URL JDBC).
- **Connexion locale reussie, connexion distante en echec** : verifiez l'adresse IP du serveur, la regle entrante TCP du pare-feu et les identifiants.
- **Erreur d'authentification SQL** : verifiez le mode mixte, que le login est active, que le mot de passe est correct et que l'utilisateur existe dans `PoudlardM2`.
- **Base ou table introuvable** : confirmez le nom de la base et executez les scripts de schema avec un compte ayant les droits necessaires.
