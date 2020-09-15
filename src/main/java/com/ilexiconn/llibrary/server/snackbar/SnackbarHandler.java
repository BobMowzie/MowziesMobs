package com.ilexiconn.llibrary.server.snackbar;

import net.ilexiconn.llibrary.LLibrary;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public enum SnackbarHandler {
    INSTANCE;

    /**
     * show a snackbar to the user. If called on the server, all clients will show it. If there's already a snackbar
     * showing, this snackbar will be added to the queue.
     *
     * @param snackbar the snackbar to show
     */
    public void showSnackbar(Snackbar snackbar) {
        LLibrary.PROXY.showSnackbar(snackbar);
    }
}
