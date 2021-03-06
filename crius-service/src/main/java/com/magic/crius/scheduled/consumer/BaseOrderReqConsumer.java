package com.magic.crius.scheduled.consumer;

import com.magic.api.commons.tools.DateUtil;
import com.magic.crius.assemble.FailedRedisQueue;
import com.magic.crius.assemble.UserOrderDetailAssemService;
import com.magic.crius.constants.CriusConstants;
import com.magic.crius.constants.RedisConstants;
import com.magic.crius.constants.CriusInitConstants;
import com.magic.crius.enums.MongoCollections;
import com.magic.crius.po.RepairLock;
import com.magic.crius.po.UserOrderDetail;
import com.magic.crius.service.BaseOrderReqService;
import com.magic.crius.service.BaseReqService;
import com.magic.crius.service.RepairLockService;
import com.magic.crius.storage.db.SpringDataPageable;
import com.magic.crius.util.PropertiesLoad;
import com.magic.crius.vo.BaseOrderReq;
import com.magic.crius.vo.ReqQueryVo;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;

import static com.magic.crius.constants.CriusInitConstants.POLL_TIME;

/**
 * User: joey
 * Date: 2017/6/10
 * Time: 16:46
 */
@Component
public class BaseOrderReqConsumer {

    private static final Logger logger = Logger.getLogger(BaseOrderReqConsumer.class);
    private ExecutorService baseOrderTaskPool = new ThreadPoolExecutor(10, 20, 3, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.DiscardPolicy());
    private ExecutorService baseOrderHistoryTaskPool = Executors.newSingleThreadExecutor();

    @Resource
    private RepairLockService repairLockService;


    @Resource
    private BaseOrderReqService baseOrderReqService;
    @Resource
    private UserOrderDetailAssemService userOrderDetailAssemService;

    @Resource
    private BaseReqService baseReqService;


    public void init(Date date) {
        detailCalculate(date);
    }


