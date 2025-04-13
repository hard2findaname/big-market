package org.example.trigger.http;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.activity.model.entity.ActivityAccountEntity;
import org.example.domain.activity.model.entity.UserRaffleOrderEntity;
import org.example.domain.activity.service.Assembly.IActivityAssembly;
import org.example.domain.activity.service.IRaffleActivityAccountQuotaService;
import org.example.domain.activity.service.IRaffleActivityPartakeService;
import org.example.domain.award.service.IAwardService;
import org.example.domain.award.model.entity.UserAwardRecordEntity;
import org.example.domain.award.model.valobj.AwardStateVO;
import org.example.domain.rebate.model.entity.BehaviorEntity;
import org.example.domain.rebate.model.entity.BehaviorRebateOrderEntity;
import org.example.domain.rebate.model.valobj.BehaviorTypeVO;
import org.example.domain.rebate.service.IBehaviorRebateService;
import org.example.domain.strategy.model.entity.RaffleAwardEntity;
import org.example.domain.strategy.model.entity.RaffleFactorEntity;
import org.example.domain.strategy.service.IRaffleStrategy;
import org.example.domain.strategy.service.assembly.IStrategyAssembly;
import org.example.trigger.api.IRaffleActivityService;
import org.example.trigger.api.dto.ActivityDrawRequestDTO;
import org.example.trigger.api.dto.ActivityDrawResponseDTO;
import org.example.trigger.api.dto.UserActivityAccountRequestDTO;
import org.example.trigger.api.dto.UserActivityAccountResponseDTO;
import org.example.types.enums.ResponseCode;
import org.example.types.exception.AppException;
import org.example.types.model.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author atticus
 * @Date 2024/11/08 13:55
 * @description:
 */
@Slf4j
@RestController()
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/raffle/activity")
public class RaffleActivityController implements IRaffleActivityService {



    private final static SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyyMMdd");
    @Resource
    private IRaffleActivityPartakeService raffleActivityPartakeService;
    @Resource
    private IRaffleActivityAccountQuotaService raffleActivityAccountQuotaService;
    @Resource
    private IRaffleStrategy raffleStrategy;

    @Resource
    private IAwardService awardService;
    @Resource
    private IActivityAssembly activityAssembly;
    @Resource
    private IStrategyAssembly strategyAssembly;

    @Resource
    private IBehaviorRebateService behaviorRebateService;

    /**
     * 活动装配 - 数据预热 | 把活动配置的对应的 sku 一起装配
     *
     * @param activityId 活动ID
     * @return 装配结果
     * <p>
     * 接口：<a href="http://localhost:8091/api/v1/raffle/activity/armory">/api/v1/raffle/activity/assembly</a>
     * 入参：{"activityId":100001,"userId":"atticus"}
     *
     * curl --request GET \
     * --url 'http://localhost:8091/api/v1/raffle/activity/assembly?activityId=100301'
     */

