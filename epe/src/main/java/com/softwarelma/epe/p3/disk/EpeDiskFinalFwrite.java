package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.encodings.EpeEncodings;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDiskFinalFwrite extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        String postMessage = "fwrite params should be 3 to 4: the file name, the content, the encoding "
                + "(see print_default_encoding()) and optionally the \"append\" option";
        EpeAppUtils.checkRange(listExecResult.size(), 2, 4, false, false, postMessage);

        boolean doLog = execParams.getGlobalParams().isPrintToConsole();
        String filename = this.getStringAt(listExecResult, 0, postMessage);
        String content = this.getStringAtForce(listExecResult, 1, postMessage);
        String encoding = this.getStringAt(listExecResult, 2, postMessage);
        boolean appendBool = false;

        if (listExecResult.size() > 3) {
            String appendStr = this.getStringAt(listExecResult, 3, postMessage);
            EpeAppUtils.checkEquals("fwrite-4th-param", "append", appendStr, "append");
            appendBool = true;
        }

        fWrite(doLog, filename, content, encoding, appendBool);
        return this.createEmptyResult();
    }

    public static void fWrite(boolean doLog, String filename, String content, String encoding, boolean appendBool)
            throws EpeAppException {
        EpeAppUtils.checkNull("filename", filename);
        EpeAppUtils.checkNull("content", content);
        EpeAppUtils.checkNull("encoding", encoding);

        filename = EpeAppUtils.cleanFilename(filename);
        File file = new File(filename);

        if (file.exists() && !file.isFile()) {
            throw new EpeAppException("fwrite, file \"" + filename + "\" is not a file");
        }

        if (doLog) {
            EpeAppLogger.log("fwrite()");
            EpeAppLogger.log("\tfilename: " + filename);
            EpeAppLogger.log("\tencoding: " + encoding);
            EpeAppLogger.log("\tappendBool: " + appendBool);
            EpeAppLogger.log("\tcontent:\n" + content);
        }

        EpeEncodings enc = new EpeEncodings();
        enc.write(content, filename, encoding, appendBool);
    }

}
