package org.vaadin.cancelablebutton;

import org.vaadin.cancelablebutton.client.ui.CancelableButtonState;

import com.vaadin.ui.Button;

/**
 * Button that can be cancelled by clicking egain within a given delay.
 **
 */
public class CancelableButton extends Button {

    private static final long serialVersionUID = 8658800951893182452L;

    /**
     * Creates a new cancellable button with caption. Default delay is 3 seconds.
     *
     * @param caption
     *            the Button caption
     */
    public CancelableButton(String caption) {
        super(caption);
        setDelay(3);
    }

    /**
     * Creates a new cancellable button with caption and delay delay
     *
     * @param caption
     *            the Button caption
     * @param delaySeconds
     *            Number of seconds as specified for {@link #setDelay(int)}
     */
    public CancelableButton(String caption, int delaySeconds) {
        super(caption);
        setDelay(delaySeconds);
    }

    /**
     * Creates a new cancellable button with delay and click listener.
     *
     * @param caption
     *            the Button caption.
     * @param delaySeconds
     *            Number of seconds as specified for {@link #setDelay(int)}
     * @param listener
     *            the Button click listener.
     */
    public CancelableButton(String caption, int delaySeconds,
                            ClickListener listener) {
        super(caption, listener);
        setDelay(delaySeconds);
    }

    /**
     * Set delay in seconds after which the button is enabled. Makes the button
     * inactive but does not affect the enabled-property.
     *
     * @param seconds
     *            Number of seconds. Zero or negative number disables the
     *            behaviour.
     * @see #setEnabled(boolean) 
     */
    public void setDelay(int seconds) {
        getState().setDelay(seconds > 0 ? seconds : 0);
        requestRepaint();
    }

    @Override
    public CancelableButtonState getState() {
        return (CancelableButtonState) super.getState();
    }
}
