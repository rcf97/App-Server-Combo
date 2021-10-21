package Service;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.Random;
import java.util.UUID;

public class GenerateService {
    private String[] mNames;
    private String[] fNames;
    private String[] sNames;
    private Location[] locations;
    Database db;

    public GenerateService() throws IOException {
        File mFile = new File("json/mnames.json");
        File fFile = new File("json/fnames.json");
        File sFile = new File("json/snames.json");
        File lFile = new File("json/locations.json");
        this.mNames = parseNames(mFile);
        this.fNames = parseNames(fFile);
        this.sNames = parseNames(sFile);
        this.locations = parseLocation(lFile);
        db = new Database();
    }

    private static String[] parseNames(File file) throws IOException {
        try(FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(bufferedReader, JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");
            String[] names = gson.fromJson(jsonArray, String[].class);
            return names;
        }
    }

    private static Location[] parseLocation(File file) throws IOException {
        try(FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(bufferedReader, JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");
            Location[] locations = gson.fromJson(jsonArray, Location[].class);
            return locations;
        }
    }

    public String generate(String userName, String firstname, String lastname, String gender, String personid,
                           int generation) throws Exception {
        Random random = new Random();

        //Generate birth year, ranging between 1970 and 2010.
        int birthYear = random.nextInt(40) + 1970;
        Location location = locations[random.nextInt(locations.length)];
        genBirthEvent(userName, birthYear, personid, location);

        String fatherid = UUIDGenerator(Person.class);
        String motherid = UUIDGenerator(Person.class);
        int marriageYear = genMarriageEvent(userName, birthYear, fatherid, motherid, location);

        Person person = new Person(personid, userName, firstname, lastname, gender, fatherid, motherid, null);
        try {
            PersonDAO pDao = new PersonDAO(db.openConnection());
            pDao.insert(person);
            db.closeConnection(true);
        } catch (Exception ex) {
            db.closeConnection(false);
        }
        if (generation <= 0) {
            throw new Exception("Error: Invalid Generation Parameter.");
        }
        genFather(userName, location, lastname, birthYear, fatherid, motherid, marriageYear, generation - 1);
        genMother(userName, location, birthYear, fatherid, motherid, marriageYear, generation - 1);
        return personid;
    }

    private void genFather(String userName, Location location, String lastname, int childBirthYear, String personid,
                           String spouseid, int marriage, int generation) throws DataAccessException {
        Random random = new Random();
        String firstname = mNames[random.nextInt(mNames.length)];

        //Random Age, assuming earliest marriage date is
        //at 16 years old and latest is 45
        int latestBirthYear = marriage - 16;
        int birthYear = latestBirthYear - random.nextInt(29);
        genBirthEvent(userName, birthYear, personid, location);
        genDeathEvent(userName, birthYear, childBirthYear, personid, location);

        String fatherid = UUIDGenerator(Person.class);
        String motherid = UUIDGenerator(Person.class);

        Person person = null;
        if (generation == 0) {
            person = new Person(personid, userName, firstname, lastname,
                    "m", null, null, spouseid);
        }
        else {
            person = new Person(personid, userName, firstname, lastname, "m", fatherid, motherid, spouseid);
        }
        try {
            PersonDAO pDao = new PersonDAO(db.openConnection());
            pDao.insert(person);
            db.closeConnection(true);
        } catch (Exception ex) {
            db.closeConnection(false);
        }
        if (generation != 0) {
            int marriageYear = genMarriageEvent(userName, birthYear, fatherid, motherid, location);
            genFather(userName, location, lastname, birthYear, fatherid, motherid, marriageYear, generation - 1);
            genMother(userName, location, birthYear, fatherid, motherid, marriageYear, generation - 1);
        }
    }

    private void genMother(String userName, Location location, int childBirthYear, String spouseid, String personid,
                           int marriage, int generation) throws DataAccessException {
        Random random = new Random();
        String firstname = fNames[random.nextInt(fNames.length)];
        String lastname = sNames[random.nextInt(sNames.length)];

        //Random Age, assuming earliest marriage date is
        //at 16 years old and latest is 45
        int latestBirthYear = marriage - 16;
        int birthYear = latestBirthYear - random.nextInt(29);
        genBirthEvent(userName, birthYear, personid, location);
        genDeathEvent(userName, birthYear, childBirthYear, personid, location);

        String fatherid = UUIDGenerator(Person.class);
        String motherid = UUIDGenerator(Person.class);

        Person person = null;
        if (generation == 0) {
            person = new Person(personid, userName, firstname, lastname,
                    "f", null, null, spouseid);
        }
        else {
            person = new Person(personid, userName, firstname, lastname, "f", fatherid, motherid, spouseid);
        }
        try {
            PersonDAO pDao = new PersonDAO(db.openConnection());
            pDao.insert(person);
            db.closeConnection(true);
        } catch (Exception ex) {
            db.closeConnection(false);
        }
        if (generation != 0) {
            int marriageYear = genMarriageEvent(userName, birthYear, fatherid, motherid, location);
            genFather(userName, location, lastname, birthYear, fatherid, motherid, marriageYear, generation - 1);
            genMother(userName, location, birthYear, fatherid, motherid, marriageYear, generation - 1);
        }
    }

    private void genBirthEvent(String username, int birthYear, String personid, Location location) throws DataAccessException {
        Event event = null;
        String eventid = UUIDGenerator(Event.class);
        Random random = new Random();
        location = locations[random.nextInt(locations.length)];
        event = new Event(eventid, username, personid, location.getLatitude(), location.getLongitude(),
                location.getCountry(), location.getCity(), "birth", birthYear);
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.insert(event);
            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
        }
    }

    private int genMarriageEvent(String username, int birthYear, String fatherid, String motherid,
                                Location location) throws DataAccessException {
        //Marriage should be between 0-5 years before birth
        Random random = new Random();
        int marriageYear = birthYear - random.nextInt(5);
        String meventid = UUIDGenerator(Event.class);
        String feventid = UUIDGenerator(Event.class);
        location = locations[random.nextInt(locations.length)];
        Event mEvent = new Event(meventid, username, fatherid, location.getLatitude(), location.getLongitude(),
                location.getCountry(), location.getCity(), "marriage", marriageYear);
        Event fEvent = new Event(feventid, username, motherid, location.getLatitude(), location.getLongitude(),
                location.getCountry(), location.getCity(), "marriage", marriageYear);
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.insert(mEvent);
            eDao.insert(fEvent);
            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
        }
        return marriageYear;
    }

