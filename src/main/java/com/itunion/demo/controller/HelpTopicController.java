package com.itunion.demo.controller;

import com.itunion.demo.domain.form.HelpTopicForm;
import com.itunion.demo.domain.vo.HelpTopicVo;
import com.itunion.demo.service.HelpTopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/helpTopic")
public class HelpTopicController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private HelpTopicService helpTopicService;

    @RequestMapping("/dataGrid")
    public Object dataGrid() {
        long s = System.currentTimeMillis();
        List<HelpTopicVo> list = helpTopicService.selectList(new HelpTopicForm());
        log.info("query use time " + (System.currentTimeMillis() - s) + " ms");
        return list;
    }

    @RequestMapping("/{id}")
    public Object getById(@PathVariable("id") Integer id) {
        return helpTopicService.selectById(id);
    }
}