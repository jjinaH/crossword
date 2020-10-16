package com.o2o.action.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Table(name = "user_info")
public class User implements Serializable {

    @Id
    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_level")
    private short userLevel;

    @Column(name = "user_exp")
    private int userExp;

    @Column(name = "user_hint")
    private int userHint;

    @Column(name = "user_coin")
    private int userCoin;

    @Column(name = "account_timestamp", columnDefinition = "TIMESTAMP WITH TIME ZONE" )
    private OffsetDateTime accountTimestamp;

    @Column(name = "visit_timestamp", columnDefinition = "TIMESTAMP WITH TIME ZONE" )
    private OffsetDateTime visitTimestamp;

    /**
     * 20.10.15 추가된 칼럼
     */
    @Column(name = "bgm_on")
    private boolean bgmOn;
    @Column(name = "foley_on")
    private boolean foleyOn;
    @Column(name = "play_cnt")
    private int playCnt;

    public User(){ }
    public User(String userEmail){
        this.userEmail = userEmail;
        userLevel = 1;
        userExp = 0;
        userHint = 3;
        userCoin = 5000;
        accountTimestamp = OffsetDateTime.now();
        visitTimestamp = OffsetDateTime.now();
        bgmOn=true;
        foleyOn=true;
    }

    public interface getUserInfo{
        String getUserEmail();
        short getUserLevel();
        int getUserExp();
        int getUserHint();
        int getUserCoin();
        void setVisitTimestamp();
        boolean getBgmOn();
        boolean getFoleyOn();
    }

    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public short getUserLevel() {
        return userLevel;
    }
    public void setUserLevel(short userLevel) {
        this.userLevel = userLevel;
    }
    public int getUserExp() {
        return userExp;
    }
    public void setUserExp(int userExp) {
        this.userExp = userExp;
    }
    public void setUserCoin(int userCoin) {
        this.userCoin = userCoin;
    }
    public int getUserCoin() {
        return userCoin;
    }
    public int getUserHint() {
        return userHint;
    }
    public void setUserHint(int userHint) {
        this.userHint = userHint;
    }
    public OffsetDateTime getAccountTimestamp() {
        return accountTimestamp;
    }
    public void setAccountTimestamp(OffsetDateTime accountTimestamp) {
        this.accountTimestamp = accountTimestamp;
    }
    public OffsetDateTime getVisitTimestamp() {
        return visitTimestamp;
    }
    public void setVisitTimestamp() {
        this.visitTimestamp = OffsetDateTime.now();
    }

    /**
     * 20.10.15 추가 칼럼 getter, setter
     */
    public boolean getBgmOn() {
        return bgmOn;
    }
    public boolean getFoleyOn() {
        return foleyOn;
    }
    public int getPlayCnt() {
        return playCnt;
    }

    public void setBgmOn(boolean bgmOn) {
        this.bgmOn = bgmOn;
    }
    public void setFoleyOn(boolean foleyOn) {
        this.foleyOn = foleyOn;
    }
    public void setPlayCnt(int playCnt) {
        this.playCnt = playCnt;
    }
}

