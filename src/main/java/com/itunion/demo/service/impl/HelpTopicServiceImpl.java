package com.itunion.demo.service.impl;

import com.github.pagehelper.PageHelper;
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

    @Override
    public List<HelpTopicVo> selectList(HelpTopicForm form) {
        log.info("query topic list " + form.toString());
        PageHelper.startPage(form);
        List<HelpTopicVo> list = helpTopicDao.selectList(form);
        for (HelpTopicVo topic : list) {
            if (topic.getHelpCategoryId() != null) {
                topic.setCategory(helpCategoryService.selectById(topic.getHelpCategoryId()));
            }
        }
        return list;
    }

    @Override
    public int countByForm(HelpTopicForm form) {
        return helpTopicDao.countByForm(form);
    }

    @Override
    public HelpTopicVo selectById(Serializable id) {
        log.info("query topic by id " + id);
        HelpTopicVo topic = helpTopicDao.selectById(id);
        if (topic.getHelpCategoryId() != null) {
            topic.setCategory(helpCategoryService.selectById(topic.getHelpCategoryId()));
        }
        return topic;
    }

    @Override
    public void insert(HelpTopic entity) {
        helpTopicDao.insert(entity);
    }

    @Override
    public int deleteById(Serializable id) {
        return helpTopicDao.deleteById(id);
    }

    @Override
    public int updateById(HelpTopic entity) {
        return helpTopicDao.updateById(entity);
    }
}
