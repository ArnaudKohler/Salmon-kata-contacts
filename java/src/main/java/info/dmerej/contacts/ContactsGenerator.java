package info.dmerej.contacts;

import java.sql.Connection;

public class ContactsGenerator {
    private final Connection connection;

    public ContactsGenerator(Connection connection) {
        this.connection = connection;
    }

    public void insertManyContacts(int numContacts) {
        System.out.format("Inserting %d contacts ... \n", numContacts);
        for (int i = 1; i <= numContacts; i++) {
            String email = String.format("email-%d@tld", i);
            String name = String.format("name %d", i);
            String sql = "INSERT INTO contacts (id, email, name) VALUES (?, ?, ?)";
            try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, i);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, name);
            preparedStatement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("done");
    }

}
