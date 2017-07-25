package com.softwarelma.epe.p1.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EpeAppConstants {

    public static final List<String> listReservedWords = new ArrayList<>();

    public static final Map<Long, String> mapThreadIdAndExceptionSuffix = new HashMap<>();

    // static {
    // listReservedWords.add("null");
    // }

    public static enum SENT_TYPE {
        str, func, id, left_str, left_func, left_id
    }

    public static final String PROGRAM_DEFAULT_PATH = "progs/program.epe";

    /*
     * TIMESTAMP
     */
    public static final String TIMESTAMP_DEFAULT_FORMAT = "yyyyMMdd-HHmmss";

    /*
     * ENCODINGS REGISTRED
     */
    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final String ENCODING_DEFAULT = ENCODING_UTF_8;
    public static final String ENCODING_ISO_8859_15 = "ISO-8859-15";

    /*
     * OPERATION MODES
     */
    public static final String OPERATION_MODE_NAME = "name";
    public static final String OPERATION_MODE_CONTENT = "content";

    /*
     * OPENING AND CLOSING CONTAINED STRINGS
     */
    public static final String CONTAINED_STRING_OPEN = "{";
    public static final String CONTAINED_STRING_CLOSE = "}";

    /*
     * EXEC
     */
    public static final String EXEC_FILE_LIN = "run-epe.sh";
    public static final String EXEC_FILE_WIN = "run-epe.bat";
    public static final String EXEC_COMMAND_LIN = "sh ";
    public static final String EXEC_COMMAND_WIN = "";
    public static final String EXEC_NEW_TERM_COMMAND_PREFIX_WIN = "start cmd.exe @cmd /k \"";
    public static final String EXEC_NEW_TERM_COMMAND_PREFIX_LIN = "gnome-terminal -x sh -c \"";
    public static final String EXEC_NEW_TERM_COMMAND_SUFFIX_WIN = "\"";
    public static final String EXEC_NEW_TERM_COMMAND_SUFFIX_LIN = "; bash\"";

    /**
     * RandomInitVector 16 bytes IV
     */
    public static final String CIPHER_INIT_VECTOR = "12345678*/+-#{[(";

    /**
     * per rinforzare la key, lunghezza 16
     */
    public static final String CIPHER_KEY_SUFFIX = "*/+-#{[(12345678";

    /**
     * enables exception logging
     */
    public static boolean SHOW_EXCEPTIONS = true;

    /*
     * ******************** REGULAR EXPRESSIONS - BEGIN ********************
     * 
     * "big" regexs are those that can match a result bigger than desired, eg. (see REGEX_STR) the case between
     * bracketes ("a", "b") will be matched as one string and not only the first one, another example could be ("a\"b")
     */

    /*
     * CLEANING PURPOSE
     */
    public static final String REGEX_STR = "\"(.|\\n)*\"";// big
    public static final String REGEX_COMMENT_LINE = "//.*\\n";
    public static final String REGEX_COMMENT_BLOCK = "/\\*(.|\\n)*\\*/";// big

    /*
     * RIGHT SIDE
     */
    // public static final String REGEX_NULL = "null";
    public static final String REGEX_STR_INTERNAL = "\\{[0-9]*\\-[0-9]*\\}";
    public static final String REGEX_ID = "([a-zA-Z_][a-zA-Z0-9_]*)";
    public static final String REGEX_PARAM_LEVEL_1 = REGEX_STR_INTERNAL + "|" + REGEX_ID;
    public static final String REGEX_FUNC_LEVEL_1 = REGEX_ID + "\\((|(" + REGEX_PARAM_LEVEL_1 + ")|("
            + REGEX_PARAM_LEVEL_1 + ")(\\,(" + REGEX_PARAM_LEVEL_1 + "))+)\\)";
    public static final String REGEX_PARAM_LEVEL_2 = REGEX_FUNC_LEVEL_1 + "|" + REGEX_PARAM_LEVEL_1;
    public static final String REGEX_FUNC_LEVEL_2 = REGEX_ID + "\\((|(" + REGEX_PARAM_LEVEL_2 + ")|("
            + REGEX_PARAM_LEVEL_2 + ")(\\,(" + REGEX_PARAM_LEVEL_2 + "))+)\\)";
    public static final String REGEX_PARAM_LEVEL_3 = REGEX_FUNC_LEVEL_2 + "|" + REGEX_PARAM_LEVEL_2;
    public static final String REGEX_FUNC_LEVEL_3 = REGEX_ID + "\\((|(" + REGEX_PARAM_LEVEL_3 + ")|("
            + REGEX_PARAM_LEVEL_3 + ")(\\,(" + REGEX_PARAM_LEVEL_3 + "))+)\\)";
    public static final String REGEX_PARAM_LEVEL_4 = REGEX_FUNC_LEVEL_3 + "|" + REGEX_PARAM_LEVEL_3;
    public static final String REGEX_FUNC = REGEX_ID + "\\((|(" + REGEX_PARAM_LEVEL_4 + ")|(" + REGEX_PARAM_LEVEL_4
            + ")(\\,(" + REGEX_PARAM_LEVEL_4 + "))+)\\)";

    /*
     * LEFT SIDE
     */
    public static final String REGEX_LEFT = "(" + REGEX_ID + "=){1}";

    /*
     * LEFT + RIGHT SIDE
     */
    // public static final String REGEX_LEFT_NULL = REGEX_LEFT + REGEX_NULL;
    public static final String REGEX_LEFT_STR = REGEX_LEFT + REGEX_STR_INTERNAL;
    public static final String REGEX_LEFT_FUNC = REGEX_LEFT + REGEX_FUNC;
    public static final String REGEX_LEFT_ID = REGEX_LEFT + REGEX_ID;

    /*
     * BIGS
     */
    public static final List<String> REGEX_LIST_BIG = new ArrayList<>();
    static {
        REGEX_LIST_BIG.add(REGEX_STR);
        REGEX_LIST_BIG.add(REGEX_COMMENT_BLOCK);
    }

    /*
     * ********************* REGULAR EXPRESSIONS - END *********************
     */

}
