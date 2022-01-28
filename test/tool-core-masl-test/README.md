# tool-core-masl-test

This model provides regression tests for the MASL code generator.

## Project structure

The project directory structure is as follows:

```
▾  masl/
  ▾  EEs/
       A.int
       TRACE.int
  ▾  tests/
    ▸  test1/
    ▸  test2/
    ▸  test3/
▸  src/
   pom.xml
   README.md
   run.sh
   test_all.sh
```

- Each subdirectory of `masl/tests/` is taken to be a buildable test domain.
- The name of the subdirectory itself is taken to be the name of the test.
- The `masl/` and `tests/` directories should not be moved or renamed.
- Additional MASL resources may be included in the `masl/` directories (or
  subdirectories other than `test/`) and may be included in test domain folders
  via symbolic link.
- Hand written Java implementations may be included in the `src/` directory.

## Writing a new test

To write a new test, create a subdirectory of the `masl/tests/` directory.
Populate this directory with exactly one `.mod` domain model file and any
number of `.int` or MASL body implementation files. The test should have at
least one domain service marked with `pragma startup(true)` to function as the
entry point for the test.

### Evaluating test conditions

The test is considered to have passed if it exits without error. The "Assert"
(`A`) bridge has been provided by the architecture to facilitate testing:

```
domain A is
  
  //! Throws a runtime error if the value of "truth" is false
  public service assertTrue(truth: in boolean);

  //! Throws a runtime error if the value of "truth" is false
  //! The given message is attached to the error
  public service assertTrueWithMessage(truth: in boolean, message: in string);

end domain;
pragma utility("io.ciera.runtime.util");
```

The `assertTrue` service evaluates the given argument and throws a runtime
error if the expression is `false`. This error results in the immediate exit of
the generated model with a non-zero exit code and subsequent failure of the
test. If the value of the given argument is `true`, the `assertTrue` bridge is a no-op.
The `assertTrue` bridge may be called multiple times in a single test.

### Tracing

The "Tracing" (`TRACE`) bridge has been defined to provide a mechanism for
logging during testing. The interface is as follows:

```
domain TRACE is
  
  public service debug() return device;
  public service info() return device;
  public service warn() return device;
  public service error() return device;

end domain;
pragma utility();
```

Each service in the `TRACE` bridge returns a MASL `device` which may be written
to using the stream operators (`<<<` and `<<`):

```
TRACE::debug() <<< "Hello, world!";
```

The implementation of this bridge is defined in hand written java in the `src/`
folder and may be changed to suit the needs of the tester.

## Building and running a specific test

To build a specific test:

```
mvn install -Ddomain=test1
```

To run the test:

```
bash run.sh
```

## Building and running all tests

To build and run all tests:

```
bash test_all.sh
```
