package com.softwarelma.epe.p2.exec;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public abstract class EpeExecAbstractSent implements EpeExecSentInterface {

	private final String varName;
	private final String literal;
	private final String funcName;
	private final List<String> listParam;

	public EpeExecAbstractSent(String varName, String literal, String funcName, List<String> listParam)
			throws EpeAppException {
		super();

		if (literal == null && funcName == null) {
			EpeAppUtils.checkNull("literal and funcName", null);
		}

		this.varName = varName;
		this.literal = literal;
		this.funcName = funcName;

		if (listParam == null || listParam.isEmpty()) {
			this.listParam = new ArrayList<>();
		} else {
			this.listParam = new ArrayList<>(listParam);
		}
	}

	@Override
	public String toString() {
		String ret = this.varName == null ? "" : this.varName + " = ";

		if (this.literal != null) {
			ret += this.literal;
			return ret;
		}

		String params = "";
		String sep = "";

		for (String param : this.listParam) {
			params += sep + param;
			sep = ", ";
		}

		ret += this.funcName + "(" + params + ")";
		return ret;
	}

	@Override
	public String getVarName() {
		return varName;
	}

	@Override
	public String getLiteral() {
		return literal;
	}

	@Override
	public String getFuncName() {
		return funcName;
	}

	@Override
	public int size() {
		return this.listParam.size();
	}

	@Override
	public String get(int index) throws EpeAppException {
		try {
			return this.listParam.get(index);
		} catch (Exception e) {
			throw new EpeAppException("IndexOutOfBoundsException", e);
		}
	}

}
