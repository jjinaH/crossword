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
import com.o2o.action.server.util.CommonUtil;
import com.o2o.action.server.util.GameContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Controller
public class Main extends DialogflowApp {

    public static void main(String[] args) {

       DBConnector dbConnectors = new DBConnector("intern2001@o2o.kr");
        try{
            StagePropertyInfo info = new StagePropertyInfo();
            UserInfo user = new UserInfo("1","0","3","5000",info,"intern2001@o2o.kr", "true", "true", "1");
            System.out.println("accumexp : " + user.getMyExp() + "  " +user.getMyCurrentExp() + " / " + user.getMyCurrentFullExp());

        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Autowired
    private GameContext gameContext;
//    @Autowired
    private TTS tts;
    String URL = "https://actions.o2o.kr/devsvr4/test/index.html";
    StagePropertyInfo stageinfo;
    DBConnector dbConnector;
//    TTS tts;

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
    private GoogleIdToken.Payload decodeIdToken(String idTokenString) throws GeneralSecurityException, IOException {
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                                            .Builder(transport, jsonFactory)
                                            // Specify the CLIENT_ID of the app that accesses the backend:
//                                            .setAudience(Collections.singletonList(clientId))
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
        htmldata.put("command", "welcome");

        //Test 용
        if (gameContext == null) System.out.println(">>>> gameContext bean 등록 XXX");
        gameContext.test();

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
                String lastPlayLevel = dbConnector.getLastPlayLevel();
                System.out.println("accumexp : " + exp);

                UserInfo user = new UserInfo(level, exp, hint, coin, stageinfo, email ,isBgmOn, isFoleyOn, lastPlayLevel);
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

        data.put("history", "main");
        htmldata.put("command", "main");
        data.put("coin", true);

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
        String response = tts.getTtsmap().get("difficultyselect");

        String userserial = (String)data.get("user"); // User정보 가져오기
        UserInfo user = (UserInfo) Desrial(userserial); // UserInfo user = new UserInfo(1,0,3,5000,stageinfo);

        int level = "stageSelect".equals(data.get("history"))
                ? ((Double) request.getParameter("number")).intValue() //선택한 레벨
                : user.getLevel(); //TODO 유저레벨이 아닌 마지막 플레이한 레벨
//                : user.getLastPlayLevel(); //TODO main 에서 difficulty 로 넘어오면 0 으로 출력 (db랑 연동 필요한듯)
        System.out.println("이전화면 >>>>> " + data.get("history") );
        System.out.println("레벨 >>>>> " + level );

        data.put("history", "difficultySelect");
        data.put("stage", level);

        htmldata.put("command", "difficultySelect");
        htmldata.put("stage", level);

        Stage SelectStage = stageinfo.Stages[1];
//        htmldata.put("winMoney1",Float.toString(SelectStage.getBetCoin().get("easy")*SelectStage.getCoinRatio()));
//        htmldata.put("winMoney2", Float.toString(SelectStage.getBetCoin().get("medium")*SelectStage.getCoinRatio()));
//        htmldata.put("winMoney3", Float.toString(SelectStage.getBetCoin().get("hard")*SelectStage.getCoinRatio()));
//        htmldata.put("betMoney1", stageinfo.Stages[1].getBetCoin().get("easy").toString());
//        htmldata.put("betMoney2", stageinfo.Stages[1].getBetCoin().get("medium").toString());
//        htmldata.put("betMoney3", stageinfo.Stages[1].getBetCoin().get("hard").toString());
        htmldata.put("betMoney1", gameContext.getBettingCoins("easy"));
        htmldata.put("betMoney2", gameContext.getBettingCoins("medium"));
        htmldata.put("betMoney3", gameContext.getBettingCoins("hard"));
//        int time = getTimeLimit(level);
        htmldata.put("timeLimit1", gameContext.getTimeLimit(level, "easy"));
        htmldata.put("timeLimit2", gameContext.getTimeLimit(level, "medium"));
        htmldata.put("timeLimit3", gameContext.getTimeLimit(level, "hard"));

        if(data.get("coin").equals(false)) {
            response = "Sorry you're out of coins.";
//            return rb.add(new SimpleResponse().setTextToSpeech(response))
//                    .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
//                    .build();
        }
        return rb.add(new SimpleResponse().setTextToSpeech(response))
                .add(new HtmlResponse().setUrl(URL).setUpdatedState(htmldata))
                .build();
    }
    // TODO properties 변경전 임시 함수 ㅜㅜㅜ
//    private int getTimeLimit(int level) {
//        int time = 90;
//        if(level==1) time = 90;
//        if(level==2) time = 85;
//        if(level==3) time = 80;
//        if(level==4) time = 120;
//        if(level==5) time = 115;
//        if(level==6) time = 110;
//        if(level==7) time = 105;
//        if(level==8) time = 150;
//        if(level==9) time = 145;
//        if(level==10) time = 140;
//        return time;
//    }

    private ActionResponse ingame(ActionRequest request) throws ExecutionException, InterruptedException {
        ResponseBuilder rb = getResponseBuilder(request);
        Map<String, Object> data = rb.getConversationData();
        Map<String, Object> htmldata = new HashMap<>();

        String userserial = (String)data.get("user");
        UserInfo user = (UserInfo) Desrial(userserial);

        String difficulty = CommonUtil.makeSafeString(request.getParameter("difficulty"));
        int stage = (int)((double)data.get("stage"));
//        int bettingCoins = stageinfo.Stages[stage].getBetCoin().get(difficulty);
        int bettingCoins = gameContext.getBettingCoins(difficulty);
        System.out.println("bettingCoins >>> " + bettingCoins + ", /n myCoin >>> " + user.getMyCoin());
        if(user.getMyCoin()< bettingCoins) {
            data.put("coin", false);
            return difficulty(request);
        }

        data.put("history", "ingame");
        data.put("stage", stage);
        data.put("difficulty", difficulty);

        htmldata.put("command", "ingame");
        htmldata.put("stage", stage);
        htmldata.put("difficulty", difficulty);
//        htmldata.put("timeLimit", stageinfo.Stages[stage].getTime().get(difficulty)); //gameBoard.getTimeLimit());
        int timeLimit = gameContext.getTimeLimit(stage, difficulty);
        htmldata.put("timeLimit", timeLimit);
//        int answerCnt = stageinfo.Stages[stage].getAnswerCount();
        int answerCnt = gameContext.getAnswerCnt(stage);
        htmldata.put("totalWord", answerCnt); //gameBoard.getTotalWord());

        user.GameStartChange(difficulty); // 유저 게임 시작 시 코인 감소
        dbConnector.updateUserCoin(user.getMyCoin(),user.getEmail()); //db update
        user.setLastPlayLevel(stage);
        // dbUpdate
//        userRepo.save(user); //TODO 고도화시 반영 ㅜㅜ

        // 변경된 유저 정보 저장
        userserial = Createserial(user);
        data.put("user", userserial);

//        int dif = stage >=6 ? 3 : stage >= 4 ? 2 : 1; /* DB에 저장된 레벨별 단어난이도?? TODO new properties 적용 후 삭제  */
        //stage 가 6 이상이면 3, 4이상이면 2, 아니면 1
        int dif = gameContext.getDifficulty(stage);

        DBConnector dbConnector = new DBConnector(user.getEmail());
        List<String> wordlist = dbConnector.getWord(dif); //db에 저장된 문제단어를 가져온다.
        Collections.shuffle(wordlist);

        List<String> hintlist = new ArrayList<>();
        for(int i=0; i< wordlist.size(); i++) {
            String word = wordlist.get(i).replaceAll("\"","");
            wordlist.set(i, word);
            System.out.print(i+ " :: "+ word + ", ");
            String hint = dbConnector.getHint(word).get(0).replaceAll("\"","");
            hintlist.add(hint);
        }

        /**
         * 게임생성시작
         */
        GameBoard gameBoard = new GameBoard(difficulty, stage, stageinfo, wordlist, hintlist );
        String boardserial = Createserial(gameBoard); // 게임보드 직렬화 후 전송
        data.put("gameboard", boardserial);
        htmldata.put("gameboard", gameBoard);
        data.put("coin", true);

        char[][] board = gameBoard.getBoard();
        htmldata.put("board", board);

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

        String userserial = (String)data.get("user");
        UserInfo user = (UserInfo) Desrial(userserial);

        String boardserial = (String)data.get("gameboard");
        GameBoard gameBoard = (GameBoard) Desrial(boardserial);

        String response = "";
        if (word.isEmpty()) {
            htmldata.put("command", "openhint");
            String hint = "";

            if(user.getMyHint()>0) { //user가 사용할 힌트가 남아있는지 체크
                response = "open hint";
                hint = gameBoard.getHint();
                if (!hint.equals("noHint")) {
                    user.ConsumeHintCount(); // 힌트 개수 차감 //TODO
                    dbConnector.updateUserHint(user.getMyHint(), user.getEmail());
                }else{
                    response = "I gave you all the hints.";
                    hint = "I gave you all the hints.";
                    htmldata.put("nohint", true);
                }
            }else{
                response = "There are no hints available.";
                hint = "There are no hints available.";
                htmldata.put("nohint", true);
            }
            htmldata.put("hint", hint);

        } else {
            // 정답
            if (gameBoard.tryAnswer(word)) {
                htmldata.put("command", "correct");
                htmldata.put("matchpoint", gameBoard.getAnswerPoint(word));
                response = "correct";

                if (gameBoard.getResult().isWin()) // 다 맞추었는지 확인
                    htmldata.put("finish", true);
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
        System.out.println("sound : " + sound + ",,  isonoff : " + isonoff);

        String isTrue = "1".equals(isonoff) ? "true" : "false"; //false:0

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
        if(sound.equals("backgroundsound")) user.setBgmOn(isTrue);
        if(sound.equals("SoundEffect")) user.setFoleyOn(isTrue);
        
        //TODO user 테이블 업데이트

        String serial = Createserial(user);
        data.put("user", serial);

        htmldata.put("command", "settingselect");
        htmldata.put("bgmOn", user.getBgmOn());
        htmldata.put("foleyOn", user.getFoleyOn());
//        htmldata.put("sound",sound);
//        htmldata.put("onoff",isonoff);

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
