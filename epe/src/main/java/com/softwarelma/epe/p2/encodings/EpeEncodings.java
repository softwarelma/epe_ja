package com.softwarelma.epe.p2.encodings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public class EpeEncodings {

    public EpeEncodings() {
    }

    /**
     * -guess -path PATH
     */
    public void start(String[] args) throws EpeAppException {
        EpeAppUtils.checkNull("args", args);
        // this.testDirectory("/org/company/workspaces/luna-SR2/tcm/FileIn/ENC/.camel/", this.getAssertMapForTest());
        // this.testDirectory("/org/company/workspaces/luna-SR2/tcm/FileIn/ENC/.camel/");
    }

    public String cleanPath(String path) throws EpeAppException {
        EpeAppUtils.checkNull("path", path);
        return path.replace("\\", "/");
    }

    public String cleanContent(String text) throws EpeAppException {
        EpeAppUtils.checkNull("text", text);
        return this.cleanSpecialChars(this.cleanCrlf(text));
    }

    public String cleanCrlf(String text) throws EpeAppException {
        EpeAppUtils.checkNull("text", text);
        return text == null ? null : text.replace("\r\n", "\n").replace("\r", "\n");
    }

    public String cleanSpecialChars(String text) throws EpeAppException {
        EpeAppUtils.checkNull("text", text);
        // 144 e 201 sono E con accento acuto
        String target = "" + (char) 144;
        String replacement = "" + (char) 201;
        return text == null ? null : text.replace(target, replacement);
        // return text;
    }

    /**
     * verifica l'encoding di ogni file
     * 
     * @param path
     *            puo' essere un file o una cartella
     * @throws EpeAppException
     */
    public void test(String path) throws EpeAppException {
        EpeAppUtils.checkNull("path", path);
        path = this.cleanPath(path);
        this.testFile(path);
        this.testDirectory(path);
    }

    public void testFile(String filePath) throws EpeAppException {
        EpeAppUtils.checkNull("filePath", filePath);
        filePath = this.cleanPath(filePath);
        this.testFile(filePath, null, null);
    }

    private void testFile(String filePath, Map<String, String> mapFilePathEncoding, Map<String, String> mapAssert)
            throws EpeAppException {
        EpeAppUtils.checkNull("filePath", filePath);
        File file = this.getFile(filePath);

        if (file == null) {
            return;
        }

        EpeEncodingsResponse response;

        try {
            response = this.readGuessing(filePath);
        } catch (EpeAppException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("File: " + filePath);
        System.out.println("\tEncoding: " + response.getEncoding());
        System.out.println("\tCRLF: " + this.guessCrlfVisible(response.getCrlf()));

        if (mapFilePathEncoding != null) {
            String[] arrayPath = filePath.split("/");
            String filename = arrayPath[arrayPath.length - 1];
            mapFilePathEncoding.put(filename, response.getEncoding());

            if (mapAssert != null) {
                String encodingAssert = mapAssert.get(filename);
                String resultAssert = response.getEncoding().equals(encodingAssert) ? "OK" : "KO";
                System.out.println("\tResult: " + resultAssert);
            }
        }

        for (String warning : response.getListWarning()) {
            System.out.println("\tWarn: " + warning);
        }
    }

    public void testDirectory(String path) throws EpeAppException {
        EpeAppUtils.checkNull("path", path);
        this.testDirectory(path, null);
    }

    public void testDirectory(String path, Map<String, String> mapAssert) throws EpeAppException {
        EpeAppUtils.checkNull("path", path);
        path = this.cleanPath(path);
        path = path.endsWith("/") ? path : path + "/";
        File directory = this.getDirectory(path);

        if (directory == null) {
            return;
        }

        String[] arrayFile = directory.list();
        Map<String, String> mapFilePathEncoding = mapAssert == null ? null : new HashMap<String, String>();

        for (String file : arrayFile) {
            this.testFile(path + file, mapFilePathEncoding, mapAssert);
        }

        if (mapAssert != null) {
            System.out.println("\n\nTest result: " + (mapFilePathEncoding.equals(mapAssert) ? "OK" : "KO"));
        }
    }

    @SuppressWarnings("unused")
    private Map<String, String> getAssertMapForTest() {
        Map<String, String> mapAssert = new HashMap<>();
        mapAssert.put("IMUTASI.A944.A201214.P0004.RUN", "UTF-8");
        mapAssert.put("IMUTASI.A944.A201415.P0003.run", "UTF-8");
        mapAssert.put("IMUTASI.A944.A201415.P0003-1-dich", "UTF-8");
        mapAssert.put("IMUTASI.A944.A201314.P0003.RUN", "UTF-8");
        mapAssert.put("IMUTASI.A944.A201415.P0002-corretto-id-dich-ripetuto-tolto-indice-15063015231538279.RUN",
                "UTF-8");
        mapAssert.put("IMUTASI.A944.A201415.P0005.RUN", "UTF-8");
        mapAssert.put("IMUTASI.A944.A201214.P0003.RUN", "UTF-8");
        mapAssert.put("IMUTASI.A944.A201415.P0004.RUN", "UTF-8");
        mapAssert.put("IMUTASI.A944.A201415.P0001.RUN", "UTF-8");
        mapAssert.put("IMUTASI.A944.A201314.P0005.RUN", "UTF-8");
        mapAssert.put("IMUTASI.H223.A201415.P0001.RUN", "UTF-8");
        mapAssert.put("IMUTASI.A944.A201314.P0004.RUN", "UTF-8");
        mapAssert.put("IMUTASI.H223.A201415.P0002.RUN", "UTF-8");
        mapAssert.put("IMUTASI.F205.A201415.P0007.RUN", "UTF-8");

        mapAssert.put("IMUTASI.A944.A201415.P0002.RUN", "ISO-8859-15");
        mapAssert.put("IMUTASI.A944.A201214.P0001-223-dichiarazioni.RUN", "ISO-8859-15");
        mapAssert.put("IMUTASI.A944.A201314.P0002-ISO-8859-1.RUN", "ISO-8859-15");
        mapAssert.put("IMUTASI.A944.A201516.P0002-no-utf-no-iso--DESTINATI ESCLUSIVAMENTE-2.RUN", "ISO-8859-15");
        mapAssert.put("IMUTASI.F205.A201214.P0001.RUN", "ISO-8859-15");
        mapAssert.put("IMUTASI.A944.A201516.P0002-no-utf-no-iso--DESTINATI ESCLUSIVAMENTE.RUN", "ISO-8859-15");
        mapAssert.put("IMUTASI.A944.A201415.P0002-corretto-id-dich-ripetuto.RUN", "ISO-8859-15");
        mapAssert.put("IMUTASI.A944.A201214.P0002-accento.RUN", "ISO-8859-15");
        mapAssert.put("IMUTASI.A944.A201214.P0001-220-dichiarazioni-tolti-indici-14120120443249657-e-"
                + "14112709410347139-e-14112110583940191.RUN", "ISO-8859-15");
        mapAssert.put("IMUTASI.A944.A201314.P0001-145-dichiarazioni.RUN", "ISO-8859-15");
        mapAssert.put("IMUTASI.F205.A201516.P0001.RUN", "ISO-8859-15");

        return mapAssert;
    }

    private File getFile(String filePath) throws EpeAppException {
        EpeAppUtils.checkNull("filePath", filePath);
        File file = new File(filePath);
        return file.isFile() ? file : null;
    }

    private File getDirectory(String path) throws EpeAppException {
        EpeAppUtils.checkNull("path", path);
        File directory = new File(path);
        return directory.isDirectory() ? directory : null;
    }

    public EpeEncodingsResponse readGuessing(String filePath) throws EpeAppException {
        EpeAppUtils.checkNull("filePath", filePath);
        filePath = this.cleanPath(filePath);
        EpeEncodingsResponse responseUtf = read(filePath, EpeAppConstants.ENCODING_UTF_8);
        SimpleEntry<Character, Integer> cUtf = validateChars(responseUtf.getFileContent());

        if (cUtf == null) {
            return responseUtf;
        }

        EpeEncodingsResponse responseIso = read(filePath, EpeAppConstants.ENCODING_ISO_8859_15);
        SimpleEntry<Character, Integer> cIso = validateChars(responseIso.getFileContent());

        if (cIso == null) {
            return responseIso;
        }

        String warn = "Carattere sconosciuto " + EpeAppConstants.ENCODING_UTF_8 + " \"" + cUtf.getKey()
                + "\" (int val " + ((int) cUtf.getKey()) + ")";
        responseUtf.getListWarning().add(warn);
        responseIso.getListWarning().add(warn);
        warn = "Carattere sconosciuto " + EpeAppConstants.ENCODING_ISO_8859_15 + " \"" + cIso.getKey() + "\" (int val "
                + ((int) cIso.getKey()) + ")";
        responseUtf.getListWarning().add(warn);
        responseIso.getListWarning().add(warn);

        if (cUtf.getKey() == 65533 && cIso.getKey() != 65533) {
            return responseIso;
        }

        if (cUtf.getKey() != 65533 && cIso.getKey() == 65533) {
            return responseUtf;
        }

        if (cUtf.getValue() >= cIso.getValue()) {
            return responseUtf;
        }

        return responseIso;
    }

    /**
     * From: http://snippets.dzone.com/posts/show/1335
     *
     * @param filePath
     *            the name of the file to open. Not sure if it can accept URLs or just filenames. Path handling could be
     *            better, and buffer sizes are hardcoded
     * @return the string
     * @throws Exception
     *             the exception
     */
    public EpeEncodingsResponse read(String filePath, String encoding) throws EpeAppException {
        EpeAppUtils.checkEmpty("filePath", filePath);
        EpeAppUtils.checkEmpty("encoding", encoding);
        filePath = this.cleanPath(filePath);
        File file = new File(filePath);

        if (!file.exists()) {
            throw new EpeAppException("read, file \"" + filePath + "\" does not exist");
        }

        if (!file.isFile()) {
            throw new EpeAppException("read, file \"" + filePath + "\" is not a file");
        }

        String fileContent;
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader;
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(filePath);
            InputStreamReader isr = new InputStreamReader(fis, encoding);
            reader = new BufferedReader(isr);

            char[] buf = new char[1024];
            int numRead;
            String readData;

            while ((numRead = reader.read(buf)) != -1) {
                readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }

            fis.close();
            reader.close();
            fileContent = fileData.toString();

            EpeEncodingsResponse response = new EpeEncodingsResponse();
            response.setCrlf(this.guessCrlf(fileContent));
            fileContent = this.cleanContent(fileContent);
            response.setFileContent(fileContent);
            response.setEncoding(encoding);
            return response;
        } catch (Exception e) {
            throw new EpeAppException("reading file \"" + filePath + "\", encoding " + encoding, e);
        }
    }

    /**
     * From: http://snippets.dzone.com/posts/show/1335
     *
     * @param filePath
     *            the name of the file to open. Not sure if it can accept URLs or just filenames. Path handling could be
     *            better, and buffer sizes are hardcoded
     * @return the string
     * @throws Exception
     *             the exception
     */
    public String read(File file) throws Exception {
        // l'encoding utilizzato dipende da come viene instanziato il file, probabilmente UTF-8
        String fileContent;
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        char[] buf = new char[1024];
        int numRead;
        String readData;

        while ((numRead = reader.read(buf)) != -1) {
            readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }

        reader.close();
        fileContent = fileData.toString();
        fileContent = this.cleanContent(fileContent);

        return fileContent;
    }

    public String guessCrlfVisible(String crlf) throws EpeAppException {
        EpeAppUtils.checkNull("crlf", crlf);

        if (crlf.equals("\r\n")) {
            return "\\r\\n";
        } else if (crlf.equals("\r")) {
            return "\\r";
        } else if (crlf.equals("\n")) {
            return "\\n";
        }

        EpeAppUtils.checkNull("crlfVisible", null);
        return null;
    }

    public String guessCrlf(String text) throws EpeAppException {
        EpeAppUtils.checkNull("text", text);
        boolean rn = text.contains("\r\n");
        boolean r = text.contains("\r");
        boolean n = text.contains("\n");

        if (rn) {
            return "\r\n";
        } else if (r && !n) {
            return "\r";
        } else if (n && !r) {
            return "\n";
        }

        return "\n";
    }

    public void write(String content, String filePath, String encoding, boolean append) throws EpeAppException {
        EpeAppUtils.checkNull("content", content);
        EpeAppUtils.checkEmpty("filePath", filePath);
        EpeAppUtils.checkEmpty("encoding", encoding);
        filePath = this.cleanPath(filePath);
        File file = new File(filePath);

        if (!file.exists()) {
            Map.Entry<String, String> filePathAndName = EpeAppUtils.retrieveFilePathAndName(filePath);
            File dir = new File(filePathAndName.getKey());
            dir.mkdirs();
        }

        Charset charset = Charset.forName(encoding);
        Path path = Paths.get(filePath);
        OpenOption optionAppend = file.exists() ? (append ? StandardOpenOption.APPEND
                : StandardOpenOption.TRUNCATE_EXISTING) : StandardOpenOption.CREATE_NEW;

        try (BufferedWriter writer = Files.newBufferedWriter(path, charset, optionAppend)) {
            writer.write(content, 0, content.length());
        } catch (Exception e) {
            throw new EpeAppException("write file \"" + filePath + "\"", e);
        }
    }

    private SimpleEntry<Character, Integer> validateChars(String text) throws EpeAppException {
        EpeAppUtils.checkNull("text", text);
        String allChars = getAllChars();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (allChars.contains(c + "")) {
                continue;
            }

            return new SimpleEntry<>(c, i);
        }

        return null;
    }

    private String getAllChars() {
        char[] specialChars = new char[] { '\u00C0', '\u00E0', '\u00C1', '\u00E1', // a accentate
                '\u00C8', '\u00E8', '\u00C9', '\u00E9', // e accentate
                '\u00CC', '\u00EC', '\u00CD', '\u00ED', // i accentate
                '\u00D2', '\u00F2', '\u00D3', '\u00F3', // o accentate
                '\u00D9', '\u00F9', '\u00DA', '\u00FA', // u accentate
                96, // accento grave
                128, // "?", quello normale e' 63
                133, // sconosciuto
                138, // sconosciuto
                158, // "?", quello normale e' 63
                176, // zero piccolo, d'ordine
                209, // Ñ
                216, // uno zero strano
                241, // ñ
                     // 65533,//non usare, char di sostituzione
                '\u20AC' // euro, per ultimo, puo' creare ambiguita'
        };
        String specialCharsStr = "";
        for (char c : specialChars) {
            specialCharsStr += c;
        }
        String commonCharsStr = "\n\t\r .,;:'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
                + "\\/|@#~!\"$%&()[]{}<>_-+/*=?¿^";
        return specialCharsStr + commonCharsStr;
    }

}
