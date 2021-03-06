package com.magic.crius.storage.mongo.impl;

import com.magic.crius.dao.mongo.JpReqMongoDao;
import com.magic.crius.enums.MongoCollectionFlag;
import com.magic.crius.enums.MongoCollections;
import com.magic.crius.storage.mongo.JpReqMongoService;
import com.magic.crius.vo.JpReq;
import com.magic.crius.vo.ReqQueryVo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * User: joey
 * Date: 2017/6/5
 * Time: 20:40
 */
@Service
public class JpReqMongoServiceImpl implements JpReqMongoService {

    @Resource
    private JpReqMongoDao jpReqMongoDao;

    @Override
    public boolean save(JpReq req) {
        try {
            return jpReqMongoDao.save(req) != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean saveFailedData(JpReq req) {
        try {
            return jpReqMongoDao.save(req, MongoCollectionFlag.MONGO_FAILED.collName(MongoCollections.jpReq)) != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public JpReq getByReqId(Long id) {
        try {
            Query query = new Query();
            query.addCriteria(new Criteria("reqId").is(id));
            return jpReqMongoDao.findOne(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public boolean saveSuc(Collection<JpReq> reqs) {
        try {
            return jpReqMongoDao.save(reqs, MongoCollectionFlag.SAVE_SUC.collName(MongoCollections.jpReq));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Long> getSucIds(ReqQueryVo queryVo) {
        try {
            return jpReqMongoDao.getSucIds(queryVo, MongoCollectionFlag.SAVE_SUC.collName(MongoCollections.jpReq));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<JpReq> getNotProc(ReqQueryVo queryVo, Pageable pageable) {
        try {
            return jpReqMongoDao.getNotProc(queryVo, MongoCollections.jpReq, pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<JpReq> getSaveFailed(Long startTime, Long endTime) {
        try {
            return jpReqMongoDao.getSaveFailed(startTime, endTime, MongoCollections.jpReq);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
