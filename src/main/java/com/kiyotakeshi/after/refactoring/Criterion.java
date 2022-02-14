package com.kiyotakeshi.after.refactoring;

// 雇用者が求職者に、求職者が雇用者に対して求めている条件
public class Criterion implements Scoreable {

    private Answer answer;
    private Weight weight;
    private int score;

    public Criterion(Answer answer, Weight weight) {
        this.answer = answer;
        this.weight = weight;
    }

    public Answer getAnswer() {
        return answer;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int getScore() {
        return 0;
    }

    public boolean matches(Answer answer) {
        return getWeight() == Weight.DontCare || answer.match(getAnswer());
    }
}
