package com.softwarelma.epe.p3.print;

import com.softwarelma.epe.p2.exec.EpeExecAbstractFactory;

public final class EpePrintFactory extends EpeExecAbstractFactory {

    private static EpePrintFactory factory;

    public static EpePrintFactory getFactoryInstance() {
        if (factory != null) {
            return factory;
        }

        synchronized (EpePrintFactory.class) {
            if (factory != null) {
                return factory;
            }

            EpePrintFactory factory2 = new EpePrintFactory();
            factory = factory2;
            return factory;
        }
    }

    private EpePrintFactory() {
    }

    @Override
    public String getClassNamPrefix() {
        return "com.softwarelma.epe.p3.print.EpePrintFinal";
    }

}
