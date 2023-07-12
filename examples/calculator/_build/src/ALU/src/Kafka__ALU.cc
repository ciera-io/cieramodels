//
// File: Kafka__ALU.cc
//
#include "ALU_OOA/__ALU_interface.hh"
#include "ALU_OOA/__ALU_services.hh"

#include "kafka/ActionHandler.hh"
#include "kafka/Kafka.hh"

namespace Kafka {
namespace masld_ALU {


bool registerServiceHandlers() {
  return true;
}

} // namespace masld_ALU
} // namespace Kafka
//
namespace {

bool handlersRegistered = Kafka::masld_ALU::registerServiceHandlers();

}
