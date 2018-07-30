package com.itunion.demo.domain.vo;

import com.itunion.demo.domain.HelpTopic;

public class HelpTopicVo extends HelpTopic {

    private HelpCategoryVo category;

    public void setCategory(HelpCategoryVo category) {
        this.category = category;
    }

    public HelpCategoryVo getCategory() {
        return category;
    }
}