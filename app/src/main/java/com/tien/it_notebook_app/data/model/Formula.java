package com.tien.it_notebook_app.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "formulas",
    foreignKeys = @ForeignKey(
        entity = Topic.class,
        parentColumns = "id",
        childColumns = "topicId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("topicId")}
)
public class Formula {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private int topicId;

    private String content;

    private String explanation;

    private String examples;

    private String[] tags;

    private boolean isFavorite;

    private long createdAt;

    private long updatedAt;

    private long lastViewedAt;

    public Formula(String title, int topicId, String content, String explanation,
                   String examples, String[] tags, boolean isFavorite,
                   long createdAt, long updatedAt, long lastViewedAt) {
        this.title = title;
        this.topicId = topicId;
        this.content = content;
        this.explanation = explanation;
        this.examples = examples;
        this.tags = tags;
        this.isFavorite = isFavorite;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastViewedAt = lastViewedAt;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getLastViewedAt() {
        return lastViewedAt;
    }

    public void setLastViewedAt(long lastViewedAt) {
        this.lastViewedAt = lastViewedAt;
    }
}