    private void detailCalculate(Date date) {
        for (int i = 0; i < CriusInitConstants.THREAD_SIZE; i++) {
            baseOrderTaskPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        currentDataCalculate(date);
                    } catch (Exception e) {
                        logger.error("---detailCalculate baseOrder--", e);
                    }
                }
            });
        }

        baseOrderHistoryTaskPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    repairCacheHistoryTask(date);
                    if (PropertiesLoad.repairOrderScheduleFlag()) {
                        logger.info("----repairMongoAbnormal BaseOrderReq");
                        repairMongoAbnormal(date);
                    }
                } catch (Exception e) {
                    logger.error("---detailCalculate-task-- baseOrder", e);
                }
            }
        });
    }

    /**
     * redis中数据处理
     *
     * @param date
     */
    private void currentDataCalculate(Date date) {
        int countNum = 0;

        List<BaseOrderReq> reqList = baseOrderReqService.batchPopRedis(date);
        int queuePopCount = 0;
        while (FailedRedisQueue.baseOrderQueue.size() > 0) {
            if (++queuePopCount > RedisConstants.BATCH_POP_NUM) {
                logger.info("currentDataCalculate baseOrder queuePopCount > 100, process insert, list.size is : " + reqList.size());
                flushData(reqList);
            }
            reqList.add(FailedRedisQueue.baseOrderQueue.poll());
        }
        while (reqList != null && reqList.size() > 0 && countNum++ < POLL_TIME) {
            logger.info("currentDataCalculate baseOrder pop datas, size : " + reqList.size());
            flushData(reqList);
            reqList = baseOrderReqService.batchPopRedis(date);
            try {
                Thread.sleep(CriusConstants.POLL_POP_SLEEP_TIME);
            } catch (InterruptedException e) {
                logger.error("--currentDataCalculate baseOrder--", e);
            }
        }
    }

    /**
     * 清洗数据入库
     *
     * @param list
     */
    private void flushData(Collection<BaseOrderReq> list) {
        if (list != null && list.size() > 0) {
            List<UserOrderDetail> details = new ArrayList<>();
            List<BaseOrderReq> sucReqs = new ArrayList<>();
            for (BaseOrderReq req : list) {
                details.add(userOrderDetailAssemService.assembleUserOrderDetail(req));
                    /*成功的数据*/
                sucReqs.add(assembleSucReq(req));
            }
            userOrderDetailAssemService.batchSave(details);
            //todo 成功的id处理
            if (!baseOrderReqService.saveSuc(sucReqs)) {

            }
        }
    }

    /**
     * 处理缓存中上一个小时缓存中未处理的数据
     *
     * @param date
     */
    private void repairCacheHistoryTask(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, -1);
        List<BaseOrderReq> reqList = baseOrderReqService.batchPopRedis(calendar.getTime());
        while (reqList != null && reqList.size() > 0) {
            logger.info("------repairCacheHistoryTask ,baseOrder , list : " + reqList.size());
            flushData(reqList);
            reqList = baseOrderReqService.batchPopRedis(calendar.getTime());
        }
    }

    /**
     * 纠正mongo中上一个小时内添加失败的数据和未处理的数据
     */
    private void repairMongoAbnormal(Date date) {
        String hhDate = DateUtil.formatDateTime(date, DateUtil.format_yyyyMMddHH);
        Date endDate = DateUtil.parseDate(hhDate, DateUtil.format_yyyyMMddHH);
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(endDate);
        startDate.add(Calendar.HOUR, -1);
        RepairLock repairLock = repairLockService.getTimeLock(MongoCollections.baseOrderReq, Integer.parseInt(DateUtil.formatDateTime(startDate.getTime(), DateUtil.format_yyyyMMddHH)));
        if (repairLock != null) {
            return;
        }
        repairLock = new RepairLock();
        repairLock.setProduceTime(date.getTime());
        repairLock.setCollectionName(MongoCollections.baseOrderReq);
        repairLock.setValue(CriusConstants.REPAIR_LOCK_VALUE);
        if (repairLockService.save(repairLock)) {
//            mongoFailed(startDate.getTimeInMillis(), endDate.getTime());
            mongoNoProc(startDate.getTimeInMillis(), endDate.getTime(), hhDate, Integer.parseInt(DateUtil.formatDateTime(startDate.getTime(), DateUtil.format_yyyyMMdd)));
        }

    }

    /**
     * mongo插入失败数据处理
     *
     * @param startTime
     * @param endTime
     */
    private void mongoFailed(Long startTime, Long endTime) {
        List<BaseOrderReq> failedReqs = baseOrderReqService.getSaveFailed(startTime, endTime);
        if (failedReqs != null && failedReqs.size() > 0) {
            logger.info("------mongoFailed ,baseOrder , reqIds :" + failedReqs.size() + " , startTime : " + startTime + " endTime :" + endTime);
            flushData(failedReqs);
        }
    }

    /**
     * mongo未处理数据处理
     *
     * @param startTime
     * @param endTime
     */
    private void mongoNoProc(Long startTime, Long endTime, String hhDate, Integer pdate) {
        ReqQueryVo queryVo = new ReqQueryVo(startTime,endTime, pdate);
        List<Long> reqIds = baseOrderReqService.getSucIds(queryVo);
        queryVo.setReqIds(reqIds);
        SpringDataPageable pageable = new SpringDataPageable();
        pageable.setSort(new Sort("reqId"));
        pageable.setPagesize(CriusConstants.MONGO_NO_PROC_SIZE);
        pageable.setPagenumber(baseReqService.getNoProcPage(RedisConstants.getNoProcPage(RedisConstants.CLEAR_PREFIX.PLUTUS_BASE_GAME, hhDate)));


        List<BaseOrderReq> withDrawReqs = baseOrderReqService.getNotProc(queryVo, pageable);
        while (withDrawReqs != null && withDrawReqs.size() > 0) {
            logger.info("------start mongoNoProc ,baseOrder , noProcReqIds.size : " + withDrawReqs.size() + ", startTime : " + startTime + " endTime :" + endTime);
            flushData(withDrawReqs);
            pageable.setPagenumber(baseReqService.getNoProcPage(RedisConstants.getNoProcPage(RedisConstants.CLEAR_PREFIX.PLUTUS_BASE_GAME, hhDate)));
            withDrawReqs = baseOrderReqService.getNotProc(queryVo, pageable);
        }
    }


    private BaseOrderReq assembleSucReq(BaseOrderReq req) {
        /*成功的数据*/
        BaseOrderReq sucReq = new BaseOrderReq();
        sucReq.setReqId(req.getReqId());
        sucReq.setProduceTime(req.getProduceTime());
        sucReq.setConsumerTime(req.getConsumerTime());
        sucReq.setPdate(req.getPdate());
        sucReq.setInsertDatetime(req.getInsertDatetime());
        sucReq.setBcBetId(req.getBcBetId());
        return sucReq;
    }
}



