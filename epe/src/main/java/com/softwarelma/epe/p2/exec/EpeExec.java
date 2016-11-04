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

	public void execute(EpeExecSentInterface execSent) throws EpeAppException {
		EpeAppUtils.checkNull("execSent", execSent);
		String varName = execSent.getVarName();
		EpeExecContent execContent;

		/*
		 * right term could be: null, "string", var or func; the first 3 are
		 * literals
		 */
		if (execSent.getLiteral() != null) {
			execContent = this.getExecContentFromLiteral(execSent);
		} else {
			execContent = this.getExecContentFromFunc(execSent);
		}

		if (varName == null) {
			return;
		}

		this.mem.putAlsoNull(varName, execContent);
	}

	/*
	 * literal could be a string with the content: null, "string" or varName
	 */
	private EpeExecContent getExecContentFromLiteral(EpeExecSentInterface execSent) throws EpeAppException {
		EpeAppUtils.checkNull("execSent", execSent);
		String literal = execSent.getLiteral();
		EpeAppUtils.checkNull("literal", literal);
		EpeExecContent execContent = null;

		if (literal.equals("null")) {
			execContent = new EpeExecContent(null);
		} else if (literal.startsWith("\"") && literal.endsWith("\"")) {
			EpeFuncInterface funcEcho = this.funcFactory.getNewInstance("echo");
			List<EpeExecContent> listExecContent = new ArrayList<>();
			EpeExecContent execContent2 = new EpeExecContent(new EpeExecContentInternal(literal));
			listExecContent.add(execContent2);
			EpeExecParams execParams = new EpeExecParams();
			execParams.setPrintToConsole(false);
			execContent = funcEcho.doFunc(execParams, listExecContent);
		} else {
			// varName
			execContent = this.mem.getAlsoNull(literal);
		}

		return execContent;
	}

	private EpeExecContent getExecContentFromFunc(EpeExecSentInterface execSent) throws EpeAppException {
		EpeAppUtils.checkNull("execSent", execSent);
		String funcName = execSent.getFuncName();
		EpeAppUtils.checkNull("funcName", funcName);
		EpeExecContent execContent = null;
		List<EpeExecContent> listExecContent;
		EpeExecParams execParams;

		if (this.dbFactory.isDb(funcName)) {
			EpeDbInterface db = this.dbFactory.getNewInstance(funcName);
			listExecContent = this.getExecContentList(execSent);
			execParams = new EpeExecParams();
			execContent = db.doFunc(execParams, listExecContent);
		}

		if (this.diskFactory.isDisk(funcName)) {
			if (execContent != null) {
				throw new EpeAppException("The func " + funcName + " can't be registred in more than one exec module");
			}

			EpeDiskInterface disk = this.diskFactory.getNewInstance(funcName);
			listExecContent = this.getExecContentList(execSent);
			execParams = new EpeExecParams();
			execContent = disk.doFunc(execParams, listExecContent);
		}

		if (this.funcFactory.isFunc(funcName)) {
			if (execContent != null) {
				throw new EpeAppException("The func " + funcName + " can't be registred in more than one exec module");
			}

			EpeFuncInterface func = this.funcFactory.getNewInstance(funcName);
			listExecContent = this.getExecContentList(execSent);
			execParams = new EpeExecParams();
			execParams.setPrintToConsole(true);
			execContent = func.doFunc(execParams, listExecContent);
		}

		EpeAppUtils.checkNull("execContent for funcName " + funcName, execContent);
		return execContent;
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
