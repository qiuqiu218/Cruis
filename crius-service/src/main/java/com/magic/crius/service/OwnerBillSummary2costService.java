package com.magic.crius.service;

import com.magic.crius.po.OwnerBillSummary2cost;

import java.util.List;

/**
 * User: joey
 * Date: 2017/7/8
 * Time: 14:24
 * 业主成本部分退佣汇总
 */
public interface OwnerBillSummary2costService {

    boolean save(OwnerBillSummary2cost cost);

    boolean batchInsert(List<OwnerBillSummary2cost> costs);
}
