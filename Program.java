import FileHandler.SurveyReader;
import FileHandler.SurveyWriter;
import Survey.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public abstract class Program {
    public static void main(String[] args) {

        ProgramState programState = null;
        Scanner scanner = new Scanner(System.in);

        while (programState != ProgramState.EXIT) {
            programState = ShowMenuAndGetChoice(scanner);

            switch(programState) {
                case CREATE:
                    MenuCreate(scanner);
                    break;
                case CLEAR:
                    MenuClear(scanner);
                    break;
                case FILL:
                    MenuFill(scanner);
                    break;
                case EXIT:
                default:
            }
        }
    }

    private static void MenuFill(Scanner scanner) {
        String filePath;

        System.out.println("====================");
        System.out.println("--------Fill--------");

        System.out.print("Podaj sciezke pliku: ");
        filePath = scanner.nextLine();

        //If FilePath is empty
        if (filePath.isEmpty()) {
            System.out.println("Odpowiedz nie moze byc pusta!");
            return;
        }

        //If file exists
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Plik nie istnieje!");
            return;
        }

        ArrayList<SurveyQuestion> questionList = new ArrayList<>();
        SurveyReader SReader = new SurveyReader(filePath);
        SurveyQuestion question;

        //Get all Question from file
        try {
            do {
                question = SReader.ReadNextQuestion(true);

                if (question != null) {
                    questionList.add(question);
                }
            }while (question != null);
        } catch(IOException exception) {
            System.out.println("Wystapil blad: " + exception.getMessage());
            return;
        }

        //Fill all question
        for (SurveyQuestion surveyQuestion : questionList) {
            while (!FillQuestion(scanner, surveyQuestion)) {}
        }

        //Clear file and save question
        try {
            FileWriter FWriter = new FileWriter(filePath, false);
            FWriter.write("");
            FWriter.close();

            SurveyWriter SWriter = new SurveyWriter(filePath);

            for(SurveyQuestion surveyQuestion : questionList) {
                SWriter.SaveQuestion(surveyQuestion);
            }
            SWriter.CloseFile();
        } catch(IOException exception) {
            System.out.println("Wystapil blad: " + exception.getMessage());
            return;
        }
        SurveyQuestion.SetAnswered(SurveyQuestion.GetAnswered() + 1);

        System.out.println("Ankieta zostala wypelniona");
    }

    private static void MenuClear(Scanner scanner) {
        String filePath;

        System.out.println("====================");
        System.out.println("--------CLEAR-------");

        System.out.print("Podaj sciezke pliku: ");
        filePath = scanner.nextLine();

        //If FilePath is empty
        if (filePath.isEmpty()) {
            System.out.println("Odpowiedz nie moze byc pusta!");
            return;
        }

        //If file exists
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Plik nie istnieje!");
            return;
        }

        ArrayList<SurveyQuestion> questionList = new ArrayList<>();
        SurveyReader SReader = new SurveyReader(filePath);
        SurveyQuestion question;

        //Get all Question from file
        try {
            do {
                question = SReader.ReadNextQuestion(true);

                if (question != null) {
                    questionList.add(question);
                }
            }while (question != null);
        } catch(IOException exception) {
            System.out.println("Wystapil blad: " + exception.getMessage());
            return;
        }

        //Reset answered to all Question
        for (SurveyQuestion surveyQuestion : questionList) {
            surveyQuestion.ClearAnswers();
        }

        //Clear file and save question
        try {
            FileWriter FWriter = new FileWriter(filePath, false);
            FWriter.write("");
            FWriter.close();

            SurveyWriter SWriter = new SurveyWriter(filePath);

            for(SurveyQuestion surveyQuestion : questionList) {
                SWriter.SaveQuestion(surveyQuestion);
            }
            SWriter.CloseFile();
        } catch(IOException exception) {
            System.out.println("Wystapil blad: " + exception.getMessage());
            return;
        }

        System.out.println("Pytania zostały wyczyszczone");
    }

    private static void MenuCreate(Scanner scanner) {
        String filePath;

        System.out.println("====================");
        System.out.println("-------CREATE-------");

        System.out.print("Podaj sciezke nowego pliku: ");
        filePath = scanner.nextLine();

        //If FilePath is empty
        if (filePath.isEmpty()) {
            System.out.println("Odpowiedz nie moze byc pusta!");
            return;
        }

        //If file exists
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                SurveyWriter.CreateFile(filePath);
            } catch (IOException exception) {
                System.out.println("Wystapil blad: " + exception.getMessage());
                return;
            }
        }

        boolean isEnd = false;
        int counter = 0;
        ArrayList<SurveyQuestion> questionList = new ArrayList<>();
        while (!isEnd) {
            questionList.add(CreateQuestionWithAnswers(scanner));
            System.out.println("Utworzono pytanie numer " + (++counter));

            isEnd = !GetAnswerYN(scanner, "Czy chcesz dodac kolejne pytanie <Y/N>: ");
        }

        String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
        filePath = filePath.substring(0, (filePath.lastIndexOf("\\")));

        System.out.println("Zapisywanie pytan do pliku...");
        SurveyQuestion.SetData(0, new Date(), fileName, filePath);

        try {
            SurveyWriter SWriter = new SurveyWriter();
            for (SurveyQuestion question : questionList) {
                SWriter.SaveQuestion(question);
            }

            SWriter.CloseFile();
        } catch(IOException exception) {
            System.out.println("Wystapil blad: " + exception.getMessage());
            return;
        }

        System.out.println("Pytania zostaly zapisane.");
    }

    private static boolean FillQuestion(Scanner scanner, SurveyQuestion question) {
        System.out.println("--------------------");
        System.out.println("Pytanie nr " + question.GetNumber() + ":");
        System.out.println("\t" + question.GetQuestion());

        ArrayList<SurveyAnswer> answers = question.GetAnswers();
        int counter = 0;
        for(SurveyAnswer answer : answers) {
            ++counter;
            System.out.println("\tOdpowiedz [" + counter + "]: " + answer.GetAnswer());
        }

        String userChoice;
        if (question.GetType() == SurveyType.SingleAnswer) {
            System.out.print("Podaj numer wybranej przez ciebie odpowiedzi: ");
            userChoice = scanner.nextLine();

            if (!isInteger(userChoice)) {
                System.out.println("Odpowiedz musi byc liczba!");
                return false;
            }
            int correctChoice = Integer.parseInt(userChoice);

            if (correctChoice >= 1 && correctChoice <= counter) {
                answers.get(correctChoice - 1).AddOneAnswered();
            } else {
                System.out.println("Liczba musi nalezec do jednej z odpowiedzi!");
                return false;
            }
        }else {
            System.out.print("Podaj numer wybranej przez ciebie odpowiedzi (Mozesz podac wiecej niz jedna po spacji): ");
            userChoice = scanner.nextLine();

            //Check if each answer is number
            String[] multiChoice = userChoice.split(" ");
            boolean isGood = true;
            for (String choice : multiChoice) {
                if (!isInteger(choice))
                    isGood = false;
            }
            if (!isGood) {
                System.out.println("Odpowiedzi musza byc liczbami!");
                return false;
            }

            //Parse into integer each answer
            int[] correctChoice = new int[multiChoice.length];
            for (int i = 0; i < multiChoice.length; i++) {
                correctChoice[i] = Integer.parseInt(multiChoice[i]);
            }

            for (int choice : correctChoice) {
                if (!(choice >= 1 && choice <= counter))
                    isGood = false;
            }
            if (!isGood) {
                System.out.println("Kazda podana liczba musi byc liczba przypisana do odpowiedzi!");
                return false;
            }

            for (int choice : correctChoice) {
                answers.get(choice - 1).AddOneAnswered();
            }
        }
        return true;
    }

    private static SurveyQuestion CreateQuestionWithAnswers(Scanner scanner) {
        String question;

        System.out.println("--------------------");
        System.out.print("Podaj pytanie: ");
        question = scanner.nextLine();

        String userChoice;
        int numberOfAnswers = 0;
        while (numberOfAnswers <= 0 || numberOfAnswers > 5) {
            System.out.print("Podaj ile odpowiedzi chcesz zamiescic <1-5>: ");
            userChoice = scanner.nextLine();

            if (!isInteger(userChoice)) {
                System.out.println("Odpowiedz musi byc liczba!");
                continue;
            }
            numberOfAnswers = Integer.parseInt(userChoice);
            if (numberOfAnswers <= 0 || numberOfAnswers > 5) {
                System.out.println("Odpowiedz musi sie miescic w przedziale <1-5>!");
            }
        }

        SurveyType surveyType = null;
        boolean userAnswer = GetAnswerYN(scanner, "Czy na pytanie mozna odpowiedziec tylko 1 wyborem? <Y/N>: ");
        if (userAnswer)
            surveyType = SurveyType.SingleAnswer;
        else
            surveyType = SurveyType.MultiAnswer;

        ArrayList<SurveyAnswer> answerList = new ArrayList<>();
        SurveyAnswer answer = null;
        String answerText;
        for (int i = 0; i < numberOfAnswers; i++) {
            while (answer == null) {
                System.out.print("Podaj Odpowiedz nr " + (i + 1) + ": ");
                answerText = scanner.nextLine();

                if (answerText.isEmpty()) {
                    System.out.println("Odpowiedz nie moze byc pusta!");
                    continue;
                }

                answer = new SurveyAnswer(answerText);
            }
            answerList.add(answer);
            answer = null;
        }

        SurveyAnswer popular = SurveyAnswer.FindPopularAnswer(answerList);
        if (surveyType == SurveyType.SingleAnswer) {
            return new SurveySingle(question, answerList, popular);
        } else {
            return new SurveyMulti(question, answerList, popular);
        }
    }

    private static ProgramState ShowMenuAndGetChoice(Scanner scanner) {
        String userChoice;
        int correctChoice;

        System.out.println("====================");
        System.out.println("--------Menu--------");
        System.out.println("1. Utworz ankiete");
        System.out.println("2. Wyczysc ankiete");
        System.out.println("3. Wypelnij ankiete");
        System.out.println("4. Zamknij program");
        System.out.println("====================");

        System.out.print("Podaj numer wyboru: ");
        userChoice = scanner.nextLine();

        //If userChoice is empty
        if (userChoice.isEmpty()) {
            System.out.println("Odpowiedz nie moze byc pusta!");
            return ProgramState.INVALID;
        }

        //If userChoice doesn't have valid number
        if (isInteger(userChoice)) {
            correctChoice = Integer.parseInt(userChoice);
        } else {
            System.out.println("Odpowiedz musi byc liczba!");
            return ProgramState.INVALID;
        }

        switch(correctChoice) {
            case 1:
                return ProgramState.CREATE;
            case 2:
                return ProgramState.CLEAR;
            case 3:
                return ProgramState.FILL;
            case 4:
                return ProgramState.EXIT;
            default:
                System.out.println("Numer musi odpowiadac jednemu z podanych wyborow!");
                return ProgramState.INVALID;
        }
    }

    private static boolean GetAnswerYN(Scanner scanner, String text) {
        while (true) {
            System.out.print(text);
            String userChoice = scanner.nextLine();

            if (userChoice.isEmpty()) {
                System.out.println("Odpowiedz nie moze byc pusta!");
                continue;
            }

            if (userChoice.equals("Y") || userChoice.equals("y"))
                return true;
            if (userChoice.equals("N") || userChoice.equals("n"))
                return false;
            System.out.println("Odpowiedz musi byc 'Y' albo 'N'!");
        }
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private enum ProgramState {
        CREATE, CLEAR, FILL, EXIT, INVALID
    }
}
