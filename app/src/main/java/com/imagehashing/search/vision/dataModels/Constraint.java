package com.imagehashing.search.vision.dataModels;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Constraint {
    @Id
    public long id;

    private boolean isSimilar;
    private ToOne<Image> firstImage;
    private ToOne<Image> secondImage;

    public Constraint() {
    }

    public boolean isSimilar() {
        return isSimilar;
    }

    public void setSimilar(boolean similar) {
        isSimilar = similar;
    }

    public ToOne<Image> getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(ToOne<Image> firstImage) {
        this.firstImage = firstImage;
    }

    public ToOne<Image> getSecondImage() {
        return secondImage;
    }

    public void setSecondImage(ToOne<Image> secondImage) {
        this.secondImage = secondImage;
    }
}
