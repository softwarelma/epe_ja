package com.softwarelma.epe.p3.disk;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public final class EpeDiskModelDir extends EpeDiskModelFileDir {

	private final List<EpeDiskModelFileDir> listFileDir = new ArrayList<>();

	protected EpeDiskModelDir(String location, String name) throws EpeAppException {
		super(location, name);
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append(this.getLocation());
		ret.append(this.getName());
		ret.append("\n");

		for (EpeDiskModelFileDir fileDir : this.listFileDir) {
			ret.append(fileDir.toString("\t"));
		}

		return ret.toString();
	}

	@Override
	public String toString(String tabs) {
		StringBuilder ret = new StringBuilder();
		ret.append(tabs);
		ret.append(this.getLocation());
		ret.append(this.getName());
		ret.append("\n");

		for (EpeDiskModelFileDir fileDir : this.listFileDir) {
			ret.append(fileDir.toString(tabs + "\t"));
		}

		return ret.toString();
	}

	@Override
	protected boolean isDir() {
		return true;
	}

	@Override
	protected boolean isFile() {
		return false;
	}

	@Override
	protected EpeDiskModelDir toDir() throws EpeAppException {
		return this;
	}

	@Override
	protected EpeDiskModelFile toFile() throws EpeAppException {
		throw new EpeAppException("dir class can not be cast to file");
	}

	protected int size() {
		return this.listFileDir.size();
	}

	protected EpeDiskModelFileDir get(int index) throws EpeAppException {
		EpeAppUtils.checkRange(index, 0, this.listFileDir.size(), false, true);
		return this.listFileDir.get(index);
	}

	protected void add(EpeDiskModelFileDir fileDir) throws EpeAppException {
		EpeAppUtils.checkNull("fileDir", fileDir);
		this.listFileDir.add(fileDir);
	}

}
