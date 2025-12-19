package GiaoDien;

public class Question {

    public String content;
    public String a;
    public String b;
    public String c;
    public String d;
    public String correct; // A, B, C, D

    public Question(String content, String a, String b,
                    String c, String d, String correct) {
        this.content = content;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.correct = correct;
    }
    }