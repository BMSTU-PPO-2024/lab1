package com.github.romanqed.devspark;

import com.github.romanqed.devspark.database.Model;

import java.util.List;

@Model("tests")
public class Test {
    public String id;
    public List<String> lst;
}
