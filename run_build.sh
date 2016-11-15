#!/usr/bin/env bash

#Release
if [ "$TRAVIS_EVENT_TYPE" = "api" ] && [ ! -z "$developmentVersion" ] && [ ! -z "$scmTag" ] && [ ! -z "$releaseVersion" ] && [ ! -z "$githubMail" ] && [ ! -z "$githubUsername" ]; then
    #mvn --settings deploy-settings.xml clean deploy -e -PIT,DEB,RPM -B -U
    
    git config --global user.email "$githubMail"
    git config --global user.name "$githubUsername"
    git clean -fdx
    git checkout -f $TRAVIS_BRANCH
    git reset --hard $TRAVIS_BRANCH

    mvn -DdevelopmentVersion=$developmentVersion -Dscm.tag=$scmTag -DreleaseVersion=$releaseVersion -Dmaven.test.failure.ignore=false -Dscm.developerConnection=scm:git:git@github.com:mkruszynski/motech.git -Dscm.connection=scm:git:git@github.com:mkruszynski/motech.git release:clean release:prepare release:perform
fi

if [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
    if [ "$DB" = "mysql" ]; then
        echo "USE mysql;\nUPDATE user SET password=PASSWORD('password') WHERE user='root';\nFLUSH PRIVILEGES;\n" | mysql -u root
        mvn clean install -PIT -U
    elif [ "$DB" = "psql" ]; then
        mvn -Dmotech.sql.password=password -Dmotech.sql.user=postgres -Dmaven.test.failure.ignore=false -Dmotech.sql.driver=org.postgresql.Driver -Dmotech.sql.dbtype=psql -Dmotech.sql.url=jdbc:postgresql://localhost:5432/ clean install -PIT -U
    fi
fi