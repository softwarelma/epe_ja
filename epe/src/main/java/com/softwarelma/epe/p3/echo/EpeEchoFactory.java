package com.softwarelma.epe.p3.echo;

import com.softwarelma.epe.p2.exec.EpeExecAbstractFactory;

public final class EpeEchoFactory extends EpeExecAbstractFactory {

    private static EpeEchoFactory factory;

    public static EpeEchoFactory getFactoryInstance() {
        if (factory != null) {
            return factory;
        }

        synchronized (EpeEchoFactory.class) {
            if (factory != null) {
                return factory;
            }

            EpeEchoFactory factory2 = new EpeEchoFactory();
            factory = factory2;
            return factory;
        }
    }

    private EpeEchoFactory() {
    }

    @Override
    public String getClassNamPrefix() {
        return "com.softwarelma.epe.p3.echo.EpeEchoFinal";
    }

}
