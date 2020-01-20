#!/bin/bash

export TERM=${TERM:-dumb}
cd resource-source
./gradlew --no-daemon clean test