package com.kiyotakeshi.before;

import java.util.HashMap;
import java.util.Map;

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
        score = 0;
        boolean kill = false;
        boolean anyMatches = false;

        // criterion(条件)がプロフィールに含まれている回答とマッチするか判定
        for (Criterion criterion : criteria) {
            Answer answer = answers.get(criterion.getAnswer().getQuestionText());
            boolean match = criterion.getWeight() == Weight.DontCare || answer.match(criterion.getAnswer());

            // 必須条件を満たしていなければ false
            if (!match && criterion.getWeight() == Weight.MustMatch) {
                kill = true;
            }
            // 条件を満たす回答がプロフィールにあるとその条件で指定されるスコアがプロフィールに加算される
            if (match) {
                score += criterion.getWeight().getValue();
            }
            anyMatches |= match;
        }
        if (kill) {
            return false;
        }
        return anyMatches;
    }

    public int score(){
        return score;
    }
}
