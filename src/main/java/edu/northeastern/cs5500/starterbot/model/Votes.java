package edu.northeastern.cs5500.starterbot.model;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

public class Votes implements Model {
    private ObjectId _id;
    @Getter @Setter private String ticker;
    @Getter @Setter private int votes;
    @Getter @Setter private ArrayList<String> voters;

    public Votes() {}

    @Override
    public ObjectId getId() {
        return _id;
    }

    @Override
    public void setId(ObjectId id) {
        _id = id;
    }
}
