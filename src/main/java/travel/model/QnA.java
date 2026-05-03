package travel.model;

public class QnA {
    
    private int questionID;
    private int customerID;
    private int employeeID;
    private String question;
    private String answer;

    public QnA() { }

    public QnA(int questionID, int customerID, int employeeID, String question, String answer) {
        this.questionID = questionID;
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.question = question;
        this.answer = answer;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
