//
// File: Kafka__HelloWorld.cc
//

#include "HelloWorld_OOA/__HelloWorld_services.hh"
#include "kafka/Kafka.hh"

namespace Kafka {
namespace masld_HelloWorld {

bool registerServices() {
  const int x = 7;
  Kafka::KafkaHandler::getInstance().subscribe("HelloWorld_add_five", [=]() {
    ::masld_HelloWorld::interceptor_masls_add_five::instance().callService()(x);
  });
  Kafka::KafkaHandler::getInstance().subscribe(
      "HelloWorld_multiply_by_two", [=]() {
        ::masld_HelloWorld::interceptor_masls_multiply_by_two::instance()
            .callService()(x);
      });

  return true;
}

} // namespace masld_HelloWorld
} // namespace Kafka

namespace {
bool masld_HelloWorld_registered =
    ::Kafka::masld_HelloWorld::registerServices();
}
