package ru.maxi.retail.osminina.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.maxi.retail.osminina.db.QueryManager;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TopProductController {

    private QueryManager queryManager;

    public TopProductController(QueryManager queryManager) {
        this.queryManager = queryManager;
    }

    @GetMapping("/getTopProduct")
    public ModelAndView getTopProduct() {
        Map<String, Object> model = new HashMap<>();
        model.put("kppNumber", "");
        return new ModelAndView("topProduct", model);

    }

    @PostMapping("/getTopProduct")
    public ModelAndView getTopProduct(String kppNumber) {
        Map<String, Object> model = new HashMap<>();
        model.put("products", queryManager.getTop(kppNumber));
        model.put("kppNumber", kppNumber);
        return new ModelAndView("topProduct", model);

    }
}
