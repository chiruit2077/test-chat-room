/**
 * 
 */
package com.gmail.hasszhao.chatroom.dataset;

/**
 * DataSet represents a lines of chat history from server
 * 
 */
public final class ChatTexts {
    private ChatText[] Texts;

    public ChatTexts( ChatText[] _texts ) {
        super();
        Texts = _texts;
    }

    public ChatText[] getTexts() {
        return Texts;
    }

}
