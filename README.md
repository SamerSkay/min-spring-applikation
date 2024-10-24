## Min Spring Applikation



### Beskrivning

Denna applikationen är utformad för att hantera användare och roller med CRUD-funktionalitet. Varje User kan ha en eller flera roller. Applikationen är kopplad till en MySQL-databas som körs på AWS RDS, för en skalbar och stabil backend-lösning. För att automatisera deploy processen så så har jag implementerat en CI/CD-pipeline med GitHub Actions och Elastic Beanstalk i AWS. 

Här nedan kommer en guidning genom installationsprocessen, hur CI/CD-pipeline är konfigurerad och hur man kör applikationen lokalt. 



### Krav

Innan man kör applikationen så måste man se till att man har följande installerat på sin dator:
- Java 21 - (eller den senaste versionen av Java) behövs för att köra Spring Boot.
- Maven - för att bygga och paketera applikationen, hantering av projektets beroenden.
- Git - för att klona projektet från GitHub.
- AWS konto - för att skapa en MySQL-databas i RDS och för att skapa en Elastic Beanstalk applikation.



#### Steg-för-steg guide för att klona och köra projektet

```bash
1. Klona projektet från GitHub
```bash
git clone https://github.com/SamerSkay/min-spring-applikation.git
cd min-spring-applikation

