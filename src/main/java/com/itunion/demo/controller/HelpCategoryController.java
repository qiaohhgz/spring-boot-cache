package com.itunion.demo.controller;

import com.itunion.demo.domain.form.HelpCategoryForm;
import com.itunion.demo.domain.vo.HelpCategoryVo;
import com.itunion.demo.service.HelpCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/helpCategory")
public class HelpCategoryController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private HelpCategoryService helpCategoryService;

    @RequestMapping("/dataGrid")
    public Object dataGrid() {
        long s = System.currentTimeMillis();
        List<HelpCategoryVo> list = helpCategoryService.selectList(new HelpCategoryForm());
        log.info("query use time " + (System.currentTimeMillis() - s) + " ms");
        return list;
    }

    @RequestMapping("/{id}")
    public Object getById(@PathVariable("id") Integer id) {
        return helpCategoryService.selectById(id);
    }

}