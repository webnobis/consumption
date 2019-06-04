#!/bin/sh
${java.home}/bin/java -cp 'lib/*' -jar ${project.build.finalName}.${project.packaging} ${deliver.repository.path}