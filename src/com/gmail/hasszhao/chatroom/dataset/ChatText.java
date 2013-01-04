/**
 * 
 */
package com.gmail.hasszhao.chatroom.dataset;

/**
 * DataSet represents a line of chat history from server
 * 
 */
public final class ChatText {
    private String Sender;
    private String Text;

    public ChatText( String _sender, String _text ) {
        super();
        Sender = _sender;
        Text = _text;
    }

    public String getSender() {
        return Sender;
    }

    public String getText() {
        return Text;
    }
}
