package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecContentInternal;
import com.softwarelma.epe.p2.exec.EpeExecParams;

public final class EpeDiskFinalFread extends EpeDiskAbstract {

	@Override
	public EpeExecContent doFunc(EpeExecParams execParams, List<EpeExecContent> listExecContent)
			throws EpeAppException {
		EpeAppUtils.checkNull("execParams", execParams);
		EpeAppUtils.checkNull("listExecContent", listExecContent);
		List<String> ret = new ArrayList<>();
		EpeExecContentInternal contentInternal;

		if (listExecContent.isEmpty()) {
			throw new EpeAppException("fread params not found");
		}

		for (EpeExecContent filename : listExecContent) {
			EpeAppUtils.checkNull("filename", filename);
			String filenameStr = filename.getStr();
			EpeAppUtils.checkNull("filenameStr", filenameStr);
			filenameStr = EpeAppUtils.cleanFilename(filenameStr);
			File file = new File(filenameStr);

			if (!file.exists()) {
				throw new EpeAppException("fread, file \"" + filenameStr + "\" does not exist");
			}

			if (!file.isFile()) {
				throw new EpeAppException("fread, file \"" + filenameStr + "\" is not a file");
			}

			try {
				String content = EpeAppUtils.readFileAsStringGuessingEncoding(filenameStr);
				ret.add(content);
			} catch (Exception e) {
				throw new EpeAppException("fread, file \"" + filenameStr + "\" is not valid for reading", e);
			}
		}

		if (ret.size() == 1) {
			contentInternal = new EpeExecContentInternal(ret.get(0));
		} else {
			contentInternal = new EpeExecContentInternal(ret);
		}

		return new EpeExecContent(contentInternal);
	}

}
