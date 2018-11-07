package com.imagehashing.search.vision.dataModels;

import java.util.ArrayList;
import java.util.BitSet;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class Image {

    @Id
    public long id;
    @Transient
    private BitSet bits;
    private String name;
    private String LSHHashKey;
    private String semanticHashKey;
    private int width;
    private int height;
    public ToMany<Pattern> patterns;

    public Image() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLSHHashKey() {
        return LSHHashKey;
    }

    public void setLSHHashKey(String LSHHashKey) {
        this.LSHHashKey = LSHHashKey;
    }

    public String getSemanticHashKey() {
        return semanticHashKey;
    }

    public void setSemanticHashKey(String semanticHashKey) {
        this.semanticHashKey = semanticHashKey;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BitSet getBits() {
        return bits;
    }

    public void setBits(BitSet bits) {
        this.bits = bits;
    }
}
