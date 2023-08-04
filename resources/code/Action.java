import java.util.ArrayList;

public class Action {

//    do string comparison --> dot.contains
//
//    // use set more preferable
//    list/set triggers
//            list of subjects
//    list of comsued
//    list produced
            // check whether the trigger word is there --> check the subject
    // if the conditions are checked --> produces and consumed + narration
    private ArrayList<String> subjects = new ArrayList<>();
    private ArrayList<String> consumed = new ArrayList<>();
    private ArrayList<String> produced = new ArrayList<>();
    private String narration;

    //--------------------------- GETTER ---------------------------//
    public ArrayList<String> getSubjects() {
        return subjects;
    }

    public ArrayList<String> getConsumed() {
        return consumed;
    }

    public ArrayList<String> getProduced() {
        return produced;
    }

    public String getNarration() {
        return narration;
    }

    //--------------------------- SETTERS ---------------------------//
    public void setNarration(String narration) {
        this.narration = narration;
    }

    //--------------------------- ADDERS ---------------------------//
    public void addSubjects(String subject) {
        subjects.add(subject);
    }

    public void addConsumed(String consume) {
        consumed.add(consume);
    }

    public void addProduced(String produce) {
        produced.add(produce);
    }

    //--------------------------- CHECKS ---------------------------//
    public boolean checkIfContainsSubject(Action action, String subjectName) {
        ArrayList<String> subjectList = action.getSubjects();
        for(String subject : subjectList) {
            if(subjectName.equals(subject.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
