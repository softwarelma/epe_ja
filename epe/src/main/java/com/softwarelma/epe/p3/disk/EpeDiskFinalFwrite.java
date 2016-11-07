package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.encodings.EpeEncodings;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDiskFinalFwrite extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        String postMessage = "fwrite params should be 2 to 4: the file name, the content, optionally the encoding and optionally the append option";
        EpeAppUtils.checkRange(listExecResult.size(), 2, 4, false, false, postMessage);

        // FILE

        String filename = this.getStringAt(listExecResult, 0, postMessage);
        filename = EpeAppUtils.cleanFilename(filename);
        File file = new File(filename);

        if (file.exists() && !file.isFile()) {
            throw new EpeAppException("fwrite, file \"" + filename + "\" is not a file");
        }

        // CONTENT

        String content = this.getStringAtForce(listExecResult, 1, postMessage);

        // ENCODING

        String encoding = null;
        if (listExecResult.size() > 2) {
            encoding = this.getStringAt(listExecResult, 2, postMessage);
        }

        // APPEND

        String appendStr = null;
        boolean appendBool = false;
        if (listExecResult.size() > 3) {
            appendStr = this.getStringAt(listExecResult, 3, postMessage);
            appendBool = "append".equals(appendStr);
        }

        // WRITING

        try {
            EpeEncodings enc = new EpeEncodings();
            enc.write(content, filename, encoding, appendBool);
        } catch (Exception e) {
            throw new EpeAppException("fwrite, file \"" + filename + "\" is not valid for writing", e);
        }

        return this.createEmptyResult();
    }

}
