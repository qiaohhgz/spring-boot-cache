package com.itunion.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.itunion.demo.cache.CacheService;
import com.itunion.demo.domain.HelpCategory;
import com.itunion.demo.domain.form.HelpCategoryForm;
import com.itunion.demo.domain.vo.HelpCategoryVo;
import com.itunion.demo.repository.HelpCategoryDao;
import com.itunion.demo.service.HelpCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service("helpCategoryService")
public class HelpCategoryServiceImpl implements HelpCategoryService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private HelpCategoryDao helpCategoryDao;
    @Autowired
    private CacheService cacheService;

    @Override
    public List<HelpCategoryVo> selectList(HelpCategoryForm form) {
        String finger = cacheService.getFinger(form);
        String key = String.format("category:selectList:%s", finger);
        if (cacheService.hasKey(key)) {
            String json = cacheService.getValue(key);
            return JSON.parseObject(json, new ArrayList<HelpCategoryVo>().getClass());
        } else {
            log.info("query category list from db > " + JSON.toJSON(form));
            List<HelpCategoryVo> data = helpCategoryDao.selectList(form);
            cacheService.setValue(key, JSON.toJSONString(data), 30);
            return data;
        }
    }

    @Override
    public int countByForm(HelpCategoryForm form) {
        return helpCategoryDao.countByForm(form);
    }

    @Override
    public HelpCategoryVo selectById(Serializable id) {
        log.info("query category from DB " + id);
        return helpCategoryDao.selectById(id);
    }

    @Override
    public void insert(HelpCategory entity) {
        helpCategoryDao.insert(entity);
    }

    @Override
    public int deleteById(Serializable id) {
        return helpCategoryDao.deleteById(id);
    }

    @Override
    public int updateById(HelpCategory entity) {
        return helpCategoryDao.updateById(entity);
    }

}
