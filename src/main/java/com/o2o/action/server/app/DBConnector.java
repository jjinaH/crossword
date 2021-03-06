package com.o2o.action.server.app;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Service
public class DBConnector implements Serializable {

    String commandGetTotalRank = "/getTotalRank";
    String commandGetMyRank = "/getMyRank/";
    String commandReset = "/resetUser/";
    String commandUpdate = "/update";
    String defaultSendUrl = "https://actions.o2o.kr/devsvr4";
    QueryController queryController;
    JsonParser jsonParser;
    JsonArray user;

    public DBConnector(String email) {

        String userCheckUrl = defaultSendUrl + "/getUser/" + email;
        queryController = new QueryController();
        String result = queryController.get(userCheckUrl); //TODO
        System.out.println("result =" + result.length() + "result");

        jsonParser = new JsonParser();

        if(result.length() == 3){
            //처음사용자 등록
            String fisrtInputUrl = defaultSendUrl + "/createUser/" + email;
            String createResult = queryController.get(fisrtInputUrl);
            System.out.println("createResult = " + createResult);
            DBConnector dbConnector = new DBConnector(email);
        }else{
            //이미 등록된 사용
            //유저 한명 data 전체 row
            user = (JsonArray) jsonParser.parse(result);
            String userData = user.toString();

            System.out.println("user = " + userData);
            System.out.println(user.get(0).getAsJsonObject().size());
        }
    }
    public String getBgmOn() {
        return user.get(0).getAsJsonObject().get("bgmOn").toString();
    }
    public String getFoleyOn() {
        return user.get(0).getAsJsonObject().get("foleyOn").toString();
    }
    public String getLastPlayLevel() { return user.get(0).getAsJsonObject().get("lastPlayLevel").toString(); }
    //유저 각각 요소 가져오는거
    public String getUserLevel() {
        return user.get(0).getAsJsonObject().get("userLevel").toString();
    }
    public String getUserExp() {
        return user.get(0).getAsJsonObject().get("userExp").toString();
    }
    public String getUserCoin() {
        return user.get(0).getAsJsonObject().get("userCoin").toString();
    }
    public String getUserHint() {
        return user.get(0).getAsJsonObject().get("userHint").toString();
    }
    public void updateUserLevel(int level, String email){

        String updateUserLevelUrl = defaultSendUrl + commandUpdate + "Level/" + level + "/" + email;
        String updateUserLevelResult = queryController.get(updateUserLevelUrl);
        System.out.println(updateUserLevelResult);
    }
    public void updateUserExp(int exp, String email){

        String updateUserExpUrl = defaultSendUrl + commandUpdate + "Exp/" + exp + "/" + email;
        String updateUserExpResult = queryController.get(updateUserExpUrl);
        System.out.println("updateresult : " + updateUserExpResult);
    }
    public void updateUserHint(int hint, String email){

        String updateUserHintUrl = defaultSendUrl + commandUpdate + "Hint/" + hint + "/" + email;
        String updateUserHintResult = queryController.get(updateUserHintUrl);
        System.out.println("updatehint : " + updateUserHintResult);
    }
    public void updateUserCoin(int coin, String email){

        String updateUserCoinUrl = defaultSendUrl + commandUpdate + "Coin/" + coin + "/" + email;
        String updateUserCoinResult = queryController.get(updateUserCoinUrl);
        System.out.println("coin" + coin + "email" + email + "updatecoin : " +updateUserCoinResult);
    }
    public void resetUserData(String email){
        String resetUserDataUrl = defaultSendUrl + commandReset + email;
        String resetUserDataResult = queryController.get(resetUserDataUrl);
        System.out.println(resetUserDataResult);
    }


    public String[][] getTotalRank(){
        String getTotalRankUrl = defaultSendUrl + commandGetTotalRank;
        String getTotalRankResult = queryController.get(getTotalRankUrl);
        JsonArray totalRankArray = (JsonArray) jsonParser.parse(getTotalRankResult);
        List<JsonArray> totalRankList = Arrays.asList(totalRankArray);
        int rowSize = totalRankArray.size();
        int colSize = totalRankArray.get(0).getAsJsonObject().size();
        String[][] totalRank2X = new String[rowSize][2];
        for(int i = 0; i<rowSize; i++){
            totalRank2X[i][0] = totalRankArray.get(i).getAsJsonObject().get("userEmail").toString();
            System.out.print(totalRank2X[i][0]);
            totalRank2X[i][1] = totalRankArray.get(i).getAsJsonObject().get("userExp").toString();
            System.out.println(totalRank2X[i][1]);

        }

        return totalRank2X;
    }

    public int getMyRank(String email){
        String getMyRankUrl = defaultSendUrl + commandGetMyRank + email;
        String getMyRankResult = queryController.get(getMyRankUrl);
        System.out.println("myrank - " + jsonParser.parse(getMyRankResult).getAsInt());
        return jsonParser.parse(getMyRankResult).getAsInt();
    }

    /**
     * 문제 단어를 요청하는 쿼리
     * @param difficulty
     */
    public List<String> getWord(int difficulty){
        System.out.println(difficulty);
        List<String> wordList = new ArrayList<>();
        String getWordUrl = defaultSendUrl + "/getWord/" + difficulty;
        String getWordResult = queryController.get(getWordUrl);
        JsonArray wordArray = (JsonArray) jsonParser.parse(getWordResult);
        for(int i = 0; i<wordArray.size(); i++){
            wordList.add(wordArray.get(i).getAsJsonObject().get("wordContent").toString());
        }

        return wordList;
    }
    /**
     * 각 단어의 힌트를 요청하는 쿼리
     * @param word
     */
    public List<String> getHint(String word){
        List<String> hintList = new ArrayList<>();
        String getHintUrl = defaultSendUrl + "/getHint/" + word;
        String getHintResult = queryController.get(getHintUrl);
        JsonArray hintArray = (JsonArray) jsonParser.parse(getHintResult);
        for(int i = 0; i<hintArray.size(); i++){
            hintList.add(hintArray.get(i).getAsJsonObject().get("hintContent").toString());
        }

        return hintList;
    }

}