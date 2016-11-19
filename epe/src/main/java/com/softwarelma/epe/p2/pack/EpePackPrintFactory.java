package com.softwarelma.epe.p2.pack;

import com.softwarelma.epe.p2.exec.EpeExecAbstractFactory;

public final class EpePackPrintFactory extends EpeExecAbstractFactory {

    private static EpePackPrintFactory factory;

    public static EpePackPrintFactory getFactoryInstance() {
        if (factory != null) {
            return factory;
        }

        synchronized (EpePackPrintFactory.class) {
            if (factory != null) {
                return factory;
            }

            EpePackPrintFactory factory2 = new EpePackPrintFactory();
            factory = factory2;
            return factory;
        }
    }

    private EpePackPrintFactory() {
    }

    @Override
    public String getClassNamPrefix() {
        return "com.softwarelma.epe.p3.print.EpePrintFinal";
    }

}
