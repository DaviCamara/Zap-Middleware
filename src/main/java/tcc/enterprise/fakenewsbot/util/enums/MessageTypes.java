package tcc.enterprise.fakenewsbot.util.enums;

public enum MessageTypes {

    TEXT("text"), AUDIO("audio"), IMAGE("imagem"), INTERACTIVE("interactive"),
    DOCUMENT("document"), VIDEO("video");

    private String description;

    MessageTypes(String description){
        this.description = description;
    }
    public String getDescription(){return description;}

}
