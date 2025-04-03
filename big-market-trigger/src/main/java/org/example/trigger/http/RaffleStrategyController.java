package org.example.trigger.http;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.activity.service.IRaffleActivityAccountQuotaService;
import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;
import org.example.domain.strategy.model.entity.StrategyAwardEntity;
import org.example.domain.strategy.service.IRaffleAward;
import org.example.domain.strategy.service.IRaffleRule;
import org.example.domain.strategy.service.IRaffleStrategy;
import org.example.domain.strategy.service.assembly.IStrategyAssembly;
import org.example.trigger.api.IRaffleStrategyService;
import org.example.trigger.api.dto.RaffleAwardListRequestDTO;
import org.example.trigger.api.dto.RaffleAwardListResponseDTO;
import org.example.trigger.api.dto.RaffleStrategyRequestDTO;
import org.example.trigger.api.dto.RaffleResponseDTO;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.example.types.model.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author atticus
 * @Date 2024/10/09 20:05
 * @description: 抽奖服务
 */
@Slf4j
@RestController()
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/strategy")
public class RaffleStrategyController implements IRaffleStrategyService {

    @Resource
    private IStrategyAssembly strategyAssembly;
    @Resource
    private IRaffleRule raffleRule;

    @Resource
    private IRaffleAward raffleAward;