    @RequestMapping(value = "assembly", method = RequestMethod.GET)
    @Override
    public Response<Boolean> activityAssembly(@RequestParam Long activityId) {
        try {
            log.info("活动装配，预热开始");
            // 活动装配
            activityAssembly.assembleActivitySKUByActivityId(activityId);

            //策略装配
            strategyAssembly.assembleLotteryStrategyByActivityId(activityId);
            Response<Boolean> response = Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .build();
            log.info("活动装配，预热完成");
            return response;
        }catch (Exception e){
            log.error("活动装配，数据预热，失败，activityId:{}",activityId,e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }


    /**
     * 抽奖接口
     *
     * @param request 请求对象
     * @return 抽奖结果
     * <p>
     * 接口：<a href="http://localhost:8091/api/v1/raffle/activity/draw">/api/v1/raffle/activity/draw</a>
     * 入参：{"activityId":100001,"userId":"xiaofuge"}
     *
     * curl --request POST \
     *   --url http://localhost:8091/api/v1/raffle/activity/draw \
     *   --header 'content-type: application/json' \
     *   --data '{
     *     "userId":"atticus",
     *     "activityId": 100301
     * }'
     */
    @RequestMapping(value = "draw", method = RequestMethod.POST)
    @Override
    public Response<ActivityDrawResponseDTO> draw(@RequestBody ActivityDrawRequestDTO request) {
        try {

            log.info("活动抽奖 userId:{} activityId:{}", request.getUserId(), request.getActivityId());

            // 1. 参数校验
            if(StringUtils.isBlank(request.getUserId()) || null == request.getActivityId()){
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            // 2. 参与活动 - 创建参与记录
            UserRaffleOrderEntity orderEntity = raffleActivityPartakeService.createOrder(request.getUserId(), request.getActivityId());
            log.info("活动抽奖，创建订单 userId:{} activityId:{} orderId:{}", request.getUserId(), request.getActivityId(), orderEntity.getOrderId());

            // 3. 抽奖策略 - 执行抽奖
            RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                    .userId(orderEntity.getUserId())
                    .strategyId(orderEntity.getStrategyId())
                    .endDateTime(orderEntity.getEndDateTime())
                    .build());
            // 存放结果 - 写入中奖记录
            UserAwardRecordEntity userAwardRecordEntity = UserAwardRecordEntity.builder()
                    .userId(orderEntity.getUserId())
                    .activityId(orderEntity.getActivityId())
                    .strategyId(orderEntity.getStrategyId())
                    .orderId(orderEntity.getOrderId())
                    .awardId(raffleAwardEntity.getAwardId())
                    .awardTitle(raffleAwardEntity.getAwardTitle())
                    .awardTime(new Date())
                    .awardState(AwardStateVO.create)
                    .awardConfig(raffleAwardEntity.getAwardConfig())
                    .build();
            awardService.saveUserAwardRecord(userAwardRecordEntity);

            // 中奖结果返回
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(ActivityDrawResponseDTO.builder()
                            .awardId(raffleAwardEntity.getAwardId())
                            .awardTitle(raffleAwardEntity.getAwardTitle())
                            .awardIndex(raffleAwardEntity.getSort())
                            .build())
                    .build();
        }catch (AppException e){
            log.error("活动抽奖失败", e);
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        }catch (Exception e){
            log.error("活动抽奖失败", e);
            return Response.<ActivityDrawResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }


    /**
     *
     * @param: userId
     * @return: Response<Boolean>
     *<p>
     * 接口：<a href="http://localhost:8091/api/v1/raffle/activity/daily_sign_rebate">/api/v1/raffle/activity/daily_sign_rebate</a>
     * 入参：A 或 atticus
     * <p>
     * curl -X POST http://localhost:8091/api/v1/raffle/activity/daily_sign_rebate
     * -d "userId=xiaofuge"
     * -H "Content-Type: application/x-www-form-urlencoded"
     */


    @RequestMapping(value = "daily_sign_rebate", method = RequestMethod.POST)
    @Override
    public Response<Boolean> dailySignRebate(@RequestParam String userId) {
        try{
            log.info("每日签到返利开始 userId: {}",userId);
            BehaviorEntity behaviorEntity = new BehaviorEntity();
            behaviorEntity.setUserId(userId);
            behaviorEntity.setBehaviorTypeVO(BehaviorTypeVO.SIGN);
            behaviorEntity.setOutBusinessNo(dateFormatDay.format(new Date()));

            List<String> orderIdList = behaviorRebateService.createOrder(behaviorEntity);
            log.info("每日签到返利完成 userId: {}",userId, JSON.toJSONString(orderIdList));
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(true)
                    .build();

        }catch (AppException e){
            log.error("每日签到返利异常 userId: {}",userId, e);
            return Response.<Boolean>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        }catch (Exception e){
            log.error("每日签到返利失败 userId: {}",userId, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }

    }

    @Override
    @RequestMapping(value = "is_daily_sign_rebated", method = RequestMethod.POST)
    public Response<Boolean> isDailySignRebated(@RequestParam String userId) {
        try{
            log.info("查询用户是否完成签到开始 userId: {}", userId);
            String outBusinessNo = dateFormatDay.format(new Date());
            List<BehaviorRebateOrderEntity> behaviorRebateOrderEntityList = behaviorRebateService.queryOrderByBusinessNo(userId, outBusinessNo);
            log.info("查询用户是否完成签到完成 userId: {} order.size: {}", userId, behaviorRebateOrderEntityList.size());
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(!behaviorRebateOrderEntityList.isEmpty())
                    .build();
        }catch (Exception e){
            log.error("查询用户是否完成签到失败 userId: {}", userId , e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .data(false)
                    .build();
        }

    }

    @Override
    @RequestMapping(value = "query_user_activity_account", method = RequestMethod.POST)
    public Response<UserActivityAccountResponseDTO> queryUserActivityAccount(@RequestBody UserActivityAccountRequestDTO request) {
        try {
            log.info("查询用户活动账户开始 userId:{} activityId:{}", request.getUserId(), request.getActivityId());
            // 1. 参数校验
            if(StringUtils.isBlank(request.getUserId()) || null == request.getActivityId()){
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
            }
            ActivityAccountEntity activityAccountEntity = raffleActivityAccountQuotaService.queryActivityAccountEntity(request.getActivityId(), request.getUserId());
            UserActivityAccountResponseDTO userActivityAccountResponseDTO = new UserActivityAccountResponseDTO();
            userActivityAccountResponseDTO.setTotalCount(activityAccountEntity.getTotalCount());
            userActivityAccountResponseDTO.setTotalCountSurplus(activityAccountEntity.getTotalCountSurplus());
            userActivityAccountResponseDTO.setDayCount(activityAccountEntity.getDayCount());
            userActivityAccountResponseDTO.setDayCountSurplus(activityAccountEntity.getDayCountSurplus());
            userActivityAccountResponseDTO.setMonthCount(activityAccountEntity.getMonthCount());
            userActivityAccountResponseDTO.setMonthCountSurplus(activityAccountEntity.getMonthCountSurplus());
            log.info("查询用户活动账户完成 userId:{} activityId:{}", request.getUserId(), request.getActivityId());
            return Response.<UserActivityAccountResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(userActivityAccountResponseDTO)
                    .build();
        }catch (Exception e){
            log.error("查询用户活动账户完成 userId: {}", request.getUserId() , e);
            return Response.<UserActivityAccountResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }

    }
}
