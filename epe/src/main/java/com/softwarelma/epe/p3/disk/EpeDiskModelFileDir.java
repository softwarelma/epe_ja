package com.softwarelma.epe.p3.disk;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;

public abstract class EpeDiskModelFileDir {

    private final String location;
    private final String name;

    protected EpeDiskModelFileDir(String location, String name) throws EpeAppException {
        EpeAppUtils.checkNull("location", location);
        EpeAppUtils.checkNull("name", name);
        this.location = EpeAppUtils.cleanFilename(location);
        this.name = EpeAppUtils.cleanFilename(name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        EpeDiskModelFileDir other = (EpeDiskModelFileDir) obj;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public abstract String toString();

    public abstract String toString(String tabs);

    protected abstract boolean isDir();

    protected abstract boolean isFile();

    protected abstract EpeDiskModelDir toDir() throws EpeAppException;

    protected abstract EpeDiskModelFile toFile() throws EpeAppException;

    protected String getLocation() {
        return location;
    }

    protected String getName() {
        return name;
    }

}
