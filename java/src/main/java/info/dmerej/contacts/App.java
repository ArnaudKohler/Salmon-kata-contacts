package info.dmerej.contacts;


import java.io.File;

public class App {

    private final Database database;
    private final ContactsGenerator contactsGenerator;

    public App() {
        File file = new File("contacts.sqlite3");
        if (!file.exists()) {
            database = new Database(file);
            database.migrate();
        } else {
            database = new Database(file);
        }
        contactsGenerator = new ContactsGenerator(database.getConnection());
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Not enough args");
            System.exit(2);
        }
        int count = Integer.parseInt(args[0]);

        
        App app = null;
        long start = System.currentTimeMillis();
        
        try {
            app = new App();
            app.contactsGenerator.insertManyContacts(count);
            app.lookupContact(count);
        } finally {
            long end = System.currentTimeMillis();
            long elapsed = end - start;
            System.out.format("Program took %d ms\n", elapsed);
            if (app != null) {
                app.close();
            }
        }
    }

    private void lookupContact(int count) {
        String email = String.format("email-%d@tld", count);
        long start = System.currentTimeMillis();
        var contact = database.findContactByEmail(email);
        long end = System.currentTimeMillis();
        long elapsed = end - start;
        System.out.format("Query took %d ms\n", elapsed);
        if (contact.isEmpty()) {
            throw new RuntimeException("Contact not found");
        }
    }

    public void close() {
        database.close();
    }

}

