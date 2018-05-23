package org.vaadin.cancelablebutton.client.ui;

import com.vaadin.shared.ui.button.ButtonState;


public class CancelableButtonState extends ButtonState {

    private int delay = 0;
    public boolean clickWithDelay = false;

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

}
