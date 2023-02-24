package Survey;

import java.util.ArrayList;
import java.util.Date;

public abstract class SurveyQuestion implements ISurveyReader {
    private static int LastNumber;
    private static int Answered;
    private static Date CreateDate;
    private static String FileName;
    private static String FilePath;

    protected int Number;
    protected SurveyType Type;
    protected String Question;
    protected ArrayList<SurveyAnswer> Answers;
    protected SurveyAnswer PopularAnswer;

    public static void SetData(int answered, Date createDate, String fileName, String filePath) {
        LastNumber = 0;
        Answered = answered;
        CreateDate = createDate;
        FileName = fileName;
        FilePath = filePath;
    }

    public static void SetAnswered(int answered) {
        Answered = answered;
    }
    public static int GetAnswered() {
        return Answered;
    }
    public static Date GetCreateDate() {
        return CreateDate;
    }
    public static String GetFileName() {
        return FileName;
    }
    public static String GetFilePath() {
        return FilePath;
    }

    protected static int GetNextNumber() {
        return ++LastNumber;
    }

    protected SurveyQuestion(SurveyType type, String question, ArrayList<SurveyAnswer> answers, SurveyAnswer popularAnswer) {
        Number = GetNextNumber();
        Type = type;
        Question = question;
        Answers = answers;
        PopularAnswer = popularAnswer;
    }
    protected SurveyQuestion(SurveyType type, String question) {
        this(type, question, null, null);
    }

    public boolean ClearAnswers() {
        if (Answers == null) return false;
        Answered = 0;
        SurveyAnswer.ResetAnswered(Answers);
        return true;
    }

    public void SetAnswers(ArrayList<SurveyAnswer> list, SurveyAnswer popularAnswer) {
        Answers = list;
        PopularAnswer = popularAnswer;
    }

    public int GetNumber() {
        return Number;
    }
    public SurveyType GetType() {
        return Type;
    }
    public String GetQuestion() {
        return Question;
    }
    public ArrayList<SurveyAnswer> GetAnswers() {
        return Answers;
    }
    public SurveyAnswer GetPopularAnswer() {
        return PopularAnswer;
    }
}
