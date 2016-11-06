package com.softwarelma.epe.p1.app;

import java.util.ArrayList;
import java.util.List;

public abstract class EpeAppConstants {

    public static final List<String> listReservedWords = new ArrayList<>();
    // static {
    // listReservedWords.add("null");
    // }

    public static enum SENT_TYPE {
        str, func, id, left_str, left_func, left_id
    }

    public static final String PROGRAM_DEFAULT_PATH = "progs/program.epe";

    /*
     * ENCODINGS REGISTRED
     */
    public static final String ENCODING_UTF_8 = "UTF-8";
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
     * ******************** REGULAR EXPRESSIONS - BEGIN ********************
     * 
     * "big" regexs are those that can match a result bigger than desired, eg. "a", "b" will match the whole string and
     * not only the first one
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
    public static final String REGEX_STR_INTERNAL = "\\{.*\\}";// big
    public static final String REGEX_ID = "([a-zA-Z_][a-zA-Z0-9_]*)";
    public static final String REGEX_PARAM_LEVEL_1 = REGEX_STR_INTERNAL + "|" + REGEX_ID;
    public static final String REGEX_FUNC_LEVEL_1 = REGEX_ID + "\\((|(" + REGEX_PARAM_LEVEL_1 + ")|("
            + REGEX_PARAM_LEVEL_1 + ")(\\,(" + REGEX_PARAM_LEVEL_1 + "))+)\\)";
    public static final String REGEX_PARAM_LEVEL_2 = REGEX_FUNC_LEVEL_1 + "|" + REGEX_PARAM_LEVEL_1;
    public static final String REGEX_FUNC = REGEX_ID + "\\((|(" + REGEX_PARAM_LEVEL_2 + ")|(" + REGEX_PARAM_LEVEL_2
            + ")(\\,(" + REGEX_PARAM_LEVEL_2 + "))+)\\)";

    /*
     * LEFT SIDE
     */
    public static final String REGEX_LEFT = "(" + REGEX_ID + "=){1}";

    /*
     * LEFT + RIGHT SIDE
     */
    // public static final String REGEX_LEFT_NULL = REGEX_LEFT + REGEX_NULL;
    public static final String REGEX_LEFT_STR = REGEX_LEFT + REGEX_STR_INTERNAL;// big
    public static final String REGEX_LEFT_FUNC = REGEX_LEFT + REGEX_FUNC;
    public static final String REGEX_LEFT_ID = REGEX_LEFT + REGEX_ID;

    /*
     * BIGS
     */
    public static final List<String> REGEX_LIST_BIG = new ArrayList<>();
    static {
        REGEX_LIST_BIG.add(REGEX_STR);
        REGEX_LIST_BIG.add(REGEX_COMMENT_BLOCK);
        REGEX_LIST_BIG.add(REGEX_STR_INTERNAL);
        REGEX_LIST_BIG.add(REGEX_LEFT_STR);
    }

    /*
     * ********************* REGULAR EXPRESSIONS - END *********************
     */

}
