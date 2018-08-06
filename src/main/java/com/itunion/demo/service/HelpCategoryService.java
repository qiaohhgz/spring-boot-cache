package com.itunion.demo.service;

import com.itunion.demo.domain.HelpCategory;
import com.itunion.demo.domain.form.HelpCategoryForm;
import com.itunion.demo.domain.vo.HelpCategoryVo;

import java.io.Serializable;
import java.util.List;

public interface HelpCategoryService {

    List<HelpCategoryVo> selectList(HelpCategoryForm form);

    int countByForm(HelpCategoryForm form);

    HelpCategoryVo selectById(Serializable id);

    void insert(HelpCategory entity);

    int deleteById(Serializable id);

    int updateById(HelpCategory entity);

}
