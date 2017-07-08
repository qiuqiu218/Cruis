package com.magic.crius.storage.db.impl;

import com.magic.crius.dao.crius.db.OwnerBillSummary2gameMapper;
import com.magic.crius.po.OwnerBillSummary2game;
import com.magic.crius.storage.db.OwnerBillSummary2gameDbService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * User: joey
 * Date: 2017/7/8
 * Time: 14:27
 */
@Service
public class OwnerBillSummary2gameDbServiceImpl implements OwnerBillSummary2gameDbService {

    @Resource
    private OwnerBillSummary2gameMapper ownerBillSummary2gameMapper;

    @Override
    public boolean save(OwnerBillSummary2game summary2game) {
        return ownerBillSummary2gameMapper.insert(summary2game) > 0;
    }
}
