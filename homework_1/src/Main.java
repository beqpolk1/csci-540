public class Main {

    public static void main(String[] args) {
        Environment env = new Environment("test_db");
        TestDBQueries.selectAllFromPerson(env);
    }
}
