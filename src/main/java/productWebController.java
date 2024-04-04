import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kursinis.kursinis.hibernateControllers.CustomHib;
import com.kursinis.kursinis.model.*;
import webControllers.serializers.LocalDateGsonSerializer;
import webControllers.serializers.ProductGsonSerializer;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@Controller
public class productWebController {

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("kursinioDB");
    CustomHib customHib = new CustomHib(entityManagerFactory);

    @RequestMapping(value = "/product/getAllProducts", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getAllProducts(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateGsonSerializer());
        builder.registerTypeAdapter(Product.class, new ProductGsonSerializer());
        Gson gson = builder.create();

        List<Product> productList = customHib.getAllRecords(Product.class);

        return gson.toJson(productList);

    }
    @RequestMapping(value = "/product/updateProduct/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCourse(@RequestBody String request, @PathVariable(name = "id") int id) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Product product = new Product();
        product.setId(id);
        customHib.update(product);
        return "Success";
    }

    @RequestMapping(value = "/product/addProduct", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String addNewCourse(@RequestBody String request) {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        String price;
        price = null;
        Product product = new Product();
        customHib.create(product);
        return "Success";
    }

    @RequestMapping(value = "/product/deleteProduct/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteCourse(@PathVariable(name = "id") int id) {
        customHib.delete(id);
        return "Success";
    }
}
