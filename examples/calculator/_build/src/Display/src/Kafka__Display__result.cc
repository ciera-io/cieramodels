//
// File: __Display__result.cc
//
#include "Display_OOA/__Display_interface.hh"
#include "Display_OOA/__Display_services.hh"
#include "swa/Process.hh"

#include "kafka/BufferedIO.hh"
#include "kafka/Producer.hh"

namespace masld_Display {
void masls_result(double maslp_value) {
  Kafka::BufferedOutputStream buf;
  buf << maslp_value;
  Kafka::Producer::getInstance().publish(
      ::SWA::Process::getInstance().getDomain("Display").getId(),
      ::masld_Display::serviceId_masls_result, buf);
}

const bool localServiceRegistration_masls_result =
    interceptor_masls_result::instance().registerLocal(&masls_result);

} // namespace masld_Display
