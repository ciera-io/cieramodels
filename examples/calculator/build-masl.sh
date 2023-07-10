#!/bin/bash
docker run -it -v $PWD/_build:/root levistarrett/masl-build
mkdir -p _build/bin
cp _build/src/ALU/ALU_transient_standalone _build/bin
cp _build/src/Display/Display_transient_standalone _build/bin
