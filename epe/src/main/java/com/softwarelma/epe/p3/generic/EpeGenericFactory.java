package com.softwarelma.epe.p3.generic;

import com.softwarelma.epe.p2.exec.EpeExecAbstractFactory;

public final class EpeGenericFactory extends EpeExecAbstractFactory {

    private static EpeGenericFactory factory;

    public static EpeGenericFactory getFactoryInstance() {
        if (factory != null) {
            return factory;
        }

        synchronized (EpeGenericFactory.class) {
            if (factory != null) {
                return factory;
            }

            EpeGenericFactory factory2 = new EpeGenericFactory();
            factory = factory2;
            return factory;
        }
    }

    private EpeGenericFactory() {
    }

    @Override
    public String getClassNamPrefix() {
        return "com.softwarelma.epe.p3.generic.EpeGenericFinal";
    }

}
