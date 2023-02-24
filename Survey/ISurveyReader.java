package Survey;

import java.util.ArrayList;

public interface ISurveyReader {
    int GetNumber();
    SurveyType GetType();
    String GetQuestion();
    ArrayList<SurveyAnswer> GetAnswers();
    SurveyAnswer GetPopularAnswer();
}
