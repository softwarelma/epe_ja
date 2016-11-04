package com.softwarelma.epe.p2.exec;

/**
 * this class is intended to implement a NOP, so the contentInternal can be null
 */
public final class EpeExecContent {

	private final EpeExecContentInternal contentInternal;

	public EpeExecContent(EpeExecContentInternal contentInternal) {
		this.contentInternal = contentInternal;
	}

	public EpeExecContentInternal getContentInternal() {
		return contentInternal;
	}

	public String getStr() {
		return this.contentInternal == null ? null : this.contentInternal.getStr();
	}

}
