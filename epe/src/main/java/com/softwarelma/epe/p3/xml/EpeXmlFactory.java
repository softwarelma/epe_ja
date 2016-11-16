package com.softwarelma.epe.p3.xml;

import com.softwarelma.epe.p2.exec.EpeExecAbstractFactory;

public final class EpeXmlFactory extends EpeExecAbstractFactory {

    private static EpeXmlFactory factory;

    public static EpeXmlFactory getFactoryInstance() {
        if (factory != null) {
            return factory;
        }

        synchronized (EpeXmlFactory.class) {
            if (factory != null) {
                return factory;
            }

            EpeXmlFactory factory2 = new EpeXmlFactory();
            factory = factory2;
            return factory;
        }
    }

    private EpeXmlFactory() {
    }

    @Override
    public String getClassNamPrefix() {
        return "com.softwarelma.epe.p3.xml.EpeXmlFinal";
    }

}
