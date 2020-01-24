#!/bin/bash
set -e -u
export TERM=${TERM:-dumb}

pushd git-resource
   ./gradlew :Groupservice:build
popd

mkdir -p group-service-build/
cp git-resource/GroupService/build/libs/*.jar group-service-build/
cp git-resource/GroupService/build/publications/mavenJava/pom-default.xml group-service-build/pom.xml

mkdir -p analysis-input/
cp --recursive git-resource/GroupService/* analysis-input/