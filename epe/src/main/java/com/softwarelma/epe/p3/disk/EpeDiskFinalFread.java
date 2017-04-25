package com.softwarelma.epe.p3.disk;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p2.encodings.EpeEncodings;
import com.softwarelma.epe.p2.encodings.EpeEncodingsResponse;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDiskFinalFread extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "fread, expected the file name and optionally the encoding.";
        String filenameStr = this.getStringAt(listExecResult, 0, postMessage);
        String encodingStr = this.getStringAt(listExecResult, 1, postMessage, null);

        EpeEncodings enc = new EpeEncodings();
        EpeEncodingsResponse response = encodingStr == null ? enc.readGuessing(filenameStr)
                : enc.read(filenameStr, encodingStr);
        String str = fReadAsString(execParams.getGlobalParams().isPrintToConsole(), filenameStr, encodingStr);
        return this.createResult(str);
    }

    public static String fReadAsString(boolean doLog, String filenameStr, String encodingStr) throws EpeAppException {
        return fRead(doLog, filenameStr, encodingStr).getFileContent();
    }

    public static EpeEncodingsResponse fRead(boolean doLog, String filenameStr, String encodingStr)
            throws EpeAppException {
        EpeEncodings enc = new EpeEncodings();
        EpeEncodingsResponse response = encodingStr == null ? enc.readGuessing(filenameStr)
                : enc.read(filenameStr, encodingStr);
        String str = response.getFileContent();

        if (doLog) {
            EpeAppLogger.log("fread()");
            EpeAppLogger.log("\tfilename: " + filenameStr);
            EpeAppLogger.log("\tencoding: " + response.getEncoding());

            for (String warn : response.getListWarning()) {
                EpeAppLogger.log("\twarn: " + warn);
            }

            EpeAppLogger.log("\tcontent:\n" + str);
        }

        return response;
    }

}
