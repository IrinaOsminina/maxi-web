package ru.maxi.retail.osminina.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.maxi.retail.osminina.db.QueryManager;

import java.util.HashMap;
import java.util.Map;


@Controller
public class IndexController {

    private QueryManager queryManager;

    public IndexController(QueryManager queryManager) {
        this.queryManager = queryManager;
    }

    @GetMapping("/")
    public ModelAndView index() {
        Map<String, Object> model = new HashMap<>();
        model.put("checkCount", queryManager.getCheckCount());
        model.put("kppCount", queryManager.getKppCount());
        return new ModelAndView("index", model);
    }

}
