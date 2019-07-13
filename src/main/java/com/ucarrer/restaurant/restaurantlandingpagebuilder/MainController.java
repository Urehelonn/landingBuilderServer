package com.ucarrer.restaurant.restaurantlandingpagebuilder;



import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@RestController
public class MainController {

    @Autowired
    JdbcTemplate sql;

    @GetMapping("/hello")
    List<Map<String,Object>> findByUsername(@RequestParam(name="name", required=true) String name, Model model) {
        return sql.queryForList("select * from member where username like ?", new Object[] { name });
    }

    @RequestMapping("/")
    public List<Map<String,Object>> getAll() {
        return sql.queryForList("select * from member");
    }

}