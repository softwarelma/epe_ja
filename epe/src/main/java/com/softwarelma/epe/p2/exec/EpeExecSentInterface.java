package com.softwarelma.epe.p2.exec;

import com.softwarelma.epe.p1.app.EpeAppException;

public interface EpeExecSentInterface {

	/**
	 * @return varName on the left side
	 */
	public String getVarName();

	/**
	 * @return null, "string" or string (varName on the right side)
	 */
	public String getLiteral();

	public String getFuncName();

	public int size();

	public String get(int index) throws EpeAppException;

}
