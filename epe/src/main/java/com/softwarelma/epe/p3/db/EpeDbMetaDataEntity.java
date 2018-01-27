package com.softwarelma.epe.p3.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p3.db.EpeDbMetaDataColumn;

public class EpeDbMetaDataEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String table;// upperCase

    // upperCase, the att is the col name
    private final List<String> listAttribute;

    // upperCase, the att is the col name
    private final Map<String, EpeDbMetaDataColumn> mapAttAndMetaAtt;

    public EpeDbMetaDataEntity(String table, List<String> listAtrribute,
            Map<String, EpeDbMetaDataColumn> mapAttAndMetaAtt) throws EpeAppException {
        EpeAppUtils.checkEmpty("table", table);
        List<String> listAttributeNorm = this.retrieveListAttributeNorm(listAtrribute);
        Map<String, EpeDbMetaDataColumn> mapAttAndMetaAttNorm = this.retrieveMapAttAndMetaAttNorm(mapAttAndMetaAtt);
        EpeAppUtils.checkEquals("listAttribute", "mapAttAndMetaAtt.keySet", listAttributeNorm,
                mapAttAndMetaAttNorm.keySet());
        this.table = table.toUpperCase();
        this.listAttribute = listAttributeNorm;
        this.mapAttAndMetaAtt = mapAttAndMetaAttNorm;
    }

    @Override
    public String toString() {
        return "EpeDbMetaDataEntity [\n    table=" + table + ",\n    listAttribute=" + listAttribute
                + ",\n    mapAttAndMetaAtt=" + this.toStringMapAttAndMetaAtt() + "]";
    }

    private String toStringMapAttAndMetaAtt() {
        StringBuilder sb = new StringBuilder("{");
        String sep = "";

        for (String att : this.mapAttAndMetaAtt.keySet()) {
            sb.append(sep);
            sep = ",";
            sb.append("\n    ");
            sb.append(att);
            sb.append("=");
            sb.append(this.mapAttAndMetaAtt.get(att));
        }

        sb.append("\n  }");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((listAttribute == null) ? 0 : listAttribute.hashCode());
        result = prime * result + ((table == null) ? 0 : table.hashCode());
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
        EpeDbMetaDataEntity other = (EpeDbMetaDataEntity) obj;
        if (listAttribute == null) {
            if (other.listAttribute != null)
                return false;
        } else if (!listAttribute.equals(other.listAttribute))
            return false;
        if (table == null) {
            if (other.table != null)
                return false;
        } else if (!table.equals(other.table))
            return false;
        return true;
    }

    public void validateSetAttribute(Set<String> setAttribute) throws EpeAppException {
        EpeAppUtils.checkEquals("listAttribute", "setAttribute", this.listAttribute, setAttribute);
    }

    private Map<String, EpeDbMetaDataColumn> retrieveMapAttAndMetaAttNorm(
            Map<String, EpeDbMetaDataColumn> mapAttAndMetaAtt) throws EpeAppException {
        EpeAppUtils.checkEmptyMap("mapAttAndMetaAtt", mapAttAndMetaAtt);
        Map<String, EpeDbMetaDataColumn> mapAttAndMetaAttNorm = new LinkedHashMap<>();
        EpeDbMetaDataColumn metaAtt;
        String attributeUpper;

        for (String attribute : mapAttAndMetaAtt.keySet()) {
            EpeAppUtils.checkEmpty("attribute", attribute);
            metaAtt = mapAttAndMetaAtt.get(attribute);
            EpeAppUtils.checkNull("metaAtt", metaAtt);
            attributeUpper = attribute.toUpperCase();
            EpeAppUtils.checkEquals("attributeUpper", "metaAtt.attribute", attributeUpper, metaAtt.getColumn());
            mapAttAndMetaAttNorm.put(attributeUpper, metaAtt);
        }

        return mapAttAndMetaAttNorm;
    }

    private List<String> retrieveListAttributeNorm(List<String> listAttribute) throws EpeAppException {
        EpeAppUtils.checkEmptyList("listAttribute", listAttribute);
        List<String> listAttributeNorm = new ArrayList<>();

        for (String attribute : listAttribute) {
            EpeAppUtils.checkEmpty("attribute", attribute);
            listAttributeNorm.add(attribute.toUpperCase());
        }

        return listAttributeNorm;
    }

    public List<String> retrieveListAttributeNoFK() throws EpeAppException {
        List<String> listAttribute = new ArrayList<>();

        for (String attribute : this.listAttribute) {
            EpeAppUtils.checkEmpty("attribute", attribute);

            if (!attribute.startsWith(EpeDbEntityColumns.COLUMN_PREFIX_ID)) {
                listAttribute.add(attribute);
            }
        }

        return listAttribute;
    }

    public List<String> retrieveListAttributeFK() throws EpeAppException {
        List<String> listAttribute = new ArrayList<>();

        for (String attribute : this.listAttribute) {
            EpeAppUtils.checkEmpty("attribute", attribute);

            if (attribute.startsWith(EpeDbEntityColumns.COLUMN_PREFIX_ID)) {
                listAttribute.add(attribute);
            }
        }

        return listAttribute;
    }

    public boolean isSystemTable() {
        return this.table.startsWith(EpeDbEntityTables.TABLE_PREFIX_SYS);
    }

    public boolean isAdminTable() {
        return this.table.startsWith(EpeDbEntityTables.TABLE_PREFIX_DMN);
    }

    public boolean isFinalTable() {
        return this.table.startsWith(EpeDbEntityTables.TABLE_PREFIX_FNL);
    }

    public String getTable() {
        return this.table;
    }

    public int getCols() {
        return this.listAttribute.size();
    }

    public String getAttribute(int index) throws EpeAppException {
        EpeAppUtils.checkRange(index, 0, this.listAttribute.size(), false, true);
        return this.listAttribute.get(index);
    }

    public String getClassName(int index) throws EpeAppException {
        EpeDbMetaDataColumn metaAtt = this.mapAttAndMetaAtt.get(this.getAttribute(index));
        EpeAppUtils.checkNull("metaAtt", metaAtt);
        return metaAtt.getClassName();
    }

    public String getClassName(String attribute) throws EpeAppException {
        EpeAppUtils.checkEmpty("attribute", attribute);
        EpeDbMetaDataColumn metaAtt = this.mapAttAndMetaAtt.get(attribute.toUpperCase());
        EpeAppUtils.checkNull("metaAtt", metaAtt);
        return metaAtt.getClassName();
    }

    public int getPrecision(int index) throws EpeAppException {
        EpeDbMetaDataColumn metaAtt = this.mapAttAndMetaAtt.get(this.getAttribute(index));
        EpeAppUtils.checkNull("metaAtt", metaAtt);
        return metaAtt.getPrecision();
    }

    public int getPrecision(String attribute) throws EpeAppException {
        EpeAppUtils.checkEmpty("attribute", attribute);
        EpeDbMetaDataColumn metaAtt = this.mapAttAndMetaAtt.get(attribute.toUpperCase());
        EpeAppUtils.checkNull("metaAtt", metaAtt);
        return metaAtt.getPrecision();
    }

    public int getScale(int index) throws EpeAppException {
        EpeDbMetaDataColumn metaAtt = this.mapAttAndMetaAtt.get(this.getAttribute(index));
        EpeAppUtils.checkNull("metaAtt", metaAtt);
        return metaAtt.getScale();
    }

    public int getScale(String attribute) throws EpeAppException {
        EpeAppUtils.checkEmpty("attribute", attribute);
        EpeDbMetaDataColumn metaAtt = this.mapAttAndMetaAtt.get(attribute.toUpperCase());
        EpeAppUtils.checkNull("metaAtt", metaAtt);
        return metaAtt.getScale();
    }

    public boolean isNullable(int index) throws EpeAppException {
        EpeDbMetaDataColumn metaAtt = this.mapAttAndMetaAtt.get(this.getAttribute(index));
        EpeAppUtils.checkNull("metaAtt", metaAtt);
        return metaAtt.isNullable();
    }

    public boolean isNullable(String attribute) throws EpeAppException {
        EpeAppUtils.checkEmpty("attribute", attribute);
        EpeDbMetaDataColumn metaAtt = this.mapAttAndMetaAtt.get(attribute.toUpperCase());
        EpeAppUtils.checkNull("metaAtt", metaAtt);
        return metaAtt.isNullable();
    }

    public String getDefaultValue(int index) throws EpeAppException {
        EpeDbMetaDataColumn metaAtt = this.mapAttAndMetaAtt.get(this.getAttribute(index));
        EpeAppUtils.checkNull("metaAtt", metaAtt);
        return metaAtt.getDefaultValue();
    }

    public String getDefaultValue(String attribute) throws EpeAppException {
        EpeAppUtils.checkEmpty("attribute", attribute);
        EpeDbMetaDataColumn metaAtt = this.mapAttAndMetaAtt.get(attribute.toUpperCase());
        EpeAppUtils.checkNull("metaAtt", metaAtt);
        return metaAtt.getDefaultValue();
    }

    public String getComment(int index) throws EpeAppException {
        EpeDbMetaDataColumn metaAtt = this.mapAttAndMetaAtt.get(this.getAttribute(index));
        EpeAppUtils.checkNull("metaAtt", metaAtt);
        return metaAtt.getComment();
    }

    public String getComment(String attribute) throws EpeAppException {
        EpeAppUtils.checkEmpty("attribute", attribute);
        EpeDbMetaDataColumn metaAtt = this.mapAttAndMetaAtt.get(attribute.toUpperCase());
        EpeAppUtils.checkNull("metaAtt", metaAtt);
        return metaAtt.getComment();
    }

    public void validateTypeAndNullable(String attribute, Object value) throws EpeAppException {
        if (value == null) {
            if (!this.isNullable(attribute)) {
                throw new EpeAppException("The attribute " + attribute + " can't be null");
            }
        } else {
            String className = this.getClassName(attribute);
            EpeAppUtils.checkEquals("meataDataClassName", "valueClassName", className, value.getClass().getName());
        }
    }

    public void setColumnClassNameIfNotNull(String attribute, Object obj) throws EpeAppException {
        EpeAppUtils.checkEmpty("attribute", attribute);
        attribute = attribute.toUpperCase();

        if (obj != null && this.mapAttAndMetaAtt.get(attribute).getClassName() == null) {
            this.mapAttAndMetaAtt.get(attribute).setClassName(obj.getClass().getName());
        }
    }

}
