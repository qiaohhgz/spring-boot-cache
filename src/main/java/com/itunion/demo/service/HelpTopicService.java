package com.itunion.demo.service;

import com.itunion.demo.domain.HelpTopic;
import com.itunion.demo.domain.form.HelpTopicForm;
import com.itunion.demo.domain.vo.HelpTopicVo;
import com.itunion.demo.repository.HelpTopicDao;
import com.itunion.demo.service.HelpTopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

public interface HelpTopicService {

    List<HelpTopicVo> selectList(HelpTopicForm form);

    int countByForm(HelpTopicForm form);

    HelpTopicVo selectById(Serializable id);

    void insert(HelpTopic entity);

    int deleteById(Serializable id);

    int updateById(HelpTopic entity);

}
