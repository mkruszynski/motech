#!/usr/bin/env bash

if [ "$DB" = "mysql" ]; then
    echo "USE mysql;\nUPDATE user SET password=PASSWORD('password') WHERE user='root';\nFLUSH PRIVILEGES;\n" | mysql -u root
elif [ "$DB" = "psql" ]; then
    sudo apt-get install postgresql
    sed -i 's/motech.sql.dbtype=mysql/motech.sql.dbtype=psql/g' ~/mkruszynski/motech/maven.properties
    cat ~/mkruszynski/motech/maven.properties
fi