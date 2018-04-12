package com.softwarelma.epe.p3.disk;

import com.softwarelma.epe.p1.app.EpeAppException;

public final class EpeDiskModelFile extends EpeDiskModelFileDir {

	protected EpeDiskModelFile(String location, String name) throws EpeAppException {
		super(location, name);
	}

	@Override
	public String toString() {
		return this.getLocation() + this.getName() + "\n";
	}

	@Override
	public String toString(String tabs) {
		return tabs + this.toString();
	}

	@Override
	protected boolean isDir() {
		return false;
	}

	@Override
	protected boolean isFile() {
		return true;
	}

	@Override
	protected EpeDiskModelDir toDir() throws EpeAppException {
		throw new EpeAppException("file class can not be cast to dir");
	}

	@Override
	protected EpeDiskModelFile toFile() throws EpeAppException {
		return this;
	}

}
