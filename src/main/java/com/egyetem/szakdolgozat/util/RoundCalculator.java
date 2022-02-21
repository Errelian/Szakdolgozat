package com.egyetem.szakdolgozat.util;

import com.egyetem.szakdolgozat.database.tournamentToTeams.TournamentToTeams;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RoundCalculator {

    public static List<TournamentToTeams> calcNextRound2(List<TournamentToTeams> previousRound, Integer roundNumber){
        List<TournamentToTeams> nextRound = new ArrayList<>();

        previousRound.sort(Comparator.comparing(TournamentToTeams::getPosition));
        if (previousRound.size() == 1){
            return new ArrayList<>();
        }

        for (int i=0; i < previousRound.size()-1; i= i+2){
            if (previousRound.get(i).getEliminationRound() == null || (previousRound.get(i).getEliminationRound() >= roundNumber)){ //if the current team is not yet eliminated
                if (previousRound.get(i+1).getEliminationRound() != null && previousRound.get(i+1).getEliminationRound() <= roundNumber){
                    nextRound.add(previousRound.get(i));
                }
            }
            else{
                nextRound.add(previousRound.get(i+1));
            }
        }

        for (int i = 0; i < nextRound.size(); i++){
            nextRound.get(i).setPosition(i+1);
        }

        return nextRound;
    }

    public static List<TournamentToTeams> calcNextRound(List<TournamentToTeams> previousRound, Integer roundNumber){
        List<TournamentToTeams> nextRound = new ArrayList<>();

        previousRound.sort(Comparator.comparing(TournamentToTeams::getPosition));
        //System.out.println(previousRound);
        if (previousRound.size() == 1){
            return new ArrayList<>();
        }
        for (int i=1; i <= previousRound.size(); i++){
            //System.out.println(previousRound.get(i).getPosition());
            if(previousRound.get(i-1).getEliminationRound() == null || previousRound.get(i-1).getEliminationRound() > roundNumber){
                if(previousRound.get(i-1).getPosition() % 2 == 0){
                    if (previousRound.get(i-2).getEliminationRound() != null && previousRound.get(i-2).getEliminationRound() <= roundNumber){
                        nextRound.add(previousRound.get(i-1));
                    }
                }
                else{
                    if(i < previousRound.size()) {
                        if (previousRound.get(i).getEliminationRound() != null && previousRound.get(i).getEliminationRound() <= roundNumber) {
                            nextRound.add(previousRound.get(i - 1));
                        }
                    }
                }
            }
        }

        for (int i = 1; i <= nextRound.size(); i++){
            nextRound.get(i-1).setPosition(i);
        }

        return nextRound;
    }
}
