package org.example.test.infrastructure;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.infrastructure.persistent.dao.IAwardDAO;
import org.example.infrastructure.persistent.po.Award;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author atticus
 * @Date 2024/09/27 22:57
 * @description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AwardDAOTest {
    @Resource
    private IAwardDAO awardDAO;

    @Test
    public void test_queryAwardDAO(){
        List<Award> list = awardDAO.queryAwardList();
        log.info("测试结果: {}", JSON.toJSONString(list));
    }
}