    private void genDeathEvent(String username, int birthYear, int childBirthYear, String personid,
                                    Location location) throws DataAccessException {
        //Death should be between child birth year and 120 years old
        int latestDeathYear = birthYear + 120;
        int latestDeathAge = latestDeathYear - childBirthYear;
        int earliestDeathYear = childBirthYear;
        Random random = new Random();
        int deathYear = random.nextInt(latestDeathAge) + childBirthYear;
        location = locations[random.nextInt(locations.length)];

        String eventid = UUIDGenerator(Event.class);
        Event event = new Event(eventid, username, personid, location.getLatitude(), location.getLongitude(),
                location.getCountry(), location.getCity(), "death", deathYear);
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.insert(event);
            db.closeConnection(true);
        } catch (DataAccessException ex) {
            db.closeConnection(false);
        }
    }

    public static String UUIDGenerator(java.lang.Class c) throws DataAccessException {
        assert (c == Person.class) || (c == AuthToken.class) || (c == Event.class);
        String id = UUID.randomUUID().toString();
        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            if (c == Person.class) {
                PersonDAO pDao = new PersonDAO(conn);
                Person person = pDao.find(id);
                while (person != null) {
                    id = UUID.randomUUID().toString();
                    person = pDao.find(id);
                }
            } else if (c == Event.class) {
                EventDAO eDao = new EventDAO(conn);
                Event event = eDao.find(id);
                while (event != null) {
                    id = UUID.randomUUID().toString();
                    event = eDao.find(id);
                }
            } else if (c == AuthToken.class) {
                AuthTokenDAO aDao = new AuthTokenDAO(conn);
                AuthToken authToken = aDao.find(id);
                while (authToken != null) {
                    id = UUID.randomUUID().toString();
                    authToken = aDao.find(id);
                }
            }
            db.closeConnection(true);
            return id;
        } catch (DataAccessException ex) {
            db.closeConnection(false);
            //FIXME: throw Database Exception error, where should I handle that?
        }
        return null;
    }
}
