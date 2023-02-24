package Survey;

import java.util.ArrayList;

public class SurveySingle extends SurveyQuestion {
    public SurveySingle(String question, ArrayList<SurveyAnswer> answers, SurveyAnswer popularAnswer) {
        super(SurveyType.SingleAnswer, question, answers, popularAnswer);
    }
    public SurveySingle(String question) {
        super(SurveyType.SingleAnswer, question);
    }
}
