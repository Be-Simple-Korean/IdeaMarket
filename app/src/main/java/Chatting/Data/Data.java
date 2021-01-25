package Chatting.Data;

public class Data {
    private String nick;
    private String content;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Data(String nick, String content) {
        this.nick = nick;
        this.content = content;
    }
}
