package our;

import sa.Cooling;

public class SAHelper {
    private  double T, T0, Tn, a;
    private  int n, k;
    public double lastProb = -1.0;

    public SAHelper(double _T0, double _Tn, double _a) {
        T0 = T = _T0;
        Tn = _Tn;
        a = _a;
        n = 100;
    }
    public void cooldown (int idx, double per) {
        k = (int) (n * per);
        switch (idx) {
            case 1:
                T = Cooling.exponentialMultiplicativeCooling(T0, a, k);
                break;
            case 2:
                T = Cooling.logarithmicalMultiplicativeCooling(T0, a, k);
                break;
            case 3:
                T = Cooling.linearMultiplicativeCooling(T0, a, k);
                break;
            case 4:
                T = Cooling.quadraticMultiplicativeCooling(T0, a, k);
                break;
            case 5:
                T = Cooling.linearAdditiveCooling(Tn, T0, n, k);
                break;
            case 6:
                T = Cooling.quadraticAdditiveCooling(Tn, T0, n, k);
                break;
            case 7:
                T = Cooling.exponentialAdditiveCooling(Tn, T0, n, k);
                break;
            case 8:
                T = Cooling.trigonometricAdditiveCooling(Tn, T0, n, k);
                break;
        }
    }

    public double probability(double Vc, double Vn) {
        lastProb = Math.pow(Math.E, (Vc - Vn)/T);
        return lastProb;
    }
}
