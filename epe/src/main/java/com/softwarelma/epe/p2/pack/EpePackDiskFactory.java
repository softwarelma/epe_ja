package com.softwarelma.epe.p2.pack;

import com.softwarelma.epe.p2.exec.EpeExecAbstractFactory;

public final class EpePackDiskFactory extends EpeExecAbstractFactory {

    private static EpePackDiskFactory factory;

    public static EpePackDiskFactory getFactoryInstance() {
        if (factory != null) {
            return factory;
        }

        synchronized (EpePackDiskFactory.class) {
            if (factory != null) {
                return factory;
            }

            EpePackDiskFactory factory2 = new EpePackDiskFactory();
            factory = factory2;
            return factory;
        }
    }

    private EpePackDiskFactory() {
    }

    @Override
    public String getClassNamPrefix() {
        return "com.softwarelma.epe.p3.disk.EpeDiskFinal";
    }

}
