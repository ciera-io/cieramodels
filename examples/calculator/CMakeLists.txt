cmake_minimum_required(VERSION 3.5)

set(CMAKE_CXX_STANDARD 14)

project(calculator VERSION 1.0)

set(CMAKE_PREFIX_PATH ${CMAKE_PREFIX_PATH} /opt/masl)
set(CMAKE_PREFIX_PATH ${CMAKE_PREFIX_PATH} /opt/poco)

set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} /opt/masl/lib/cmake/MaslCore)
# set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} /opt/masl/lib/cmake/UtilityDomains)
set(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} /opt/poco/lib/cmake/Poco)

set(INSTALL_LIB_DIR lib CACHE PATH "Installation directory for libraries")
set(INSTALL_BIN_DIR bin CACHE PATH "Installation directory for executables")
set(INSTALL_INCLUDE_DIR include CACHE PATH "Installation directory for header files")

include(MaslCoreConfig)
# include(UtilityDomainsConfig)
include(SimpleAdd)

find_package(Poco REQUIRED COMPONENTS XML Foundation)
find_package(Sqlite3 REQUIRED)
find_package(Boost REQUIRED QUIET COMPONENTS system)
link_libraries(Boost::boost)

add_library(rdkafka SHARED IMPORTED)
set_property(TARGET rdkafka PROPERTY IMPORTED_LOCATION "/opt/rdkafka/lib/librdkafka.so")
include_directories("/opt/rdkafka/include")
add_library(cppkafka SHARED IMPORTED)
set_property(TARGET cppkafka PROPERTY IMPORTED_LOCATION "/opt/cppkafka/lib/libcppkafka.so")
include_directories("/opt/cppkafka/include")

add_subdirectory(src/ALU)
add_subdirectory(src/Display)
add_subdirectory(src/calculator)
