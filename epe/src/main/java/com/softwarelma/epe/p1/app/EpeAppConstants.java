package com.softwarelma.epe.p1.app;

import java.util.ArrayList;
import java.util.List;

public abstract class EpeAppConstants {

    public static final List<String> listReservedWords = new ArrayList<>();
    static {
        listReservedWords.add("null");
    }

    public static final String PROGRAM_DEFAULT_PATH = "progs/program.txt";

    /*
     * ENCODINGS REGISTRED
     */
    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String ENCODING_ISO_8859_15 = "ISO-8859-15";

    /*
     * REGULAR EXPRESSIONS
     */
    public static final String REGEX_ID = "[a-zA-Z_][a-zA-Z0-9_]*";
    public static final String REGEX_NULL = "null";
    public static final String REGEX_STR_INTERNAL = "\\{.*\\}";// big
    public static final String REGEX_FUNC = REGEX_ID + "\\((|(" + REGEX_ID + ")|(" + REGEX_ID + ")(\\," + REGEX_ID
            + ")+)\\)";
    public static final String REGEX_LEFT = "(" + REGEX_ID + "=)*";

    // FIXME id->varName
    public static final String REGEX_LEFT_ID = REGEX_LEFT + REGEX_ID;

    public static final String REGEX_LEFT_NULL = REGEX_LEFT + REGEX_NULL;
    public static final String REGEX_LEFT_STR = REGEX_LEFT + REGEX_STR_INTERNAL;// big
    public static final String REGEX_LEFT_FUNC = REGEX_LEFT + REGEX_FUNC;
    public static final String REGEX_STR = "\"(.|\\n)*\"";// big
    public static final String REGEX_COMMENT_LINE = "//.*\\n";
    public static final String REGEX_COMMENT_BLOCK = "/\\*(.|\\n)*\\*/";// big

    /*
     * OPERATION MODES
     */
    public static final String OPERATION_MODE_NAME = "name";
    public static final String OPERATION_MODE_CONTENT = "content";

}
