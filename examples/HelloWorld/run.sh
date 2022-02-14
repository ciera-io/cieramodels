#!/bin/bash
PROPS="-Dio.ciera.runtime.logLevel=FINEST -Dio.ciera.runtime.haltWhenIdle"
java $PROPS -jar target/HelloWorld-*-jar-with-dependencies.jar $@
