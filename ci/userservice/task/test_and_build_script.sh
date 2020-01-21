#!/bin/bash
set -e -u
export TERM=${TERM:-dumb}

pushd git-resource
   ./gradlew :Userservice:build
popd

mkdir -p user-service-build/
cp git-resource/UserService/build/libs/*.jar user-service-build/
cp git-resource/UserService/build/publications/mavenJava/pom-default.xml user-service-build/pom.xml

mkdir -p analysis-input/
cp --recursive git-resource/UserService/* analysis-input/