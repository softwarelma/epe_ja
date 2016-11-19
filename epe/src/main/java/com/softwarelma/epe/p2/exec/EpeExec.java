package com.softwarelma.epe.p2.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppConstants.SENT_TYPE;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppGlobalParams;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.prog.EpeProgSentInterface;
import com.softwarelma.epe.p3.echo.EpeEchoFinalEcho;
import com.softwarelma.epe.p3.generic.EpeGenericFinalList_packs;
import com.softwarelma.epe.p3.mem.EpeMem;

public final class EpeExec {

    private final List<EpeExecFactoryInterface> listFactory = new ArrayList<>();
    private final EpeMem mem = new EpeMem();

    public EpeExec() throws EpeAppException {
        List<String> listPack = EpeGenericFinalList_packs.retriveListPack();
        EpeExecFactoryInterface factory;

        for (String pack : listPack) {
            pack = pack.substring(0, 1).toUpperCase() + pack.substring(1);
            String className = "com.softwarelma.epe.p2.pack.EpePack" + pack + "Factory";

            try {
                Class<?> clazz = Class.forName(className);
                factory = (EpeExecFactoryInterface) clazz.newInstance();
            } catch (ClassNotFoundException e) {
                throw new EpeAppException("EpeExec for class name: " + className, e);
            } catch (InstantiationException e) {
                throw new EpeAppException("EpeExec for class name: " + className, e);
            } catch (IllegalAccessException e) {
                throw new EpeAppException("EpeExec for class name: " + className, e);
            }

            this.listFactory.add(factory);
        }
    }

    public EpeExecResult execute(EpeAppGlobalParams globalParams, EpeProgSentInterface progSent,
            Map<String, String> mapNotContainedReplaced) throws EpeAppException {
        EpeAppUtils.checkNull("progSent", progSent);
        String varName = progSent.getLeftSideVarName();
        EpeExecResult execResult;

        /*
         * right term could be: "string", var or func; the first 2 are literals
         */
        if (progSent.getType().equals(SENT_TYPE.func) || progSent.getType().equals(SENT_TYPE.left_func)) {
            execResult = this.getExecContentFromFunc(globalParams, progSent, mapNotContainedReplaced);
        } else {
            EpeExecResult execResultTemp = this.getExecContentFromLiteral(globalParams, progSent);
            execResult = new EpeExecResult();
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
    private EpeExecResult getExecContentFromLiteral(EpeAppGlobalParams globalParams, EpeProgSentInterface progSent)
            throws EpeAppException {
        EpeAppUtils.checkNull("progSent", progSent);
        String literal = progSent.getLiteralOrFuncName();
        EpeAppUtils.checkNull("literal", literal);
        EpeExecResult execResult;

        if (literal.equals("null")) {
            execResult = new EpeExecResult();
            execResult.setExecContent(new EpeExecContent(null));
        } else if (literal.startsWith("\"") && literal.endsWith("\"")) {
            execResult = this.doEcho(globalParams, literal);
        } else {
            // varName
            EpeExecContent execContent = this.mem.getAlsoNull(literal);
            execResult = new EpeExecResult();
            execResult.setExecContent(execContent);
        }

        return execResult;
    }

    private EpeExecResult doEcho(EpeAppGlobalParams globalParams, String literal) throws EpeAppException {
        EpeExecInterface funcEcho = new EpeEchoFinalEcho();
        List<EpeExecResult> listExecResult = new ArrayList<>();
        EpeExecContent execContent = new EpeExecContent(new EpeExecContentInternal(literal));
        EpeExecResult result = new EpeExecResult();
        result.setExecContent(execContent);
        listExecResult.add(result);
        EpeExecParams execParams = new EpeExecParams(globalParams);
        boolean printToConsole = globalParams.isPrintToConsole();
        globalParams.setPrintToConsole(false);
        EpeExecResult execResult = funcEcho.doFunc(execParams, listExecResult);
        globalParams.setPrintToConsole(printToConsole);
        return execResult;
    }

    private EpeExecResult retrieveExecResult(EpeAppGlobalParams globalParams, EpeExecResult result,
            EpeExecFactoryInterface factory, EpeProgSentInterface progSent, boolean[] known, String message,
            Map<String, String> mapNotContainedReplaced) throws EpeAppException {
        if (!factory.isFunc(progSent.getLiteralOrFuncName())) {
            return result;
        }

        EpeAppUtils.checkBooleanForceFalse("known[0]", known[0]);
        known[0] = true;
        EpeAppUtils.checkNullForceNull("execResult", result, message);
        EpeExecInterface func = factory.getNewFuncInstance(progSent.getLiteralOrFuncName());
        List<EpeExecResult> listExecResult = this.getExecContentList(globalParams, progSent, mapNotContainedReplaced);
        EpeExecParams execParams = new EpeExecParams(globalParams);
        result = func.doFunc(execParams, listExecResult);
        return result;
    }

    private EpeExecResult getExecContentFromFunc(EpeAppGlobalParams globalParams, EpeProgSentInterface progSent,
            Map<String, String> mapNotContainedReplaced) throws EpeAppException {
        EpeAppUtils.checkNull("progSent", progSent);
        String funcName = progSent.getLiteralOrFuncName();
        EpeAppUtils.checkNull("funcName", funcName);
        EpeExecResult result = null;
        boolean[] known = { false };
        String message = "The func " + funcName + " can't be registred in more than one exec module";

        for (EpeExecFactoryInterface factory : this.listFactory) {
            result = this.retrieveExecResult(globalParams, result, factory, progSent, known, message,
                    mapNotContainedReplaced);
        }

        EpeAppUtils.checkBooleanForceTrue("known[0]", known[0], "Unknown func " + funcName);
        EpeAppUtils.checkNull("execResult for funcName " + funcName, result);
        EpeAppUtils.checkNull("execContent for funcName " + funcName, result.getExecContent());
        return result;
    }

    private List<EpeExecResult> getExecContentList(EpeAppGlobalParams globalParams, EpeProgSentInterface progSent,
            Map<String, String> mapNotContainedReplaced) throws EpeAppException {
        List<EpeExecResult> listExecResult = new ArrayList<>();

        for (int i = 0; i < progSent.size(); i++) {
            EpeProgSentInterface param = progSent.get(i);
            EpeExecResult execResult;

            if (param.getType().equals(SENT_TYPE.id)) {
                EpeExecContent execContent = this.mem.getAlsoNull(param.getLiteralOrFuncName());
                execResult = new EpeExecResult();
                execResult.setExecContent(execContent);
            } else if (param.getType().equals(SENT_TYPE.str)) {
                execResult = this.doEcho(globalParams, param.getLiteralOrFuncName());
            } else if (param.getType().equals(SENT_TYPE.func)) {
                execResult = this.getExecContentFromFunc(globalParams, param, mapNotContainedReplaced);
            } else {
                throw new EpeAppException("Sent type not valid: " + param.getType());
            }

            listExecResult.add(execResult);
        }

        return listExecResult;
    }

}
