package com.o2o.action.server.app;

//import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import javax.print.DocFlavor;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;

public class GameBoard implements Serializable {

    private BoardCell Board[][]; // 보드 배열
    private AnswerWord[] answers; // 정답변수
    private  ArrayList<AnswerWord> answeredList; // 맞춘 정답 리스트
    private ArrayList<AnswerWord> restAnswerList; // 맞춰야 할 정답 리스트
    private int answercount; //정답 개수

    private int col; // 보드판 가로길이
    private int row; // 보드판 세로길이
    private int stage; //선택한 레벨
    private String difficulty; //선택한 난이도

    private StagePropertyInfo stageinfo; // 스테이지 프로퍼티 정보
    private List<String> wordlist; // 정답 배열
    private List<String> hintlist; // 힌트 배열

    public GameBoard(String _difficulty, int _stage, StagePropertyInfo _stageinfo, List<String> _wordlist, List<String>_hintlist) {
        stageinfo = _stageinfo;
        stage = _stage;
        difficulty = _difficulty;
        int dif = stage >=8 ? 3 : stage >= 4 ? 2 : 1; //TODO new properties
        row = stageinfo.Stages[dif].getSize_Row();
        col = row;
        answercount = stageinfo.Stages[dif].getAnswerCount();
        wordlist = _wordlist;
        hintlist = _hintlist;

        makeBoard(); // 보드판 생성
    }
    private void makeBoard() {
        Board = new BoardCell[row][col];
        answeredList = new ArrayList<AnswerWord>();
        restAnswerList = new ArrayList<AnswerWord>();

        loadAnswer(); // 정답 불러오기

        for (int i=0; i<answercount; i++) restAnswerList.add(answers[i]); // 남은정답 리스트에 모든 정답 넣기

        ArrayList dirarray1 = new ArrayList(Arrays.asList(2,4)); // 오른쪽 아래 1~5스테이지
        ArrayList dirarray2 = new ArrayList(Arrays.asList(2,3,4)); // 오른쪽, 오른쪽 아래,아래 6~9스테이지
        ArrayList dirarray3 = new ArrayList(Arrays.asList(0,1,2,3,4,5,6,7)); // 전방향 10스테이지

        // 유저레벨이 아닌 선택한 레벨에 따라 정답 배치 방향 결정
        ArrayList dirarray = stage<6? dirarray1 : stage<10 ? dirarray2 : stage>=10 ? dirarray3 : null;
//        if(userlevel<6) dirarray = dirarray1;
//        else if(userlevel<10) dirarray = dirarray2;
//        else if( userlevel>=10) dirarray = dirarray3;

        // 보드판 알고리즘 클래스 인스턴스 생성
        // TODO 보드알고리즘을 먼저 돌리고, AnswerClass를 생성하는 것이 효율적이지 않을까?
        // TODO Answer클래스 BoardAlgo클래스 생성 요건 확인 필요
        BoardAlgorithm boardAlgorithm = new BoardAlgorithm(col,row,Board,answers);
        // 보드판 알고리즘이 성공할때까지 계속 시도
        boolean issucess = false;
        while(!issucess) {
            System.out.println("보드생성 실패!");
            boardAlgorithm = new BoardAlgorithm(col,row,Board,answers);

            boardAlgorithm.MakeUpBoardAnswer(dirarray); // 보드판에 정답 알파벳 넣기
            boardAlgorithm.MakeUpBoardAlphabet(); //보드판에서 정답아닌곳에 랜덤 알파벳 구성
            issucess = boardAlgorithm.isBoardSuccess;
        }

        Board = boardAlgorithm.Successboard; // 성공보드 저장하기
        printBoard(Board,col,row); //보드판 출력
    }
    private void loadAnswer() {
        if(wordlist.size()<answercount || hintlist.size()<answercount) {
            System.out.println("DB의 정답이나 힌트 리스트가 정답 개수 보다 적습니다.");
            return;
        }
        answers = new AnswerWord[answercount]; //TODO
        for(int i=0; i< answercount; i++) {
            AnswerWord answerWord = new AnswerWord(wordlist.get(i), hintlist.get(i) );//new String[]{hintlist.get(i)});
            answers[i]=answerWord;
        }
        Arrays.sort(answers);
    }

    public String getHint() {
        if(restAnswerList.size()==0) return "맞춰야 할 단어가 더이상 없습니다.";
//        for(int i=0; i< restAnswerList.size(); i++) { } //TODO ?? 샂제 ???
//        return restAnswerList.get(0).useHint();

        //첫번째 정답의 힌트스택검사
        AnswerWord answer = restAnswerList.get(0);
        System.out.println(" answer >>> "+answer.getAnswer() );
        String hint = answer.useHint();

        //첫번째 정답의 힌트스택이 비었을 경우
        System.out.println(" hint stack poped >>> " + hint );
        if(restAnswerList.size() > 1 && hint.equals("noHint")) {
            for(int i=1; i<restAnswerList.size(); i++) {
                String tempStr = restAnswerList.get(i).useHint();
                if(!tempStr.equals("noHint")) {
                    hint = tempStr;
                    break;
                }
            }
        }

        return hint;
    }

    private void printBoard(BoardCell[][]board,int x, int y) {
        for (int i = 0; i<y; i++) {
            for (int j = 0; j<x; j++) {
                System.out.print(Board[i][j].cellchar + " ");
            }
            System.out.println();
        }
    }

    // 보드판 배열 가져오기
    public char[][] getBoard() {
        char board[][] = new char[row][col];
        for (int i=0; i< row; i++) {
            for (int j=0; j< col; j++) {
                board[i][j] = Board[i][j].cellchar;
            }
        }
        return board;
    }




    public boolean tryAnswer(String _answer) {

        for (int i = 0; i<answercount; i++) { // TODO 반복문으로 확인하는 것은 비효율, 배열 contain? include?로 변경
            if(answers[i].getAnswer().equals(_answer)) { // 정답인 경우

                answeredList.add(answers[i]); // 정답리스트에 추가하고 맞춰야할 리스트에서 제거
                if(restAnswerList.size()>0) restAnswerList.remove(answers[i]);
                return true;
            }
        }
        return false;
    }

    public String[] getAnswerPoint(String _answer) { // 정답 표시할 좌표정보 return
        for (int i = 0; i<answercount; i++) {
            if(answers[i].getAnswer().equals(_answer)) {
                return answers[i].getAlphabetPoint();
            }
        }
        System.out.println("좌표정보 가져오기 오류 : 가져오려는 정답이 부적절합니다");
        return null;
    }
    public Result getResult() { // 다 맞추었는지 확인
        Result result = new Result(); //TODO 굳이 클래스까지 써야 할까 ?
        // 결과 객체에 정답리스트와 맞춰야할답 리스트 복사 후 리턴
        for (AnswerWord i : answeredList) {
            if(!result.answer.contains(i.getAnswer())) {
                result.answer.add(i.getAnswer());
            }
        }
        for (AnswerWord i : restAnswerList) {
            if(!result.restword.contains(i.getAnswer())) {
                result.restword.add(i.getAnswer());
            }
        }
        return result;
    }
}
