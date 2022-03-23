#!/bin/bash
MASLMC="docker run -v $PWD:/workspace levistarrett/masl-compiler -skiptranslator Sqlite"
$MASLMC -domainpath /root/masl/utils:. -mod STRING_OOA/STRING.mod -output _build/src/STRING && \
$MASLMC -domainpath /root/masl/utils:. -mod HelloWorld_OOA/HelloWorld.mod -output _build/src/HelloWorld && \
$MASLMC -domainpath /root/masl/utils:. -prj HelloWorld_proc/HelloWorld_proc.prj -output _build/src/HelloWorld_proc && \
cp CMakeLists.txt _build
docker run -it -v $PWD/_build:/workspace levistarrett/masl-build
