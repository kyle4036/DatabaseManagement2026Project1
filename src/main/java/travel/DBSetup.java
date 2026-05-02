package travel;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

public class DBSetup {

    public static void resetAndSeed() {
        try (Connection conn = DBConnection.get();
             Statement stmt = conn.createStatement()) {

            String schema = Files.readString(Paths.get("sql/schema_01.sql"));
            for (String s : schema.split(";")) {
                if (!s.trim().isEmpty()) stmt.execute(s.trim());
            }

            String seed = Files.readString(Paths.get("sql/seed.sql"));
            for (String s : seed.split(";")) {
                if (!s.trim().isEmpty()) stmt.execute(s.trim());
            }

            System.out.println("DB reset complete.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        resetAndSeed();
    }
}
