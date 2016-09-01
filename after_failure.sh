#!/bin/bash

if [[$TRAVIS_BRANCH == 'master' ]]; then
    mvn clean install -PIT
elif [[$TRAVIS_PULL_REQUEST == true ]]; then
    echo "PULL REQUEST"
fi