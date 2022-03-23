import io.ciera.runtime.api.domain.Domain;

import sis.SIS;

module SIS {

    exports sis;

    opens sis.classes to io.ciera.runtime;

    provides Domain with SIS;

    requires io.ciera.runtime.util;
    requires transitive io.ciera.runtime;
    requires static commons.csv;

}
