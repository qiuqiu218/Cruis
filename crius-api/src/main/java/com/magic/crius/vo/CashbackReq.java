package com.magic.crius.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * User: joey
 * Date: 2017/5/29
 * Time: 17:04
 * 返水（成功）
 * mongo中已经使用分表，每天一张表
 */
public class CashbackReq {

    @JSONField(name = "BillId")
    private Long billId;
    @JSONField(name = "ReqId")
    private Long reqId;
    @JSONField(name = "UserId")
    private Long userId;
    @JSONField(name = "AgentId")
    private Long agentId;
    @JSONField(name = "OwnerId")
    private Long ownerId;
    @JSONField(name = "Amount")
    private Long amount;
    @JSONField(name = "Currency")
    private String currency;
    @JSONField(name = "Rate")
    private Integer rate;
    @JSONField(name = "BettAmount")
    private Long bettAmount;
    @JSONField(name = "VaildBettAmount")
    private Long vaildBettAmount;
    @JSONField(name = "GameHallId")
    private Long gameHallId;
    @JSONField(name = "GameHallName")
    private String gameHallName;
    @JSONField(name = "GamePlatformId")
    private Long gamePlatformId;
    @JSONField(name = "GamePlatformName")
    private String gamePlatformName;
    @JSONField(name = "ProduceTime")
    private Long produceTime;

    @JSONField(name = "Balance")
    private Long balance;   //余额

    @JSONField(name = "GamePlatformHalltypeId")
    private Long gamePlatformHalltypeId;    //游戏类型id
    @JSONField(name = "GamePlatformHalltypeName")
    private String gamePlatformHalltypeName;

    /**
     * 消费时间
     */
    private Long consumerTime;

    /**
     * 统计日期，也用于mongo表名称
     */
    private Integer pdate;

    public Long getReqId() {
        return reqId;
    }

    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Long getBettAmount() {
        return bettAmount;
    }

    public void setBettAmount(Long bettAmount) {
        this.bettAmount = bettAmount;
    }

    public Long getVaildBettAmount() {
        return vaildBettAmount;
    }

    public void setVaildBettAmount(Long vaildBettAmount) {
        this.vaildBettAmount = vaildBettAmount;
    }

    public Long getGameHallId() {
        return gameHallId;
    }

    public void setGameHallId(Long gameHallId) {
        this.gameHallId = gameHallId;
    }

    public String getGameHallName() {
        return gameHallName;
    }

    public void setGameHallName(String gameHallName) {
        this.gameHallName = gameHallName;
    }

    public Long getGamePlatformId() {
        return gamePlatformId;
    }

    public void setGamePlatformId(Long gamePlatformId) {
        this.gamePlatformId = gamePlatformId;
    }

    public String getGamePlatformName() {
        return gamePlatformName;
    }

    public void setGamePlatformName(String gamePlatformName) {
        this.gamePlatformName = gamePlatformName;
    }

    public Long getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(Long produceTime) {
        this.produceTime = produceTime;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getGamePlatformHalltypeId() {
        return gamePlatformHalltypeId;
    }

    public void setGamePlatformHalltypeId(Long gamePlatformHalltypeId) {
        this.gamePlatformHalltypeId = gamePlatformHalltypeId;
    }

    public String getGamePlatformHalltypeName() {
        return gamePlatformHalltypeName;
    }

    public void setGamePlatformHalltypeName(String gamePlatformHalltypeName) {
        this.gamePlatformHalltypeName = gamePlatformHalltypeName;
    }

    public Long getConsumerTime() {
        return consumerTime;
    }

    public void setConsumerTime(Long consumerTime) {
        this.consumerTime = consumerTime;
    }

    public Integer getPdate() {
        return pdate;
    }

    public void setPdate(Integer pdate) {
        this.pdate = pdate;
    }
}
