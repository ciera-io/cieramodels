//
// File: Kafka__Display__error.cc
//
#include "Display_OOA/__Display_interface.hh"
#include "Display_OOA/__Display_services.hh"
#include "swa/String.hh"
#include "swa/Process.hh"

#include "kafka/BufferedIO.hh"
#include "kafka/Producer.hh"

namespace masld_Display {
void masls_error(const ::SWA::String &maslp_message) {
  Kafka::BufferedOutputStream buf;
  buf << maslp_message;
  Kafka::Producer::getInstance().publish(
      ::SWA::Process::getInstance().getDomain("Display").getId(),
      ::masld_Display::serviceId_masls_error, buf);
}

const bool localServiceRegistration_masls_error =
    interceptor_masls_error::instance().registerLocal(&masls_error);

} // namespace masld_Display
