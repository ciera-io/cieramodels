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
  NAME     ALU_interface
  SOURCES  
           __ALU_types.cc
           __ALU_interface.cc
  LINKS    
           MaslCore::asn1
           MaslCore::swa
  INSTALL  
  EXPORT   ${MaslExportTarget}
  INCLUDES 
           ALU_OOA/__ALU_types.hh
           ALU_OOA/__ALU_services.hh
           ALU_OOA/__ALU_interface.hh
           ALU_OOA/__ALU__InputError.hh
  )

simple_add_shared_library (
  NAME     ALU
  SOURCES  
           __ALU.cc
           __ALU_private_types.cc
           __ALU__Memory.cc
           __ALU__Display.cc
           __ALU__Operation.cc
           __ALU__Calculator.cc
           __ALU___TEST_disp_result.cc
           __ALU___TEST_disp_error.cc
           __ALU__disp.cc
           __ALU__MemoryPopulation.cc
           __ALU__DisplayPopulation.cc
           __ALU__OperationPopulation.cc
           __ALU__CalculatorPopulation.cc
           __ALU___TEST_disp_resultPopulation.cc
           __ALU___TEST_disp_errorPopulation.cc
           __ALU__disp__result.cc
           __ALU__disp__error.cc
           __ALU__Memory__store.cc
           __ALU__Memory__reset.cc
           __ALU__Display__clearDisplay.cc
           __ALU__Display__displaying_memory_value.cc
           __ALU__Display__toggling_sign.cc
           __ALU__Display__receiving_whole_part.cc
           __ALU__Display__receiving_fractional_part.cc
           __ALU__Display__evaluating.cc
           __ALU__Display__displaying_error.cc
           __ALU__Operation__evaluate.cc
           __ALU__Calculator__init.cc
           __ALU__key.cc
           __ALU__clear.cc
           __ALU__testcase1.cc
           __ALU__testcase2.cc
           __ALU__testcase3.cc
           __ALU__testcase4.cc
           __ALU__testcase5.cc
  LINKS    
           ALU_interface
           MaslCore::swa
           Boost::boost
  INSTALL  
  EXPORT   ${MaslExportTarget}
  INCLUDES 
           ALU_OOA/__ALU.hh
           ALU_OOA/__ALU_terminators.hh
           ALU_OOA/__ALU_private_types.hh
  )

simple_add_shared_library (
  NAME    ALU_metadata
  SOURCES MetaData__ALU.cc
  LINKS   
          ALU_interface
          ALU
          MaslCore::MetaData
          MaslCore::swa
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    ALU_if_metadata
  SOURCES MetaData__if__ALU.cc
  LINKS   
          ALU_interface
          MaslCore::MetaData
          MaslCore::swa
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    ALU_inspector
  SOURCES 
          Inspector__ALU.cc
          Inspector_private_types__ALU.cc
          Inspector__ALU__Memory.cc
          Inspector__ALU__Display.cc
          Inspector__ALU__Operation.cc
          Inspector__ALU__Calculator.cc
          Inspector__ALU___TEST_disp_result.cc
          Inspector__ALU___TEST_disp_error.cc
          Inspector__ALU__disp.cc
  LINKS   
          ALU_interface
          ALU_if_inspector
          ALU
          MaslCore::Inspector
          MaslCore::swa
          Boost::boost
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    ALU_if_inspector
  SOURCES Inspector_types__ALU.cc
  LINKS   MaslCore::Inspector
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    ALU_transient
  SOURCES 
          Transient__ALU__Memory.cc
          Transient__ALU__MemoryPopulation.cc
          Transient__ALU__Display.cc
          Transient__ALU__DisplayPopulation.cc
          Transient__ALU__Operation.cc
          Transient__ALU__OperationPopulation.cc
          Transient__ALU__Calculator.cc
          Transient__ALU__CalculatorPopulation.cc
          Transient__ALU___TEST_disp_result.cc
          Transient__ALU___TEST_disp_resultPopulation.cc
          Transient__ALU___TEST_disp_error.cc
          Transient__ALU___TEST_disp_errorPopulation.cc
  LINKS   
          ALU
          MaslCore::swa
          MaslCore::transient
          Boost::boost
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_interface_library (
  NAME    ALU_standalone_deps
  SOURCES 
  LINKS   ALU
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_interface_library (
  NAME     ALU_common_metadata
  SOURCES  
  LINKS    
  INSTALL  
  EXPORT   ${MaslExportTarget}
  INCLUDES ALU_OOA/MetaData__ALU.hh
  )

simple_add_interface_library (
  NAME    ALU_transient_standalone_deps
  SOURCES 
  LINKS   ALU_transient
  INSTALL 
  EXPORT  ${MaslExportTarget}
  )

simple_add_shared_library (
  NAME    ALU_kafka
  SOURCES 
          Kafka__ALU.cc
  LINKS   
          MaslCore::Kafka
          ALU_interface
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
  NAME    ALU_transient_standalone
  SOURCES initStandalone.cc
  LINKS   
          ALU_transient_standalone_deps
          ALU_standalone_deps
          MaslCore::swa
  )


include (
  /root/ALU_OOA/custom/custom.cmake
  OPTIONAL
  )

