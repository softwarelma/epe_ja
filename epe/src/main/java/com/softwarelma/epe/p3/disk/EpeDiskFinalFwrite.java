package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDiskFinalFwrite extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(listExecResult.size(), 2, 4, false, false,
                "fwrite params should be 2 to 4: the file name, the content, optionally the encoding and optionally the append option");

        // FILE

        EpeExecResult result = listExecResult.get(0);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent filename = result.getExecContent();
        EpeAppUtils.checkNull("filename", filename);
        String filenameStr = filename.getStr();
        EpeAppUtils.checkNull("filenameStr", filenameStr);
        filenameStr = EpeAppUtils.cleanFilename(filenameStr);
        File file = new File(filenameStr);

        if (file.exists() && !file.isFile()) {
            throw new EpeAppException("fwrite, file \"" + filenameStr + "\" is not a file");
        }

        // CONTENT

        result = listExecResult.get(1);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        String contentStr = content.getStr();
        EpeAppUtils.checkNull("contentStr", contentStr);

        // ENCODING

        String encodingStr = null;
        if (listExecResult.size() > 2) {
            result = listExecResult.get(2);
            EpeAppUtils.checkNull("result", result);
            EpeExecContent encoding = result.getExecContent();
            EpeAppUtils.checkNull("encoding", encoding);
            encodingStr = encoding.getStr();
            // EpeAppUtils.checkNull("encodingStr", encodingStr);
        }

        // APPEND

        String appendStr = null;
        boolean appendBool = false;
        if (listExecResult.size() > 3) {
            result = listExecResult.get(3);
            EpeAppUtils.checkNull("result", result);
            EpeExecContent append = result.getExecContent();
            EpeAppUtils.checkNull("append", append);
            appendStr = append.getStr();
            // EpeAppUtils.checkNull("appendStr", appendStr);
            appendBool = appendStr != null && appendStr.equals("append");
        }

        // WRITING

        try {
            EpeAppUtils.writeFile(contentStr, filenameStr, encodingStr, appendBool);
        } catch (Exception e) {
            throw new EpeAppException("fwrite, file \"" + filenameStr + "\" is not valid for writing", e);
        }

        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(null));
        return execResult;
    }

}
