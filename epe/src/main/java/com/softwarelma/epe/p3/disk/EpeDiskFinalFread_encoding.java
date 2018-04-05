package com.softwarelma.epe.p3.disk;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.encodings.EpeEncodings;
import com.softwarelma.epe.p2.encodings.EpeEncodingsResponse;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;
import com.softwarelma.epe.p3.generic.EpeGenericFinalReplace_encoding;

public final class EpeDiskFinalFread_encoding extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "fread_encoding, expected {the file name and optionally the encoding} or \n"
                + "{the file name, optionally the maven-like bool (default false) and a prop (at any position) \n"
                + "called " + EpeGenericFinalReplace_encoding.PROP_PROP_FILE + "}.";

        String propFile = retrievePropValueOrNull("replace_encoding", listExecResult,
                EpeGenericFinalReplace_encoding.PROP_PROP_FILE);
        listExecResult = retrieveNoProps(listExecResult, postMessage);

        if (propFile == null) {
            String filenameStr = getStringAt(listExecResult, 0, postMessage);
            String encodingStr = getStringAt(listExecResult, 1, postMessage, null);
            EpeEncodingsResponse response = fRead(execParams.getGlobalParams().isPrintToConsole(), filenameStr,
                    encodingStr);
            List<String> listStr = new ArrayList<>();
            listStr.add(response.getFileContent());
            listStr.add(response.getEncoding());
            listStr.add(response.getCrlf());
            listStr.add(response.getListWarning().toString());
            return createResult(listStr);
        }

        String filenameStr = getStringAt(listExecResult, 0, postMessage);
        EpeEncodingsResponse response = fRead(execParams.getGlobalParams().isPrintToConsole(), filenameStr, null);
        String text = response.getFileContent();

        String mavenLikeStr = getStringAt(listExecResult, 1, postMessage, "false");
        boolean mavenLike = EpeAppUtils.parseBoolean(mavenLikeStr);

        return EpeGenericFinalReplace_encoding.replaceEncoding(execParams, text, mavenLike, propFile);
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
