package com.kiyotakeshi.after.refactoring;

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
        calculateScore(criteria);

        if (doesNotMeetAnyMustMatchCriterion(criteria)) {
            return false;
        }
        return anyMatches(criteria);
    }

    /**
     * 必須条件にマッチしないものがあるか
     * @param criteria
     * @return
     */
    private boolean doesNotMeetAnyMustMatchCriterion(Criteria criteria) {
        for (Criterion criterion : criteria) {
            boolean match = criterion.matches(answerMatching(criterion));
            if (!match && criterion.getWeight() == Weight.MustMatch) {
                return true;
            }
        }
        return false;
    }

    /**
     * 条件を満たす回答がプロフィールにあるとその条件で指定されるスコアがプロフィールに加算される
     * @param criteria
     */
    private void calculateScore(Criteria criteria) {
        score = 0;
        for (Criterion criterion : criteria) {
            if (criterion.matches(answerMatching(criterion))) {
                score += criterion.getWeight().getValue();
            }
        }
    }

    private boolean anyMatches(Criteria criteria) {
        boolean anyMatches = false;
        for (Criterion criterion : criteria) {
            // 　|= 複合代入演算子
            anyMatches |= criterion.matches(answerMatching(criterion));
        }
        return anyMatches;
    }

    private Answer answerMatching(Criterion criterion) {
        return answers.get(criterion.getAnswer().getQuestionText());
    }

    public int score() {
        return score;
    }
}
