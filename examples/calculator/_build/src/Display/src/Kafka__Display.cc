//
// File: Kafka__Display.cc
//
#include "Display_OOA/__Display_interface.hh"
#include "Display_OOA/__Display_services.hh"

#include "kafka/ActionHandler.hh"
#include "kafka/Kafka.hh"

namespace Kafka {
namespace masld_Display {

class masls_resultHandler : public ActionHandler {
  std::function<void()> getInvoker(MessageQueue &queue) const;
};

class masls_resultInvoker {

public:
  masls_resultInvoker(MessageQueue &queue) : maslp_value() {
    queue >> maslp_value;
  }
  void operator()() {
    ::masld_Display::interceptor_masls_result::instance().callService()(
        maslp_value);
  }

private:
  double maslp_value;
};

class masls_errorHandler : public ActionHandler {
  std::function<void()> getInvoker(MessageQueue &queue) const;
};

class masls_errorInvoker {

public:
  masls_errorInvoker(MessageQueue &queue) : maslp_message() {
    queue >> maslp_message;
  }
  void operator()() {
    ::masld_Display::interceptor_masls_error::instance().callService()(
        maslp_message);
  }

private:
  ::SWA::String maslp_message;
};

std::function<void()>
masls_resultHandler::getInvoker(MessageQueue &queue) const {
  return masls_resultInvoker(queue);
}

std::function<void()>
masls_errorHandler::getInvoker(MessageQueue &queue) const {
  return masls_errorInvoker(queue);
}

bool registerServiceHandlers() {
  // KafkaHandler::getInstance().subscribe("Display_result", masls_resultHandler());
  // KafkaHandler::getInstance().subscribe("Display_error", masls_errorHandler());
  return true;
}

} // namespace masld_Display
} // namespace Kafka
//
namespace {

bool handlersRegistered = Kafka::masld_Display::registerServiceHandlers();

}
