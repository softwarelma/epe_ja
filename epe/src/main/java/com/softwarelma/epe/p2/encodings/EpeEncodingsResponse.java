package com.softwarelma.epe.p2.encodings;

import java.util.ArrayList;
import java.util.List;

public class EpeEncodingsResponse {

    private String fileContent;
    private String encoding;
    /**
     * il CRLF trovato nel file, non quello presente in fileContent, fileContent viene pulito e presenta solo \n
     */
    private String crlf;
    private final List<String> listWarning = new ArrayList<>();

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getCrlf() {
        return crlf;
    }

    public void setCrlf(String crlf) {
        this.crlf = crlf;
    }

    public List<String> getListWarning() {
        return listWarning;
    }

}
