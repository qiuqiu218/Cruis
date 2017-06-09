package com.magic.crius.storage.db.impl;

import com.magic.crius.dao.db.UserOrderDetailMapper;
import com.magic.crius.po.UserOrderDetail;
import com.magic.crius.storage.db.UserOrderDetailDbService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * User: joey
 * Date: 2017/6/8
 * Time: 16:29
 */
@Service
public class UserOrderDetailDbServiceImpl implements UserOrderDetailDbService {

    @Resource
    private UserOrderDetailMapper userOrderDetailMapper;

    @Override
    public boolean batchSave(List<UserOrderDetail> details) {
        return userOrderDetailMapper.batchInsert(details) > 0;
    }
}
