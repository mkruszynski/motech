#!/usr/bin/env bash

if [ "$TRAVIS_EVENT_TYPE" != "cron" ]; then
    echo "USE mysql;\nUPDATE user SET password=PASSWORD('password') WHERE user='root';\nFLUSH PRIVILEGES;\n" | mysql -u root
    mvn -Dmysql.password=password -Dmaven.test.failure.ignore=false -Dmysql.user=root -DunitTests.skip=true clean install -PFT
fi