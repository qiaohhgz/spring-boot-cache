package com.itunion.demo.controller;

import com.itunion.demo.domain.form.HelpCategoryForm;
import com.itunion.demo.domain.vo.HelpCategoryVo;
import com.itunion.demo.service.HelpCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(key = "'dataGrid' + #form.toString()", value = "HelpCategoryController", sync = true)
    @RequestMapping("/dataGrid")
    public Object dataGrid(HelpCategoryForm form) {
        long s = System.currentTimeMillis();
        List<HelpCategoryVo> list = helpCategoryService.selectList(form);
        log.info("query use time " + (System.currentTimeMillis() - s) + " ms");
        return list;
    }

    @RequestMapping("/{id}")
    public Object getById(@PathVariable("id") Integer id) {
        long s = System.currentTimeMillis();
        HelpCategoryVo vo = helpCategoryService.selectById(id);
        log.info("query use time " + (System.currentTimeMillis() - s) + " ms");
        return vo;
    }

}