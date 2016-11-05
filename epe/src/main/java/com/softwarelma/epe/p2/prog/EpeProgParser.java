package com.softwarelma.epe.p2.prog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public final class EpeProgParser {

    private final EpeProgParserSearch parserSearch = new EpeProgParserSearch();
    private final EpeProgParserCleaner parserCleaner = new EpeProgParserCleaner(this.parserSearch);
    private final EpeProgParserSent parserSent = new EpeProgParserSent(this.parserSearch);

    public List<EpeProgSentInterface> retrieveProgSentList(String programContent,
            Map<String, String> mapNotContainedReplaced) throws EpeAppException {
        EpeAppUtils.checkNull("programContent", programContent);
        List<EpeProgSentInterface> listProgSent = new ArrayList<>();
        String endOfProgram = "\n";
        programContent = programContent.endsWith(endOfProgram) ? programContent : programContent + endOfProgram;

        String ncs = EpeAppUtils.getNotContainedString(programContent, "[", "]");
        String sDoubleBackSlash = EpeAppUtils.getNotContainedString(ncs, 0, "]");
        String sBackSlashedQuote = EpeAppUtils.getNotContainedString(ncs, 1, "]");
        programContent = this.parserCleaner.cleanProgramContent(programContent, mapNotContainedReplaced,
                sDoubleBackSlash, sBackSlashedQuote);
        String[] sSplit = programContent.split(";");

        for (String token : sSplit) {
            String sentStr = token.trim();

            if (sentStr.isEmpty()) {
                continue;
            }

            EpeProgSentInterface progSent = this.parserSent.getProgSent(sentStr, mapNotContainedReplaced,
                    sDoubleBackSlash, sBackSlashedQuote);
            // sentStr = EpeAppUtils.replaceNotContainedWithReplaced(sentStr, mapNotContainedReplaced);
            listProgSent.add(progSent);
        }

        if (listProgSent.isEmpty()) {
            throw new EpeAppException("Void program");
        }

        return listProgSent;
    }

}
