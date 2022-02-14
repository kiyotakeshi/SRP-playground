package com.kiyotakeshi.before;

public class Answer {
    private int i;
    private Question question;

    public Answer(int i, Question question) {
        this.i = i;
        this.question = question;
    }

    public Answer(String matchingValue, Question characteristic) {
        this.i = characteristic.indexOf(matchingValue);
        this.question = characteristic;
    }

    public String getQuestionText(){
        return question.getText();
    }

    public Question getCharacteristic() {
        return question;
    }

    @Override
    public String toString() {
        return String.format("%s %s", question.getText(), question.getAnswerChoices(i));
    }

    public boolean match(int expected){
        return question.match(expected, i);
    }

    public boolean match(Answer otherAnswer){
        return question.match(i, otherAnswer.i);
    }
}
