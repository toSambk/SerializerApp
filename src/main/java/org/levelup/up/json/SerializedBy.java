package org.levelup.up.json;

public @interface SerializedBy {

    Class <? extends JsonDeserializer> serializer();

    Class <? extends JsonDeserializer> deserializer();

}
