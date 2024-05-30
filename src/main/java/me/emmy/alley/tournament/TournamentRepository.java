package me.emmy.alley.tournament;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmy
 * Project: alley
 * Date: 30/05/2024 - 21:10
 */

@Getter
@Setter
public class TournamentRepository {

    private final List<Tournament> tournaments = new ArrayList<>();
}
