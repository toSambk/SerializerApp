package org.levelup.up.json;

//  {
//  "name": "Cat",
//  "age":  12
//  }


public interface JsonSerializer <T> {

    String serialize (T object);
}
