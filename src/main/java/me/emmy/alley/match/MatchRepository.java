package me.emmy.alley.match;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MatchRepository {
    private final List<AbstractMatch> matches = new ArrayList<>();
}
