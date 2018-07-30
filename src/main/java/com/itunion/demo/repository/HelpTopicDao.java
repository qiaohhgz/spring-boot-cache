package com.itunion.demo.repository;

import com.itunion.demo.domain.HelpTopic;
import com.itunion.demo.domain.form.HelpTopicForm;
import com.itunion.demo.domain.vo.HelpTopicVo;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface HelpTopicDao {

    List<HelpTopicVo> selectList(HelpTopicForm form);

    HelpTopicVo selectFirst(HelpTopicForm form);

    int countByForm(HelpTopicForm form);

    HelpTopicVo selectById(Serializable id);

    void insert(HelpTopic entity);

    int delete(HelpTopic query);

    int deleteById(Serializable id);

    int updateById(HelpTopic entity);

    int updateByIdSelective(HelpTopic entity);

    void deleteByIdInBatch(List<Serializable> idList);

    void insertInBatch(List<HelpTopic> entityList);
}
