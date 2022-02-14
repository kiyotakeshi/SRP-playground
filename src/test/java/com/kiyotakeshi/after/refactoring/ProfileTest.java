package com.kiyotakeshi.after.refactoring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileTest {

    private Profile profile;
    private Profile profile2;
    private ProfilePool profilePool;

    private Question questionBonus;
    private Answer answerYesBonus;
    private Answer answerNoBonus;

    private Question questionRelocation;
    private Answer answerYesRelocation;
    private Answer answerNoRelocation;

    private Question questionLongVacation;
    private Answer answerYesLongVacation;
    private Answer answerNoLongVacation;

    private Criteria criteria;

    @BeforeEach
    void createProfile() {
        profilePool = new ProfilePool();
        profile = new Profile("Mike popcorn, Inc");
        profile2 = new Profile("Kendrick West, Inc");
    }

    @BeforeEach
    void createCriteria() {
        criteria = new Criteria();
    }

    @BeforeEach
    void createQuestionAndAnswer() {
        questionBonus = new BooleanQuestion(1, "has Bonus?");
        answerYesBonus = new Answer(Bool.TRUE, questionBonus);
        answerNoBonus = new Answer(Bool.FALSE, questionBonus);

        questionRelocation = new BooleanQuestion(1, "has relocation?");
        answerYesRelocation = new Answer(Bool.TRUE, questionRelocation);
        answerNoRelocation = new Answer(Bool.FALSE, questionRelocation);

        questionLongVacation = new BooleanQuestion(1, "can take long vacation?");
        answerYesLongVacation = new Answer(Bool.TRUE,questionLongVacation);
        answerNoLongVacation = new Answer(Bool.FALSE, questionLongVacation);
    }

    @Test
    @DisplayName("必須条件にマッチしない場合、 matches は false を返す")
    void matchAnswersFalseWhenMustMatchCriteriaNotMet() {
        // set up(arrange)
        profile.add(answerNoBonus);
        criteria.add(
                new Criterion(answerYesBonus, Weight.MustMatch));

        // exercise(act)
        boolean matches = profile.matches(criteria);

        // verify(assert)
        Assertions.assertFalse(matches);
    }

    @Test
    @DisplayName("不問の条件がある場合、 matches は true を返す")
    void matchAnswersTrueForAnyDontCareCriteria() {
        // set up(arrange)
        profile.add(answerNoBonus);
        criteria.add(
                new Criterion(answerYesBonus, Weight.DontCare));

        // exercise(act)
        boolean matches = profile.matches(criteria);

        // verify(assert)
        Assertions.assertTrue(matches);
    }

    @Test
    public void matchAnswersTrueWhenAnyOfMultipleCriteriaMatch() {
        // set up(arrange)
        profile.add(answerYesRelocation);
        profile.add(answerNoLongVacation);
        criteria.add(new Criterion(answerYesRelocation, Weight.Important));
        criteria.add(new Criterion(answerYesLongVacation, Weight.Important));

        // exercise(act)
        boolean matches = profile.matches(criteria);

        // verify(assert)
        Assertions.assertTrue(matches);
    }

    @Test
    public void matchAnswersFalseWhenNoneOfMultipleCriteriaMatch() {
        profile.add(answerNoRelocation);
        profile.add(answerNoLongVacation);
        criteria.add(new Criterion(answerYesRelocation, Weight.Important));
        criteria.add(new Criterion(answerYesLongVacation, Weight.Important));

        boolean matches = profile.matches(criteria);

        Assertions.assertFalse(matches);
    }

    private Criteria generateCriteria(Question question, int value, Weight weight) {
        Criteria criteria = new Criteria();
        criteria.add(new Criterion(new Answer(value, question), weight));
        return criteria;
    }

    @Test
    void answersResultsInScoredOrder() {
        // set up(arrange)
        profile.add(answerNoRelocation);
        profilePool.add(profile);
        profile2.add(answerYesRelocation);
        profilePool.add(profile2);

        profilePool.score(generateCriteria(questionRelocation, Bool.TRUE, Weight.Important));

        // exercise(act)
        List<Profile> ranked = profilePool.ranked();

        // verify(assert)
        assertThat(ranked.toArray()).isEqualTo(new Profile[]{profile2, profile});
    }
}
