/*
 *  ASTI Services (c) © 2013
 *  Consultoria de Software.
 */

package data.service;

import core.data.access.utils.QueryFilter;
import core.data.service.IGeneralDataService;
import core.data.session.AbstractTransactionOperation;
import data.session.HbSession;
import entities.Sysuser;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;

/**
 *
 * @author Rene Vera Apale
 */
public class SysuserService implements IGeneralDataService<Sysuser> {
    
    private final HbSession session;
    
    public SysuserService() {
        session = HbSession.getInstance();
    }

    @Override
    public Sysuser createOrUpdate(final Sysuser record) throws Exception {
        AbstractTransactionOperation op = new AbstractTransactionOperation() {
            @Override
            public Object execute(Object... params) {
                if (record.getId() == null)
                    session.getCurrentSession().save(record);
                else {
                    if (record.getPassword() == null) { // Retrieve current values because of not null constraints
                        Query qry = session.getCurrentSession().createSQLQuery("select password, passwordSalt from sysuser where id = :uid");
                        qry.setInteger("uid", record.getId());
                        Object[] creds = (Object[])qry.list().get(0);
                        record.setPassword(creds[0].toString());
                        record.setPasswordSalt(creds[1].toString());
                    }
                    session.getCurrentSession().update(record);
                }
                return record;
            }
        };
        return (Sysuser) session.execute(op);
    }

    @Override
    public void delete(final Sysuser record) throws Exception {
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
    public Sysuser getRecord(final int id) throws Exception {
        AbstractTransactionOperation op = new AbstractTransactionOperation() {
            @Override
            public Object execute(Object... params) {
                return session.getCurrentSession().get(Sysuser.class, id);
            }
        };
        return (Sysuser) session.execute(op);
    }

    @Override
    public List<Sysuser> getRecordsList(final List<QueryFilter> filters, final String orderBy, final boolean ascending) throws Exception {
        AbstractTransactionOperation op = new AbstractTransactionOperation() {
            @Override
            public Object execute(Object... params) {
                Criteria criteria = session.getCurrentSession().createCriteria(Sysuser.class);
                if (filters != null)
                    for (QueryFilter qf : filters)
                        criteria.add(session.createRestrictionByFilter(qf));
                if (orderBy != null) {
                    if (ascending)
                        criteria.addOrder(Order.asc(orderBy));
                    else
                        criteria.addOrder(Order.desc(orderBy));
                }
                return criteria.list();
            }
        };
        return (List<Sysuser>) session.execute(op);
    }
}