    @Resource
    private IRaffleStrategy raffleStrategy;
    //账户额度领域服务
    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;
    /**
     * @description:
     * 策略装配，将策略信息装配到缓存中
     * <a href="http://localhost:8091/api/v1/raffle/strategy/strategy_assembly">/api/v1/raffle/strategy_armory</a>
     * @param: strategyId 策略ID
     * @return: Response<Boolean> 装配结果
     **/
    @RequestMapping(value = "strategy_assembly", method = RequestMethod.GET)
    @Override
    public Response<Boolean> strategyAssembly(Long strategyId) {
        try{
            log.info("抽奖策略装配开始 strategyId：{}", strategyId);
            boolean strategyStatus = strategyAssembly.assembleLotteryStrategy(strategyId);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(strategyStatus)
                    .build();
            log.info("抽奖策略装配完成 strategyId：{} response: {}", strategyId, JSON.toJSONString(strategyStatus));
            return response;
        }catch (Exception e){
            log.error("抽奖策略装配失败 strategyId：{}", strategyId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }
    /**
     * @description:
     * 查询奖品列表
     * <a href="http://localhost:8091/api/v1/raffle/strategy/query_raffle_award_list">/api/v1/raffle/query_raffle_award_list</a>
     * @param: request
     * @return: Response<List<RaffleAwardListResponseDTO>>
     **/
    @RequestMapping(value = "query_raffle_award_list", method = RequestMethod.POST)
    @Override
    public Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(@RequestBody RaffleAwardListRequestDTO request) {
        try{
            log.info("查询抽奖奖品列表配开始 userId:{} activityId：{}", request.getUserId(),request.getActivityId());
            //1. 参数校验
            if(StringUtils.isBlank(request.getUserId()) || null == request.getActivityId()){
                return Response.<List<RaffleAwardListResponseDTO>>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }
            //2. 查询奖品配置
            List<StrategyAwardEntity> strategyAwardEntityList = raffleAward.queryRaffleStrategyAwardListByActivityId(request.getActivityId());
            //3. 获取规则配置
            String[] treeIdList = strategyAwardEntityList.stream()
                    .map(StrategyAwardEntity::getRuleModels)
                    .filter(ruleModel -> ruleModel != null && !ruleModel.isEmpty())
                    .toArray(String[]::new);
            //4. 查询规则配置- 获取奖品解锁限制
            Map<String, Integer> ruleLockCountMap = raffleRule.queryAwardRuleLockCount(treeIdList);
            //5. 查询抽奖次数 - 用户已经参与抽奖的次数
            Integer accountDailyLottery = raffleActivityAccountQuotaService.queryAccountDailyLottery(request.getActivityId(), request.getUserId());

//            log.info("\n测试打印： userId: {}, activityId: {}, 用户当日参与抽奖次数: {}\n", request.getUserId(), request.getActivityId(), accountDailyLottery);

            //6. 遍历，填充数据
            List<RaffleAwardListResponseDTO> raffleAwardListResponseDTOS = new ArrayList<>(strategyAwardEntityList.size());
            for (StrategyAwardEntity strategyAward : strategyAwardEntityList){

                //
                Integer awardUnlockCount = ruleLockCountMap.get(strategyAward.getRuleModels());
                RaffleAwardListResponseDTO raffleAwardListResponseDTO = RaffleAwardListResponseDTO.builder()
                        .awardId(strategyAward.getAwardId())
                        .awardTitle(strategyAward.getAwardTitle())
                        .awardSubtitle(strategyAward.getAwardSubtitle())
                        .sort(strategyAward.getSort())
                        .awardUnlockCount(awardUnlockCount)
                        .isAwardUnLocked(null == awardUnlockCount || accountDailyLottery > awardUnlockCount)
                        .unlockRemaining(null == awardUnlockCount || awardUnlockCount <= accountDailyLottery ? 0 : awardUnlockCount - accountDailyLottery)
                        .build();

                log.info("\n测试， awardId:{} awardTitle:{} isAwardLocked:{} unLockRemaining:{}\n",
                        raffleAwardListResponseDTO.getAwardId(),
                        raffleAwardListResponseDTO.getAwardTitle(),
                        raffleAwardListResponseDTO.isAwardUnLocked(),
                        raffleAwardListResponseDTO.getUnlockRemaining());

                raffleAwardListResponseDTOS.add(raffleAwardListResponseDTO);
            }
            Response<List<RaffleAwardListResponseDTO>> response = Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(raffleAwardListResponseDTOS)
                    .build();
            log.info("\n查询抽奖奖品列表配置完成 userId:{} activityId：{} response:{}\n", request.getUserId(),request.getActivityId(), JSON.toJSONString(response));
            // 返回结果
            return response;

        }catch (Exception e){
            log.error("查询抽奖奖品列表配置失败 userId:{} activityId：{}", request.getUserId(),request.getActivityId(), e);
            return Response.<List<RaffleAwardListResponseDTO>>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();

        }
    }
    /**
     * @description:
     * 随机抽奖接口
     * <a href="http://localhost:8091/api/v1/raffle/strategy/random_raffle">/api/v1/raffle/random_raffle</a>
     * @param: requestDTO       请求参数 {"strategyId":1000001}
     * @return: Response<RaffleResponseDTO>
     **/
    @RequestMapping(value = "perform_raffle", method = RequestMethod.POST)
    @Override
    public Response<RaffleResponseDTO> performRaffle(@RequestBody RaffleStrategyRequestDTO requestDTO) {
        try{
            log.info("随机抽奖开始 strategyId: {}", requestDTO.getStrategyId());
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                    .strategyId(requestDTO.getStrategyId())
                    .userId("system")
                    .build());
            RaffleResponseDTO responseDTO = RaffleResponseDTO.builder()
                    .awardId(raffleAwardEntity.getAwardId())
                    .awardIndex(raffleAwardEntity.getSort())
                    .build();

            log.info("随机抽奖完成 strategyId: {}, 抽奖结果: {}", requestDTO.getStrategyId(), JSON.toJSONString(responseDTO));
            return Response.<RaffleResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        }catch (AppException e) {
            log.info("随机抽奖失败 strategyId: {}, 原因: {}", requestDTO.getStrategyId(), e.getInfo());
            return Response.<RaffleResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        }catch (Exception e){
            log.info("随机抽奖失败 strategyId: {}", requestDTO.getStrategyId(), e);
            return Response.<RaffleResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
