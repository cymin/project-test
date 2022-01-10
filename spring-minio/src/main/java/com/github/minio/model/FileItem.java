package com.github.minio.model;

import java.time.ZonedDateTime;

public class FileItem {
    boolean dir = false;
    long size;
    ZonedDateTime lastModified;
    boolean latest;
    String etag;
    String objectName;
    String versionId;
    boolean deleteMarker;

    public boolean isDir() {
        return dir;
    }

    public void setDir(boolean dir) {
        this.dir = dir;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public ZonedDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(ZonedDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isLatest() {
        return latest;
    }

    public void setLatest(boolean latest) {
        this.latest = latest;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public boolean isDeleteMarker() {
        return deleteMarker;
    }

    public void setDeleteMarker(boolean deleteMarker) {
        this.deleteMarker = deleteMarker;
    }
}
