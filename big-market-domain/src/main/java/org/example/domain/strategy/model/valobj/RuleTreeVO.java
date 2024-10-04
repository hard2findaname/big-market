package org.example.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author atticus
 * @Date 2024/10/03 18:32
 * @description: 规则树对象【注意；不具有唯一ID，不需要改变数据库结果的对象，可以被定义为值对象】
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleTreeVO {
    /** 规则树ID */
    private Integer treeId;
    /** 规则树名称 */
    private String treeName;
    /** 规则树描述 */
    private String treeDesc;
    /** 规则根节点 */
    private String treeRootRuleNode;

    /** 规则节点 */
    private Map<String, RuleTreeNodeVO> treeNodeMap;

}