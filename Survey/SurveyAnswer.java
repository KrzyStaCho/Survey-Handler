package Survey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SurveyAnswer {
    private int Answered;
    private String Answer;

    public SurveyAnswer(int answered, String answer) {
        Answered = answered;
        Answer = answer;
    }
    public SurveyAnswer(String answer) {
        Answered = 0;
        Answer = answer;
    }

    public static void ResetAnswered(ArrayList<SurveyAnswer> list) {
        list.forEach((n) -> n.ResetAnswered());
    }
    public static void AddOneAnswered(ArrayList<SurveyAnswer> list) {
        list.forEach((n) -> n.AddOneAnswered());
    }
    public static SurveyAnswer FindPopularAnswer(ArrayList<SurveyAnswer> list) {
        Comparator<SurveyAnswer> comp = (o1, o2) -> {
            if (o1.Answered > o2.Answered)
                return 1;
            if (o1.Answered == o2.Answered)
                return 0;
            return -1;
        };

        return Collections.max(list, comp);
    }

    public void ResetAnswered() {
        Answered = 0;
    }
    public void AddOneAnswered() {
        ++Answered;
    }
    public int GetAnswered() {
        return Answered;
    }
    public String GetAnswer() {
        return Answer;
    }
}
