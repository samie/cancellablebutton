package org.vaadin.cancelablebutton.unit;

import com.vaadin.ui.Notification;
import org.junit.Test;
import org.vaadin.cancelablebutton.CancelableButton;

import java.time.LocalDate;

public class MyComponentUnitTest {

    @Test
    public void testMaxClickCount() {
        CancelableButton myComponent = new CancelableButton("Click me",
                2,
                e -> {
                    Notification.show("Clicked "+ LocalDate.now());
                });
    }
    
}
