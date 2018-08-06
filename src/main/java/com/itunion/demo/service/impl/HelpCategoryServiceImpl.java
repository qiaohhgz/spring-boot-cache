package com.itunion.demo.service.impl;

import com.itunion.demo.domain.HelpCategory;
import com.itunion.demo.domain.form.HelpCategoryForm;
import com.itunion.demo.domain.vo.HelpCategoryVo;
import com.itunion.demo.repository.HelpCategoryDao;
import com.itunion.demo.service.HelpCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service("helpCategoryService")
public class HelpCategoryServiceImpl implements HelpCategoryService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private HelpCategoryDao helpCategoryDao;

    public List<HelpCategoryVo> selectList(HelpCategoryForm form) {
        return helpCategoryDao.selectList(form);
    }

    public int countByForm(HelpCategoryForm form) {
        return helpCategoryDao.countByForm(form);
    }

    @Cacheable(key = "#id", value = "help_category")
    public HelpCategoryVo selectById(Serializable id) {
        log.info("query category from DB " + id);
        return helpCategoryDao.selectById(id);
    }

    public void insert(HelpCategory entity) {
        helpCategoryDao.insert(entity);
    }

    @CacheEvict(key = "#id", value = "help_category")
    public int deleteById(Serializable id) {
        return helpCategoryDao.deleteById(id);
    }

    @CacheEvict(key = "#entity.helpCategoryId", value = "help_category")
    public int updateById(HelpCategory entity) {
        return helpCategoryDao.updateById(entity);
    }

}
