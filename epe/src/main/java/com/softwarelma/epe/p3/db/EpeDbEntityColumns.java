package com.softwarelma.epe.p3.db;

public class EpeDbEntityColumns {

    /*
     * COLUMN PREFIXES
     */

    public static final String COLUMN_PREFIX_ID = "ID_";

    /*
     * ALL-TABLES COLUMNS
     */

    /**
     * The only PK of any table.
     */
    public static final String ID = "ID";

    /**
     * It should be human readable.
     */
    public static final String NAME = "NAME";

    public static final String ID_USR_INSERT = "ID_USR_INSERT";

    public static final String ID_USR_UPDATE = "ID_USR_UPDATE";

    public static final String TMS_INSERT = "TMS_INSERT";

    public static final String TMS_UPDATE = "TMS_UPDATE";

    // TABLE SYS_TBL

    /**
     * comma-separated, eg. "ID,NAME"
     */
    public static final String DESCR_SHORT_COLS = "DESCR_SHORT_COLS";

    /**
     * eg. "{NAME} ({ID})"
     */
    public static final String DESCR_SHORT_TEMPL = "DESCR_SHORT_TEMPL";

    /**
     * comma-separated, eg. "ID,NAME"
     */
    public static final String DESCR_LONG_COLS = "DESCR_LONG_COLS";

    /**
     * eg. "{NAME} ({ID})"
     */
    public static final String DESCR_LONG_TEMPL = "DESCR_LONG_TEMPL";

}
