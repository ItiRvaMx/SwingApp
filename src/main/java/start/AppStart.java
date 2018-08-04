/*
 *  ASTI Services (c) © 2013
 *  Consultoria de Software.
 */

package start;

import config.SwingConfig;
import core.config.AppConfig;
import data.session.HbSession;
import java.io.IOException;
import javax.swing.UIManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rene Vera Apale
 */
public class AppStart {

    public static void main(String[] args) throws IOException {
        System.out.println(UIManager.getIcon("OptionPane.errorIcon"));
    }
}