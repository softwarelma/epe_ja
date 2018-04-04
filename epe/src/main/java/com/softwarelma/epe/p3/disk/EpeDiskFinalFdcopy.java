package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDiskFinalFdcopy extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "fdcopy, expected the source file/dir name and the destination file/dir name.";
        EpeAppUtils.checkRange(2, 2, 2, false, false, "fdcopy params should be 2, file name and destination");
        String sourceFilename = getStringAt(listExecResult, 0, postMessage);
        String destinationFilename = getStringAt(listExecResult, 1, postMessage);
        doFdCopy(sourceFilename, destinationFilename);
        return createEmptyResult();
    }

    public static void doFdCopy(String sourceFilename, String destinationFilename) throws EpeAppException {
        try {
            sourceFilename = EpeAppUtils.cleanFilename(sourceFilename);
            destinationFilename = EpeAppUtils.cleanFilename(destinationFilename);
            File fileOrigin = new File(sourceFilename);
            File fileDestination = new File(destinationFilename);

            if (!fileOrigin.exists()) {
                throw new EpeAppException("fcopy from \"" + sourceFilename + "\" does not exist");
            }

            if (fileOrigin.isDirectory()) {
                if (fileDestination.isDirectory()) {
                    // existing dir
                    FileUtils.copyDirectory(fileOrigin, fileDestination, true);
                } else if (fileDestination.isFile()) {
                    // existing file
                    throw new EpeAppException("fcopy to \"" + destinationFilename + "\" is not a directory");
                } else if (destinationFilename.endsWith("/")) {
                    // new dir
                    FileUtils.copyDirectory(fileOrigin, fileDestination, true);
                } else {
                    // new file
                    throw new EpeAppException("fcopy to \"" + destinationFilename + "\" is not a directory");
                }
            } else if (fileOrigin.isFile()) {
                if (fileDestination.isDirectory()) {
                    // existing dir
                    FileUtils.copyFileToDirectory(fileOrigin, fileDestination, true);
                } else if (fileDestination.isFile()) {
                    // existing file
                    FileUtils.copyFile(fileOrigin, fileDestination);
                } else if (destinationFilename.endsWith("/")) {
                    // new dir
                    FileUtils.copyFileToDirectory(fileOrigin, fileDestination, true);
                } else {
                    // new file
                    FileUtils.copyFile(fileOrigin, fileDestination);
                }
            } else {
                throw new EpeAppException(
                        "fdcopy from \"" + sourceFilename + "\" is neither a directory nor a normal file");
            }
        } catch (IOException e) {
            throw new EpeAppException("fdcopy from \"" + sourceFilename + "\" to \"" + destinationFilename + "\"", e);
        }
    }

}
