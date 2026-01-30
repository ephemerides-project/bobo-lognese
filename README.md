# BOBO-LOGNÈSE

## Installation
Pour installer BOBO-LOGNÈSE, suivez les étapes ci-dessous :
1. Clonez le dépôt GitHub :
   ```bash
   git clone https://github.com/ephemerides-project/bobo-lognese.git
   ```
2. Accédez au répertoire du projet :
   ```bash
   cd bobo-lognese
   ```
3. Builder l'application :
   ```bash
   ./gradlew clean war
   ```
4. Déployez le fichier WAR généré dans votre serveur d'applications Java préféré (comme TomEE).
5. Déployez la base de données en exécutant docker compose :
   ```bash
   docker compose up -d
   ```
6. Accédez à l'application via votre navigateur web à l'adresse donnée par TomEE