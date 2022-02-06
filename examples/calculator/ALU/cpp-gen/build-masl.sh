#!/bin/bash
MASLBASE="docker run -it -v $PWD:/workspace levistarrett/masl-base"
MASLMC="docker run -v $PWD:/workspace levistarrett/masl-compiler"
$MASLMC -mod /workspace/masl/ALU/ALU.mod -test -output /workspace/cpp-gen/src/ALU
$MASLMC -domainpath /workspace/masl/ALU -prj /workspace/masl/calculator/calculator.prj -output /workspace/cpp-gen/src/calculator
$MASLBASE bash -c "cd cpp-gen && cmake . -DCMAKE_INSTALL_PREFIX=/workspace/cpp-gen"
$MASLBASE bash -c "cd cpp-gen && make install"
