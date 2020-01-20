#!/bin/bash
set -e -u
export TERM=${TERM:-dumb}

pushd git-resource
   ./gradlew clean test
popd