package com.o2o.action.server.app;

import java.io.Serializable;

public class UserInfo implements Serializable { //유저정보 클래스 - 실제로는 DBConnector에서 받아올것임
    private String email; // 유저 이메일
    private int mylevel; // 유저레벨
    private int myExp; // 유저 누적 경험치
    private int myCurrentExp; // 유저 현재 경험치
    private int myCurrentFullExp; // 유저 현재 전체 경험치
    private int myHint; // 유저 힌트
    private int myCoin; // 유저 코인
    private StagePropertyInfo stageInfo; // 스테이지 프로퍼티 정보
    private StorePropertyInfo storeInfo; // 상점 프로퍼티 정보
    /**
     * 20.10.15 추가된 필드
     * 유저별 저장한 배경음악, 효과음 세팅값
     * 누적 게임횟수
     */
    private String bgmOn;
    private String foleyOn;
    private int playCnt;
    private String lastPlayLevel;

    // 유저 정보 생성 - DB에서 초기화할것임.
    public UserInfo(String _mylevel, String _myExp, String _myHint, String _myCoin, StagePropertyInfo _stageInfo, String _email
                    , String _bgmOn, String _foleyOn, String _lastPlayLevel ) {

        bgmOn = _bgmOn;
        foleyOn = _foleyOn;
        lastPlayLevel = _lastPlayLevel;

        mylevel = Integer.parseInt(_mylevel) ;
        myExp = Integer.parseInt(_myExp);
        myHint = Integer.parseInt(_myHint);
        myCoin = Integer.parseInt(_myCoin);
        stageInfo = _stageInfo;
        email =_email;
        // 상점정보는 해당 UserInfo 내에서만 사용하기 때문에 이곳에서 인스턴스 생성
        storeInfo = new StorePropertyInfo();

        // 레벨이 1일때는 누적 경험치가 내 현재 경험치임
        if(mylevel==1) {
            // 현재 풀 경험치는 현재 레벨의 레벨업 경험치에서 이전 레벨의 레벨업 경험치를 뺀거임
            myCurrentFullExp = stageInfo.Stages[mylevel].getLevelUpExp();
            myCurrentExp = myExp;

        }else { // 레벨이 1이상일때는 누적경험치 에서 이전 레벨업 경험치를 빼줘야함

            // 현재 풀 경험치는 현재 레벨의 레벨업 경험치에서 이전 레벨의 레벨업 경험치를 뺀거임
            myCurrentFullExp = stageInfo.Stages[mylevel].getLevelUpExp() - stageInfo.Stages[mylevel-1].getLevelUpExp();
            // 현재 경험치는 현재 누적 경험치 - 이전 레벨의 레벨업 경험치임.
            myCurrentExp = myExp - stageInfo.Stages[mylevel-1].getLevelUpExp();
        }

    }

    public int getLevel() {
        return mylevel;
    }

    public int getMyExp() {
        return myExp;
    }

    public int getMyHint() {
        return myHint;
    }

    public int getMyCoin() {
        return myCoin;
    }

    public String getEmail() {
        return email;
    }

    public int getMyCurrentExp() {
        return myCurrentExp;
    }

    public int getMyCurrentFullExp() {
        return myCurrentFullExp;
    }


    // 유저 레벨업 - 레벨과 코인 증가
    private void UserLevelUp() {
        // levelupcoin은 해당 유저의 레벨에 따라 근거하므로 mylevel이 스테이지의 인덱스가 됨.
        int levelupcoin = stageInfo.Stages[mylevel].getLevelUpCoin();
        mylevel++;
        myCoin += levelupcoin;
        // 현재 풀 경험치는 현재 레벨의 레벨업 경험치에서 이전 레벨의 레벨업 경험치를 뺀거임
        myCurrentFullExp = stageInfo.Stages[mylevel].getLevelUpExp() - stageInfo.Stages[mylevel-1].getLevelUpExp();
        // 현재 경험치는 현재 누적 경험치 - 이전 레벨의 레벨업 경험치임.
        myCurrentExp = myExp - stageInfo.Stages[mylevel-1].getLevelUpExp();

    }

    // 게임상에서 힌트 사용
    public void ConsumeHintCount() {
        if(myHint>0) myHint--;
    }

    // 스테이지 클리어 - 경험치, 코인 증가
    public void UserStageClearChange(int _stage, String _difficulty) {

        int winexp = stageInfo.Stages[_stage].getExp().get(_difficulty);
        int wincoin = stageInfo.Stages[_stage].getBetCoin().get(_difficulty);
        float coinratio = stageInfo.Stages[_stage].getCoinRatio();
        // levelupexp는 해당 유저의 레벨에 따라 근거하므로 mylevel이 스테이지의 인덱스가 됨.
        int levelupexp = stageInfo.Stages[mylevel].getLevelUpExp();

        myCoin+=wincoin*coinratio; // 코인 증가
        myExp+=winexp; // 누적경험치 증가

        // 레벨이 1일때는 누적 경험치가 내 현재 경험치임
        if(mylevel==1) {
            // 현재 풀 경험치는 현재 레벨의 레벨업 경험치에서 이전 레벨의 레벨업 경험치를 뺀거임
            myCurrentFullExp = levelupexp;
            myCurrentExp = myExp;

        } else { // 레벨이 1이상일때는 누적경험치 에서 이전 레벨업 경험치를 빼줘야함

            // 현재 풀 경험치는 현재 레벨의 레벨업 경험치에서 이전 레벨의 레벨업 경험치를 뺀거임
            myCurrentFullExp = levelupexp - stageInfo.Stages[mylevel-1].getLevelUpExp();
            // 현재 경험치는 현재 누적 경험치 - 이전 레벨의 레벨업 경험치임.
            myCurrentExp = myExp - stageInfo.Stages[mylevel-1].getLevelUpExp();
        }

        // 레벨업 확인
        if(myExp>= levelupexp) {
            UserLevelUp(); // 유저 레벨업
        }
    }
    // 게임 시작시 변경 - 배팅코인만큼 감소
    public void GameStartChange(int _stage, String _difficulty) {
        // 코인 감소
        myCoin -= stageInfo.Stages[_stage].getBetCoin().get(_difficulty);
    }

    // 상점에서 힌트구매 - 코인 감소, 힌트 증가
    public void HintPurchaseChange() {
        myCoin-=storeInfo.getHintpurchase_losecoin(); // 코인 감소
        myHint+=storeInfo.getHintpurchase_gethint(); // 힌트 증가
    }
    // 상점에서 광고 보고 코인 충전 - 코인 증가
    public void CoinChargeChange() {
        myCoin+=storeInfo.getHintcharge_getcoin();
    }
    // 상점에서 코인구매 - 코인 증가
    public void CoinPurchaseChange() {
        myCoin+=storeInfo.getCoinpurchase_getcoin();
    }

    /**
     * 20.10.15 추가 칼럼 getter, setter
     */
    public String getBgmOn() {
        return bgmOn;
    }
    public void setBgmOn(String bgmOn) {
        this.bgmOn = bgmOn;
    }

    public String getFoleyOn() {
        return foleyOn;
    }
    public void setFoleyOn(String foleyOn) {
        this.foleyOn = foleyOn;
    }

    public int getPlayCnt() {
        return playCnt;
    }
    public void setPlayCnt(int playCnt) {
        this.playCnt = playCnt;
    }
    public int getLastPlayLevel() { return playCnt; }
    public void setLastPlayLevel(int playCnt) { this.playCnt = playCnt; }
}