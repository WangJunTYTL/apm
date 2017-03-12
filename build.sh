#!/bin/bash

mvn clean install  -Dmaven.test.skip=true -f apm-pom/pom.xml || exit 1
mvn clean install  -Dmaven.test.skip=true -f apm-perf4j/pom.xml || exit 1
mvn clean install  -Dmaven.test.skip=true -f apm-extension/pom.xml || exit 1
mvn clean install  -Dmaven.test.skip=true -f apm-alert/pom.xml || exit 1

mvn jetty:run -Dmaven.test.skip=true -f apm-dashboard/pom.xml


