package com.example.jtechstack.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pair<T, U> {
    private T left;

    private U right;
}
