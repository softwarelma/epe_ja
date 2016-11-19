package com.softwarelma.epe.p2.pack;

import com.softwarelma.epe.p2.exec.EpeExecAbstractFactory;

public final class EpePackEchoFactory extends EpeExecAbstractFactory {

    @Override
    public String getClassNamPrefix() {
        return "com.softwarelma.epe.p3.echo.EpeEchoFinal";
    }

}
