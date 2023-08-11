public class BenchMark {

    public static void main(String[] args) {

        int[] sizes = { 100,
            500,
            750,
            1000,
            2000,
            2500,
            3000,
            4000,
            5000,
            6000
        };
        double[] densities = { 1 };

        String[] arguments = new String[7];
        for (int i = 0; i < sizes.length; i++) {
            for (int j = 0; j < densities.length; j++) {
                arguments[0] = sizes[i] + "";
                arguments[1] = sizes[i] + "";
                arguments[2] = "0";
                arguments[3] = sizes[i] + "";
                arguments[4] = "0";
                arguments[5] = sizes[i] + "";
                arguments[6] = densities[j] + "";
                MonteCarloMinimizationParallel.main(arguments);
                break;
            }
        }
    }
}