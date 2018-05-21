package com.softwarelma.epe.p3.db;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.softwarelma.epe.p1.app.EpeAppConstants;
import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public class EpeDbEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean insert;
    private final EpeDbMetaDataEntity metaData;
    private final Map<String, Object> mapAttAndValue;
    private final List<String> listAttUpdated;

    public static Map<String, Object> retrieveVoidMapAttAndValue(EpeDbMetaDataEntity metaData) throws EpeAppException {
        Map<String, Object> mapAttAndValue = new HashMap<>();
        for (int i = 0; i < metaData.getCols(); i++)
            mapAttAndValue.put(metaData.getAttribute(i), null);
        return mapAttAndValue;
    }

    public EpeDbEntity(EpeDbMetaDataEntity metaData) throws EpeAppException {
        this(metaData, retrieveVoidMapAttAndValue(metaData));
    }

    public EpeDbEntity(EpeDbMetaDataEntity metaData, Map<String, Object> mapAttAndValue) throws EpeAppException {
        EpeAppUtils.checkNull("metaData", metaData);
        this.metaData = metaData;
        this.mapAttAndValue = this.retrieveMapAttAndValueNorm(mapAttAndValue);
        Set<String> setAtt = this.mapAttAndValue.keySet();
        this.metaData.validateSetAttribute(setAtt);
        boolean insert = this.getOrNull(EpeDbEntityColumns.ID) == null;
        // boolean insert = this.getLong(EpeDbEntityColumns.ID) == null;

        if (!insert) {
            Object value;

            for (String attribute : setAtt) {
                value = this.mapAttAndValue.get(attribute);
                this.metaData.validateTypeAndNullable(attribute, value);
            }
        }

        this.insert = insert;
        this.listAttUpdated = new ArrayList<>();
    }

    private EpeDbEntity(boolean insert, EpeDbMetaDataEntity metaData, Map<String, Object> mapAttAndValue,
            List<String> listAttUpdated) throws EpeAppException {
        this.insert = insert;
        this.metaData = metaData;
        this.mapAttAndValue = this.retrieveMapAttAndValueNorm(mapAttAndValue);
        this.listAttUpdated = listAttUpdated;
    }

    @Override
    public String toString() {
        return "\nEpeDbEntity [\n  insert=" + insert + ",\n  metaData=" + metaData + ",\n  mapAttAndValue="
                + mapAttAndValue + ",\n  listAttUpdated=" + listAttUpdated + "]";
    }

    public EpeDbEntity retrieveClone() throws EpeAppException {
        Map<String, Object> mapAttAndValue = new HashMap<>(this.mapAttAndValue);
        List<String> listAttOriginal = new ArrayList<>(this.listAttUpdated);
        EpeDbEntity clone = new EpeDbEntity(this.insert, this.metaData, mapAttAndValue, listAttOriginal);
        return clone;
    }

    private Map<String, Object> retrieveMapAttAndValueNorm(Map<String, Object> mapAttAndValue) throws EpeAppException {
        EpeAppUtils.checkEmptyMap("mapAttAndValue", mapAttAndValue);
        Map<String, Object> mapAttAndValueNorm = new HashMap<>();

        for (String attribute : mapAttAndValue.keySet()) {
            EpeAppUtils.checkEmpty("attribute", attribute);
            mapAttAndValueNorm.put(attribute.toUpperCase(), mapAttAndValue.get(attribute));
        }

        return mapAttAndValueNorm;
    }

    public boolean isInsert() {
        return insert;
    }

    public void setInsert(boolean insert) {
        this.insert = insert;
    }

    public EpeDbMetaDataEntity getMetaData() {
        return metaData;
    }

    @SuppressWarnings("unchecked")
    public <T> T getWithType(String attribute, Class<T> clazz) throws EpeAppException {
        Object value = this.get(attribute);
        EpeAppUtils.checkInstanceOf(attribute, value, clazz);
        return (T) value;
    }

    public Long getLong(String attribute) throws EpeAppException {
        return this.getWithType(attribute, Long.class);
    }

    /**
     * the type of the value must be String
     */
    public String getString(String attribute) throws EpeAppException {
        return this.getWithType(attribute, String.class);
    }

    /**
     * The type of the value gets converted to String. Null becomes "".
     */
    public String getToString(String attribute) throws EpeAppException {
        String className = this.metaData.getClassName(attribute);
        Object value = this.get(attribute);

        if (value == null) {
            return "";
        } else if (className.equals(String.class.getName())) {
            return value.toString();
        } else if (className.equals(Integer.class.getName())) {
            return value.toString();
        } else {
            throw new EpeAppException("Unknown class " + className);
        }
    }

    public String getToStringForDb(String attribute) throws EpeAppException {
        String className = this.metaData.getClassName(attribute);
        Object value = this.get(attribute);

        if (value == null || value.toString().isEmpty()) {
            return null;
        } else if (className.equals(String.class.getName())) {
            return getToStringAsVarchar(value.toString());
        } else if (className.equals(Integer.class.getName())) {
            return value.toString();
        } else if (className.equals(Timestamp.class.getName())) {
            // TODO oracle and others
            String dateStr = EpeAppUtils.retrieveTimestamp(EpeAppConstants.TIMESTAMP_DEFAULT_FORMAT, (Timestamp) value);
            dateStr = getToStringAsVarchar(dateStr);
            return "STR_TO_DATE(" + dateStr + ", '%Y%m%d-%H%i%s')";
        } else {
            throw new EpeAppException("Unknown class " + className);
        }
    }

    /**
     * replace ' with ''
     */
    public static String getToStringAsVarchar(String value) throws EpeAppException {
        EpeAppUtils.checkNull("value", value);
        // TODO replace & with && in case of oracle and others
        return "'" + value.replace("'", "''")/* .replace("&", "&&") */ + "'";
    }

    public Object getOrNull(String attribute) throws EpeAppException {
        EpeAppUtils.checkEmpty("attribute", attribute);
        attribute = attribute.toUpperCase();
        EpeAppUtils.checkContains(this.mapAttAndValue.keySet(), "attribute", attribute);
        return this.mapAttAndValue.get(attribute);
    }

    public Object get(String attribute) throws EpeAppException {
        Object value = this.getOrNull(attribute);
        this.metaData.validateTypeAndNullable(attribute, value);
        return value;
    }

    public void set(String attribute, Object value) throws EpeAppException {
        EpeAppUtils.checkEmpty("attribute", attribute);
        attribute = attribute.toUpperCase();
        EpeAppUtils.checkContains(this.mapAttAndValue.keySet(), "attribute", attribute);
        this.metaData.validateTypeAndNullable(attribute, value);
        this.mapAttAndValue.put(attribute, value);

        if (!this.listAttUpdated.contains(attribute)) {
            this.listAttUpdated.add(attribute);
        }
    }

    public void setFromString(String attribute, String valueStr) throws EpeAppException {
        String className = this.metaData.getClassName(attribute);
        EpeAppUtils.checkEmpty("className", className);
        Object value;

        if (valueStr == null || valueStr.isEmpty()) {
            value = null;
        } else if (className.equals(String.class.getName())) {
            value = valueStr;
        } else if (className.equals(Integer.class.getName())) {
            try {
                value = Integer.parseInt(valueStr);
            } catch (NumberFormatException e) {
                throw new EpeAppException("Invalid integer \"" + valueStr + "\"");
            }
        } else if (className.equals(Timestamp.class.getName())) {
            value = EpeAppUtils.parseTimestamp(EpeAppConstants.TIMESTAMP_DEFAULT_FORMAT, valueStr);
        } else {
            throw new EpeAppException("Unknown class " + className);
        }

        this.set(attribute, value);
    }

    private String retrieveDescriptionShortOrLong(boolean shortDescr, String descriptionTemplate,
            String descriptionColumns) throws EpeAppException {
        if (EpeAppUtils.isEmpty(descriptionTemplate) || EpeAppUtils.isEmpty(descriptionColumns)) {
            return shortDescr ? this.getString(EpeDbEntityColumns.NAME)
                    : "(" + this.get(EpeDbEntityColumns.ID) + ") " + this.getString(EpeDbEntityColumns.NAME);
        }

        String[] arrayAttribute = descriptionColumns.split(Pattern.quote(","));
        String valueStr;

        for (String attribute : arrayAttribute) {
            valueStr = this.getToString(attribute);
            valueStr = valueStr == null ? "" : valueStr;
            descriptionTemplate = descriptionTemplate.replace("{" + attribute + "}", valueStr);
        }

        return descriptionTemplate;
    }

    public String retrieveDescriptionShort() throws EpeAppException {
        return this.retrieveDescriptionShortOrLong(true, EpeDbEntityColumns.DESCR_SHORT_TEMPL,
                EpeDbEntityColumns.DESCR_SHORT_COLS);
    }

    public String retrieveDescriptionLong() throws EpeAppException {
        return this.retrieveDescriptionShortOrLong(false, EpeDbEntityColumns.DESCR_LONG_TEMPL,
                EpeDbEntityColumns.DESCR_LONG_COLS);
    }

    public String retrieveDescription(String attribute) throws EpeAppException {
        EpeAppUtils.checkEmpty("attribute", attribute);

        if (this.mapAttAndValue.containsKey(attribute.toUpperCase())) {
            Object value = this.mapAttAndValue.get(attribute.toUpperCase());
            // EpeAppUtils.checkNull("value", value);
            return value == null ? "" : value + "";
        } else if (attribute.equals("retrieveDescriptionShort()")) {
            return this.retrieveDescriptionShort();
        } else if (attribute.equals("retrieveDescriptionLong()")) {
            return this.retrieveDescriptionLong();
        } else {
            throw new EpeAppException("Unknown attribute: " + attribute);
        }
    }

    public static String retrieveInsertBlank(String table) throws EpeAppException {
        EpeAppUtils.checkEmpty("table", table);
        return "INSERT INTO " + table.toLowerCase() + " (name) VALUES ('???')";
    }

    public String retrieveInsert() throws EpeAppException {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(this.metaData.getTable());
        sb.append(" (");
        String sep = "";

        for (int i = 0; i < this.metaData.getCols(); i++) {
            sb.append(sep);
            sep = ", ";
            sb.append(this.metaData.getAttribute(i));
        }

        sb.append(") VALUES (");
        sep = "";

        for (int i = 0; i < this.metaData.getCols(); i++) {
            sb.append(sep);
            sep = ", ";
            sb.append(this.getToStringForDb(this.metaData.getAttribute(i)));
        }

        sb.append(")");
        return sb.toString();
    }

    public String retrieveUpdate() throws EpeAppException {
        if (this.insert)
            throw new EpeAppException("Entity not valid for update");
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(this.metaData.getTable().toLowerCase());
        sb.append(" SET ");
        String sep = "";

        for (String col : this.listAttUpdated) {
            if (col.toUpperCase().equals("ID"))
                continue;
            sb.append(sep);
            sep = ", ";
            sb.append(col);
            sb.append(" = ");
            sb.append(this.getToStringForDb(col));
        }

        sb.append(" WHERE id = ");
        sb.append(this.get("id"));
        return sb.toString();
    }

    public String retrieveDelete() throws EpeAppException {
        return "DELETE FROM " + this.metaData.getTable().toLowerCase() + " WHERE id = " + this.getToString("id");
    }

}
