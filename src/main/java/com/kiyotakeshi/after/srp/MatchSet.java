package com.kiyotakeshi.after.srp;

import java.util.Map;

/**
 * 条件とプロフィールのマッチング
 */
public class MatchSet {
    private Map<String, Answer> answers;
    private int score = 0;
    private Criteria criteria;

    public MatchSet(Map<String, Answer> answers, Criteria criteria) {
        this.answers = answers;
        this.criteria = criteria;
        calculateScore();
    }

    /**
     * 条件を満たす回答がプロフィールにあるとその条件で指定されるスコアがプロフィールに加算される
     */
    private void calculateScore() {
        for (Criterion criterion : criteria) {
            if (criterion.matches(answerMatching(criterion))) {
                score += criterion.getWeight().getValue();
            }
        }
    }

    private Answer answerMatching(Criterion criterion) {
        return answers.get(criterion.getAnswer().getQuestionText());
    }

    public int getScore() {
        return score;
    }

    public boolean matches() {
        score = new MatchSet(answers, criteria).getScore();

        if (doesNotMeetAnyMustMatchCriterion()) {
            return false;
        }
        return anyMatches();
    }

    /**
     * 必須条件にマッチしないものがあるか
     *
     * @return
     */
    private boolean doesNotMeetAnyMustMatchCriterion() {
        for (Criterion criterion : criteria) {
            boolean match = criterion.matches(answerMatching(criterion));
            if (!match && criterion.getWeight() == Weight.MustMatch) {
                return true;
            }
        }
        return false;
    }

    private boolean anyMatches() {
        boolean anyMatches = false;
        for (Criterion criterion : criteria) {
            // 　|= 複合代入演算子
            anyMatches |= criterion.matches(answerMatching(criterion));
        }
        return anyMatches;
    }
}
