#!/bin/bash

pushd autocomplete || exit 1; 
(
	mvn clean package -DskipTests
	cf push
)
popd || exit 1
pushd autocomplete-map || exit 1
(
	mvn clean package -DskipTests
	cf push
)
popd || exit 1
pushd presentation || exit 1
(
	mvn clean package -DskipTests
	cf push
)
popd || exit 1

