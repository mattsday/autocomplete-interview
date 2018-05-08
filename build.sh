#!/bin/bash

pushd autocomplete;
(
	mvn clean package dockerfile:build -DskipTests
	docker push mattsday/autocomplete
)
popd
pushd autocomplete-map
(
	mvn clean package dockerfile:build -DskipTests
	docker push mattsday/autocomplete-map
)
popd
pushd presentation
(
	mvn clean package dockerfile:build -DskipTests
	docker push mattsday/presentation
)
popd

