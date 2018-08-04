/*
 *  ASTI Services (c) © 2013
 *  Consultoria de Software.
 */

package data.session;

import config.GlobalCfg;
import core.config.AppConfig;
import core.data.access.utils.QueryFilter;
import core.data.session.AbstractDataSession;
import core.data.session.AbstractTransactionOperation;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Rene Vera Apale
 */
public class HbSession extends AbstractDataSession {
    
    private static final Logger LOGGER = LogManager.getLogger("appLogger");
    private static HbSession INSTANCE;
    
    private HbSession() { }
    
    public static HbSession getInstance() {
        synchronized (HbSession.class) {
            if (INSTANCE == null)
                INSTANCE = new HbSession();
            return INSTANCE;
        }
    }

    @Override
    public void init(AppConfig config) {
        if (this.sessionFactory == null) {
            Configuration hbConfig = new Configuration();
            hbConfig.setProperty(GlobalCfg.USER, config.getProperty(GlobalCfg.USER))
                    .setProperty(GlobalCfg.PASSWORD, config.getProperty(GlobalCfg.PASSWORD))
                    .setProperty(GlobalCfg.URL, config.getProperty(GlobalCfg.URL))
                    .setProperty(GlobalCfg.DRIVER_NAME, config.getProperty(GlobalCfg.DRIVER_NAME))
                    .configure();
            
            this.sessionFactory = hbConfig.buildSessionFactory();
        }
    }
    
    @Override
    public void close() throws Exception {
        if (sessionFactory != null && !sessionFactory.isClosed())
            sessionFactory.close();
    }

    @Override
    public void beginTransaction() throws Exception {
        if (sessionFactory != null)
            transaction = sessionFactory.getCurrentSession().beginTransaction();
    }

    @Override
    public void commit() throws Exception {
        if (transaction != null)
            transaction.commit();
    }

    @Override
    public void rollback() throws Exception {
        if (transaction != null)
            transaction.rollback();
    }
    
    public Object execute(AbstractTransactionOperation op, Object... params) throws Exception {
        try {
            beginTransaction();
            Object res = op.execute(params);
            commit();
            return res;
        } catch(Exception commitException) {
            try {
                rollback();
            } catch(Exception rollbackException) {
                LOGGER.fatal("Exception while rolling back!", rollbackException);
            } finally {
                throw commitException;
            }
        }
    }
    
    public Criterion createRestrictionByFilter(QueryFilter qf) {
        switch (qf.getQueryType()) {
            case CONJ:
            case DISJ:
                List<QueryFilter> conditions = (List<QueryFilter>)qf.getValue();
                Criterion[] criteriaList = new Criterion[conditions.size()];
                for (int i = 0; i < conditions.size(); i++)
                    criteriaList[i] = createRestrictionByFilter(conditions.get(i));
                if (qf.getQueryType() == QueryFilter.Type.DISJ)
                    return Restrictions.disjunction(criteriaList);
                else
                    return Restrictions.conjunction(criteriaList);
            case EQ:
                return Restrictions.eq(qf.getProperty(), qf.getValue());
            case GE:
                return Restrictions.ge(qf.getProperty(), qf.getValue());
            case GT:
                return Restrictions.gt(qf.getProperty(), qf.getValue());
            case GTP:
                return Restrictions.gtProperty(qf.getProperty(), qf.getValue().toString());
            case IN:
                return Restrictions.in(qf.getProperty(), (List)qf.getValue());
            case INN:
                return Restrictions.isNotNull(qf.getProperty());
            case IS_NULL:
                return Restrictions.isNull(qf.getProperty());
            case LE:
                return Restrictions.le(qf.getProperty(), qf.getValue());
            case LK:
                return Restrictions.ilike(qf.getProperty(), qf.getValue().toString(), MatchMode.ANYWHERE);
            case LT:
                return Restrictions.lt(qf.getProperty(), qf.getValue());
            case LTP:
                return Restrictions.leProperty(qf.getProperty(), qf.getValue().toString());
            default:
                return Restrictions.ne(qf.getProperty(), qf.getValue());
        }
    }
}