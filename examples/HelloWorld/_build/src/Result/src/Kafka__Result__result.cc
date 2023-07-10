//
// File: Kafka__Result__result.cc
//

#include "Result_OOA/__Result_services.hh"
#include "kafka/Kafka.hh"
#include "swa/String.hh"

namespace masld_Result {

void masls_result(const ::SWA::String &maslp_r) {
  Kafka::KafkaHandler::getInstance().publish("Result_result",
                                             std::string(maslp_r));
}

const bool localServiceRegistration_masls_result =
    interceptor_masls_result::instance().registerLocal(&masls_result);

} // namespace masld_Result
