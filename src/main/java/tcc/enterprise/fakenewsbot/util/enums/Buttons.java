package tcc.enterprise.fakenewsbot.util.enums;

public enum Buttons {
    SITE("visitar-site","Visitar o nosso site!"), SUPPORT("suporte","Falar com o Suporte");

    private String nome;
    private String text;

    Buttons(String nome, String text){
        this.nome = nome;
        this.text = text;
    }
    public String getNome(){return nome;}

    public String getText(){return text;}
}
