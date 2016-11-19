package com.softwarelma.epe.p3.mem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecContentInternal;

public final class EpeMem {

    private final Map<String, EpeExecContent> mapVarNameExecContent = new HashMap<>();

    public void putAlsoNull(String varName, EpeExecContent content) throws EpeAppException {
        EpeAppUtils.checkNull("varName", varName);
        EpeAppUtils.checkNull("content", content);

        if (content.getContentInternal() == null) {
            this.mapVarNameExecContent.remove(varName);
            return;
        }

        this.mapVarNameExecContent.put(varName, this.rewrap(content));
    }

    public EpeExecContent getAlsoNull(String varName) throws EpeAppException {
        EpeAppUtils.checkNull("varName", varName);
        EpeExecContent content = this.mapVarNameExecContent.get(varName);
        content = content == null ? new EpeExecContent(null) : this.rewrap(content);
        return content;
    }

    private EpeExecContent rewrap(EpeExecContent content) throws EpeAppException {
        EpeExecContentInternal contentInternal;

        if (content.isNull()) {
            contentInternal = null;
        } else if (content.isString()) {
            contentInternal = new EpeExecContentInternal(content.getStr());
        } else if (content.isListString()) {
            List<String> listStr = new ArrayList<>(content.getListStr());
            contentInternal = new EpeExecContentInternal(listStr);
        } else {
            throw new EpeAppException("Invalid internal content type");
        }

        EpeExecContent contentRet = new EpeExecContent(contentInternal);
        return contentRet;
    }

}
