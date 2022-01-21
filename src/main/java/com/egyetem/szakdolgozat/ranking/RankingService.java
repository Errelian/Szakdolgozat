package com.egyetem.szakdolgozat.ranking;

import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournament.persistance.TournamentRepository;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

@Service
public class RankingService {

    private static final String BASE_URL = "https://%s.api.riotgames.com/lol";
    private static final String SUMMONER_URL = "/summoner/v4/summoners/by-name/%s";
    private static final String RANK_URL = "/league/v4/entries/by-summoner/%s";
    private static final String RIOT_TOKEN = System.getenv("RIOT_TOKEN");



    TournamentRepository tournamentRepository;

    public RankingService(TournamentRepository tournamentRepository){
        this.tournamentRepository = tournamentRepository;
    }


    @Async("asyncExecutor")
    public void updateRank(Tournament tournament) {

        tournament.setUpdating(true);
        tournamentRepository.save(tournament);

        String url = riotBaseUrlBuilder(BASE_URL, tournament.getRegionId());

        final WebClient webClient = WebClient
            .builder()
            .baseUrl(url)
            .defaultHeader("X-Riot-Token", RIOT_TOKEN)
            .build();

        List<RegionalAccount> accounts = new ArrayList<>();

        for (TournamentToTeams tournamentToTeams : tournament.getTeams()) {
            for (SiteUser user : tournamentToTeams.getTeam().getTeamMembers()) {
                accounts.add(user.getRegionalAccountByRegion(tournament.getRegionId()).get());
            }
        }


        List<IdDeserializer> ids = Flux.fromIterable(accounts)
            .flatMap(account -> webClient.get().uri(String.format(SUMMONER_URL, account.getInGameName()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(IdDeserializer.class))
            .collectList()
            .block();
        System.out.println(ids);

        if (ids != null) {
            List<List<RankDeserializer>> rankings = Flux.fromIterable(ids)
                .flatMap(idDeserializer -> webClient.get().uri(String.format(RANK_URL, idDeserializer.getId()))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<RankDeserializer>>() {
                    }))
                .collectList()
                .block();
            System.out.println(rankings);
        }


        tournament.setUpdating(false);
        tournamentRepository.save(tournament);
    }

    @Async("asyncExecutor")
    public String riotBaseUrlBuilder(String baseUrl, String region) {
        if ("KR".equals(region) || "RU".equals(region)) {
            return new Formatter().format(baseUrl, region).toString();
        }
        System.out.println(new Formatter().format(baseUrl, region.concat("1")).toString());
        return new Formatter().format(baseUrl, region.concat("1")).toString();
    }

}
