public class MyAppFactory {
    public MyApp create(final String name, final int startYear, final int endYear) {
        return new MyApp(name, startYear, endYear);
    }
}