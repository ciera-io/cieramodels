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
  NAME     Display_interface
  SOURCES  
           __Display_types.cc
           __Display_interface.cc
  LINKS    
           MaslCore::asn1
           MaslCore::swa
  INSTALL  
  EXPORT   ${MaslExportTarget}
  INCLUDES 
           Display_OOA/__Display_types.hh
           Display_OOA/__Display_services.hh
           Display_OOA/__Display_interface.hh
  )

simple_add_shared_library (
  NAME     Display
  SOURCES  
           __Display.cc
           __Display_private_types.cc
           __Display__result.cc
           __Display__error.cc
  LINKS    
           Display_interface
           MaslCore::swa
  INSTALL  
  EXPORT   ${MaslExportTarget}
  INCLUDES 
           Display_OOA/__Display.hh
           Display_OOA/__Display_terminators.hh
           Display_OOA/__Display_private_types.hh
  )

simple_add_shared_library (
  NAME    Display_metadata
  SOURCES MetaData__Display.cc
  LINKS   
          MaslCore::MetaData
          Display_interface
          MaslCore::swa
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    Display_if_metadata
  SOURCES MetaData__if__Display.cc
  LINKS   
          MaslCore::MetaData
          Display_interface
          MaslCore::swa
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    Display_inspector
  SOURCES 
          Inspector__Display.cc
          Inspector_private_types__Display.cc
  LINKS   
          MaslCore::Inspector
          Boost::boost
          Display_interface
          Display_if_inspector
          MaslCore::swa
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    Display_if_inspector
  SOURCES Inspector_types__Display.cc
  LINKS   MaslCore::Inspector
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    Display_transient
  SOURCES transient_dummy.cc
  LINKS   
          Display
          MaslCore::transient
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_interface_library (
  NAME    Display_standalone_deps
  SOURCES 
  LINKS   Display
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_interface_library (
  NAME     Display_common_metadata
  SOURCES  
  LINKS    
  INSTALL  
  EXPORT   ${MaslExportTarget}
  INCLUDES Display_OOA/MetaData__Display.hh
  )

simple_add_interface_library (
  NAME    Display_transient_standalone_deps
  SOURCES 
  LINKS   Display_transient
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    Display_kafka
  SOURCES 
          Kafka__Display.cc
  LINKS   
          MaslCore::Kafka
          Display_interface
          MaslCore::swa
          cppkafka
          rdkafka
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

# ------------------------------------------------------------------------
#  Executables
# ------------------------------------------------------------------------

simple_add_executable (
  NAME    Display_transient_standalone
  SOURCES initStandalone.cc
  LINKS   
          Display_transient_standalone_deps
          Display_standalone_deps
          MaslCore::swa
  )


include (
  /root/Display_OOA/custom/custom.cmake
  OPTIONAL
  )

