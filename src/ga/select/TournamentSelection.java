package ga.select;

import util.Path;

import java.util.*;

public class TournamentSelection implements Selection {

    private final int tournamentSize;

    /**
     * cf) the bigger tournamentSize, the higher selection pressure
     * @param tournamentSize should be 2^r, where r is integer
     */
    public TournamentSelection(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    @Override
    public int[] select(Path[] population) {
        int[] parentsIdx = new int[2];
        parentsIdx[0] = compete(population);
        parentsIdx[1] = compete(population);

        return parentsIdx;
    }

    private int compete(Path[] population) {
        Random random = new Random();
        int minIdx = 0;
        int maxIdx = population.length - 1;

        HashMap<Path, Integer> hashMap = new HashMap<>();
        Queue<Path> tournament = new LinkedList<>();
        for(int i = 0; i < tournamentSize; i++) {
            int randomIdx = random.nextInt(maxIdx - minIdx + 1) + minIdx;
            Path randomPath = population[randomIdx];
            hashMap.put(randomPath, randomIdx);
            tournament.offer(randomPath);
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

        return hashMap.get(tournament.poll());
    }
}
