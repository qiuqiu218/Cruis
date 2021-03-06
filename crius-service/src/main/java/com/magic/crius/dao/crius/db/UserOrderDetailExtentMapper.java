package com.magic.crius.dao.crius.db;

import com.magic.crius.po.UserOrderDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 注单详情
 */
@Component
public interface UserOrderDetailExtentMapper {

    int batchInsert(@Param("list") List<UserOrderDetail> userOrderDetails);

    int insert(UserOrderDetail userOrderDetail);
}