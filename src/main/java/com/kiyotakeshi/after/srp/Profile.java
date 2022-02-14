package com.kiyotakeshi.after.srp;

import java.util.HashMap;
import java.util.Map;

/**
 * プロフィールに関する情報の管理
 */
public class Profile {
    private Map<String, Answer> answers = new HashMap<>();
    private int score;
    private String name;

    public Profile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // 質問とその回答を追加
    public void add(Answer answer) {
        answers.put(answer.getQuestionText(), answer);
    }

    public boolean matches(Criteria criteria) {
        MatchSet matchSet = new MatchSet(answers, criteria);
        score = matchSet.getScore();
        return matchSet.matches();
    }

    private Answer answerMatching(Criterion criterion) {
        return answers.get(criterion.getAnswer().getQuestionText());
    }

    public int score() {
        return score;
    }
}
