package com.egyetem.szakdolgozat.ranking.Service;

import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;

public interface RankingService {

    public void updateRank(Tournament tournament);

    String riotBaseUrlBuilder(String baseUrl, String region);
}
