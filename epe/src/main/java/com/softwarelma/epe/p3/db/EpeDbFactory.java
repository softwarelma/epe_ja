package com.softwarelma.epe.p3.db;

import com.softwarelma.epe.p2.exec.EpeExecAbstractFactory;

public final class EpeDbFactory extends EpeExecAbstractFactory {

    private static EpeDbFactory factory;

    public static EpeDbFactory getFactoryInstance() {
        if (factory != null) {
            return factory;
        }

        synchronized (EpeDbFactory.class) {
            if (factory != null) {
                return factory;
            }

            EpeDbFactory factory2 = new EpeDbFactory();
            factory = factory2;
            return factory;
        }
    }

    private EpeDbFactory() {
    }

    @Override
    public String getClassNamPrefix() {
        return "com.softwarelma.epe.p3.db.EpeDbFinal";
    }

}
