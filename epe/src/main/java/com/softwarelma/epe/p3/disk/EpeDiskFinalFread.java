package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.encodings.EpeEncodings;
import com.softwarelma.epe.p2.encodings.EpeEncodingsResponse;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecContentInternal;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeDiskFinalFread extends EpeDiskAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        List<String> ret = new ArrayList<>();
        EpeExecContentInternal contentInternal;

        if (listExecResult.isEmpty()) {
            throw new EpeAppException("fread params not found");
        }

        for (EpeExecResult result : listExecResult) {
            EpeAppUtils.checkNull("result", result);
            EpeExecContent execContent = result.getExecContent();
            EpeAppUtils.checkNull("execContent", execContent);
            String filenameStr = execContent.getStr();
            EpeAppUtils.checkNull("filenameStr", filenameStr);
            filenameStr = EpeAppUtils.cleanFilename(filenameStr);
            File file = new File(filenameStr);

            if (!file.exists()) {
                throw new EpeAppException("fread, file \"" + filenameStr + "\" does not exist");
            }

            if (!file.isFile()) {
                throw new EpeAppException("fread, file \"" + filenameStr + "\" is not a file");
            }

            try {
                EpeEncodings enc = new EpeEncodings();
                EpeEncodingsResponse response = enc.readGuessing(filenameStr);
                ret.add(response.getFileContent());
            } catch (Exception e) {
                throw new EpeAppException("fread, file \"" + filenameStr + "\" is not valid for reading", e);
            }
        }

        if (ret.size() == 1) {
            contentInternal = new EpeExecContentInternal(ret.get(0));
        } else {
            contentInternal = new EpeExecContentInternal(ret);
        }

        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(contentInternal));
        return execResult;
    }

}
