package sa;

public class Cooling {

    /**
     * Exponential multiplicative cooling
     * @param T0 initial temperature
     * @param a 0.8 <= a <= 0.9
     * @param k temperature cycle
     * @return
     */
    public static double exponentialMultiplicativeCooling(double T0, double a, int k) {
        return T0 * Math.pow(a, k);
    }

    /**
     * Logarithmical multiplicative cooling
     * @param T0 initial temperature
     * @param a a > 1
     * @param k temperature cycle
     * @return
     */
    public static double logarithmicalMultiplicativeCooling(double T0, double a, int k) {
        return T0 / (1 + a * Math.log(1 + k));
    }

    /**
     * Linear multiplicative cooling
     * @param T0 initial temperature
     * @param a a > 0
     * @param k temperature cycle
     * @return
     */
    public static double linearMultiplicativeCooling(double T0, double a, int k) {
        return T0  / (1 + a * k);
    }

    /**
     * Quadratic multiplicative cooling
     * @param T0 initial temperature
     * @param a a > 0
     * @param k temperature cycle
     * @return
     */
    public static double quadraticMultiplicativeCooling(double T0, double a, int k) {
        return T0 / (1 + a * Math.pow(k, 2));
    }

    /**
     * Linear additive cooling
     * @param Tn final temperature
     * @param T0 initial temperature
     * @param n final cooling cycles
     * @param k temperature cycle
     * @return
     */
    public static double linearAdditiveCooling(double Tn, double T0, int n, int k) {
        return Tn + (T0 - Tn) * ((double) (n - k) / n);
    }

    /**
     * Quadratic additive cooling
     * @param Tn final temperature
     * @param T0 initial temperature
     * @param n final cooling cycles
     * @param k temperature cycle
     * @return
     */
    public static double quadraticAdditiveCooling(double Tn, double T0, int n, int k) {
        return Tn + (T0 - Tn) * Math.pow((double) (n - k) / n, 2);
    }

    /**
     * Exponential additive cooling
     * @param Tn final temperature
     * @param T0 initial temperature
     * @param n final cooling cycles
     * @param k temperature cycle
     * @return
     */
    public static double exponentialAdditiveCooling(double Tn, double T0, int n, int k) {
        return Tn + (T0 - Tn) / (1 + Math.pow(Math.E, 2 * Math.log(T0 - Tn) * (k - 0.5 * n) / (double) n));
    }

    /**
     * Trigonometric additive cooling
     * @param Tn final temperature
     * @param T0 initial temperature
     * @param n final cooling cycles
     * @param k temperature cycle
     * @return
     */
    public static double trigonometricAdditiveCooling(double Tn, double T0, int n, int k) {
        return Tn + 0.5 * (T0 - Tn) * (1 + Math.cos(k * Math.PI / (double) n));
    }
}
