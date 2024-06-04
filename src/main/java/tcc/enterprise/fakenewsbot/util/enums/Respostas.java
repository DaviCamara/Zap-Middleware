package tcc.enterprise.fakenewsbot.util.enums;

public enum Respostas {
    SITE("Show!, segue o link do nosso site: http://MediaGuard.com.br"), SUPPORT("Massa! manda um email para gente na página http://MediaGuard.com.br ou no MediaGuard@gmail.com"), AGUARDE("Estamos processando seu audio, por gentileza aguarde! Evite utilizar o microfone padrão do seu celular para enviar áudios a serem verificados pois isso pode afetar a performance"), REDE_NEURAL("Percentual de acerto: %.2f%%");

    private String text;

    Respostas( String text){
        this.text = text;
    }
    public String getText(){return text;}

}
