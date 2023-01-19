import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Value;

import java.util.Locale;

import static groovy.json.JsonOutput.toJson;

public class Data {

    public static String generateStatus(String local) {
        Faker faker = new Faker(new Locale(local));
        String[] status = new String[]{"active", "blocked"};
        return status[faker.number().numberBetween(0, 1)];
    }

    public static String generateLogin(String local) {
        Faker faker = new Faker(new Locale(local));
        return faker.name().username();
    }

    public static String generatePass(String local) {
        Faker faker = new Faker(new Locale(local));
        return faker.internet().password();
    }

    @Value
    public static class UserInfo {
        String name;
        String pass;
        String status;

        public UserInfo(String name, String pass, String status) {
            this.name = name;
            this.pass = pass;
            this.status = status;
        }

        public static UserInfo generateUser(String local) {
            return new UserInfo(Data.generateLogin(local), Data.generatePass(local), generateStatus(local));
        }
   }

   public static String toJson() {
       Gson gson = new GsonBuilder()
               .setPrettyPrinting()
               .create();
       return gson.toJson(UserInfo.generateUser("eng"));
   }
}

