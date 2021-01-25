package Chatting.Data;

public class ChatRoomData {
    private String nick;
    private String content;
    private String time;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ChatRoomData(String nick, String content, String time) {
        this.nick = nick;
        this.content = content;
        this.time = time;
    }
}
