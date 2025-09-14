package com.ftn.sbnz.model.models;

import com.github.javaparser.ast.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Period {

    private int startYear;
    private int endYear;

    public Period(int startYear, int endYear) {
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    @Override
    public String toString() {
        return "[" + startYear + ", " + endYear + "]";
    }

    public Period intersect(Period other) {
        int intersectStart = Math.max(this.startYear, other.startYear);
        int intersectEnd = Math.min(this.endYear, other.endYear);

        if (intersectStart >= intersectEnd) {
            return null;
        }
        return new Period(intersectStart, intersectEnd);
    }

    public List<Period> subtract(Period periodToRemove) {
        List<Period> result = new ArrayList<>();

        Period intersection = this.intersect(periodToRemove);

        if (intersection == null) {
            result.add(this);
            return result;
        }

        if (this.startYear < intersection.startYear) {
            result.add(new Period(this.startYear, intersection.startYear));
        }

        if (this.endYear > intersection.endYear) {
            result.add(new Period(intersection.endYear, this.endYear));
        }

        return result;
    }


}
