package tcc.enterprise.fakenewsbot.util.enums;

public enum MessageIndex {

    ONE("visitar-site"), TWO("suporte"), NINENINEEIGTH("aguarde"), NINENINENINE("rede-neural");
    private String id;

    MessageIndex(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
