#!/bin/bash
MASLMC="docker run -v $PWD:/root levistarrett/masl-compiler -skiptranslator Sqlite"
$MASLMC -domainpath /opt/masl/lib/masl:. -mod HelloWorld_OOA/src/main/masl/HelloWorld_OOA/HelloWorld.mod -output _build/src/HelloWorld && \
$MASLMC -domainpath /opt/masl/lib/masl:HelloWorld_OOA/src/main/masl -prj HelloWorld_proc/src/main/masl/HelloWorld_proc.prj -output _build/src/HelloWorld_proc && \
cp CMakeLists.txt _build
