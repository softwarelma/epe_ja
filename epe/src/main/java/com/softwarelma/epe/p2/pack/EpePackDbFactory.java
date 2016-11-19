package com.softwarelma.epe.p2.pack;

import com.softwarelma.epe.p2.exec.EpeExecAbstractFactory;

public final class EpePackDbFactory extends EpeExecAbstractFactory {

    private static EpePackDbFactory factory;

    public static EpePackDbFactory getFactoryInstance() {
        if (factory != null) {
            return factory;
        }

        synchronized (EpePackDbFactory.class) {
            if (factory != null) {
                return factory;
            }

            EpePackDbFactory factory2 = new EpePackDbFactory();
            factory = factory2;
            return factory;
        }
    }

    private EpePackDbFactory() {
    }

    @Override
    public String getClassNamPrefix() {
        return "com.softwarelma.epe.p3.db.EpeDbFinal";
    }

}
