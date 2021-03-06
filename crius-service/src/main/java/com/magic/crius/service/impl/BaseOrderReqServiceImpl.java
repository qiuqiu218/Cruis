package com.magic.crius.service.impl;

import com.magic.api.commons.ApiLogger;
import com.magic.crius.assemble.FailedRedisQueue;
import com.magic.crius.service.BaseOrderReqService;
import com.magic.crius.storage.mongo.BaseOrderReqMongoService;
import com.magic.crius.storage.redis.BaseOrderReqRedisService;
import com.magic.crius.vo.BaseOrderReq;
import com.magic.crius.vo.ReqQueryVo;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * User: joey
 * Date: 2017/6/8
 * Time: 19:25
 */
@Service
public class BaseOrderReqServiceImpl implements BaseOrderReqService {

    @Resource
    private BaseOrderReqRedisService baseOrderReqRedisService;
    @Resource
    private BaseOrderReqMongoService baseOrderReqMongoService;

    @Override
    public boolean save(BaseOrderReq req) {

        //此处逻辑改为如果mongo插入成功才写入redis
        if (baseOrderReqMongoService.save(req)) {
            if (!baseOrderReqRedisService.save(req)) {
                //TODO 缓存保存失败如何处理，睡眠2毫秒，然后重试，如果失败，扔进队列中
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ApiLogger.warn("save baseOrderReq false, retry one time");
                if (!baseOrderReqRedisService.save(req)) {
                    FailedRedisQueue.baseOrderQueue.add(req);
                }
            }
        } else {
            baseOrderReqMongoService.saveFailedData(req);
        }
        return true;
    }

    @Override
    public BaseOrderReq getByReqId(BaseOrderReq req) {
        return baseOrderReqMongoService.getByReqId(req);
    }

    @Override
    public List<BaseOrderReq> batchPopRedis(Date date) {
        return baseOrderReqRedisService.batchPop(date);
    }

    @Override
    public List<Long> getSucIds(ReqQueryVo queryVo) {
        return baseOrderReqMongoService.getSucIds(queryVo);
    }

    @Override
    public List<BaseOrderReq> getNotProc(ReqQueryVo queryVo, Pageable pageable) {
        return baseOrderReqMongoService.getNotProc(queryVo, pageable);
    }

    @Override
    public List<BaseOrderReq> getSaveFailed(Long startTime, Long endTime) {
        return baseOrderReqMongoService.getSaveFailed(startTime, endTime);
    }

    @Override
    public boolean saveSuc(List<BaseOrderReq> reqs) {
        return baseOrderReqMongoService.saveSuc(reqs);
    }
}
