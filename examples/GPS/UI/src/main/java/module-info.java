module UI {

    exports ui.shared;
    exports ui.ui;

    provides ui.ui.UI with ui.ui.internal.UI;

    requires io.ciera.runtime.util;
    requires transitive io.ciera.runtime;
    requires static com.googlecode.lanterna;
    requires static java.desktop;
    requires jsonrpc4j;

}
