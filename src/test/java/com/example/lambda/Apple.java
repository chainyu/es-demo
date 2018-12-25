package com.example.lambda;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@Getter
@Setter
public class Apple {

    private String name;

    private Integer weight;

    private Integer inv;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getInv() {
        return inv;
    }

    public void setInv(Integer inv) {
        this.inv = inv;
    }

    public static void main(String[] args) {
        List<Apple> apples = new ArrayList<>();
        apples.sort((a1,a2) -> {return a1.getWeight().compareTo(a2.getWeight());});
        apples.sort(Comparator.comparing(Apple :: getWeight));

    }
}

