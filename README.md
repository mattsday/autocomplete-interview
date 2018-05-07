# Autocomplete Solution
This includes example code enough to run an autocompletion example either stand-alone or in Google App Engine Flexible.

It is primarily written in Spring Boot and comprises of three microservices:

1. **Autocomplete Map Generator** - `autocomplete-map/` - generates autocomplete mappings and stores them in Google Cloud Storage - triggers on a pub/sub notification from Cloud Storage
2. **Autocomplete Microservice** - `autocomplete/` - builds an in-memory map of autocomplete mappings and serves them - replaces the in-memory database when alerted by cloud storage
3. **Presentation Layer** - `presentation/` - provides the Javascript and so on for the app

To get up and running customise each app's `src/main/resources/application.yml` file for your needs and then run the following for each microservice:

```
mvn clean package appengine:deploy -DskipTests
```

```
pushd autocomplete;
(
	mvn dockerfile:build -DskipTests
	docker push mattsday/autocomplete
)&
popd
pushd autocomplete-map
(
	mvn dockerfile:build -DskipTests
	docker push mattsday/autocomplete-map
)&
popd
pushd presentation
(
	mvn dockerfile:build -DskipTests
	docker push mattsday/presentation
)&
popd
```
