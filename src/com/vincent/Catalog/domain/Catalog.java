package com.vincent.Catalog.domain;

import java.util.ArrayList;
import java.util.List;

import com.vincent.File.domain.File;


public class Catalog {

    private String cId; // 对应
    private String cName; // 对应
    private String cDate; // 对应
    private Catalog parent;
    private String isShare; // 对应
    private String uId;
    private int cLevel;
    private List<Catalog> children = new ArrayList<>();
    private List<File> myFile;
    private String cF;
    private boolean highlight;

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public String getcF() {
        return cF;
    }

    public void setcF(String cF) {
        this.cF = cF;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcDate() {
        return cDate;
    }

    public void setcDate(String cDate) {
        this.cDate = cDate;
    }

    public Catalog getParent() {
        return parent;
    }

    public void setParent(Catalog parent) {
        this.parent = parent;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public int getcLevel() {
        return cLevel;
    }

    public void setcLevel(int cLevel) {
        this.cLevel = cLevel;
    }

    public List<Catalog> getChildren() {
        return children;
    }

    public void setChildren(List<Catalog> children) {
        this.children = children;
    }

    public List<File> getMyFile() {
        return myFile;
    }

    public void setMyFile(List<File> myFile) {
        this.myFile = myFile;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cDate == null) ? 0 : cDate.hashCode());
        result = prime * result + ((cId == null) ? 0 : cId.hashCode());
        result = prime * result + cLevel;
        result = prime * result + ((cName == null) ? 0 : cName.hashCode());
        result = prime * result
                + ((children == null) ? 0 : children.hashCode());
        result = prime * result + ((isShare == null) ? 0 : isShare.hashCode());
        result = prime * result + ((myFile == null) ? 0 : myFile.hashCode());
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        result = prime * result + ((uId == null) ? 0 : uId.hashCode());
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
        Catalog other = (Catalog) obj;
        if (cDate == null) {
            if (other.cDate != null)
                return false;
        } else if (!cDate.equals(other.cDate))
            return false;
        if (cId == null) {
            if (other.cId != null)
                return false;
        } else if (!cId.equals(other.cId))
            return false;
        if (cLevel != other.cLevel)
            return false;
        if (cName == null) {
            if (other.cName != null)
                return false;
        } else if (!cName.equals(other.cName))
            return false;
        if (children == null) {
            if (other.children != null)
                return false;
        } else if (!children.equals(other.children))
            return false;
        if (isShare == null) {
            if (other.isShare != null)
                return false;
        } else if (!isShare.equals(other.isShare))
            return false;
        if (myFile == null) {
            if (other.myFile != null)
                return false;
        } else if (!myFile.equals(other.myFile))
            return false;
        if (parent == null) {
            if (other.parent != null)
                return false;
        } else if (!parent.equals(other.parent))
            return false;
        if (uId == null) {
            if (other.uId != null)
                return false;
        } else if (!uId.equals(other.uId))
            return false;
        return true;
    }

    @Override
    public String toString() {
        /*return "Catalog [cId=" + cId + ", cName=" + cName + ", cDate=" + cDate
                + ", parent=" + parent + ", isShare=" + isShare + ", uId="
                + uId + ", cLevel=" + cLevel + ", children=" + children
                + ", myFile=" + myFile + "]";*/
        return cId;
    }

}
