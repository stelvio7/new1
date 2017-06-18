package com.noh.util;

import com.squareup.otto.Bus;

/**
 * Created by HOME on 2017-04-10.
 */

public final class EventProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private EventProvider() {
        // No instances.
    }

}
