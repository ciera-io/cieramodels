add_library(rdkafka SHARED IMPORTED)
set_property(TARGET rdkafka PROPERTY IMPORTED_LOCATION "/opt/rdkafka/lib/librdkafka.so")
include_directories("/opt/rdkafka/include")
add_library(cppkafka SHARED IMPORTED)
set_property(TARGET cppkafka PROPERTY IMPORTED_LOCATION "/opt/cppkafka/lib/libcppkafka.so")
include_directories("/opt/cppkafka/include")

add_compile_options ( "-w" )

add_compile_options ( "-fpermissive" )

set (
  CMAKE_SHARED_LINKER_FLAGS
  "-Wl,--no-as-needed"
  )

set (
  CMAKE_EXE_LINKER_FLAGS
  "-Wl,--no-as-needed"
  )

find_package (
  Boost
  REQUIRED
  QUIET
  COMPONENTS
  system
  )

# ------------------------------------------------------------------------
#  Archives
# ------------------------------------------------------------------------


# ------------------------------------------------------------------------
#  Libraries
# ------------------------------------------------------------------------

simple_add_shared_library (
  NAME     HelloWorld_interface
  SOURCES  
           __HelloWorld_types.cc
           __HelloWorld_interface.cc
  LINKS    
           MaslCore::swa
           MaslCore::asn1
  INSTALL  
  EXPORT   ${MaslExportTarget}
  INCLUDES 
           HelloWorld_OOA/__HelloWorld_types.hh
           HelloWorld_OOA/__HelloWorld_services.hh
           HelloWorld_OOA/__HelloWorld_interface.hh
  )

simple_add_shared_library (
  NAME     HelloWorld
  SOURCES  
           __HelloWorld.cc
           __HelloWorld_private_types.cc
           __HelloWorld__RESULT.cc
           __HelloWorld__RESULT__result.cc
           __HelloWorld__multiply_by_two.cc
           __HelloWorld__add_five.cc
           __HelloWorld__testcase1.cc
  LINKS    
           Boost::boost
           MaslCore::swa
           HelloWorld_interface
  INSTALL  
  EXPORT   ${MaslExportTarget}
  INCLUDES 
           HelloWorld_OOA/__HelloWorld.hh
           HelloWorld_OOA/__HelloWorld_terminators.hh
           HelloWorld_OOA/__HelloWorld_private_types.hh
  )

simple_add_shared_library (
  NAME    HelloWorld_metadata
  SOURCES MetaData__HelloWorld.cc
  LINKS   
          MaslCore::swa
          MaslCore::MetaData
          HelloWorld
          HelloWorld_interface
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    HelloWorld_if_metadata
  SOURCES MetaData__if__HelloWorld.cc
  LINKS   
          MaslCore::swa
          MaslCore::MetaData
          HelloWorld_interface
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    HelloWorld_inspector
  SOURCES 
          Inspector__HelloWorld.cc
          Inspector_private_types__HelloWorld.cc
          Inspector__HelloWorld__RESULT.cc
  LINKS   
          MaslCore::Inspector
          Boost::boost
          MaslCore::swa
          HelloWorld
          HelloWorld_interface
          HelloWorld_if_inspector
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    HelloWorld_kafka
  SOURCES 
          Kafka__HelloWorld.cc
  LINKS   
          MaslCore::Kafka
          MaslCore::swa
          HelloWorld_interface
          cppkafka
          rdkafka
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    HelloWorld_if_inspector
  SOURCES Inspector_types__HelloWorld.cc
  LINKS   MaslCore::Inspector
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    HelloWorld_transient
  SOURCES transient_dummy.cc
  LINKS   
          HelloWorld
          MaslCore::transient
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_interface_library (
  NAME    HelloWorld_standalone_deps
  SOURCES 
  LINKS   HelloWorld
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_interface_library (
  NAME     HelloWorld_common_metadata
  SOURCES  
  LINKS    
  INSTALL  
  EXPORT   ${MaslExportTarget}
  INCLUDES HelloWorld_OOA/MetaData__HelloWorld.hh
  )

simple_add_interface_library (
  NAME    HelloWorld_transient_standalone_deps
  SOURCES 
  LINKS   HelloWorld_transient
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )


# ------------------------------------------------------------------------
#  Executables
# ------------------------------------------------------------------------

simple_add_executable (
  NAME    HelloWorld_transient_standalone
  SOURCES initStandalone.cc
  LINKS   
          HelloWorld_transient_standalone_deps
          HelloWorld_standalone_deps
          MaslCore::swa
  )


include (
  /root/HelloWorld_OOA/src/main/masl/HelloWorld_OOA/custom/custom.cmake
  OPTIONAL
  )

