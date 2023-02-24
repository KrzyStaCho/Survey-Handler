package FileHandler;

import Survey.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SurveyReader {
    private String FullPath;
    private File SFile;
    private Scanner FReader;

    public SurveyReader(String fullPath) {
        FullPath = fullPath;
        SFile = new File(fullPath);
        FReader = null;
    }
    public SurveyReader() {
        this(SurveyQuestion.GetFilePath() + "\\" + SurveyQuestion.GetFileName());
    }

    public SurveyQuestion ReadNextQuestion(boolean readAnswers) throws IOException {
        if (!SurveyWriter.CheckFilePath(FullPath)) {
            return null;
        }
        if (FReader == null) {
            FReader = new Scanner(SFile);
        }

        SurveyQuestion surveyQuestion = null;

        //Question - First line
        if (FReader.hasNextLine()) {
            String data = FReader.nextLine();
            surveyQuestion = GetSurveyQuestion(data);

            if (surveyQuestion == null) {
                return null;
            }
        }

        if (FReader.hasNextLine()) {
            String data = FReader.nextLine();

            if (readAnswers) {
                surveyQuestion = GetSurveyAnswer(surveyQuestion, data);
            }
        }

        return surveyQuestion;
    }
    public void CloseFile() {
        FReader.close();
        FReader = null;
    }

    private SurveyQuestion GetSurveyQuestion(String data) {
        String regex = "^\\[\\d+\\-\\b(Single|Multi)\\b\\]*+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);

        if (!matcher.find())
            return null;

        String splitData[] = data.split("]"); // [1] { [20-Single }; [2] { Question? }
        splitData[0] = splitData[0].substring(1); //Remove first '['
        int indexOf = splitData[0].indexOf("-");

        int answered = Integer.parseInt(splitData[0].substring(0, indexOf));
        String SType = splitData[0].substring((indexOf + 1));
        SurveyType type = (SType.equals("Single")) ? SurveyType.SingleAnswer : null;
        type = (SType.equals("Multi")) ? SurveyType.MultiAnswer : type;

        if (type == null) {
            return null;
        }

        String question = splitData[1];

        SurveyQuestion.SetAnswered(answered);
        if (type == SurveyType.SingleAnswer) {
            return new SurveySingle(question);
        } else return new SurveyMulti(question);
    }

    private SurveyQuestion GetSurveyAnswer(SurveyQuestion surveyQuestion, String data) {
        String regex = "^[\\([0-9]+\\)[A-Za-z0-9\\s,.]+;]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(data);

        if (!matcher.find())
            return null;

        String answers[] = data.split("[);]"); // [1] { (5 }; [2] { Answer1 }; ...

        //Remove '(' in 0 2 3 ... array index
        for (int i = 0; i < answers.length; i++) {
            if (i % 2 == 0)
                answers[i] = answers[i].substring(1);
        }

        ArrayList<SurveyAnswer> surveyAnswers = new ArrayList<>();

        //Fill surveyAnswers list
        for (int i = 0; i < answers.length; i++) {
            if (i % 2 == 0) {
                int answered = Integer.parseInt(answers[i]);
                ++i;

                SurveyAnswer answer = new SurveyAnswer(answered, answers[i]);
                surveyAnswers.add(answer);
            }
        }

        SurveyAnswer popularAnswer = SurveyAnswer.FindPopularAnswer(surveyAnswers);
        surveyQuestion.SetAnswers(surveyAnswers, popularAnswer);
        return surveyQuestion;
    }
}

// [20-Single]Question?
// (5)Answer1;(10)Answer2;(5)Answer3;

// !! [];()