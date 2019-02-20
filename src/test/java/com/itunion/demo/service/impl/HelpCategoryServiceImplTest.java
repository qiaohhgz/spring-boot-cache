package com.itunion.demo.service.impl;

import com.itunion.demo.domain.form.HelpCategoryForm;
import com.itunion.demo.service.HelpCategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelpCategoryServiceImplTest {

    @Autowired
    private HelpCategoryService helpCategoryService;

    @Test
    public void testSelectList() throws Exception {
        HelpCategoryForm form = new HelpCategoryForm();
        helpCategoryService.selectList(form);
    }
}