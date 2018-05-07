# Autocomplete Solution
This includes example code enough to run an autocompletion example on GKE.

It is primarily written in Spring Boot and comprises of three microservices:

1. **Autocomplete Map Generator** - `autocomplete-map/` - generates autocomplete mappings and stores them in Google Cloud Storage - triggers on a pub/sub notification from Cloud Storage
2. **Autocomplete Microservice** - `autocomplete/` - builds an in-memory map of autocomplete mappings and serves them - replaces the in-memory database when alerted by cloud storage
3. **Presentation Layer** - `presentation/` - provides the Javascript and so on for the app

## Compiling / Building Docker images
To get up and running customise each app's `src/main/resources/application.yml` file for your needs and then run the following for each microservice:

```bash
# Replace this with your docker prefix
DOCKER_PREFIX=mattsday

pushd autocomplete;
(
	mvn clean package dockerfile:build -DskipTests
	docker push ${DOCKER_PREFIX}/autocomplete
)& wait
popd
pushd autocomplete-map
(
	mvn clean package dockerfile:build -DskipTests
	docker push ${DOCKER_PREFIX}/autocomplete-map
)& wait
popd
pushd presentation
(
	mvn clean package dockerfile:build -DskipTests
	docker push ${DOCKER_PREFIX}/presentation
)& wait
popd
```

## Running in k8s

```
kubectl create -f interview.yml
```
