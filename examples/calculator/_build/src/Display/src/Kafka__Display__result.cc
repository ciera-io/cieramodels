//
// File: __Display__result.cc
//
#include "Display_OOA/__Display_interface.hh"
#include "Display_OOA/__Display_services.hh"

#include "kafka/Kafka.hh"
#include "kafka/BufferedIO.hh"

namespace masld_Display
{
  void masls_result ( double maslp_value )
  {
    Kafka::BufferedOutputStream buf;
    buf.write(maslp_value);
    Kafka::KafkaHandler::getInstance().publish("Display_result", buf);
  }

  const bool localServiceRegistration_masls_result = interceptor_masls_result::instance().registerLocal( &masls_result );

}
