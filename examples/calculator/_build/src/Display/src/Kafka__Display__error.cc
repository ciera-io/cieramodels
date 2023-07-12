//
// File: Kafka__Display__error.cc
//
#include "Display_OOA/__Display_interface.hh"
#include "Display_OOA/__Display_services.hh"
#include "swa/String.hh"

#include "kafka/Kafka.hh"
#include "kafka/BufferedIO.hh"

namespace masld_Display
{
  void masls_error ( const ::SWA::String& maslp_message )
  {
    Kafka::BufferedOutputStream buf;
    buf.write(maslp_message);
    Kafka::KafkaHandler::getInstance().publish("Display_error", buf);
  }

  const bool localServiceRegistration_masls_error = interceptor_masls_error::instance().registerLocal( &masls_error );

}
