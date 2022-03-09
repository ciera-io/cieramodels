import io.ciera.runtime.api.application.Application;
import io.ciera.runtime.api.domain.Domain;

import ui.ui.UI;

module UI {

    exports ui.shared;

    uses Application;
    provides Domain with UI;

    requires io.ciera.runtime;

    requires static com.googlecode.lanterna;
    requires static java.desktop;

}
