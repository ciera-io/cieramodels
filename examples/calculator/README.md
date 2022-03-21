# calculator

## Build

### C++

Assure the docker daemon is running.

```
./build-masl.sh
```

### Java

```
mvn install
```

## Run

### C++

```
docker run -it -v $PWD:/workspace levistarrett/masl-exe bin/calculator_transient
```

### Java

```
bash run.sh
```
