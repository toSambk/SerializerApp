package org.levelup.up.json;

public interface JsonDeserializer <T> {

    T deserialize (String json);

}
