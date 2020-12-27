package com.robpercival.workey;

public class ChatMessage {
    private String chatText;

    public ChatMessage(String chatText) {
        this.chatText = chatText;
    }

    public String getChatText() {
        return chatText;
    }

    public void setChatText(String chatText) {
        this.chatText = chatText;
    }

    public ChatMessage() {
    }
}
