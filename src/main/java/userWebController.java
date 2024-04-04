import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kursinis.kursinis.hibernateControllers.CustomHib;
import com.kursinis.kursinis.model.User;
import com.kursinis.kursinis.model.Warehouse;
import webControllers.serializers.CustomerGsonSerializer;
import webControllers.serializers.LocalDateGsonSerializer;
import webControllers.serializers.ManagerGsonSerializer;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.kursinis.kursinis.model.Warehouse;
import java.time.LocalDate;
import java.util.Properties;

@Controller
public class userWebController {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kursinioDB");
    CustomHib customHib = new CustomHib(entityManagerFactory);

    @RequestMapping(value = "/getUserById/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getUserById(@PathVariable(name = "id") int id){

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateGsonSerializer());
        builder.registerTypeAdapter(User.class, new CustomerGsonSerializer());
        Gson gson = builder.create();
        User user = customHib.getEntityById(User.class, id);
        return user != null ? gson.toJson(user) : "";
    }

    @RequestMapping(value = "/user/getUserByCredentials", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getUserByCredentials(@RequestBody String data){

        System.out.println(data);
        Gson parser = new Gson();
        Properties properties = parser.fromJson(data, Properties.class);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateGsonSerializer());
        builder.registerTypeAdapter(User.class, new CustomerGsonSerializer());
        Gson gson = builder.create();

        User user = customHib.getUserByCredentials(properties.getProperty("login"), properties.getProperty("password"));

        return user != null ? gson.toJson(user) : "";
    }


    @RequestMapping(value = "/user/updateuser", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String updateUserFromAndroid(@RequestBody String data){

        Gson parser = new Gson();
        Properties properties = parser.fromJson(data, Properties.class);

        var id = Integer.parseInt(properties.getProperty("id"));
        var name = properties.getProperty("name");
        var surname = properties.getProperty("surname");
        var password = properties.getProperty("password");


        User user = customHib.getEntityById(User.class, id);

        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);

        customHib.update(user);

        user = customHib.getEntityById(User.class, id);

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateGsonSerializer());
        builder.registerTypeAdapter(User.class, new ManagerGsonSerializer());
        builder.registerTypeAdapter(User.class, new CustomerGsonSerializer());
        Gson gson = builder.create();

        return user != null ? gson.toJson(user) : "";
    }

    @RequestMapping(value = "/user/addPerson", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String addNewPerson(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Object Warehouse;
        Warehouse = null;
        User user = new User(properties.getProperty("login"), properties.getProperty("password"), LocalDate.parse(properties.getProperty("birthDate")), properties.getProperty("name"),properties.getProperty("surname"), properties.getProperty("type"), properties.getProperty("address"), properties.getProperty("cardNo"), (com.kursinis.kursinis.model.Warehouse) properties.get(Warehouse));
        customHib.create(user);
        return "Success";
    }

}
