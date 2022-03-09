package com.egyetem.szakdolgozat.ranking.serialization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RankDeserializer {

    private String tier;

    private String summonerName;

    public RankDeserializer(String tier, String summonerName) {
        this.tier = tier;
        this.summonerName = summonerName;
    }

    public RankDeserializer() {
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getSummonerName() {
        return summonerName;
    }

    public void setSummonerName(String summonerName) {
        this.summonerName = summonerName;
    }

    @Override
    public String toString() {
        return "RankDeserializer{" +
            "tier='" + tier + '\'' +
            ", summonerName='" + summonerName + '\'' +
            '}';
    }
}
