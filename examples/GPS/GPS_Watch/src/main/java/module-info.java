
module GPS_Watch {

    requires HeartRateMonitor;
    requires Location;
    requires Tracking;
    requires UI;
    requires transitive io.ciera.runtime;
    requires jsonrpc4j;

}
