package org.example.trigger.api;

import org.example.trigger.api.dto.RaffleAwardListRequestDTO;
import org.example.trigger.api.dto.RaffleAwardListResponseDTO;
import org.example.trigger.api.dto.RaffleRequestDTO;
import org.example.trigger.api.dto.RaffleResponseDTO;
import org.example.types.model.Response;

import java.util.List;

/**
 * @Author atticus
 * @Date 2024/10/09 19:52
 * @description:
 */
public interface IRaffleService {
    /**
     * @description: 奖品策略装配接口
     * @param: strategyId           策略ID
     * @return: Response<Boolean>
     **/
    Response<Boolean> strategyAssembly(Long strategyId);
    /**
     * @description: 查询抽奖奖品列表配置
     * @param: requestDTO   抽奖查询请求参数
     * @return: Response<List<RaffleAwardListResponseDTO>> 奖品列表数据
     **/
    Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO);
    /**
     * @description: 随机抽奖接口
     * @param: requestDTO
     * @return: Response<RaffleResponseDTO>
     **/
    Response<RaffleResponseDTO> performRaffle(RaffleRequestDTO requestDTO);
}
