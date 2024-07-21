# bookfestival-planner

Der "BookFestival-Planner" ist ein Autorenverwaltungstool eines Verlags, um eine Übersicht über kommende Buchmessen und den daran teilnehmenden Autoren zu erhalten. Nutzern soll es möglich sein Buchmessen abzurufen und zu filtern und Autoren für die jeweiligen Messen einzutragen

## Clonen des Git-Repositorys:
Wähle klonen in der GitHub CLI und kopiere die URL.
Ändere das aktuelle Arbeitsverzeichnis zum Speicherort, in dem Du das geklonte Verzeichnis haben willst
Gibt git clone ein und füge die zuvor kopierte URL ein.
```shell script
cd /pfad/zu/ihrem/verzeichnis
git clone https://github.com/benutzername/bookFestival.git
cd bookFestival

```


## Building the application 
```shell script
./mvnw clean compile
```

## Running the application in dev mode
```shell script
./mvnw quarkus:dev
```

## Testing the application
```shell script
./mvnw test 
```
## Es gibt folgende Nutzer:
Nutzername: anna
Passwort: 123456
Rolle: Admin

Nutzername: emma
Passwort: author
Rolle: Author


## Beschreibung zur Ausführung der grafischen Oberfläche:
Nachdem die Anwendung im dev mode ausgeführt wurde, kann über:
```shell script
http://localhost:8080/api/v1/web/welcome 
```
die Startseite der Anwendung geöffnet werden. Anschließend kann man zwischen "Autoren" und "Buchmessen"
wählen. Dann wird ein Login-Formular von Keycloak bereitgestellt. Um die Anwendung in verschiedenen Rollen testen zu können,
müssen die jeweiligen Nutzerdaten eingegeben werden. Danach wird man auf die jeweilige Ressource weitergeleitet.
Um sich auszuloggen und die andere Nutzerrolle zu testen, kann entweder der Pfad:
```shell script
http://localhost:8080/api/v1/web/logout
```
eingegeben werden. Danach muss man eigenständig den Pfad:
```shell script
http://localhost:8080/api/v1/web/welcome 
```
aufrufen. Oder man clickt auf den "Logout"-Button im Footer und wird wieder zurück auf die Willkommensseite weitergeleitet.

## Beschreibung zur Ausführung der Anwendung mit Swagger:
Die Anwendung kann über die Swagger-Ui mit folgendem Pfad getestet werden:
```shell script
http://localhost:8080/swagger-ui/
```
Wenn man auf die Ressourcen zugreifen möchte, muss man auf den Button: "Authorize" clicken. 
Dann werden einem die verfügbaren Autorisierungsmethoden angezeigt. Um sich zu autorisieren wählt man:

```shell script
"SecurityScheme (OAuth2, implicit)"
```
Man clickt dann auf "authorize" und wird man dann auf das Login-Formular von Keycloak weitergeleitet. 
Dort übergibt man die jeweiligen Nutzerdaten. Um die Endpunkte der Anwendung dann zu testen, muss man eigenständig auf den Pfad:
```shell script
http://localhost:8080/swagger-ui/
```
zurück navigieren. 