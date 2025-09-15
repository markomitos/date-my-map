package com.ftn.sbnz.model.models;

import java.util.List;

public class FinalPath {

    private List<String> path;

    public FinalPath(List<String> path) {
        this.path = path;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public FinalPath() {}
}