package sample;

public class Prescription {

    private String name;
    private String strength;
    private String dose;
    private String duration;
    private String advice;

    public Prescription(String name, String strength, String dose, String duration, String advice) {
        this.name = name;
        this.strength = strength;
        this.dose = dose;
        this.duration = duration;
        this.advice = advice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String toString() {
        return "Name: " + name + ", Strength: " + strength + ", Dose: " + dose + ", Duration: " + duration + ", Advice: " + advice;
    }
}
