package com.ftn.sbnz.model.models;

import com.ftn.sbnz.model.events.AnswerProvidedEvent;
import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.drools.ruleunits.api.RuleUnitData;
import org.drools.ruleunits.api.SingletonStore;

public class DatingRuleUnit implements RuleUnitData {

    private final SingletonStore<PossiblePeriods> periods;
    private final DataStore<AnswerProvidedEvent> answers;

    public DatingRuleUnit() {
        this.periods = DataSource.createSingleton();
        this.answers = DataSource.createStore();
    }

    public DatingRuleUnit(PossiblePeriods periods) {
        this();
        this.periods.set(periods);
    }

    public SingletonStore<PossiblePeriods> getPeriods() {
        return periods;
    }

    public DataStore<AnswerProvidedEvent> getAnswers() {
        return answers;
    }
}