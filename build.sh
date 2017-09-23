#!/bin/bash

mvn clean install  -Dmaven.test.skip=true -f apm-pom/pom.xml || exit 1
mvn clean install  -Dmaven.test.skip=true -f apm-core/pom.xml || exit 1
mvn clean install  -Dmaven.test.skip=true -f apm-alert/pom.xml || exit 1

# 本地测试数据库连接 ，如需更改可到conf.properties配置
mysql -uroot -e 'CREATE DATABASE IF NOT EXISTS test default character set utf8 COLLATE utf8_general_ci'
if [ $? != 0 ];then
    echo '在本地测试时，需要先启动本地MySQL服务，并有权限执行下面语句：mysql -uroot -e 'CREATE DATABASE IF NOT EXISTS test default character set utf8 COLLATE utf8_general_ci''
    echo '如果你需要更改数据库连接，可以conf.properties配置'
    exit 1
fi

mvn jetty:run -Dmaven.test.skip=true -f apm-simple-web/pom.xml


