package edu.northeastern.cs5500.starterbot.model;

import org.bson.types.ObjectId;

import lombok.Data;

@Data
public class UpVoteDocumentResponse implements Model{


    private String ticker;
    private int votes;
    private ObjectId _id;

    public UpVoteDocumentResponse(ObjectId _id, String ticker, int votes) {
        this._id = _id;
        this.ticker = ticker;
        this.votes = votes;
    }

    // getters and setters

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Override
    public ObjectId getId() {
        return _id;
    }

    @Override
    public void setId(ObjectId id) {
        this._id = id;
    }
}