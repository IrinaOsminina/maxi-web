package ru.maxi.retail.osminina.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.maxi.retail.osminina.db.QueryManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TotalSumController {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private QueryManager queryManager;

    public TotalSumController(QueryManager queryManager) {
        this.queryManager = queryManager;
    }

    @GetMapping("/getSum")
    public ModelAndView getSum() {
        Date date = new Date();
        Map<String, Object> model = new HashMap<>();
        model.put("totalSum", queryManager.getTotalSum(date));
        model.put("date", SIMPLE_DATE_FORMAT.format(date));
        return new ModelAndView("totalSum", model);
    }

    @PostMapping("/getSum")
    public ModelAndView getSum(String date) {
        Date formatDate = new Date();
        try {
            formatDate = SIMPLE_DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            //TODO:логирование
            e.printStackTrace();
        }
        Map<String, Object> model = new HashMap<>();
        model.put("totalSum",  queryManager.getTotalSum(formatDate));
        model.put("date", date);
        return new ModelAndView("totalSum", model);
    }

}
