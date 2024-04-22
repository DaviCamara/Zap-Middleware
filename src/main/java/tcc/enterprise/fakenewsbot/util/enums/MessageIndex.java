package tcc.enterprise.fakenewsbot.util.enums;

public enum MessageIndex {

    ONE("visitar-site","Visitar o nosso site"), TWO("suporte","Falar com o Suporte"), NINENINENINE("rede-neural", "4");
private String id;
    private String text;

    MessageIndex(String id, String text){
        this.id = id;
        this.text = text;
    }
    public String getText(){return text;}
    public String getId(){return id;}

}
