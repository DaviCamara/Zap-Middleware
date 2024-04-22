package tcc.enterprise.fakenewsbot.util.enums;

public enum MessageIndex {

    ONE("Visitar o nosso site"), TWO("Falar com o Suporte");

    private String text;

    MessageIndex(String text){
        this.text = text;
    }
    public String getText(){return text;}

}
