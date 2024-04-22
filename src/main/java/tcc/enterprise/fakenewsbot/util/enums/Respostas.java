package tcc.enterprise.fakenewsbot.util.enums;

public enum Respostas {
    SITE("Show!, segue o link do nosso site: http://MediaGuard.com.br"), SUPPORT("Massa! manda um email para gente na página http://MediaGuard.com.br ou no MediaGuard@gmail.com");

    private String text;

    Respostas( String text){
        this.text = text;
    }
    public String getText(){return text;}

}
