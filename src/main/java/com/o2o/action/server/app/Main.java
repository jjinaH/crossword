package com.o2o.action.server.app;

import com.google.actions.api.*;
import com.google.actions.api.response.ResponseBuilder;
import com.google.actions.api.response.helperintent.SignIn;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.actions_fulfillment.v2.model.*;
import com.o2o.action.server.model.User;
import com.o2o.action.server.util.CommonUtil;

import java.io.*;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Main extends DialogflowApp {

    public static void main(String[] args) {

       DBConnector dbConnectors = new DBConnector("intern2001@o2o.kr");
        try{
            StagePropertyInfo info = new StagePropertyInfo();
            UserInfo user = new UserInfo("1","0","3","5000",info,"intern2001@o2o.kr", "true", "true");
            System.out.println("accumexp : " + user.getMyExp() + "  " +user.getMyCurrentExp() + " / " + user.getMyCurrentFullExp());

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    String URL = "https://actions.o2o.kr/devsvr4/test/index.html";

    StagePropertyInfo stageinfo;
    TTS tts;
    DBConnector dbConnector;
    private void setUp() {
        tts = new TTS();
        try {
            stageinfo = new StagePropertyInfo();
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        }

    }

   static String Createserial(Object obj) {
        byte[] serializedMember = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(obj);
            serializedMember = baos.toByteArray();
            out.close();
            System.out.println("serial!! " + serializedMember);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(serializedMember);
    }

   static Object Desrial(String outst) {
        byte[] inserializedMember = Base64.getDecoder().decode(outst);
        ;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(inserializedMember);
            ObjectInputStream out = new ObjectInputStream(bais);
            System.out.println("serial!!!");
            out.close();
            return out.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private GoogleIdToken.Payload getUserProfile(String idToken) {
        GoogleIdToken.Payload profile = null;
        try {
            profile = decodeIdToken(idToken);
        } catch (Exception e) {
            //LOGGER.error("error decoding idtoken");
            //LOGGER.error(e.toString());
        }
        return profile;
    }
    private GoogleIdToken.Payload decodeIdToken(String idTokenString)
            throws GeneralSecurityException, IOException {
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier verifier =
                new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                        // Specify the CLIENT_ID of the app that accesses the backend:
//                        .setAudience(Collections.singletonList(clientId))
                        .build();
        GoogleIdToken idToken = verifier.verify(idTokenString);
        return idToken.getPayload();
    }

    @ForIntent("Default Welcome Intent")
    public ActionResponse defaultWelcome(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        Map<String, Object> htmldata = new HashMap<>();

        request.getConversationData().clear();
        data.clear();
        setUp();
        data.put("history", "welcome");
        data.put("special case", false);
        htmldata.put("command", "welcome");

//        UserSettingInfo userSettingInfo = new UserSettingInfo();
//        String settingserial = Createserial(userSettingInfo);
//        data.put("setting", settingserial);

        //db 연결
        if (!request.hasCapability("actions.capability.INTERACTIVE_CANVAS")) {
            return rb.add(new SimpleResponse().setSsml("Inveractive Canvas가 지원되지 않는 기기예요."))
                    .endConversation().build();
        } else {
            if (request.isSignInGranted()) {
                GoogleIdToken.Payload profile = getUserProfile(request.getUser().getIdToken());
                System.out.println("case 1");
                //이메일 가져옴
                String email = profile.getEmail();
                dbConnector = new DBConnector(email);
                String level = dbConnector.getUserLevel();
                String exp = dbConnector.getUserExp();
                String coin = dbConnector.getUserCoin();
                String hint = dbConnector.getUserHint();

                String isBgmOn = dbConnector.getBgmOn();
                String isFoleyOn = dbConnector.getFoleyOn();
                System.out.println("accumexp : " + exp);

                UserInfo user = new UserInfo(level, exp, hint, coin, stageinfo, email ,isBgmOn, isFoleyOn);
                String serial = Createserial(user);
                data.put("user", serial);
                htmldata.put("inputemail", email);
                //신규유저인지 아닌지
                //sign한후에 email
                return rb.add(new SimpleResponse().setTextToSpeech(tts.getTtsmap().get("welcome")))
                        .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
                        .build();
            }else{ System.out.println("case 2");
                return rb.add(new SignIn().setContext("To get your account details")).build();
            }
        }
    }

    @ForIntent("home")
    public ActionResponse home(ActionRequest request) throws ExecutionException, InterruptedException {
        return main(request);
    }

    @ForIntent("mainFromWelcome")
    public ActionResponse mainFromWelcome(ActionRequest request) throws ExecutionException, InterruptedException {
        return main(request);
    }

    @ForIntent("stageFromMain")
    public ActionResponse stageSelect(ActionRequest request) throws ExecutionException, InterruptedException {
        return stage(request);
    }

    @ForIntent("stageFromDifficulty")
    public ActionResponse stageFromDifficulty(ActionRequest request) throws ExecutionException, InterruptedException {
        return stage(request);
    }

    @ForIntent("difficultyFromMain")
    public ActionResponse difficultySelect(ActionRequest request) throws ExecutionException, InterruptedException {
        return difficulty(request);
    }

    @ForIntent("difficultyFromStage")
    public ActionResponse difficultyFromStage(ActionRequest request) throws ExecutionException, InterruptedException {
        return difficulty(request);
    }
    @ForIntent("difficultyFromResult")
    public ActionResponse difficultyFromResult(ActionRequest request) throws ExecutionException, InterruptedException {
        return difficulty(request);
    }
    @ForIntent("ingameFromDifficulty")
    public ActionResponse ingameFromDifficulty(ActionRequest request) throws ExecutionException, InterruptedException {
        return ingame(request);
    }

    @ForIntent("main")
    public ActionResponse main(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        Map<String, Object> htmldata = new HashMap<>();
        data.put("special case", false); // 세팅,상점, 랭킹인 경우 - 스페셜 케이스 -> 뒤로 돌아갈 수 있음
        data.put("history", "main");
        htmldata.put("command", "main");

        // User정보 가져오기
        String userserial = (String)data.get("user");
        UserInfo user = (UserInfo) Desrial(userserial);

        htmldata.put("level", user.getLevel());
        htmldata.put("myExp", user.getMyCurrentExp());
        htmldata.put("fullExp", user.getMyCurrentFullExp());
        htmldata.put("myHint", user.getMyHint());
        htmldata.put("myCoin", user.getMyCoin());
        htmldata.put("fullExp", user.getMyCurrentFullExp());
        htmldata.put("bgmOn", user.getBgmOn());
        htmldata.put("foleyOn", user.getFoleyOn());

        return rb.add(new SimpleResponse().setTextToSpeech(tts.getTtsmap().get("main")))
                .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
                .build();
    }

    public ActionResponse stage(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        Map<String, Object> htmldata = new HashMap<>();

        data.put("history", "stageSelect");
        htmldata.put("command", "stageSelect");

        return rb.add(new SimpleResponse().setTextToSpeech(tts.getTtsmap().get("stageselect")))
                .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
                .build();
    }

    public ActionResponse difficulty(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        Map<String, Object> htmldata = new HashMap<>();

        String userserial = (String)data.get("user"); // User정보 가져오기
        UserInfo user = (UserInfo) Desrial(userserial); // UserInfo user = new UserInfo(1,0,3,5000,stageinfo);

        //메인에서 왔는지, 스테이지에서왔는지
//        int stage;
//        if (data.get("history").equals("main"))  stage = user.getLevel(); //TODO
//        else {
//            stage = ((Double) request.getParameter("number")).intValue();
//            System.out.println("선택한 level : "+stage + " user level : " + user.getLevel());
//            if(stage>user.getLevel()) return stage(request);
//        }

        int level = "stageSelect".equals(data.get("history"))
                ? ((Double) request.getParameter("number")).intValue() //선택한 레벨
                : user.getLevel(); //TODO 유저레벨이 아닌 마지막 플레이한 레벨
        System.out.println("이전화면 >>>>> " + data.get("history") );
        System.out.println("레벨 >>>>> " + level );

        data.put("history", "difficultySelect");
        data.put("special case", false);
        data.put("stage", level);

        htmldata.put("command", "difficultySelect");
        htmldata.put("stage", level);
        Stage SelectStage = stageinfo.Stages[level];
        htmldata.put("winMoney1",Float.toString(SelectStage.getBetCoin().get("easy")*SelectStage.getCoinRatio()));
        htmldata.put("winMoney2", Float.toString(SelectStage.getBetCoin().get("medium")*SelectStage.getCoinRatio()));
        htmldata.put("winMoney3", Float.toString(SelectStage.getBetCoin().get("hard")*SelectStage.getCoinRatio()));
        htmldata.put("betMoney1", stageinfo.Stages[level].getBetCoin().get("easy").toString());
        htmldata.put("betMoney2", stageinfo.Stages[level].getBetCoin().get("medium").toString());
        htmldata.put("betMoney3", stageinfo.Stages[level].getBetCoin().get("hard").toString());
        htmldata.put("timeLimit1", stageinfo.Stages[level].getTime().get("easy").toString());
        htmldata.put("timeLimit2", stageinfo.Stages[level].getTime().get("medium").toString());
        htmldata.put("timeLimit3", stageinfo.Stages[level].getTime().get("hard").toString());

        return rb.add(new SimpleResponse().setTextToSpeech(tts.getTtsmap().get("difficultyselect")))
                .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
                .build();
    }

    private ActionResponse ingame(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        Map<String, Object> htmldata = new HashMap<>();

        /**
         * difficulty 메소드에서 분기되어야함, 레벨화면을 거쳤는지에 따라 ..

        if (data.get("history").equals("result") || data.get("history").equals("result")) {
            difficulty = CommonUtil.makeSafeString(data.get("difficulty"));
        } else {
            difficulty = CommonUtil.makeSafeString(request.getParameter("difficulty"));
        }*/
        String difficulty = CommonUtil.makeSafeString(request.getParameter("difficulty"));
        int stage = (int)((double)data.get("stage"));

        data.put("history", "ingame");
        data.put("stage", stage);
        data.put("difficulty", difficulty);

        // User정보 가져오기
        String userserial = (String)data.get("user");
        UserInfo user = (UserInfo) Desrial(userserial);

        user.GameStartChange(stage, difficulty); // 유저 게임 시작 시 코인 감소
        System.out.println("ㄷㄷ user.getMyCoin() >>> "+user.getMyCoin());
        System.out.println("ㄷㄷ user.getEmail() >>> "+user.getEmail());
        dbConnector.updateUserCoin(user.getMyCoin(),user.getEmail()); //db update
        // 변경된 유저 정보 저장
        userserial = Createserial(user);
        data.put("user",userserial);

        int dif = 0;
        if(difficulty.equals("easy")) dif = 1;
        else if(difficulty.equals("medium")) dif = 2;
        else if(difficulty.equals("hard")) dif = 3;

        //나중에 구성자 바꾸어야됌
        DBConnector dbConnector = new DBConnector(user.getEmail());
        List<String> wordlist = dbConnector.getWord(dif);
        Collections.shuffle(wordlist);
        List<String> hintlist = new ArrayList<>();
        for(int i=0; i< wordlist.size(); i++) {
            String word = wordlist.get(i).replaceAll("\"","");
            wordlist.set(i, word);
            System.out.print(i+ " :: "+ word + ", ");
            String hint = dbConnector.getHint(word).get(0).replaceAll("\"","");
            hintlist.add(hint);
        }

        GameBoard gameBoard = new GameBoard(difficulty, stage, stageinfo, wordlist, hintlist, user.getLevel());

        String boardserial = Createserial(gameBoard); // 게임보드 직렬화 후 전송
        data.put("gameboard",boardserial);
        char[][] board = gameBoard.getBoard();

        htmldata.put("command", "ingame");
        htmldata.put("board", board);

        htmldata.put("timeLimit", gameBoard.getTimeLimit());
        htmldata.put("totalWord", gameBoard.getTotalWord());
        htmldata.put("gameboard", gameBoard);
        htmldata.put("stage", stage);
        htmldata.put("difficulty", dif);

        return rb.add(new SimpleResponse().setTextToSpeech(tts.getTtsmap().get("ingame")))
                .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
                .build();
    }

    @ForIntent("ingameMessage")
    public ActionResponse ingameMessage(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        Map<String, Object> htmldata = new HashMap<>();

        String word = CommonUtil.makeSafeString(request.getParameter("word"));
//        String hint = CommonUtil.makeSafeString(request.getParameter("hint"));
        // User정보 가져오기
        String userserial = (String)data.get("user");
        UserInfo user = (UserInfo) Desrial(userserial);
        // GameBoard정보 가져오기
        String boardserial = (String)data.get("gameboard");
        GameBoard gameBoard = (GameBoard) Desrial(boardserial);

        String response = "";
        if (word.isEmpty()) {
            htmldata.put("command", "openhint");

            user.ConsumeHintCount(); // 힌트 개수 차감
            dbConnector.updateUserHint(user.getMyHint(),user.getEmail());
            htmldata.put("hint", gameBoard.getHintMessage());
            response = "open hint";

        } else {
            if (gameBoard.tryAnswer(word)) {
                htmldata.put("command", "correct");
                htmldata.put("matchpoint", gameBoard.GetAnswerPoint(word));
                response = "correct";
                Result result = gameBoard.getResult();
                if (result.isWin()) htmldata.put("finish", true);
                else htmldata.put("finish", false);
            } else {
                htmldata.put("command", "wrong");
                response = "wrong";
            }
        }
        // 게임보드 저장
        boardserial = Createserial(gameBoard);
        data.put("gameboard",boardserial);
        // 유저 정보 저장
        userserial = Createserial(user);
        data.put("user",userserial);
        return rb.add(new SimpleResponse().setTextToSpeech(response))
                .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
                .build();
    }

    @ForIntent("Default Fallback Intent")
    public ActionResponse fallback(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        Map<String, Object> htmldata = new HashMap<>();
        HtmlResponse htmlResponse = new HtmlResponse();

        if (data.get("history") != null && data.get("history").equals("ingame")) {
            htmldata.put("command", "wrong");
            return rb.add(new SimpleResponse().setTextToSpeech("wrong"))
                    .add(htmlResponse.setUrl(URL).setUpdatedState(htmldata))
                    .build();
        }

        return rb.add(new SimpleResponse().setTextToSpeech("Please say one more time"))
                .add(htmlResponse.setUrl(URL).setUpdatedState(htmldata))
                .build();
    }

    @ForIntent("result")
    public ActionResponse result(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        Map<String, Object> htmldata = new HashMap<>();
        HtmlResponse htmlResponse = new HtmlResponse();

        String result = CommonUtil.makeSafeString(request.getParameter("result"));

        data.put("history", "result");
        data.put("special case", false);

        // User정보 가져오기
        String userserial = (String)data.get("user");
        UserInfo user = (UserInfo) Desrial(userserial);

        //결과, stage, difficulty에 따라 user의 level, exp, coin 정보를 조정한다.
        String response = "";
        double stage = (double)data.get("stage");
        String difficulty = (String)data.get("difficulty");
        if (result.equals("success")) {
            response = tts.getTtsmap().get("success");
            user.UserStageClearChange((int)stage,difficulty);
            dbConnector.updateUserExp(user.getMyExp(),user.getEmail());
            dbConnector.updateUserCoin(user.getMyCoin(),user.getEmail());
            dbConnector.updateUserLevel(user.getLevel(),user.getEmail());
        } else {
            response = tts.getTtsmap().get("fail");
        }
        htmldata.put("command", "result");
        htmldata.put("result", result);
        htmldata.put("level", user.getLevel());
        htmldata.put("myExp", user.getMyCurrentExp());
        htmldata.put("fullExp", user.getMyCurrentFullExp());

        htmldata.put("myHint", user.getMyHint());
        htmldata.put("myCoin", user.getMyCoin());
        // GameBoard정보 가져오기
        String boardserial = (String)data.get("gameboard");
        GameBoard gameBoard = (GameBoard) Desrial(boardserial);
        Result results = gameBoard.getResult();
        htmldata.put("correctList", results.getAnser());
        htmldata.put("wrongList", results.getRestWord());
        // 유저 정보 저장
        String serial = Createserial(user);
        data.put("user",serial);
        return rb.add(new SimpleResponse().setTextToSpeech(response))
                .add(htmlResponse.setUrl(URL).setUpdatedState(htmldata))
                .build();
    }


    @ForIntent("setting")
    public ActionResponse setting(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        Map<String, Object> htmldata = new HashMap<>();

        data.put("special case", true);
        htmldata.put("command", "setting");

        String userserial = (String)data.get("user");
        UserInfo user = (UserInfo) Desrial(userserial);

//        String settingserial = (String)data.get("setting");
//        UserSettingInfo userSettingInfo =  (UserSettingInfo) Desrial(settingserial);
//        System.out.println("soundeffect" + userSettingInfo.isSoundEffect() + "backgroundsound" + userSettingInfo.isBackGroundSound());
//        htmldata.put("soundeffect", userSettingInfo.isSoundEffect());
//        htmldata.put("backgroundsound", userSettingInfo.isBackGroundSound());

        htmldata.put("bgmOn", user.getBgmOn());
        htmldata.put("foleyOn", user.getFoleyOn());

        return rb.add(new SimpleResponse().setTextToSpeech(tts.getTtsmap().get("setting")))
                .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
                .build();
    }
    @ForIntent("settingselect")
    public ActionResponse settingselect(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        Map<String, Object> htmldata = new HashMap<>();

        data.put("special case", true);

        String sound = request.getParameter("Sound").toString();
        String isonoff = request.getParameter("onoff").toString();
        System.out.println("sound : " + sound + "isonoff : " + isonoff);

        String isTrue = "true".equals(isonoff) ? "true" : "false";

//        String serial = (String)data.get("setting");
//        UserSettingInfo userSettingInfo = (UserSettingInfo) Desrial(serial);
//        // 설정값을 userSettingInfo 객체에 세팅
//        if(sound.equals("BackgroundSound")) {
//            userSettingInfo.setBackGroundSound(Integer.parseInt(isonoff));
//        } else if(sound.equals("SoundEffect")) {
//            userSettingInfo.setSoundEffect(Integer.parseInt(isonoff));
//        }
//        // 설정된 객체를 저장
//        String settingserial = Createserial(userSettingInfo);
//        data.put("setting", settingserial);
        String userserial = (String)data.get("user");
        UserInfo user = (UserInfo) Desrial(userserial);
        if(sound.equals("BackgroundSound")) user.setBgmOn(isTrue);
        if(sound.equals("SoundEffect")) user.setFoleyOn(isTrue);
        
        //TODO user 테이블 업데이트

        String serial = Createserial(user);
        data.put("user", serial);

        htmldata.put("command", "settingselect");
//        htmldata.put("sound",sound);
//        htmldata.put("onoff",isonoff);

        htmldata.put("bgmOn", user.getBgmOn());
        htmldata.put("foleyOn", user.getFoleyOn());

        return rb.add(new SimpleResponse().setTextToSpeech(tts.getTtsmap().get("setting")))
                .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
                .build();
    }
    @ForIntent("ranking")
    public ActionResponse ranking(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        Map<String, Object> htmldata = new HashMap<>();

        data.put("special case", true);
        htmldata.put("command", "ranking");
        DBConnector db= new DBConnector("test");

        String userserial = (String)data.get("user");
        UserInfo user = (UserInfo) Desrial(userserial);
        htmldata.put("myRank",db.getMyRank(user.getEmail()));
        htmldata.put("totalRank",db.getTotalRank());

        return rb.add(new SimpleResponse().setTextToSpeech(tts.getTtsmap().get("ranking")))
                .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
                .build();
    }

    @ForIntent("store")
    public ActionResponse shop(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        Map<String, Object> htmldata = new HashMap<>();

        data.put("special case", true);
        htmldata.put("command", "shop");

        return rb.add(new SimpleResponse().setTextToSpeech(tts.getTtsmap().get("store")))
                .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
                .build();
    }



    @ForIntent("return")
    public ActionResponse returnback(ActionRequest request) throws ExecutionException, InterruptedException {

        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        Map<String, Object> htmldata = new HashMap<>();

        String history = CommonUtil.makeSafeString(data.get("history"));
        Boolean isSpecial = (Boolean) (data.get("special case"));

        if (isSpecial) {
            htmldata.put("command", history);
            data.put("special case", false);

            return rb.add(new SimpleResponse().setTextToSpeech("return"))
                    .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
                    .build();
        }

        return rb.add(new SimpleResponse().setTextToSpeech("can't return"))
                .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
                .build();
    }

//    ActionRequest ingameRequest;
//    public ActionResponse sendTxQuery(String text) {
//        if(ingameRequest==null) {System.out.println("ingamerequst null" + text);}
//        System.out.println(ingameRequest.getIntent() + text);
//        ResponseBuilder rb = getResponseBuilder(ingameRequest);
//        Map<String, Object> data = rb.getConversationData();
//        Map<String, Object> htmldata = new HashMap<>();

//        System.out.println("recv query from Mainjava before text.eqult fail");
//        if(text.equals("fail")) {
//            System.out.println("recv query from Mainjava after text.eqult fail");
//            htmldata.put("command","result");
//            htmldata.put("result","success");
//            Result results = gameBoard.getResult();
//            htmldata.put("level", user.getLevel());
//            htmldata.put("myExp", user.getMyExp());
//            htmldata.put("fullExp", user.getFullExp());
//            htmldata.put("myHint", user.getMyHint());
//            htmldata.put("myCoin", user.getMyCoin());
//            htmldata.put("correctList", results.getAnser());
//            htmldata.put("wrongList", results.getRestWord());
//            rb.removeContext("ingame");
//            rb.add(new ActionContext("result",3));

//            return rb.add(new SimpleResponse().setTextToSpeech(tts.getTtsmap().get("result")))
//                      .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
//                      .build();
//      }
//        return rb.add(new SimpleResponse().setTextToSpeech("Loose!"))
//            .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
//            .build();
//    }

}
