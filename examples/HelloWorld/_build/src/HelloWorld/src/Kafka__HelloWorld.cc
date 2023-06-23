//
// File: Inspector__HelloWorld.cc
//
#include "HelloWorld_OOA/__HelloWorld_interface.hh"
#include "HelloWorld_OOA/__HelloWorld_services.hh"

#include "swa/console.hh"
#include "swa/Process.hh"
#include "swa/RealTimeSignalListener.hh"

#include <stdexcept>
#include <iostream>
#include <csignal>
#include <boost/program_options.hpp>
#include "cppkafka/consumer.h"
#include "cppkafka/configuration.h"
#include "cppkafka/utils/consumer_dispatcher.h"

#include <thread>

using std::string;
using std::exception;
using std::function;

using cppkafka::Consumer;
using cppkafka::ConsumerDispatcher;
using cppkafka::Configuration;
using cppkafka::Message;
using cppkafka::TopicPartition;
using cppkafka::TopicPartitionList;
using cppkafka::Error;

namespace Kafka
{
  namespace masld_HelloWorld
  {

    void initialiseConsumers()
    {
      const string brokers = "kafka:9093";
      const string topic_name = "HelloWorld_hello";
      const string group_id = "masld_HelloWorld";

      // Construct the configuration
      Configuration config = {
          { "metadata.broker.list", brokers },
          { "group.id", group_id },
          // Disable auto commit
          { "enable.auto.commit", false }
      };

      // Create the consumer
      Consumer consumer(config);

      // Subscribe to the topic
      consumer.subscribe({ topic_name });

      // Create a consumer dispatcher
      ConsumerDispatcher dispatcher(consumer);

      // Stop processing on SIGINT
      ::SWA::Process::getInstance().registerShutdownListener([&]() {
        dispatcher.stop();
      });

      // create a signal listener to call the service in the main thread
      ::SWA::RealTimeSignalListener sigListener([&](int pid, int uid) {
        ::masld_HelloWorld::interceptor_masls_hello::instance().callService()();
      }, ::SWA::Process::getInstance().getActivityMonitor());

      // Now run the dispatcher, providing a callback to handle messages, one to handle
      // errors and another one to handle EOF on a partition
      dispatcher.run(
          // Callback executed whenever a new message is consumed
          [&](Message msg) {

              // call the service
              sigListener.queueSignal();

              // Now commit the message
              consumer.commit(msg);
          }
          // // Whenever there's an error (other than the EOF soft error)
          // [](Error error) {
          //     ::SWA::console() << "[+] Received error notification: " << error << std::endl;
          // },
          // // Whenever EOF is reached on a partition, print this
          // [](ConsumerDispatcher::EndOfFile, const TopicPartition& topic_partition) {
          //     ::SWA::console() << "Reached EOF on partition " << topic_partition << std::endl;
          // }
      );

    }

    bool initialise()
    {
      ::SWA::Process::getInstance().registerStartedListener([]() {
        std::thread* t1 = new std::thread(&initialiseConsumers);
      });
    }

  }
}
namespace 
{

  bool initialised = ::Kafka::masld_HelloWorld::initialise();

}
