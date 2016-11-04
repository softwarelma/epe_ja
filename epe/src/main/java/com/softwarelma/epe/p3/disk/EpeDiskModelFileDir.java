package com.softwarelma.epe.p3.disk;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public abstract class EpeDiskModelFileDir {

	private final String location;
	private final String name;

	protected EpeDiskModelFileDir(String location, String name) throws EpeAppException {
		EpeAppUtils.checkNull("location", location);
		EpeAppUtils.checkNull("name", name);
		this.location = EpeAppUtils.cleanFilename(location);
		this.name = EpeAppUtils.cleanFilename(name);
	}

	@Override
	public abstract String toString();

	public abstract String toString(String tabs);

	protected abstract boolean isDir();

	protected abstract boolean isFile();

	protected abstract EpeDiskModelDir toDir() throws EpeAppException;

	protected abstract EpeDiskModelFile toFile() throws EpeAppException;

	protected String getLocation() {
		return location;
	}

	protected String getName() {
		return name;
	}

}
