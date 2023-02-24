package FileHandler;

import Survey.SurveyAnswer;
import Survey.SurveyQuestion;
import Survey.SurveyType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SurveyWriter {
    private String FullPath;
    private FileWriter FWriter;

    public SurveyWriter(String fullPath) throws IOException {
        FullPath = fullPath;

        if (CheckFilePath(FullPath)) {
            FWriter = new FileWriter(FullPath, true);
        } else throw new IOException("File doesn't exists");
    }
    public SurveyWriter() throws IOException {
        this(SurveyQuestion.GetFilePath() + "\\" + SurveyQuestion.GetFileName());
    }

    public static boolean CheckFilePath(String path) {
        File file = new File(path);

        if (!file.exists())
            return false;
        if (!file.isFile())
            return false;
        if (!file.canWrite())
            return false;
        if (!file.canRead())
            return false;
        return true;
    }
    public static boolean CreateFile(String path) throws IOException {
        File file = new File(path);
        return file.createNewFile();
    }
    public static boolean DeleteFile(String path) {
        File file = new File(path);
        if (CheckFilePath(path))
            return file.delete();
        return false;
    }

    public void SaveQuestion(SurveyQuestion sQuestion) throws IOException {
        String question = "[" + SurveyQuestion.GetAnswered() + "-";
        question += (sQuestion.GetType() == SurveyType.SingleAnswer) ? "Single]" : "Multi]";
        question += sQuestion.GetQuestion() + "\n";

        ArrayList<SurveyAnswer> list = sQuestion.GetAnswers();
        if (!list.isEmpty()) {
            for (SurveyAnswer answer : list) {
                String sAnswer = "(" + answer.GetAnswered() + ")" + answer.GetAnswer() + ";";
                question += sAnswer;
            }
        }
        question += "\n";

        FWriter.write(question);
    }

    public boolean RenameFile(String newPath) {
        File file = new File(FullPath);
        File newFile = new File(newPath);
        if (CheckFilePath(FullPath) && !newFile.exists()) {
            file.renameTo(newFile);
            FullPath = newPath;
        } return false;
    }
    public void CloseFile() throws IOException {
        FWriter.close();
    }
}


// [20-Single]Question?
// (5)Answer1;(10)Answer2;(5)Answer3;