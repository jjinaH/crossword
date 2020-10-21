package com.o2o.action.server.app;

import java.io.Serializable;
import java.util.HashMap;

public class TTS implements Serializable {
    public HashMap<String, String> getTtsmap() {
        return ttsmap;
    }

    private HashMap<String,String> ttsmap;
    public TTS()
    {
        ttsmap = new HashMap<>();
        String gamename = "Word Cube";
        //원래는 DB로 TTS 정보 가져올것임
        ttsmap.put("welcome","Hello, welcome to "+ gamename +". To play the game, say 'Start' or press the button. ");
        ttsmap.put("main",""+ gamename +" is a game of finding English words in a randomly arranged alphabets within a set time. Say 'Continue game' if you want to play the game, 'Choose Level' if you want to select a level, 'Settings' if you want to see my settings. Which would you like to try?");
        ttsmap.put("setting","To change the game settings, say 'Sound effect on', 'background sound off', or 'reset'.");
        ttsmap.put("stageselect","You can play "+ gamename +" at any level you want. Say your desired level, as an example 'level1' or click the button.");
        ttsmap.put("difficultyselect","In "+ gamename +" you can select the difficulty level. As the game level gets higher, the game gets more difficult, the bigger entry fee and the less time limit. However, if you win, you can earn additional coins and experience point. Select the difficulty level from Easy, medium or hard.");
        ttsmap.put("ingame","Find the word before the time limit ends! If you need help, you can say 'open hint'. Let's play! ");
        ttsmap.put("success","Clear! Say 'continue' if you want to play more games.");
//                " or 'end' if you want to quit the game or click the button.");
        ttsmap.put("fail","Fail.. Say 'again' if you want to play more games");
//                " or 'end' if you want to quit the game or click the button.");
        ttsmap.put("ranking","These are the top rankers of "+ gamename +". Would you like to give it a try?");
        ttsmap.put("store","You can buy coins and hints. To buy hints and coins, say 'purchase hints' or 'purchase coins'. You can get free coins by saying 'get coins' and watch the ad. Say 'go back' if you want to go to the game");
    }

}