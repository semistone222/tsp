package ga.select;

import util.Path;

import java.util.*;

public class TournamentSelection implements Selection {

    private final double tournamentSize;

    /**
     * cf) the bigger tournamentSize, the higher selection pressure
     * @param tournamentSize should be 2^r, where r is integer
     */
    public TournamentSelection(double tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    @Override
    public Path[] select(Path[] population) {
        Path[] parents = new Path[2];
        parents[0] = compete(population);
        parents[1] = compete(population);

        return parents;
    }

    private Path compete(Path[] population) {
        Random random = new Random();
        int minIdx = 0;
        int maxIdx = population.length - 1;

        Queue<Path> tournament = new LinkedList<>();
        for(int i = 0; i < tournamentSize; i++) {
            int randomIdx = random.nextInt(maxIdx - minIdx + 1) + minIdx;
            tournament.offer(population[randomIdx]);
        }

        while(tournament.size() > 1) {
            Path temp1 = tournament.poll();
            Path temp2 = tournament.poll();

            if(temp1.totalCost < temp2.totalCost) {
                tournament.offer(temp1);
            } else {
                tournament.offer(temp2);
            }
        }

        return tournament.poll();
    }
}
