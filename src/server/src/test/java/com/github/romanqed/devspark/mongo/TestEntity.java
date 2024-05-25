package com.github.romanqed.devspark.mongo;

import java.util.List;
import java.util.Set;

public final class TestEntity {
    private String id;
    private int i;
    private double d;
    private char c;
    private List<String> l;
    private Set<String> s;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public List<String> getL() {
        return l;
    }

    public void setL(List<String> l) {
        this.l = l;
    }

    public Set<String> getS() {
        return s;
    }

    public void setS(Set<String> s) {
        this.s = s;
    }
}
