package com.softwarelma.epe.p2.pack;

import com.softwarelma.epe.p2.exec.EpeExecAbstractFactory;

public final class EpePackEchoFactory extends EpeExecAbstractFactory {

    private static EpePackEchoFactory factory;

    public static EpePackEchoFactory getFactoryInstance() {
        if (factory != null) {
            return factory;
        }

        synchronized (EpePackEchoFactory.class) {
            if (factory != null) {
                return factory;
            }

            EpePackEchoFactory factory2 = new EpePackEchoFactory();
            factory = factory2;
            return factory;
        }
    }

    private EpePackEchoFactory() {
    }

    @Override
    public String getClassNamPrefix() {
        return "com.softwarelma.epe.p3.echo.EpeEchoFinal";
    }

}
