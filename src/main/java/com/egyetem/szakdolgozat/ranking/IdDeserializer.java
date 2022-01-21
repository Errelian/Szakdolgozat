package com.egyetem.szakdolgozat.ranking;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IdDeserializer {

    private String id;

    private String name;

    public IdDeserializer(String id) {
        this.id = id;
    }

    public IdDeserializer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public IdDeserializer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "IdDeserializer{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            '}';
    }
}
