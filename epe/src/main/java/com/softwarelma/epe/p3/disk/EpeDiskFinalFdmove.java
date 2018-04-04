package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDiskFinalFdmove extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "fdmove, expected the source file/dir name and the destination file/dir name.";
        String filenameOriginStr = getStringAt(listExecResult, 0, postMessage);
        String filenameDestinationStr = getStringAt(listExecResult, 1, postMessage);
        doFdmove(filenameOriginStr, filenameDestinationStr);
        log(execParams, "moving \"" + filenameOriginStr + "\" to \"" + filenameDestinationStr + "\"");
        return createEmptyResult();
    }

    public static void doFdmove(String filenameOriginStr, String filenameDestinationStr) throws EpeAppException {
        File fileOrigin = new File(filenameOriginStr);
        File fileDestination = new File(filenameDestinationStr);

        if (fileDestination.exists()) {
            throw new EpeAppException("fdmove to \"" + filenameDestinationStr + "\" already exists.");
        }

        try {
            if (fileOrigin.isDirectory()) {
                FileUtils.moveDirectory(fileOrigin, fileDestination);
            } else if (fileOrigin.isFile()) {
                FileUtils.moveFile(fileOrigin, fileDestination);
            } else {
                throw new EpeAppException(
                        "fdmove from \"" + filenameOriginStr + "\" is neither a directory nor a normal file");
            }
        } catch (IOException e) {
            throw new EpeAppException("fdcopy from \"" + filenameOriginStr + "\" to \"" + filenameDestinationStr + "\"",
                    e);
        }
    }

}
