package com.lego.survey.zuul.predicate;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateBasedRule;

/**
 * @author yanglf
 * @description
 * @since 2019/7/12
 **/
public class GrayAwareRule extends PredicateBasedRule {

    private AbstractServerPredicate serverPredicate;


    public GrayAwareRule() {
    }

    public GrayAwareRule(AbstractServerPredicate serverPredicate) {
        this.serverPredicate = serverPredicate;
    }

    @Override
    public AbstractServerPredicate getPredicate() {
        return serverPredicate;
    }
}
