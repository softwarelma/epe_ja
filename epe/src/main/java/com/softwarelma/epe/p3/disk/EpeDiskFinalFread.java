package com.softwarelma.epe.p3.disk;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
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
        String str = response.getFileContent();

        this.log(execParams, "fread()");
        this.log(execParams, "\tfilename: " + filenameStr);
        this.log(execParams, "\tencoding: " + response.getEncoding());

        for (String warn : response.getListWarning()) {
            this.log(execParams, "\twarn: " + warn);
        }

        this.log(execParams, "\tcontent:\n" + str);
        return this.createResult(str);
    }

}
