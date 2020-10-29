package com.o2o.action.server.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:/static/properties/game.properties")
public class GameContext { //AppConfig {
    @Autowired
    private Environment environment;

    public int getSize(int level) {
        return Integer.parseInt(environment.getProperty("level" + level + ".size"));
    }

    public int getAnswerCnt(int level) {
        return Integer.parseInt(environment.getProperty("level" + level + ".answer"));
    }

    public int getTimeLimit(int level, String difficulty) {
        int timeLimit = Integer.parseInt(environment.getProperty("level" + level + ".duration"));

        if("easy".equals(difficulty)) return timeLimit;
        else if("medium".equals(difficulty)) return timeLimit - 5;
        else if("hard".equals(difficulty)) return timeLimit - 10;

        return timeLimit; //TODO default?
    }
    public int getBettingCoins(String difficulty) {
//        System.out.println("environment.getProperty(\"betcoin.\" + difficulty) >>> " + environment.getProperty("betcoin." + difficulty));
        return Integer.parseInt(environment.getProperty("betcoin." + difficulty));
    }
    public int getWinExp(int level, String difficulty){
        int winExp = Integer.parseInt(environment.getProperty("level" + level + ".exp"));
        if("easy".equals(difficulty)) return winExp;
        else if("medium".equals(difficulty)) return winExp * 2;
        else if("hard".equals(difficulty)) return winExp * 3;

        return winExp; //TODO default?
    }

    public int getLevelUpExp(int currLevel) {
        return Integer.parseInt(environment.getProperty("level" + currLevel + ".levelupexp"));
    }

    public int getLevelUpCoin(int currLevel) {
        return Integer.parseInt(environment.getProperty("level" + currLevel + ".coin"));
    }

    public int getDifficulty (int level) {
        return Integer.parseInt(environment.getProperty("level" + level + ".difficulty"));
    }

    //Test 용
    public int getTimeLimitTest() {
        return Integer.parseInt(environment.getProperty("level1.duration"));
    }

    public void test() {
        System.out.println("gameContext bean 등록 완료!");
    }


}
