//
// File: Kafka__Display.cc
//
#include "Display_OOA/__Display_interface.hh"
#include "Display_OOA/__Display_services.hh"

#include "swa/Process.hh"

#include "kafka/BufferedIO.hh"
#include "kafka/ProcessHandler.hh"
#include "kafka/ServiceHandler.hh"

namespace Kafka {
namespace masld_Display {

class masls_resultHandler : public ServiceHandler {
public:
  std::function<void()> getInvoker(BufferedInputStream &stream) const;
};

class masls_resultInvoker {

public:
  masls_resultInvoker(BufferedInputStream &stream) : maslp_value() {
    stream >> maslp_value;
  }
  void operator()() {
    ::masld_Display::interceptor_masls_result::instance().callService()(
        maslp_value);
  }

private:
  double maslp_value;
};

class masls_errorHandler : public ServiceHandler {
public:
  std::function<void()> getInvoker(BufferedInputStream &stream) const;
};

class masls_errorInvoker {

public:
  masls_errorInvoker(BufferedInputStream &stream) : maslp_message() {
    stream >> maslp_message;
  }
  void operator()() {
    ::masld_Display::interceptor_masls_error::instance().callService()(
        maslp_message);
  }

private:
  ::SWA::String maslp_message;
};

std::function<void()>
masls_resultHandler::getInvoker(BufferedInputStream &stream) const {
  return masls_resultInvoker(stream);
}

std::function<void()>
masls_errorHandler::getInvoker(BufferedInputStream &stream) const {
  return masls_errorInvoker(stream);
}

bool registerServiceHandlers() {
  ProcessHandler::getInstance().registerServiceHandler(
      ::SWA::Process::getInstance().getDomain("Display").getId(),
      ::masld_Display::serviceId_masls_result,
      std::shared_ptr<masls_resultHandler>(new masls_resultHandler()));
  auto errorHandler = std::make_shared<masls_errorHandler>();
  ProcessHandler::getInstance().registerServiceHandler(
      ::SWA::Process::getInstance().getDomain("Display").getId(),
      ::masld_Display::serviceId_masls_error,
      std::shared_ptr<masls_errorHandler>(new masls_errorHandler()));
  return true;
}

} // namespace masld_Display
} // namespace Kafka
//
namespace {

bool handlersRegistered = Kafka::masld_Display::registerServiceHandlers();

}
