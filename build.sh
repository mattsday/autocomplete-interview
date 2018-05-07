#!/bin/bash

pushd autocomplete;
(
	mvn clean package dockerfile:build -DskipTests
	docker push mattsday/autocomplete
)& wait
popd
pushd autocomplete-map
(
	mvn clean package dockerfile:build -DskipTests
	docker push mattsday/autocomplete-map
)& wait
popd
pushd presentation
(
	mvn clean package dockerfile:build -DskipTests
	docker push mattsday/presentation
)& wait
popd


