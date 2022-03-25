# Ciera example models

## Building the examples

In a particular example folder:

```
mvn install
```

## Running the examples

In a particular example folder:

```
bash run.sh
```

## To build and run examples docker image:

### Dependencies

- [Docker](https://www.docker.com)

### Build

```
docker build -t ciera-examples .
```

### Run

```
docker run -it ciera-examples
```

Once in a shell inside the docker image, individual projects can be execused by
running their `run.sh` scripts.

### Download/run prebuilt version

```
docker run -it levistarrett:ciera-examples
```
