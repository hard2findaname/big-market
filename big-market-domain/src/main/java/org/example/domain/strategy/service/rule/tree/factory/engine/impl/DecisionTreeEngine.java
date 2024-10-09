package org.example.domain.strategy.service.rule.tree.factory.engine.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import org.example.domain.strategy.model.valobj.RuleTreeNodeLineVO;
import org.example.domain.strategy.model.valobj.RuleTreeNodeVO;
import org.example.domain.strategy.model.valobj.RuleTreeVO;
import org.example.domain.strategy.service.rule.tree.ILogicTreeNode;
import org.example.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import org.example.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;

import java.util.List;
import java.util.Map;

/**
 * @Author atticus
 * @Date 2024/10/03 20:33
 * @description:
 */
@Slf4j
public class DecisionTreeEngine implements IDecisionTreeEngine {
    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

    private final RuleTreeVO ruleTreeVO;

    public DecisionTreeEngine(Map<String, ILogicTreeNode> logicTreeNodeMap, RuleTreeVO ruleTreeVO) {
        this.logicTreeNodeGroup = logicTreeNodeMap;
        this.ruleTreeVO = ruleTreeVO;

    }


    @Override
    public DefaultTreeFactory.StrategyAwardVO process(String userId, Long strategyId, Integer awardId) {

        DefaultTreeFactory.StrategyAwardVO strategyAwardData = null;

        // 获取基础信息
        String head = ruleTreeVO.getTreeRootRuleNode();
        Map<String, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();

        // 获取起始节点「根节点记录了第一个要执行的规则」
        RuleTreeNodeVO ruleTreeNode = treeNodeMap.get(head);
        while(null != head){
            ILogicTreeNode logicTreeNode = logicTreeNodeGroup.get(ruleTreeNode.getRuleKey());
            String ruleValue = ruleTreeNode.getRuleValue();

            DefaultTreeFactory.TreeActionEntity logicEntity = logicTreeNode.logic(userId, strategyId, awardId,ruleValue);
            RuleLogicCheckTypeVO ruleLogicCheckType = logicEntity.getRuleLogicCheckType();
            strategyAwardData = logicEntity.getStrategyAwardVO();
            log.info("决策树引擎[{}] treeId:{} node:{} code:{}", ruleTreeVO.getTreeName(), ruleTreeNode.getTreeId(), head,ruleLogicCheckType.getCode());

            head = checkNextNode(ruleLogicCheckType.getCode(), ruleTreeNode.getTreeNodeLineVOList());
            ruleTreeNode = treeNodeMap.get(head);

        }
        return strategyAwardData;
    }
    private String checkNextNode(String matterValue, List<RuleTreeNodeLineVO> ruleTreeNodeLineList){
        if(null == ruleTreeNodeLineList || ruleTreeNodeLineList.isEmpty()) return null;

        for(RuleTreeNodeLineVO ruleTreeNodeLine :  ruleTreeNodeLineList){
            if(decisionLogic(matterValue,ruleTreeNodeLine)){
                return ruleTreeNodeLine.getRuleNodeTo();
            }
        }
        return null;
    }

    public boolean decisionLogic(String matterValue, RuleTreeNodeLineVO nodeLine){
        switch (nodeLine.getRuleLimitType()){
            case EQUAL:
                return matterValue.equals(nodeLine.getRuleLimitValue().getCode());
            case GE:
            case GT:
            case LE:
            case LT:
            case ENUM:
            default:
                return false;
        }
    }

}
