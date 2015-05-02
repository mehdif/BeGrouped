package smartcity.begrouped.utils;

import java.util.Date;
/**
 * Created by a on 02/05/2015.
 */
public class Msg {


    private String recipientId;
    private String senderId;
    private java.util.Date createdAt;
    private String text;

    public Msg(String recipientId, String senderId, Date createdAt,String text) {
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.createdAt=createdAt;
        this.text=text;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public String getSenderId() {
        return senderId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }
}
