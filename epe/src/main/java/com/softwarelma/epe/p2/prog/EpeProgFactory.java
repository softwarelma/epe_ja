package com.softwarelma.epe.p2.prog;

import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.encodings.EpeEncodings;
import com.softwarelma.epe.p2.encodings.EpeEncodingsResponse;

public final class EpeProgFactory {

    private static EpeProgFactory factory;

    public static EpeProgInterface getInstanceFromProgramPath(String filePath,
            Map<String, String> mapNotContainedReplaced) throws EpeAppException {
        EpeProgFactory factory = getFactory();
        return factory.initFromProgramPath(filePath, mapNotContainedReplaced);
    }

    public static EpeProgInterface getInstanceFromProgramContent(String programContent,
            Map<String, String> mapNotContainedReplaced) throws EpeAppException {
        EpeProgFactory factory = getFactory();
        return factory.initFromProgramContent(programContent, mapNotContainedReplaced);
    }

    private static EpeProgFactory getFactory() throws EpeAppException {
        if (factory != null) {
            return factory;
        }

        synchronized (EpeProgFactory.class) {
            if (factory != null) {
                return factory;
            }

            EpeProgFactory factory2 = new EpeProgFactory();
            factory = factory2;
            return factory;
        }
    }

    private EpeProgFactory() throws EpeAppException {
    }

    private EpeProgInterface initFromProgramPath(String filePath, Map<String, String> mapNotContainedReplaced)
            throws EpeAppException {
        EpeAppUtils.checkNull("filePath", filePath);
        EpeAppUtils.checkNull("mapNotContainedReplaced", mapNotContainedReplaced);
        EpeEncodingsResponse response;

        try {
            EpeEncodings enc = new EpeEncodings();
            response = enc.readGuessing(filePath);
        } catch (Exception e) {
            throw new EpeAppException("Wrong file " + filePath, e);
        }

        return this.initFromProgramContent(response.getFileContent(), mapNotContainedReplaced);
    }

    private EpeProgInterface initFromProgramContent(String programContent, Map<String, String> mapNotContainedReplaced)
            throws EpeAppException {
        EpeAppUtils.checkNull("programContent", programContent);
        EpeAppUtils.checkNull("mapNotContainedReplaced", mapNotContainedReplaced);
        EpeProgParser progParser = new EpeProgParser();
        List<EpeProgSentInterface> listProgSent = progParser.retrieveProgSentList(programContent,
                mapNotContainedReplaced);
        EpeProgInterface prog = new EpeProgDefault(listProgSent);
        return prog;
    }

}
