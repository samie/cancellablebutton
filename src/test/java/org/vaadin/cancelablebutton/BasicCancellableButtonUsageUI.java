package org.vaadin.cancelablebutton;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import org.vaadin.addonhelpers.AbstractTest;

/**
 * Add many of these with different configurations,
 * combine with different components, for regressions
 * and also make them dynamic if needed.
 */
public class BasicCancellableButtonUsageUI extends AbstractTest {

    @Override
    public Component getTestComponent() {
        HorizontalLayout l = new HorizontalLayout();

        CancelableButton btn = new CancelableButton("Click me", 2);
        btn.addClickListener(e -> {
            Notification.show("Click ok");
        });
        btn.setClickConfirms(true);

        Button start = new Button("Start 3s delay", e -> {btn.clickWithDelay(3);});
        Button start2 = new Button("Start 0s delay", e -> {btn.clickWithDelay(0);});

        l.addComponents(btn,start, start2);

        return l;
    }

}
