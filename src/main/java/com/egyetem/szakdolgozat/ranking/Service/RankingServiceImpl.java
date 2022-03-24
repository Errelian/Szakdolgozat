package com.egyetem.szakdolgozat.ranking.Service;

import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccount;
import com.egyetem.szakdolgozat.database.regionalAccount.persistance.RegionalAccountRepository;
import com.egyetem.szakdolgozat.database.tournament.persistance.Tournament;
import com.egyetem.szakdolgozat.database.tournament.persistance.TournamentRepository;
import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;
import com.egyetem.szakdolgozat.database.user.persistance.SiteUser;
import com.egyetem.szakdolgozat.ranking.serialization.IdDeserializer;
import com.egyetem.szakdolgozat.ranking.serialization.RankDeserializer;
import com.egyetem.szakdolgozat.ranking.serialization.RankEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Objects;

@Service
public class RankingServiceImpl implements RankingService {

    private static final String BASE_URL = "https://%s.api.riotgames.com/lol";
    private static final String SUMMONER_URL = "/summoner/v4/summoners/by-name/%s";
    private static final String RANK_URL = "/league/v4/entries/by-summoner/%s";
    private static final String RIOT_TOKEN = System.getenv("RIOT_TOKEN");

    private static final int ONE_SECOND = 1010;
    private static final int TWO_MINUTES = 120100;
        //in milliseconds, a little extra to make sure we are not getting rate limited

    private static int REQUEST_COUNTER = 0;


    TournamentRepository tournamentRepository;
    RegionalAccountRepository regionalAccountRepository;

    @Autowired
    public RankingServiceImpl(TournamentRepository tournamentRepository,
                          RegionalAccountRepository regionalAccountRepository) {
        this.tournamentRepository = tournamentRepository;
        this.regionalAccountRepository = regionalAccountRepository;
    }


    @Async
    @Override
    public void updateRank(Tournament tournament) {

        tournament.setUpdating(true);
        tournamentRepository.save(tournament);

        String url = riotBaseUrlBuilder(BASE_URL, tournament.getRegionId());

        WebClient webClient = WebClient
            .builder()
            .baseUrl(url)
            .defaultHeader("X-Riot-Token", RIOT_TOKEN)
            .build();

        List<RegionalAccount> accounts = new ArrayList<>();

        for (TournamentToTeams tournamentToTeams : tournament.getTeams()) {
            for (SiteUser user : tournamentToTeams.getTeam().getTeamMembers()) {
                accounts.add(user.getRegionalAccountByRegion(tournament.getRegionId()).orElseThrow(() -> new ResourceNotFoundException("Regional Account not found.")));
            }
        }


        List<List<RegionalAccount>> partitions = new ArrayList<>(); //splitting it into easily manageable chunks to avoid rate limiting
        for (int i = 0; i < accounts.size(); i += 20) {
            partitions.add(accounts.subList(i, Math.min(i + 20, accounts.size())));
        }

        try {
            List<IdDeserializer> ids = new ArrayList<>();
            for (int i = 0; i < partitions.size(); ) {
                if (REQUEST_COUNTER <= 100 || REQUEST_COUNTER + partitions.get(i).size() < 100) {
                    ids.addAll(Objects.requireNonNull(Flux.fromIterable(partitions.get(i))
                        .flatMap(account -> webClient.get().uri(String.format(SUMMONER_URL, account.getInGameName()))
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(IdDeserializer.class))
                        .collectList()
                        .block()));
                    REQUEST_COUNTER += partitions.get(i).size();
                    Thread.sleep(ONE_SECOND);
                    i++;
                } else {
                    Thread.sleep(TWO_MINUTES);
                    REQUEST_COUNTER = 0;
                }
            }

            List<List<RankDeserializer>> rankings = new ArrayList<>();

            List<List<IdDeserializer>> idPartitions = new ArrayList<>(); //splitting it into easily manageable chunks to avoid rate limiting
            for (int i = 0; i < ids.size(); i += 20) {
                idPartitions.add(ids.subList(i, Math.min(i + 20, ids.size())));
            }

            for (int i = 0; i < idPartitions.size(); ) {
                if (REQUEST_COUNTER <= 100 || REQUEST_COUNTER + partitions.get(i).size() < 100) {
                    rankings = Flux.fromIterable(idPartitions.get(i))
                        .flatMap(idDeserializer -> webClient.get().uri(String.format(RANK_URL, idDeserializer.getId()))
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<List<RankDeserializer>>() {
                            }))
                        .collectList()
                        .block();

                    REQUEST_COUNTER += idPartitions.get(i).size();
                    Thread.sleep(ONE_SECOND);
                    i++;
                } else {
                    Thread.sleep(TWO_MINUTES);
                    REQUEST_COUNTER = 0;
                }
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
                        regionalAccountRepository.findByInGameName(ranks.get(0).getSummonerName()).get();

                    regionalAccount.setRank(highest);

                    regionalAccountRepository.save(regionalAccount);
                }
            }

        } catch (Exception e) {
            System.out.println("LOG: INTERRUPTED");
        }
        tournament.setUpdating(false);
        tournamentRepository.save(tournament);
    }

    @Override
    public String riotBaseUrlBuilder(String baseUrl, String region) {
        if ("KR".equals(region) || "RU".equals(region)) {
            return new Formatter().format(baseUrl, region).toString();
        }
        return new Formatter().format(baseUrl, region.concat("1")).toString();
    }

}
