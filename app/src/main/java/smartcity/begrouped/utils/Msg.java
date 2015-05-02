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

   /* public boolean equals(Msg o) {
        return senderId.equals(o.getSenderId())  && text.equals(o.getText());
    }*/

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;

        if(o == null || (this.getClass() != o.getClass())){
            return false;
        }

            Msg msg = (Msg) o;
            return senderId.equals(msg.getSenderId())  && createdAt.toString().equals(getCreatedAt().toString()) && text.equals(msg.getText());
    }
    public int hashCode(){
        return
                senderId.hashCode() *
                createdAt.hashCode() * text.hashCode();
    }

    @Override
    public String toString() {
        return "Msg{" +
                "recipientId='" + recipientId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", createdAt=" + createdAt +
                ", text='" + text + '\'' +
                '}';
    }


}
