/*
 *  ASTI Services (c) © 2013
 *  Consultoria de Software.
 */

package data.service;

import core.data.service.IGeneralDataService;
import java.util.HashMap;

/**
 *
 * @author Rene Vera Apale
 */
public class DataserviceFactory {

    private static final HashMap<DataService, IGeneralDataService> ACTIVE_SERVICES = new HashMap<>();
    
    public static IGeneralDataService getServiceInstance(DataService target) {
        IGeneralDataService instance = ACTIVE_SERVICES.get(target);
        if (instance == null) {
            switch (target) {
                case CUSTOMER:
                    instance = new CustomerService();
                    break;
                case SYSUSER:
                    instance = new SysuserService();
                    break;
            }
            ACTIVE_SERVICES.put(target, instance);
        }
        return instance;
    }
}