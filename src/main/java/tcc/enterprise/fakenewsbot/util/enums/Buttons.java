package tcc.enterprise.fakenewsbot.util.enums;

public enum Buttons {
    SITE("site","MediaGuard.com.br"), SUPPORT("suporte","MediaGuard@gmail.com");

    private String nome;
    private String text;

    Buttons(String nome, String text){
        this.nome = nome;
        this.text = text;
    }
    public String getNome(){return nome;}

    public String getText(){return text;}
}
