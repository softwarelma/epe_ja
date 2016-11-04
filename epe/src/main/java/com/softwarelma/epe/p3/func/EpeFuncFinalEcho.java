package com.softwarelma.epe.p3.func;

import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecContent;
import com.softwarelma.epe.p2.exec.EpeExecContentInternal;
import com.softwarelma.epe.p2.exec.EpeExecParams;

public final class EpeFuncFinalEcho extends EpeFuncAbstract {

	@Override
	public EpeExecContent doFunc(EpeExecParams execParams, List<EpeExecContent> listExecContent)
			throws EpeAppException {
		EpeAppUtils.checkNull("execParams", execParams);
		EpeAppUtils.checkNull("listExecContent", listExecContent);
		StringBuilder sb = new StringBuilder();

		for (EpeExecContent content : listExecContent) {
			if (content.getContentInternal() != null) {
				String s = content.getContentInternal().getStr();

				if (s != null && s.startsWith("\"") && s.endsWith("\"") && s.length() > 1) {
					s = this.getReplacedSting(s);
					sb.append(s);
					continue;
				}
			}

			sb.append(content.getContentInternal() == null ? null : content.getContentInternal().getStr());
		}

		String ret = sb.toString();

		if (execParams.isPrintToConsole()) {
			System.out.print(ret);
		}

		return new EpeExecContent(new EpeExecContentInternal(ret));
	}

	private String getReplacedSting(String s) throws EpeAppException {
		s = s.substring(1, s.length() - 1);
		String sDoubleBackSlash = EpeAppUtils.getNotContainedString(s);
		s = s.replace("\\\\", sDoubleBackSlash);
		s = s.replace("\\\"", "\"");
		s = s.replace("\\n", "\n");
		s = s.replace("\\r", "\r");
		s = s.replace("\\t", "\t");
		s = s.replace(sDoubleBackSlash, "\\");
		return s;
	}

}
