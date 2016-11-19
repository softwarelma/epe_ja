package com.softwarelma.epe.p2.pack;

import com.softwarelma.epe.p2.exec.EpeExecAbstractFactory;

public final class EpePackGenericFactory extends EpeExecAbstractFactory {

    private static EpePackGenericFactory factory;

    public static EpePackGenericFactory getFactoryInstance() {
        if (factory != null) {
            return factory;
        }

        synchronized (EpePackGenericFactory.class) {
            if (factory != null) {
                return factory;
            }

            EpePackGenericFactory factory2 = new EpePackGenericFactory();
            factory = factory2;
            return factory;
        }
    }

    private EpePackGenericFactory() {
    }

    @Override
    public String getClassNamPrefix() {
        return "com.softwarelma.epe.p3.generic.EpeGenericFinal";
    }

}
