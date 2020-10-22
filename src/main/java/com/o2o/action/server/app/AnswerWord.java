package com.o2o.action.server.app;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Stack;
// 정답 클래스
public class AnswerWord implements Comparable, Serializable {

    private String answer; // 정답 문자
    public String getAnswer() {
        return answer;
    }

    private String[] AnswerPoint; // 정답 알파벳의 좌표 배열
    public String[] getAlphabetPoint() {
        return AnswerPoint;
    } // 정답의 알파벳 위치 배열 반환

    private String hint; // private String[] Hints; // 정답의 힌트 배열 => 배열대신 Str으로 변경, 추후 DB의 힌트가 늘어날경우 배열로 사용
    private Stack<String> hintsStack; // 정답의 힌트사용 카운트를 위한 힌트 스택

    public String useHint() { // 해당 단어의 힌트스택에서 힌트 뺴기
        if(hintsStack.empty()) return "noHint"; // 만약 스택이 비었으면 noHint 출력
        String hint = hintsStack.pop();
        return hint;
    }

    public AnswerWord(String _word, String _hint) { //String[] _hints) {
        answer = _word;
        hint = _hint; //_hints.clone();
        hintsStack = new Stack<String>();

        hintsStack.push(Character.toString(answer.charAt(0))); //세번째 힌트 :: 첫번째 글자
        String LenSt = ""; //두번째 힌트 :: 글자수
        for(int i=0; i< answer.length(); i++) LenSt +="_ ";
        hintsStack.push(LenSt);
        hintsStack.push(hint); //Hints[0]); //첫번째 힌트 :: DB의 힌트 그대로

        AnswerPoint = new String[answer.length()]; // row좌표 col좌표 이므로 *2의 배열 자리 선정
    }

    /**
     * Board 생성알고리즘에 필요한 함수들 ...
     */
    // 해당 정답의 알파벳 위치 기록
    public void setAlphabetPoint(int _alphabetindex, int _row, int _col) {
        if(_alphabetindex>=answer.length()) {
            System.out.println("인덱스가 단어의 길이를 벗어났습니다.");
            return;
        }
        String pointstring = _row+","+_col;
        // AnswerPoint[_alphabetindex*2] = _row;
        AnswerPoint[_alphabetindex] = pointstring;
    }
    public int getAnswerLength() { // 정답 길이 반환
        return answer.length();
    }
    public char getAnswerChar(int _index) { //answer str의 각 alphabet return
        return answer.charAt(_index);
    }

    @Override
    public int compareTo(@NotNull Object _answerWord) {
        AnswerWord answerWord = (AnswerWord)_answerWord;
        if (this.getAnswerLength() < answerWord.getAnswerLength()) return 1;
        else if (this.getAnswerLength() == answerWord.getAnswerLength()) return 0;
        else return -1;
    }
}