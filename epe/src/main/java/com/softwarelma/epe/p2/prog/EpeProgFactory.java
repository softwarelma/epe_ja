package com.softwarelma.epe.p2.prog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public final class EpeProgFactory {

    private static EpeProgFactory factory;
    private final Map<String, EpeProgInterface> mapFilePathProg = new HashMap<>();

    public static EpeProgInterface getInstance(String filePath, Map<String, String> mapNotContainedReplaced)
            throws EpeAppException {
        if (factory != null) {
            return factory.getInstanceByName(filePath, mapNotContainedReplaced);
        }

        synchronized (EpeProgFactory.class) {
            if (factory != null) {
                return factory.getInstanceByName(filePath, mapNotContainedReplaced);
            }

            EpeProgFactory factory2 = new EpeProgFactory();
            factory = factory2;
            return factory.getInstanceByName(filePath, mapNotContainedReplaced);
        }
    }

    private EpeProgFactory() throws EpeAppException {
    }

    private void init(String filePath, Map<String, String> mapNotContainedReplaced) throws EpeAppException {
        EpeAppUtils.checkNull("filePath", filePath);
        String programContent;

        try {
            String encoding = EpeAppUtils.readFileAsStringAndGuessEncoding(filePath);
            programContent = EpeAppUtils.readFileAsStringCharFromPath(filePath, encoding);
        } catch (Exception e) {
            throw new EpeAppException("Wrong file " + filePath, e);
        }

        EpeProgParser progParser = new EpeProgParser();
        List<EpeProgSentInterface> listProgSent = progParser.retrieveProgSentList(programContent,
                mapNotContainedReplaced);
        EpeProgInterface prog = new EpeProgDefault(listProgSent);
        this.mapFilePathProg.put(filePath, prog);
    }

    private EpeProgInterface getInstanceByName(String filePath, Map<String, String> mapNotContainedReplaced)
            throws EpeAppException {
        EpeAppUtils.checkNull("filePath", filePath);
        EpeProgInterface prog = this.mapFilePathProg.get(filePath);

        if (prog == null) {
            this.init(filePath, mapNotContainedReplaced);
            prog = this.mapFilePathProg.get(filePath);
        }

        EpeAppUtils.checkNull("prog", prog);
        return prog;
    }

}
