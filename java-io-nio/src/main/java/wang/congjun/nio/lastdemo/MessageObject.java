package wang.congjun.nio.lastdemo;

import lombok.Data;

@Data
public class MessageObject {
    private String data;
    private String name;
    private boolean isReady;

    public MessageObject(String name) {
        this.name = name;
    }

    public MessageObject() {}
}
