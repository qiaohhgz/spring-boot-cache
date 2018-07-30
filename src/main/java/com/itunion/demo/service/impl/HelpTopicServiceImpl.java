package com.itunion.demo.service.impl;

import com.itunion.demo.domain.HelpTopic;
import com.itunion.demo.domain.form.HelpTopicForm;
import com.itunion.demo.domain.vo.HelpTopicVo;
import com.itunion.demo.repository.HelpTopicDao;
import com.itunion.demo.service.HelpCategoryService;
import com.itunion.demo.service.HelpTopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service("helpTopicService")
public class HelpTopicServiceImpl implements HelpTopicService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private HelpTopicDao helpTopicDao;
    @Autowired
    private HelpCategoryService helpCategoryService;

    public List<HelpTopicVo> selectList(HelpTopicForm form) {
        List<HelpTopicVo> list = helpTopicDao.selectList(form);
        for (HelpTopicVo topic : list) {
            if (topic.getHelpCategoryId() != null) {
                topic.setCategory(helpCategoryService.selectById(topic.getHelpCategoryId()));
            }
        }
        return list;
    }

    public HelpTopicVo selectFirst(HelpTopicForm form) {
        return helpTopicDao.selectFirst(form);
    }

    public int countByForm(HelpTopicForm form) {
        return helpTopicDao.countByForm(form);
    }

    public HelpTopicVo selectById(Serializable id) {
        return helpTopicDao.selectById(id);
    }

    public void insert(HelpTopic entity) {
        helpTopicDao.insert(entity);
    }

    public int delete(HelpTopic query) {
        return helpTopicDao.delete(query);
    }

    public int deleteById(Serializable id) {
        return helpTopicDao.deleteById(id);
    }

    public int updateById(HelpTopic entity) {
        return helpTopicDao.updateById(entity);
    }

    public int updateByIdSelective(HelpTopic entity) {
        return helpTopicDao.updateByIdSelective(entity);
    }

    public void deleteByIdInBatch(List<Serializable> idList) {
        helpTopicDao.deleteByIdInBatch(idList);
    }

    public void insertInBatch(List<HelpTopic> entityList) {
        helpTopicDao.insertInBatch(entityList);
    }
}
