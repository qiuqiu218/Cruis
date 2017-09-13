package com.magic.crius.service;

import com.alibaba.fastjson.JSON;
import com.magic.crius.constants.CriusConstants;
import com.magic.crius.constants.RedisConstants;
import com.magic.crius.storage.db.SpringDataPageable;
import com.magic.crius.storage.mongo.CashbackReqMongoService;
import com.magic.crius.vo.ReqQueryVo;
import org.apache.commons.httpclient.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * User: joey
 * Date: 2017/9/12
 * Time: 15:07
 * //
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:spring-application.xml"})
public class CashMongoTest {

    //    @Resource
    private CashbackReqMongoService cashbackReqMongoService;

    @Test
    public void getSucIds() {
        ReqQueryVo queryVo = new ReqQueryVo(1505199600000L, 1505203200000L, 20170912);
        System.out.println(JSON.toJSONString(cashbackReqMongoService.getSucIds(queryVo)));

    }

    @Test
    public void dateTest() {
        System.out.println(com.magic.api.commons.tools.DateUtil.formatDateTime(new Date(1505182194223L), "yyyy-MM-dd HH"));
        System.out.println(com.magic.api.commons.tools.DateUtil.formatDateTime(new Date(1505192400000L), "yyyy-MM-dd HH"));
    }

    @Test
    public void mongoNoProc() {
        SpringDataPageable pageable = new SpringDataPageable();
        pageable.setSort(new Sort("reqId"));
        pageable.setPagesize(CriusConstants.MONGO_NO_PROC_SIZE);
        pageable.setPagenumber(1);
        System.out.println(JSON.toJSONString(pageable));


    }

    @Test
    public void mongoPageTest() {
        SpringDataPageable pageable = new SpringDataPageable();
        pageable.setSort(new Sort("reqId"));
        pageable.setPagesize(CriusConstants.MONGO_NO_PROC_SIZE);
        pageable.setPagenumber(0);


        Query query = new Query();
//        if (queryVo.getReqIds() != null && queryVo.getReqIds().size() > 0) {
//            query.addCriteria(new Criteria("reqId").nin(queryVo.getReqIds()));
//        }
//        query.addCriteria(new Criteria("produceTime").gte(queryVo.getStartTime()).lt(queryVo.getEndTime()));
        query.skip(pageable.getOffset()).limit(pageable.getPageSize());
        query.with(pageable.getSort());

        System.out.println(JSON.toJSONString(query));
    }
}
