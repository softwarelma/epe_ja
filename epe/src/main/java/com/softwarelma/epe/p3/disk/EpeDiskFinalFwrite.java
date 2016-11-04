package com.softwarelma.epe.p3.disk;

import java.io.File;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecParams;

public final class EpeDiskFinalFwrite extends EpeDiskAbstract {

	@Override
	public EpeExecContent doFunc(EpeExecParams execParams, List<EpeExecContent> listExecContent)
			throws EpeAppException {
		EpeAppUtils.checkNull("execParams", execParams);
		EpeAppUtils.checkNull("listExecContent", listExecContent);

		if (listExecContent.size() < 2 || listExecContent.size() > 4) {
			throw new EpeAppException(
					"fwrite params should be 2 to 4: the file name, the content, optionally the encoding and optionally the append option");
		}

		// FILE

		EpeExecContent filename = listExecContent.get(0);
		EpeAppUtils.checkNull("filename", filename);
		String filenameStr = filename.getStr();
		EpeAppUtils.checkNull("filenameStr", filenameStr);
		filenameStr = EpeAppUtils.cleanFilename(filenameStr);
		File file = new File(filenameStr);

		if (file.exists() && !file.isFile()) {
			throw new EpeAppException("fwrite, file \"" + filenameStr + "\" is not a file");
		}

		// CONTENT

		EpeExecContent content = listExecContent.get(1);
		EpeAppUtils.checkNull("content", content);
		String contentStr = content.getStr();
		EpeAppUtils.checkNull("contentStr", contentStr);

		// ENCODING

		String encodingStr = null;
		if (listExecContent.size() > 2) {
			EpeExecContent encoding = listExecContent.get(2);
			EpeAppUtils.checkNull("encoding", encoding);
			encodingStr = encoding.getStr();
			// EpeAppUtils.checkNull("encodingStr", encodingStr);
		}

		// APPEND

		String appendStr = null;
		boolean appendBool = false;
		if (listExecContent.size() > 3) {
			EpeExecContent append = listExecContent.get(3);
			EpeAppUtils.checkNull("append", append);
			appendStr = append.getStr();
			// EpeAppUtils.checkNull("appendStr", appendStr);
			appendBool = appendStr != null && appendStr.equals("append");
		}

		// WRITING

		try {
			EpeAppUtils.writeFile(contentStr, filenameStr, encodingStr, appendBool);
		} catch (Exception e) {
			throw new EpeAppException("fwrite, file \"" + filenameStr + "\" is not valid for writing", e);
		}

		return new EpeExecContent(null);
	}

}
