package org.vaadin.cancelablebutton.client.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Timer;
import com.vaadin.client.MouseEventDetailsBuilder;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.button.ButtonServerRpc;
import org.vaadin.cancelablebutton.CancelableButton;

import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.button.ButtonConnector;
import com.vaadin.shared.ui.Connect;

@Connect(CancelableButton.class)
public class CancelableButtonConnector extends ButtonConnector {

    private static final long serialVersionUID = 3008795499402562824L;
    private ClickEvent pendingClickEvent;
    private Timer timer;
    private int currentTime;
    private MouseEventDetails pendingMouseDetails;

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
    }

    @Override
    public CancelableButtonState getState() {
        return (CancelableButtonState) super.getState();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (pendingClickEvent != null) {
            pendingClickEvent = null;

            // Cancel previous timer
            if (timer != null) {
                timer.cancel();
            }

            endDelay();

        } else {
            pendingClickEvent = event;
            pendingMouseDetails = MouseEventDetailsBuilder.buildMouseEventDetails(event.getNativeEvent(), this.getWidget().getElement());

            //Start timer
            setDelay(getState().getDelay());
        }
    }

    protected void endDelay() {
        getWidget().setHtml(getState().caption);

        if (pendingClickEvent != null) {
            ClickEvent c = pendingClickEvent;
            pendingClickEvent = null;
            ((ButtonServerRpc)this.getRpcProxy(ButtonServerRpc.class)).click(pendingMouseDetails);
        }
    }

    private void updateCaption() {
        int c = currentTime / 1000;
        getWidget().setHtml(getState().caption + " (" + c + ")");
    }

    public void setDelay(int delay) {

        if (delay > 0) {
            currentTime = delay * 1000;
            updateCaption();
        } else {
            delay = 0;
        }

        // Cancel previous timer
        if (timer != null) {
            timer.cancel();
        }

        // If no delay is given stop here
        if (delay <= 0) {
            return;
        }

        // Create new timer
        timer = new Timer() {
            @Override
            public void run() {
                currentTime -= 1000;
                if (currentTime > 0) {
                    updateCaption();
                } else {
                    cancel();
                    endDelay();
                }
            }
        };
        timer.scheduleRepeating(1000);
    }
}
