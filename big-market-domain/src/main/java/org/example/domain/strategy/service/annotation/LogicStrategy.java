package org.example.domain.strategy.service.annotation;

import org.example.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author atticus
 * @Date 2024/10/01 16:02
 * @description: 策略自定义枚举
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)

public @interface LogicStrategy {
    DefaultLogicChainFactory.LogicModel logicMode();
}
