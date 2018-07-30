package com.itunion.demo.service.impl;

import com.itunion.demo.domain.HelpCategory;
import com.itunion.demo.domain.form.HelpCategoryForm;
import com.itunion.demo.domain.vo.HelpCategoryVo;
import com.itunion.demo.repository.HelpCategoryDao;
import com.itunion.demo.service.HelpCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public HelpCategoryVo selectFirst(HelpCategoryForm form) {
        return helpCategoryDao.selectFirst(form);
    }

    public int countByForm(HelpCategoryForm form) {
        return helpCategoryDao.countByForm(form);
    }

    @Cacheable(key = "#id", value = "help_category")
    public HelpCategoryVo selectById(Serializable id) {
        log.info("HelpCategory.selectById from DB " + id);
        return helpCategoryDao.selectById(id);
    }

    public void insert(HelpCategory entity) {
        helpCategoryDao.insert(entity);
    }

    public int delete(HelpCategory query) {
        return helpCategoryDao.delete(query);
    }

    public int deleteById(Serializable id) {
        return helpCategoryDao.deleteById(id);
    }

    public int updateById(HelpCategory entity) {
        return helpCategoryDao.updateById(entity);
    }

    public int updateByIdSelective(HelpCategory entity) {
        return helpCategoryDao.updateByIdSelective(entity);
    }

    public void deleteByIdInBatch(List<Serializable> idList) {
        helpCategoryDao.deleteByIdInBatch(idList);
    }

    public void insertInBatch(List<HelpCategory> entityList) {
        helpCategoryDao.insertInBatch(entityList);
    }
}
