package tcc.enterprise.fakenewsbot.util.enums;

public enum Respostas {
    SITE("Show!, segue o link do nosso site: https://joaovluna.github.io/mediaguard/"), SUPPORT("Massa! manda um email para gente na página https://joaovluna.github.io/mediaguard/ ou no emaile mediaguard.suporte@gmail.com"), AGUARDE("Estamos processando seu audio, por gentileza aguarde! Evite utilizar o microfone padrão do seu celular para enviar áudios a serem verificados pois isso pode afetar a performance"), REDE_NEURAL("Percentual de acerto: %.2f%%");

    private String text;

    Respostas( String text){
        this.text = text;
    }
    public String getText(){return text;}

}
