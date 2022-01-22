package com.egyetem.szakdolgozat.ranking;

import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccountRepository;
import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournament.persistance.TournamentRepository;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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
    RegionalAccountRepository regionalAccountRepository;

    public RankingService(TournamentRepository tournamentRepository,
                          RegionalAccountRepository regionalAccountRepository) {
        this.tournamentRepository = tournamentRepository;
        this.regionalAccountRepository = regionalAccountRepository;
    }


    @Async("asyncExecutor")
    public void updateRank(Tournament tournament) throws ResourceNotFoundException {

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

        List<List<RankDeserializer>> rankings = new ArrayList<>();

        if (ids != null) {
            rankings = Flux.fromIterable(ids)
                .flatMap(idDeserializer -> webClient.get().uri(String.format(RANK_URL, idDeserializer.getId()))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<RankDeserializer>>() {
                    }))
                .collectList()
                .block();
            System.out.println(rankings);
        }

        if (rankings != null) {
            for (List<RankDeserializer> ranks : rankings) {
                short highest = 0;
                for (RankDeserializer rank : ranks) {
                    if (highest < RankEnum.valueOf(rank.getTier()).getValue()) {
                        highest = RankEnum.valueOf(rank.getTier()).getValue();
                    }
                }
                RegionalAccount regionalAccount =
                    regionalAccountRepository.findByInGameName(ranks.get(0).getSummonerName())
                        .orElseThrow(() -> new ResourceNotFoundException("Regional account not found."));

                regionalAccount.setRank(highest);

                regionalAccountRepository.save(regionalAccount);
            }
        }

        tournament.setUpdating(false);
        tournamentRepository.save(tournament);
    }

    public String riotBaseUrlBuilder(String baseUrl, String region) {
        if ("KR".equals(region) || "RU".equals(region)) {
            return new Formatter().format(baseUrl, region).toString();
        }
        System.out.println(new Formatter().format(baseUrl, region.concat("1")));
        return new Formatter().format(baseUrl, region.concat("1")).toString();
    }

}
