#!/bin/bash

pushd autocomplete;
(
	mvn clean package -DskipTests
	cf push
)
popd
pushd autocomplete-map
(
	mvn clean package -DskipTests
	cf push
)
popd
pushd presentation
(
	mvn clean package -DskipTests
	cf push
)
popd

