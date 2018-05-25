package org.vaadin.cancelablebutton.client.ui;

import com.vaadin.shared.communication.ClientRpc;

/** Server to client RPC interface. */
public interface CancellableButtonClientRpc extends ClientRpc {

    void clickWithDelay();

    void restartTimer();

    void cancelClick();

}
