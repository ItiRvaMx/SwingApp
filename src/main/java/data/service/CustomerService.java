/*
 *  ASTI Services (c) © 2013
 *  Consultoria de Software.
 */

package data.service;

import core.data.access.utils.QueryFilter;
import core.data.service.IGeneralDataService;
import core.data.session.AbstractTransactionOperation;
import data.session.HbSession;
import entities.Customer;
import entities.CustomerAddress;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;

/**
 *
 * @author Rene Vera Apale
 */
public class CustomerService implements IGeneralDataService<Customer>{
    
    private final HbSession session;
    
    public CustomerService() {
        session = HbSession.getInstance();
    }

    @Override
    public Customer createOrUpdate(final Customer record) throws Exception {
        AbstractTransactionOperation op = new AbstractTransactionOperation() {
            @Override
            public Object execute(Object... params) {
                if (record.getId() == null) {
                    for (CustomerAddress address : record.getAddresses())
                        address.setCustomer(record);
                    session.getCurrentSession().save(record);
                } else {
                    for (Iterator<CustomerAddress> itr = record.getAddresses().iterator(); itr.hasNext();) {
                        CustomerAddress address = itr.next();
                        if (address.getCustomer() == null & address.getId() == null)
                            address.setCustomer(record);
                        else if (address.getCustomer()== null) {
//                            session.getCurrentSession().delete(address);
                            itr.remove();
                        }
                    }
                    session.getCurrentSession().merge(record);
                }
                return record;
            }
        };
        return (Customer) session.execute(op);
    }

    @Override
    public void delete(final Customer record) throws Exception {
        AbstractTransactionOperation op = new AbstractTransactionOperation() {
            @Override
            public Object execute(Object... params) {
                session.getCurrentSession().delete(record);
                return null;
            }
        };
        session.execute(op);
    }

    @Override
    public Customer getRecord(final int id) throws Exception {
        AbstractTransactionOperation op = new AbstractTransactionOperation() {
            @Override
            public Object execute(Object... params) {
                return session.getCurrentSession().get(Customer.class, id);
            }
        };
        return (Customer) session.execute(op);
    }

    @Override
    public List<Customer> getRecordsList(final List<QueryFilter> filters, final String orderBy, final boolean ascending) throws Exception {
        AbstractTransactionOperation op = new AbstractTransactionOperation() {
            @Override
            public Object execute(Object... params) {
                Criteria criteria = session.getCurrentSession().createCriteria(Customer.class);
                if (filters != null)
                    for (QueryFilter qf : filters)
                        criteria.add(session.createRestrictionByFilter(qf));
                if (orderBy != null)
                    criteria.addOrder(ascending ? Order.asc(orderBy) : Order.desc(orderBy));
                criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                return criteria.list();
            }
        };
        return (List<Customer>) session.execute(op);
    }
    
    public void initAddresses(final Customer record) throws Exception {
        AbstractTransactionOperation op = new AbstractTransactionOperation() {
            @Override
            public Object execute(Object... params) {
                session.getCurrentSession().update(record);
                if (!Hibernate.isInitialized(record.getAddresses()))
                    Hibernate.initialize(record.getAddresses());
                return null;
            }
        };
        session.execute(op);
    }

}