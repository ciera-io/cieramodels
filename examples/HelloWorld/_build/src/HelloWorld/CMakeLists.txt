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
           MaslCore::asn1
           MaslCore::swa
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
           HelloWorld_interface
           Boost::boost
           MaslCore::swa
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
          HelloWorld_interface
          MaslCore::MetaData
          HelloWorld
          MaslCore::swa
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    HelloWorld_if_metadata
  SOURCES MetaData__if__HelloWorld.cc
  LINKS   
          HelloWorld_interface
          MaslCore::MetaData
          MaslCore::swa
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
          HelloWorld_interface
          Boost::boost
          HelloWorld
          MaslCore::Inspector
          MaslCore::swa
          HelloWorld_if_inspector
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

