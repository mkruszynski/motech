#!/usr/bin/env bash

if [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
    mvn clean install -PIT -U -q
fi