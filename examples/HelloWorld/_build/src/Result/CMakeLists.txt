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
  NAME     Result_interface
  SOURCES  
           __Result_types.cc
           __Result_interface.cc
  LINKS    
           MaslCore::asn1
           MaslCore::swa
  INSTALL  
  EXPORT   ${MaslExportTarget}
  INCLUDES 
           Result_OOA/__Result_types.hh
           Result_OOA/__Result_services.hh
           Result_OOA/__Result_interface.hh
  )

simple_add_shared_library (
  NAME     Result_if_kafka
  SOURCES  
           Kafka__Result__result.cc
  LINKS    
           MaslCore::swa
           MaslCore::Kafka
           Result_interface
           cppkafka
           rdkafka
  INSTALL  
  EXPORT   ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME     Result_kafka
  SOURCES  
           Kafka__Result.cc
  LINKS    
           MaslCore::swa
           MaslCore::Kafka
           Result_interface
           cppkafka
           rdkafka
  INSTALL  
  EXPORT   ${MaslExportTarget}
  )



simple_add_shared_library (
  NAME     Result
  SOURCES  
           __Result.cc
           __Result_private_types.cc
  LINKS    
           MaslCore::swa
           Result_interface
  INSTALL  
  EXPORT   ${MaslExportTarget}
  INCLUDES 
           Result_OOA/__Result.hh
           Result_OOA/__Result_terminators.hh
           Result_OOA/__Result_private_types.hh
  )

simple_add_shared_library (
  NAME    Result_metadata
  SOURCES MetaData__Result.cc
  LINKS   
          MaslCore::MetaData
          MaslCore::swa
          Result_interface
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    Result_if_metadata
  SOURCES MetaData__if__Result.cc
  LINKS   
          MaslCore::MetaData
          MaslCore::swa
          Result_interface
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    Result_inspector
  SOURCES 
          Inspector__Result.cc
          Inspector_private_types__Result.cc
  LINKS   
          Boost::boost
          MaslCore::Inspector
          Result_if_inspector
          MaslCore::swa
          Result_interface
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    Result_if_inspector
  SOURCES Inspector_types__Result.cc
  LINKS   MaslCore::Inspector
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    Result_transient
  SOURCES transient_dummy.cc
  LINKS   
          MaslCore::transient
          Result
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_interface_library (
  NAME    Result_standalone_deps
  SOURCES 
  LINKS   Result
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_interface_library (
  NAME     Result_common_metadata
  SOURCES  
  LINKS    
  INSTALL  
  EXPORT   ${MaslExportTarget}
  INCLUDES Result_OOA/MetaData__Result.hh
  )

simple_add_interface_library (
  NAME    Result_transient_standalone_deps
  SOURCES 
  LINKS   Result_transient
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )


# ------------------------------------------------------------------------
#  Executables
# ------------------------------------------------------------------------

simple_add_executable (
  NAME    Result_transient_standalone
  SOURCES initStandalone.cc
  LINKS   
          Result_transient_standalone_deps
          Result_standalone_deps
          MaslCore::swa
  )


include (
  /root/HelloWorld_proc/src/main/masl/custom/custom.cmake
  OPTIONAL
  )

