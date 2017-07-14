package com.magic.crius.storage.db;

import com.magic.crius.po.ProxyBillSummary2game;

import java.util.List;

/**
 * User: joey
 * Date: 2017/7/8
 * Time: 14:37
 */
public interface ProxyBillSummary2gameDbService {


    boolean save(ProxyBillSummary2game game);

    boolean batchInsert(List<ProxyBillSummary2game> gameList);
}