2. Installera nödvändiga beroenden
```bash
mvn clean install

3. Konfigurera databasinstälninngarna
Kör man applikationen lokalt så konfiguera applikationen med ens egna AWS RDS-databasuppgifter. URL, användarnamn och lösenord till databasen ska anges. 

4. Kör applikationen
```bash
mvn spring-boot:run
```



#### Testa API:et

------------------------------------------------------------------------------------------------------------------------

GET http://localhost:8080/api/users

(Hämtar alla användare från databasen.)

------------------------------------------------------------------------------------------------------------------------

GET http://localhost:8080/api/users/{id}

(Hämtar en användare med ett specifikt id, ersätt {id} med det faktiskt ID på den användare som man vill hämta.)

------------------------------------------------------------------------------------------------------------------------

POST http://localhost:8080/api/users
Content-Type: application/json

{
"name": "John Doe",
"email": "johndoe@example.com"
}

(Detta anrop skapar en ny användare i databasen med ett namn och en e-postadress.)

------------------------------------------------------------------------------------------------------------------------

PUT http://localhost:8080/api/users/{userId}/roles/{roleId}

(Detta anrop lägger till en roll till en användare, ersätt {userId} med det faktiska ID på användaren och {roleId} med det faktiska ID på rollen.)

------------------------------------------------------------------------------------------------------------------------

PUT http://localhost:8080/api/users/{id}
Content-Type: application/json

{
"name": "Updated Name",
"email": "updated.email@example.com"
}

(Detta anrop uppdaterar användarens information baserat på det angivna ID:t. Ersätt {id} med det faktiska ID på användaren.)

------------------------------------------------------------------------------------------------------------------------

DELETE http://localhost:8080/api/users/{id}

(Detta anrop tar bort en användare från databasen. Man ersätter {id} med det faktiska ID för den användare man vill ta bort.)

------------------------------------------------------------------------------------------------------------------------

GET http://localhost:8080/api/roles

(Detta anrop hämtar alla roller som finns i databasen.)

------------------------------------------------------------------------------------------------------------------------

POST http://localhost:8080/api/roles
Content-Type: application/json

{
"name": "ROLE_NEW"
}

(Detta anrop skapar en ny roll i databasen. Ersätt "ROLE_NEW" med det namn man vill ge rollen.)

------------------------------------------------------------------------------------------------------------------------

PUT http://localhost:8080/api/roles/{id}
Content-Type: application/json

{
"name": "ROLE_UPDATED"
}

(Detta anrop uppdaterar en roll i databasen baserat på det angivna ID:t. Ersätt {id} med det faktiska ID på rollen och ersätt "ROLE_UPDATED" med det nya namnet på rollen.)

------------------------------------------------------------------------------------------------------------------------

DELETE http://localhost:8080/api/roles/{id}

(Detta anrop tar bort en roll baserat på dess ID. Ersätt {id} med det faktiska ID på den rollen man vill ta bort.)

------------------------------------------------------------------------------------------------------------------------



### CI/CD-pipeline
I detta projektet använder jag GitHub Actions för att automatisera bygg, test och deploy processerna. Här är en kort översikt hur CI/CD pipelinen är konfigurerad:

CI: Continuous Integration med GitHub Actions
Bygg och Testa: Varje gång jag pushar ändringar till master-branchen så startar GitHub Actions pipelinen. Först installeras projektets beroenden med Maven och projektet byggs sedan. Därefter körs automatiska tester med JUnit för att säkerställa att allt fungerar.

      jobs:
        build:
          runs-on: ubuntu-latest
          steps:
            - name: Check out the code
              uses: actions/checkout@v4

      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: '21'
          distribution: 'temurin'

      - name: Build with Maven
        run: mvn clean install

      - name: Run tests
        run: mvn test


CD: Continuous Deployment till Elastic Beanstalk
Konfigurera AWS Credentials: Detta hanteras via GitHub Secrets. Jag har lagt till min AWS Access Key ID och Secret Access Key som Secrets i GitHub. Dessa används för att autentisera mig mot AWS och för att kunna deploya applikationen till Elastic Beanstalk.

     - name: Configure AWS credentials
       uses: aws-actions/configure-aws-credentials@v2
       with:
         aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
         aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
         aws-region: ${{ secrets.AWS_REGION }}


Ladda upp .jar -filen till S3: Efter att projektet har byggts och testats så packas det till en .jar-fil. Denna fil laddas upp till en S3-bucket i AWS. Detta görs för att sedan kunna deploya applikationen till Elastic Beanstalk.

        - name: Upload JAR to S3
            run: |
              aws s3 cp target/min-spring-applikation-0.0.1-SNAPSHOT.jar s3://minspringapp-deploy-bucket/minspringapp.jar


Skapa en ny applikationsversion i Elastic Beanstalk: När filen har laddats upp till S3, skapas en ny version av applikationen i AWS Elastic Beanstalk.

        - name: Create Elastic Beanstalk Application Version
          run: |
            aws elasticbeanstalk create-application-version --application-name minspringapp \
              --version-label ${{ github.sha }} \
              --source-bundle S3Bucket=minspringapp-deploy-bucket,S3Key=minspringapp.jar


Uppdatera miljön i Elastic Beanstalk: Slutligen uppdateras den aktuella miljön på Elastic Beanstalk med den nya versionen av applikationen, vilket gör den senaste koden tillgänglig. 

        - name: Update Elastic Beanstalk Environment
            run: |
                aws elasticbeanstalk update-environment --environment-name minspringapp-env \
                    --version-label ${{ github.sha }}



#### Miljö och Deployment
Applikationen är konfigurerad för att köra på en AWS Elastic Beanstalk miljö, kopplad till en MySQL-databas i AWS RDS. Hela deploy-processen är automatiserad via GitHub Actions, så varje gång ny kod pushas till GitHub, så körs bygget och testerna, samt att den senaste versionen deployas automatiskt till AWS. 



#### Exempel på CI/CD processen
1. Ny kod pushas till GitHub.
2. GitHub Actions startar pipelinen och kör tester och bygger projektet.
3. Om alla tester lyckas så laddas den byggda .jar -filen upp till en S3-bucket.
4. En ny version av applikationen skapas och deployas till Elastic Beanstalk.
5. Den nya versionen är nu live och tillgänglig för användare.



### Slutsats
Detta projekt använder en enkel CI/CD-pipeline för att automatiskt testa och deploya alla kodändringar till AWS Elastic Beanstalk. Genom att använda denna process minskar vi risken för fel och gör det snabbare och enklare att få vår applikation live. Tack för att du läste!