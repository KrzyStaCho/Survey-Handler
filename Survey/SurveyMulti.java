package Survey;

import java.util.ArrayList;

public class SurveyMulti extends SurveyQuestion {
    public SurveyMulti(String question, ArrayList<SurveyAnswer> answers, SurveyAnswer popularAnswer) {
        super(SurveyType.MultiAnswer, question, answers, popularAnswer);
    }
    public SurveyMulti(String question) {
        super(SurveyType.MultiAnswer, question);
    }
}
