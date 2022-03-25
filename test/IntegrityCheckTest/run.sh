#!/bin/bash
CIERA_VERSION=3.0.0-beta1
VERSION=1.0.0-SNAPSHOT
MVN_REPO=$HOME/.m2/repository
MODULEPATH=$MVN_REPO/io/ciera/runtime-api/$CIERA_VERSION/runtime-api-$CIERA_VERSION.jar:$MVN_REPO/io/ciera/runtime/$CIERA_VERSION/runtime-$CIERA_VERSION.jar:$MVN_REPO/io/ciera/runtime-util/$CIERA_VERSION/runtime-util-$CIERA_VERSION.jar:$MVN_REPO/io/ciera/Integrity_OOA/$VERSION/Integrity_OOA-$VERSION.jar:$MVN_REPO/io/ciera/IntegrityTest/$VERSION/IntegrityTest-$VERSION.jar
PROPS="\
  -Dio.ciera.runtime.useDeterministicIDs\
  -Dio.ciera.runtime.haltWhenIdle\
"
java $PROPS --module-path $MODULEPATH -m IntegrityTest/integritytest.IntegrityTest $@
