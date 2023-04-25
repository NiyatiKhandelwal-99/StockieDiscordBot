package edu.northeastern.cs5500.starterbot.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Votes {

    @SerializedName("_id")
    private final ObjectId _id;

    @SerializedName("ticker")
    private final String ticker;

    @SerializedName("votes")
    private final Integer votes;

    @SerializedName("voters")
    private final String array[];
}
