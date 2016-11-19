package com.softwarelma.epe.p2.pack;

import com.softwarelma.epe.p2.exec.EpeExecAbstractFactory;

public final class EpePackXmlFactory extends EpeExecAbstractFactory {

    private static EpePackXmlFactory factory;

    public static EpePackXmlFactory getFactoryInstance() {
        if (factory != null) {
            return factory;
        }

        synchronized (EpePackXmlFactory.class) {
            if (factory != null) {
                return factory;
            }

            EpePackXmlFactory factory2 = new EpePackXmlFactory();
            factory = factory2;
            return factory;
        }
    }

    private EpePackXmlFactory() {
    }

    @Override
    public String getClassNamPrefix() {
        return "com.softwarelma.epe.p3.xml.EpeXmlFinal";
    }

}
