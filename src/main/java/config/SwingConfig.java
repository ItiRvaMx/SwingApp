/*
 *  ASTI Services (c) © 2013
 *  Consultoria de Software.
 */

package config;

import core.config.AppConfig;
import java.util.Properties;

/**
 *
 * @author Rene Vera Apale
 */
public class SwingConfig extends AppConfig {

    public SwingConfig(String appName, String fileName, String filePath) {
        super(appName, fileName, filePath);
    }

    @Override
    public Properties getDefault() {
        Properties props = new Properties();
        props.setProperty(GlobalCfg.DRIVER_NAME, "org.postgresql.Driver");
        props.setProperty(GlobalCfg.PASSWORD, "postgres");
        props.setProperty(GlobalCfg.URL, "jdcb:postgresql://localhost/demoapp");
        props.setProperty(GlobalCfg.USER, "postgres");
        return props;
    }

}