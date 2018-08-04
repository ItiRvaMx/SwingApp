/*
 *  ASTI Services (c) © 2013
 *  Consultoria de Software.
 */

package utils;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 *
 * @author Rene Vera Apale
 */
public class ValidationsNotifier {

    
    public static void highlightMissingJTextField(final JTextField target) {
        final Border originalBorder = target.getBorder();
        target.setBorder(BorderFactory.createLineBorder(Color.red, 1));
        target.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent fe) {
                target.setBorder(originalBorder);
                target.removeFocusListener(this);
            }
        });
    }
    
    public static void displaySimpleTooltipForJTextField(final JTextField target, int displayMillis, String message) {
        Rectangle rect = target.getBounds();
        Point onScreen = target.getLocationOnScreen();
        Double popX = onScreen.getX() + rect.getWidth() + 5,
               popY = onScreen.getY() + 3;
        JLabel popLabel = new JLabel(message);
        popLabel.setBackground(Color.decode("#fdf6ce"));
        popLabel.setOpaque(true);
        popLabel.setBorder(BorderFactory.createLineBorder(Color.yellow, 1));
        final Popup pop = PopupFactory.getSharedInstance().getPopup(target, popLabel, popX.intValue(), popY.intValue());
        pop.show();
        target.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent fe) {
              pop.hide();
            }
        });
        if (displayMillis > 0) {
            Timer timer = new Timer(displayMillis, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    pop.hide();
                }
            });
            timer.start();
        }
    }
}