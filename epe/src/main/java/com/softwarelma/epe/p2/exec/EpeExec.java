package com.softwarelma.epe.p2.exec;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p3.db.EpeDbFactory;
import com.softwarelma.epe.p3.db.EpeDbInterface;
import com.softwarelma.epe.p3.disk.EpeDiskFactory;
import com.softwarelma.epe.p3.disk.EpeDiskInterface;
import com.softwarelma.epe.p3.func.EpeFuncFactory;
import com.softwarelma.epe.p3.func.EpeFuncInterface;
import com.softwarelma.epe.p3.mem.EpeMem;

public final class EpeExec {

    private final EpeDbFactory dbFactory = EpeDbFactory.getInstance();
    private final EpeDiskFactory diskFactory = EpeDiskFactory.getInstance();
    private final EpeFuncFactory funcFactory = EpeFuncFactory.getInstance();
    private final EpeMem mem = new EpeMem();

    public EpeExecResult execute(EpeExecSentInterface execSent, boolean printToConsole) throws EpeAppException {
        EpeAppUtils.checkNull("execSent", execSent);
        String varName = execSent.getVarName();
        EpeExecResult execResult;

        /*
         * right term could be: null, "string", var or func; the first 3 are literals
         */
        if (execSent.getLiteral() != null) {
            EpeExecResult execResultTemp = this.getExecContentFromLiteral(execSent);
            execResult = new EpeExecResult(printToConsole);
            execResult.setExecContent(execResultTemp.getExecContent());
        } else {
            execResult = this.getExecContentFromFunc(execSent, printToConsole);
        }

        if (varName == null) {
            return execResult;
        }

        this.mem.putAlsoNull(varName, execResult.getExecContent());
        return execResult;
    }

    /**
     * literal could be a string with the content: null, "string" or varName, in all cases printing to console is not
     * allowed
     */
    private EpeExecResult getExecContentFromLiteral(EpeExecSentInterface execSent) throws EpeAppException {
        EpeAppUtils.checkNull("execSent", execSent);
        String literal = execSent.getLiteral();
        EpeAppUtils.checkNull("literal", literal);
        EpeExecResult execResult;

        if (literal.equals("null")) {
            execResult = new EpeExecResult(false);
            execResult.setExecContent(new EpeExecContent(null));
        } else if (literal.startsWith("\"") && literal.endsWith("\"")) {
            EpeFuncInterface funcEcho = this.funcFactory.getNewInstance("echo");
            List<EpeExecContent> listExecContent = new ArrayList<>();
            EpeExecContent execContent2 = new EpeExecContent(new EpeExecContentInternal(literal));
            listExecContent.add(execContent2);
            EpeExecParams execParams = new EpeExecParams(false);
            execResult = funcEcho.doFunc(execParams, listExecContent);
        } else {
            // varName
            EpeExecContent execContent = this.mem.getAlsoNull(literal);
            execResult = new EpeExecResult(false);
            execResult.setExecContent(execContent);
        }

        return execResult;
    }

    private EpeExecResult getExecContentFromFunc(EpeExecSentInterface execSent, boolean printToConsole)
            throws EpeAppException {
        EpeAppUtils.checkNull("execSent", execSent);
        String funcName = execSent.getFuncName();
        EpeAppUtils.checkNull("funcName", funcName);
        EpeExecResult execResult = null;
        List<EpeExecContent> listExecContent;
        EpeExecParams execParams;

        if (this.dbFactory.isDb(funcName)) {
            EpeDbInterface db = this.dbFactory.getNewInstance(funcName);
            listExecContent = this.getExecContentList(execSent);
            execParams = new EpeExecParams(printToConsole);
            execResult = db.doFunc(execParams, listExecContent);
        }

        if (this.diskFactory.isDisk(funcName)) {
            if (execResult != null) {
                throw new EpeAppException("The func " + funcName + " can't be registred in more than one exec module");
            }

            EpeDiskInterface disk = this.diskFactory.getNewInstance(funcName);
            listExecContent = this.getExecContentList(execSent);
            execParams = new EpeExecParams(printToConsole);
            execResult = disk.doFunc(execParams, listExecContent);
        }

        if (this.funcFactory.isFunc(funcName)) {
            if (execResult != null) {
                throw new EpeAppException("The func " + funcName + " can't be registred in more than one exec module");
            }

            EpeFuncInterface func = this.funcFactory.getNewInstance(funcName);
            listExecContent = this.getExecContentList(execSent);
            execParams = new EpeExecParams(printToConsole);
            execResult = func.doFunc(execParams, listExecContent);
        }

        EpeAppUtils.checkNull("execResult for funcName " + funcName, execResult);
        EpeAppUtils.checkNull("execContent for funcName " + funcName, execResult.getExecContent());
        return execResult;
    }

    private List<EpeExecContent> getExecContentList(EpeExecSentInterface execSent) throws EpeAppException {
        List<EpeExecContent> listExecContent = new ArrayList<>();

        for (int i = 0; i < execSent.size(); i++) {
            String param = execSent.get(i);
            EpeExecContent execContent = this.mem.getAlsoNull(param);
            listExecContent.add(execContent);
        }

        return listExecContent;
    }

}
