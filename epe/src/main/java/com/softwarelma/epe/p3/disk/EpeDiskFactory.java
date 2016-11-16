package com.softwarelma.epe.p3.disk;

import com.softwarelma.epe.p2.exec.EpeExecAbstractFactory;

public final class EpeDiskFactory extends EpeExecAbstractFactory {

    private static EpeDiskFactory factory;

    public static EpeDiskFactory getFactoryInstance() {
        if (factory != null) {
            return factory;
        }

        synchronized (EpeDiskFactory.class) {
            if (factory != null) {
                return factory;
            }

            EpeDiskFactory factory2 = new EpeDiskFactory();
            factory = factory2;
            return factory;
        }
    }

    private EpeDiskFactory() {
    }

    @Override
    public String getClassNamPrefix() {
        return "com.softwarelma.epe.p3.disk.EpeDiskFinal";
    }

}
