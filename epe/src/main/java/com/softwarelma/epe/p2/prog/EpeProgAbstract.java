package com.softwarelma.epe.p2.prog;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public abstract class EpeProgAbstract implements EpeProgInterface {

	private final List<EpeProgSentInterface> listProgSent;

	protected EpeProgAbstract(List<EpeProgSentInterface> listProgSent) throws EpeAppException {
		EpeAppUtils.checkNull("listProgSent", listProgSent);
		this.listProgSent = new ArrayList<>(listProgSent);
	}

	@Override
	public int size() {
		return this.listProgSent.size();
	}

	@Override
	public EpeProgSentInterface get(int index) {
		return this.listProgSent.get(index);
	}

}
