package com.softwarelma.epe.p2.prog;

import com.softwarelma.epe.p1.app.EpeAppException;

public interface EpeProgSentInterface {

	public String getType();

	public String getVarName();

	public String getLiteral();

	public String getFuncName();

	public int size();

	public String get(int index) throws EpeAppException;

}
