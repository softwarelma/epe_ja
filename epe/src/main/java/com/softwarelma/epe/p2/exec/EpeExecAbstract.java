package com.softwarelma.epe.p2.exec;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public abstract class EpeExecAbstract implements EpeExecInterface {

    protected void log(EpeExecParams execParams, String str) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);

        if (execParams.getGlobalParams().isPrintToConsole()) {
            EpeAppLogger.logSystemOutPrintln(str);
        }
    }

    protected void log(EpeExecParams execParams, List<String> list) throws EpeAppException {
        EpeAppUtils.checkNull("execParams", execParams);
        EpeAppUtils.checkNull("list", list);

        if (execParams.getGlobalParams().isPrintToConsole()) {
            EpeAppLogger.logSystemOutPrintln(list.toString());
        }
    }

    protected EpeExecResult createEmptyResult() {
        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(null));
        return execResult;
    }

    protected EpeExecResult createResult(EpeExecContent content) {
        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(content);
        return execResult;
    }

    protected EpeExecResult createResult(String str) {
        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(new EpeExecContentInternal(str)));
        return execResult;
    }

    protected EpeExecResult createResult(List<String> listStr) {
        EpeExecResult execResult = new EpeExecResult();
        execResult.setExecContent(new EpeExecContent(new EpeExecContentInternal(listStr)));
        return execResult;
    }

    protected int getIntOrSizeAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        if (this.isListStringAt(listExecResult, index, postMessage)) {
            List<String> listStr = this.getListStringAt(listExecResult, index, postMessage);
            return listStr.size();
        }

        String str = this.getStringAt(listExecResult, 0, postMessage);
        int i = EpeAppUtils.parseInt(str);
        return i;
    }

    protected int getIntAt(List<EpeExecResult> listExecResult, int index, String postMessage) throws EpeAppException {
        String str = this.getStringAt(listExecResult, 0, postMessage);
        int i = EpeAppUtils.parseInt(str);
        return i;
    }

    protected int getSizeAt(List<EpeExecResult> listExecResult, int index, String postMessage) throws EpeAppException {
        List<String> listStr = this.getListStringAt(listExecResult, index, postMessage);
        return listStr.size();
    }

    protected EpeExecContent getContentAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);

        return content;
    }

    protected String getStringAt(List<EpeExecResult> listExecResult, int index, String postMessage, String defaultStr)
            throws EpeAppException {
        if (listExecResult.size() > index) {
            String str = this.getStringAt(listExecResult, index, postMessage, false);
            return str == null ? defaultStr : str;
        } else {
            return defaultStr;
        }
    }

    protected String getStringAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        return this.getStringAt(listExecResult, index, postMessage, true);
    }

    private String getStringAt(List<EpeExecResult> listExecResult, int index, String postMessage, boolean checkNull)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        String str = content.getStr();

        if (checkNull) {
            EpeAppUtils.checkNull("str", str);
        }

        return str;
    }

    protected String getStringAtForce(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        if (this.isStringAt(listExecResult, index, postMessage)) {
            return this.getStringAt(listExecResult, index, postMessage);
        } else if (this.isListStringAt(listExecResult, index, postMessage)) {
            List<String> listStr = this.getListStringAt(listExecResult, index, postMessage);
            StringBuilder sb = new StringBuilder();

            for (String str : listStr) {
                sb.append(str);
            }

            return sb.toString();
        }

        throw new EpeAppException("Content not valid. " + postMessage);
    }

    protected List<String> getListStringAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        List<String> listStr = content.getListStr();
        EpeAppUtils.checkNull("listStr", listStr);

        return listStr;
    }

    protected List<List<String>> getListListStringAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        List<List<String>> listListStr = content.getListListStr();
        EpeAppUtils.checkNull("listListStr", listListStr);

        return listListStr;
    }

    protected boolean isStringAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        EpeExecContentInternal contentInternal = content.getContentInternal();
        EpeAppUtils.checkNull("contentInternal", contentInternal);

        return contentInternal.getStr() != null;
    }

    protected boolean isListStringAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        EpeExecContentInternal contentInternal = content.getContentInternal();
        EpeAppUtils.checkNull("contentInternal", contentInternal);

        return contentInternal.getListStr() != null;
    }

    protected boolean isListListStringAt(List<EpeExecResult> listExecResult, int index, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        EpeAppUtils.checkRange(index, 0, listExecResult.size() - 1, false, false, postMessage);

        EpeExecResult result = listExecResult.get(index);
        EpeAppUtils.checkNull("result", result);
        EpeExecContent content = result.getExecContent();
        EpeAppUtils.checkNull("content", content);
        EpeExecContentInternal contentInternal = content.getContentInternal();
        EpeAppUtils.checkNull("contentInternal", contentInternal);

        return contentInternal.getListListStr() != null;
    }

}
