package com.softwarelma.epe.p2.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppConstants.SENT_TYPE;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.prog.EpeProgSentInterface;
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

    public EpeExecResult execute(EpeProgSentInterface progSent, boolean printToConsole,
            Map<String, String> mapNotContainedReplaced) throws EpeAppException {
        EpeAppUtils.checkNull("progSent", progSent);
        String varName = progSent.getLeftSideVarName();
        EpeExecResult execResult;

        /*
         * right term could be: "string", var or func; the first 2 are literals
         */
        if (progSent.getType().equals(SENT_TYPE.func) || progSent.getType().equals(SENT_TYPE.left_func)) {
            execResult = this.getExecContentFromFunc(progSent, printToConsole, mapNotContainedReplaced);
        } else {
            EpeExecResult execResultTemp = this.getExecContentFromLiteral(progSent);
            execResult = new EpeExecResult(printToConsole);
            execResult.setExecContent(execResultTemp.getExecContent());
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
    private EpeExecResult getExecContentFromLiteral(EpeProgSentInterface progSent) throws EpeAppException {
        EpeAppUtils.checkNull("progSent", progSent);
        String literal = progSent.getLiteralOrFuncName();
        EpeAppUtils.checkNull("literal", literal);
        EpeExecResult execResult;

        if (literal.equals("null")) {
            execResult = new EpeExecResult(false);
            execResult.setExecContent(new EpeExecContent(null));
        } else if (literal.startsWith("\"") && literal.endsWith("\"")) {
            execResult = this.doEcho(literal);
        } else {
            // varName
            EpeExecContent execContent = this.mem.getAlsoNull(literal);
            execResult = new EpeExecResult(false);
            execResult.setExecContent(execContent);
        }

        return execResult;
    }

    private EpeExecResult doEcho(String literal) throws EpeAppException {
        EpeFuncInterface funcEcho = this.funcFactory.getNewInstance("echo");
        List<EpeExecContent> listExecContent = new ArrayList<>();
        EpeExecContent execContent = new EpeExecContent(new EpeExecContentInternal(literal));
        listExecContent.add(execContent);
        EpeExecParams execParams = new EpeExecParams(false);
        EpeExecResult execResult = funcEcho.doFunc(execParams, listExecContent);
        return execResult;
    }

    private EpeExecResult getExecContentFromFunc(EpeProgSentInterface progSent, boolean printToConsole,
            Map<String, String> mapNotContainedReplaced) throws EpeAppException {
        EpeAppUtils.checkNull("progSent", progSent);
        String funcName = progSent.getLiteralOrFuncName();
        EpeAppUtils.checkNull("funcName", funcName);
        EpeExecResult execResult = null;
        List<EpeExecContent> listExecContent;
        EpeExecParams execParams;

        if (this.dbFactory.isDb(funcName)) {
            EpeDbInterface db = this.dbFactory.getNewInstance(funcName);
            listExecContent = this.getExecContentList(progSent, printToConsole, mapNotContainedReplaced);
            execParams = new EpeExecParams(printToConsole);
            execResult = db.doFunc(execParams, listExecContent);
        }

        if (this.diskFactory.isDisk(funcName)) {
            if (execResult != null) {
                throw new EpeAppException("The func " + funcName + " can't be registred in more than one exec module");
            }

            EpeDiskInterface disk = this.diskFactory.getNewInstance(funcName);
            listExecContent = this.getExecContentList(progSent, printToConsole, mapNotContainedReplaced);
            execParams = new EpeExecParams(printToConsole);
            execResult = disk.doFunc(execParams, listExecContent);
        }

        if (this.funcFactory.isFunc(funcName)) {
            if (execResult != null) {
                throw new EpeAppException("The func " + funcName + " can't be registred in more than one exec module");
            }

            EpeFuncInterface func = this.funcFactory.getNewInstance(funcName);
            listExecContent = this.getExecContentList(progSent, printToConsole, mapNotContainedReplaced);
            execParams = new EpeExecParams(printToConsole);
            execResult = func.doFunc(execParams, listExecContent);
        }

        EpeAppUtils.checkNull("execResult for funcName " + funcName, execResult);
        EpeAppUtils.checkNull("execContent for funcName " + funcName, execResult.getExecContent());
        return execResult;
    }

    private List<EpeExecContent> getExecContentList(EpeProgSentInterface progSent, boolean printToConsole,
            Map<String, String> mapNotContainedReplaced) throws EpeAppException {
        List<EpeExecContent> listExecContent = new ArrayList<>();

        for (int i = 0; i < progSent.size(); i++) {
            EpeProgSentInterface param = progSent.get(i);
            EpeExecContent execContent;

            if (param.getType().equals(SENT_TYPE.id)) {
                execContent = this.mem.getAlsoNull(param.getLiteralOrFuncName());
            } else if (param.getType().equals(SENT_TYPE.str)) {
                EpeExecResult execResult = this.doEcho(param.getLiteralOrFuncName());
                // EpeExecResult execResult = this.doEcho(mapNotContainedReplaced.get(param));
                execContent = execResult.getExecContent();
            } else if (param.getType().equals(SENT_TYPE.func)) {
                EpeExecResult execResult = this.getExecContentFromFunc(param, printToConsole, mapNotContainedReplaced);
                execContent = execResult.getExecContent();
            } else {
                throw new EpeAppException("Sent type not valid: " + param.getType());
            }

            listExecContent.add(execContent);
        }

        return listExecContent;
    }

}
