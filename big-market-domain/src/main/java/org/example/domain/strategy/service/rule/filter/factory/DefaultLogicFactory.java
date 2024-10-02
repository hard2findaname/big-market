package org.example.domain.strategy.service.rule.filter.factory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.domain.strategy.model.entity.RuleActionEntity;
import org.example.domain.strategy.service.annotation.LogicStrategy;
import org.example.domain.strategy.service.rule.filter.ILogicFilter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author atticus
 * @Date 2024/10/01 16:04
 * @description: 规则过滤工厂
 */
@Service
public class DefaultLogicFactory {
    public Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {
        logicFilters.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (null != strategy) {
                logicFilterMap.put(strategy.logicMode().getCode(), logic);
            }
        });
    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String, ILogicFilter<T>> openLogicFilter() {
        return (Map<String, ILogicFilter<T>>) (Map<?, ?>) logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {

        RULE_WEIGHT("rule_weight","【抽奖前规则】根据抽奖权重返回可抽奖范围KEY","pre"),
        RULE_BLACKLIST("rule_blacklist","【抽奖前规则】黑名单规则过滤，命中黑名单则直接返回","pre"),
        RULE_LOCK("rule_lock","【抽奖中规则】抽奖n次后，解锁对应奖品可抽奖","during"),

        RULE_LUCK_AWARD("rule_luck_award","【抽奖中规则】抽奖n次后，解锁对应奖品可抽奖","post"),


        ;

        private final String code;
        private final String info;
        private final String type;

        public static boolean isDuring(String code){
            return "during".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }
        public static boolean isAfter(String code){
            return "post".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }

    }


}
