package com.example.emailbackserver.EmailModel;

import java.util.Arrays;

public class Message implements Cloneable{
    private String id;
    private String from;
    private String[] to;
    private String subject;
    private String attachment;///////////////////////need to search
    private String body;
    private String date;
    private boolean starred;
    private boolean important;
    private boolean draft;
    private boolean custom;
    private boolean read;
    private Long deleteDate;

    public Long getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Long deleteDate) {
        this.deleteDate = deleteDate;
    }

    public Message(String iD, String from, String[] to, String subject
                    , String attachment, String body, String date) {
        this.id = iD;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.attachment = attachment;
        this.body = body;
        this.date = date;
        this.starred = false;
        this.important = false;
        this.read = false;
        this.draft = false;
        this.custom = false;
    }



    public Message() {
    }

    public String getiD() {
        return id;
    }
    public String getFrom() {
        return from;
    }
    public String[] getTo() {
        return to;
    }
    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
    public String getAttachment() {return attachment;}
    public String getDate() {
        return date;
    }

    public boolean isStarred() {
        return starred;
    }
    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public boolean isImportant() {
        return important;
    }
    public void setImportant(boolean important) {
        this.important = important;
    }

    public boolean isRead() {
        return read;
    }
    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isDraft() {return draft;}
    public void setDraft(boolean draft) {this.draft = draft;}

    public boolean isCustom() {return custom;}
    public void setCustom(boolean custom) {this.custom = custom;}

    public void setFrom(String from) {
        this.from = from;}

    public void setTo(String[] to) {
        this.to = to;
    }

    @Override
    public Message clone() throws CloneNotSupportedException {
            Message message;
            message = (Message) super.clone();
            return message;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof Message)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Message m = (Message) obj;

        // Compare the data members and return accordingly
        return this.id.equals(m.getiD()) && this.from.equals(m.from) && Arrays.equals(this.to, m.to) && this.subject.equals(m.subject) &&
                this.body.equals(m.body) && this.attachment.equals(m.attachment) ;
//                && this.read == m.read &&
//                this.starred == m.starred && this.draft == m.draft && this.custom == m.custom && this.important == m.important
//                && this.date.equals(m.date);
    }

}
