package com.softwarelma.epe.p3.db;

import java.io.Serializable;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public class EpeDbMetaDataColumn implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String column;
    private final String className;
    private final int precision;
    private final int scale;
    private final boolean nullable;

    /*
     * types of col/att: long, dec, date, tms, str fix, str var, clob, blob
     */

    public EpeDbMetaDataColumn(String column, String className, int precision, int scale, boolean nullable)
            throws EpeAppException {
        EpeAppUtils.checkEmpty("column", column);
        EpeAppUtils.checkEmpty("className", className);
        this.column = column.toUpperCase();
        this.className = className;
        this.precision = precision;
        this.scale = scale;
        this.nullable = nullable;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " [column=" + column + ", className=" + className + ", precision="
                + precision + ", scale=" + scale + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((column == null) ? 0 : column.hashCode());
        result = prime * result + ((className == null) ? 0 : className.hashCode());
        result = prime * result + precision;
        result = prime * result + scale;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EpeDbMetaDataColumn other = (EpeDbMetaDataColumn) obj;
        if (column == null) {
            if (other.column != null)
                return false;
        } else if (!column.equals(other.column))
            return false;
        if (className == null) {
            if (other.className != null)
                return false;
        } else if (!className.equals(other.className))
            return false;
        if (precision != other.precision)
            return false;
        if (scale != other.scale)
            return false;
        return true;
    }

    public String getColumn() {
        return column;
    }

    public String getClassName() {
        return className;
    }

    public int getPrecision() {
        return precision;
    }

    public int getScale() {
        return scale;
    }

    public boolean isNullable() {
        return nullable;
    }

}
