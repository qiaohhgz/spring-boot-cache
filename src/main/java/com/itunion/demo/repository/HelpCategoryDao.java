package com.itunion.demo.repository;

import com.itunion.demo.domain.HelpCategory;
import com.itunion.demo.domain.form.HelpCategoryForm;
import com.itunion.demo.domain.vo.HelpCategoryVo;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface HelpCategoryDao {

    List<HelpCategoryVo> selectList(HelpCategoryForm form);

    HelpCategoryVo selectFirst(HelpCategoryForm form);

    int countByForm(HelpCategoryForm form);

    HelpCategoryVo selectById(Serializable id);

    void insert(HelpCategory entity);

    int delete(HelpCategory query);

    int deleteById(Serializable id);

    int updateById(HelpCategory entity);

    int updateByIdSelective(HelpCategory entity);

    void deleteByIdInBatch(List<Serializable> idList);

    void insertInBatch(List<HelpCategory> entityList);
}
