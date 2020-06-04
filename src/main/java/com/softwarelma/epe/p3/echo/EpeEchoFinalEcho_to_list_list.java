package com.softwarelma.epe.p3.echo;

import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeEchoFinalEcho_to_list_list extends EpeEchoAbstract {

	@Override
	public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
		String postMessage = "echo_to_list_list, expected 0 to N lists of string";
		EpeAppUtils.checkNull("listExecResult", listExecResult);
		List<List<String>> listListStr = new ArrayList<>();

		for (int i = 0; i < listExecResult.size(); i++) {
			List<String> listStr = getListStringAt(listExecResult, i, postMessage);
			// str = EpeEchoFinalEcho.retrieveEchoed(str, false);
			listListStr.add(listStr);
		}

		log(execParams, listListStr, "fake");
		return createResult(listListStr, "fake");
	}

}
