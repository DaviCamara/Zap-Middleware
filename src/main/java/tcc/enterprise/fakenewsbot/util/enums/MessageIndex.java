package tcc.enterprise.fakenewsbot.util.enums;

public enum MessageIndex {

    ONE("text"), TWO("audio"), THREE("image"), FOUR("4");

    private String text;

    MessageIndex(String text){
        this.text = text;
    }
    public String getText(){return text;}

}
