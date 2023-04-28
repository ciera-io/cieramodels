
module GPS_Watch {

    uses heartratemonitor.heartratemonitor.HeartRateMonitor;
    uses location.location.Location;
    uses tracking.tracking.Tracking;
    uses ui.ui.UI;

    requires HeartRateMonitor;
    requires Location;
    requires Tracking;
    requires UI;
    requires transitive io.ciera.runtime;
    requires jsonrpc4j;

}
