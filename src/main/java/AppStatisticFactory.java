public class AppStatisticFactory {
    public AppStatistic create(final String name, final int startYear, final int endYear) {
        return new AppStatistic(name, startYear, endYear);
    }
}