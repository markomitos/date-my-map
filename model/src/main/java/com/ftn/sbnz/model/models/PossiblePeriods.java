package com.ftn.sbnz.model.models;

import com.ftn.sbnz.model.models.Period;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PossiblePeriods {

    private List<Period> periods;
    private String finalPeriod;

    public PossiblePeriods() {
        this.periods = new ArrayList<>();
        this.periods.add(new Period(1800, 2025));
    }

    public PossiblePeriods(List<Period> periods) {
        this.periods = periods;
    }

    public List<Period> getPeriods() {
        return periods;
    }

    public void setPeriods(List<Period> periods) {
        this.periods = periods;
    }

    public String getFinalPeriod() {
        return finalPeriod;
    }

    public void intersectWith(int startYear, int endYear) {
        Period newPeriod = new Period(startYear, endYear);
        List<Period> intersectedPeriods = this.periods.stream()
                .map(p -> p.intersect(newPeriod))
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
        this.setPeriods(intersectedPeriods);
    }

    public void addPeriod(int startYear, int endYear) {
        this.periods.add(new Period(startYear, endYear));
    }

    public void subtractWith(int startYear, int endYear) {
        Period periodToRemove = new Period(startYear, endYear);
        List<Period> subtractedPeriods = this.periods.stream()
                .flatMap(p -> p.subtract(periodToRemove).stream())
                .collect(Collectors.toList());
        this.setPeriods(subtractedPeriods);
    }

    public void setFinalPeriod(String periodString) {
        this.finalPeriod = periodString;
        this.periods.clear();
        Pattern pattern = Pattern.compile("P(\\d{4})-(\\d{4})");
        Matcher matcher = pattern.matcher(periodString);
        if (matcher.find()) {
            int startYear = Integer.parseInt(matcher.group(1));
            int endYear = Integer.parseInt(matcher.group(2));
            this.periods.clear();
            this.periods.add(new Period(startYear, endYear));
        } else {
            System.err.println("Invalid final period format in DRL: " + periodString);
        }
    }

    @Override
    public String toString() {
        return "PossiblePeriods{" +
                "periods=" + periods.stream().map(Period::toString).collect(Collectors.joining(", ")) +
                '}';
    }
}
