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
    private Timer timer;
    private int currentTime;
    private MouseEventDetails pendingMouseDetails;

    @Override
    public void init() {
        super.init();
        registerRpc(CancellableButtonClientRpc.class, new CancellableButtonClientRpc() {
            @Override
            public void clickWithDelay() {
                resetTimer();
                resetCaption();
                dummyClick();
            }
            @Override
            public void restartTimer() {
                startTimer(getState().delay);
            }

            @Override
            public void cancelClick() {
                resetCaption();
                resetTimer();
            }

        });
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
    }

    @Override
    public CancelableButtonState getState() {
        return (CancelableButtonState) super.getState();
    }

    private void dummyClick() {

            // Create dummy event
            ClickEvent e = new ClickEvent() {
            };
            this.onClick(e);
    }


    @Override
    public void onClick(ClickEvent event) {

        if (pendingMouseDetails != null) {

            if (getState().clickConfirms) {
                // If we click does not cancel (i.e. confirms) we just send the click
                pendingMouseDetails = null;
                resetTimer();
                resetCaption();
                sendClickRpc(buildMouseEventDetails(event));
            } else {
                // Cancel the pending click
                resetCaption();
                resetTimer();
                return;
            }

        } else {
            // Click to start new timer. Store mouse details.
            pendingMouseDetails = buildMouseEventDetails(event);

            if (getState().delay <= 0) {
                // If no delay specified, just click right away
                resetCaption();
                sendClickRpc(pendingMouseDetails);
            } else {
                //Start timer
                startTimer(getState().delay);
            }
        }
    }

    /** Extract mouse details from a real event or generate one from dummy event. */
    private MouseEventDetails buildMouseEventDetails(ClickEvent event) {
        MouseEventDetails d;
        if (event.getSource() == null) {
            // Create dummy mouse details
            d = new MouseEventDetails();
            d.setButton(MouseEventDetails.MouseButton.LEFT);
        } else {
            // Use the original mouse details
            d = MouseEventDetailsBuilder.buildMouseEventDetails(event.getNativeEvent(), this.getWidget().getElement());
        }
        return d;
    }

    /** Reset caption to the original state. */
    protected void resetCaption() {
        getWidget().setHtml(getState().caption);
    }

    /** Send pending click, if present. */
    private void sendPendingClick() {
        if (pendingMouseDetails != null) {
            MouseEventDetails d = pendingMouseDetails;
            pendingMouseDetails = null;
            sendClickRpc(d);
        }
    }


    /** Utility to send the event to server. */
    private void sendClickRpc(MouseEventDetails d) {
        ((ButtonServerRpc)this.getRpcProxy(ButtonServerRpc.class)).click(d);

    }


    /** Update caption based on current timer. */
    private void updateCaption() {
        int c = currentTime / 1000;
        getWidget().setHtml(getState().caption + " (" + c + ")");
    }

    /** Reset timer for any pending click. */
    private void resetTimer() {
        pendingMouseDetails = null;
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }

    /** Start timer with given delay. */
    private void startTimer(int delay) {

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
        timer = null;

        // If no delay is given stop here
        if (delay <= 0) {
            resetCaption();
            sendPendingClick();
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
                    this.cancel();
                    resetCaption();
                    sendPendingClick();
                }
            }
        };
        timer.scheduleRepeating(1000);
    }
}
