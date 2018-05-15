package org.vaadin.cancelablebutton.client.ui;

import com.vaadin.shared.ui.button.ButtonState;


public class CancelableButtonState extends ButtonState {

    private static final long serialVersionUID = -2146393822472798222L;

    private int delay = 0;

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }
}
