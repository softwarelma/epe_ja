package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDiskFinalFdcopy extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("listExecResult", listExecResult);

        if (listExecResult.size() != 2) {
            throw new EpeAppException("fcopy params should be 2, file name and destination");
        }

        EpeExecResult result = listExecResult.get(0);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent filenameOrigin = result.getExecContent();
        EpeAppUtils.checkNull("filenameOrigin", filenameOrigin);

        result = listExecResult.get(1);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent filenameDestination = result.getExecContent();
        EpeAppUtils.checkNull("filenameDestination", filenameDestination);

        String filenameOriginStr = filenameOrigin.getContentInternal().getStr();
        EpeAppUtils.checkNull("filenameOriginStr", filenameOriginStr);
        filenameOriginStr = EpeAppUtils.cleanFilename(filenameOriginStr);
        File fileOrigin = new File(filenameOriginStr);

        if (!fileOrigin.exists()) {
            throw new EpeAppException("fcopy from \"" + filenameOriginStr + "\" does not exist");
        }

        String filenameDestinationStr = filenameDestination.getStr();
        EpeAppUtils.checkNull("filenameDestinationStr", filenameDestinationStr);
        filenameDestinationStr = EpeAppUtils.cleanFilename(filenameDestinationStr);
        File fileDestination = new File(filenameDestinationStr);

        this.doFdopy(filenameOriginStr, fileOrigin, filenameDestinationStr, fileDestination);
        return this.createEmptyResult();
    }

    protected void doFdopy(String filenameOriginStr, File fileOrigin, String filenameDestinationStr,
            File fileDestination) throws EpeAppException {
        try {
            if (fileOrigin.isDirectory()) {
                if (fileDestination.isDirectory()) {
                    // existing dir
                    FileUtils.copyDirectory(fileOrigin, fileDestination, true);
                } else if (fileDestination.isFile()) {
                    // existing file
                    throw new EpeAppException("fcopy to \"" + filenameDestinationStr + "\" is not a directory");
                } else if (filenameDestinationStr.endsWith("/")) {
                    // new dir
                    FileUtils.copyDirectory(fileOrigin, fileDestination, true);
                } else {
                    // new file
                    throw new EpeAppException("fcopy to \"" + filenameDestinationStr + "\" is not a directory");
                }
            } else if (fileOrigin.isFile()) {
                if (fileDestination.isDirectory()) {
                    // existing dir
                    FileUtils.copyFileToDirectory(fileOrigin, fileDestination, true);
                } else if (fileDestination.isFile()) {
                    // existing file
                    FileUtils.copyFile(fileOrigin, fileDestination);
                } else if (filenameDestinationStr.endsWith("/")) {
                    // new dir
                    FileUtils.copyFileToDirectory(fileOrigin, fileDestination, true);
                } else {
                    // new file
                    FileUtils.copyFile(fileOrigin, fileDestination);
                }
            } else {
                throw new EpeAppException("fcopy from \"" + filenameOriginStr
                        + "\" is neither a directory nor a normal file");
            }
        } catch (IOException e) {
            throw new EpeAppException("fcopy from \"" + filenameOriginStr + "\" to \"" + filenameDestinationStr + "\"",
                    e);
        }
    }

}
