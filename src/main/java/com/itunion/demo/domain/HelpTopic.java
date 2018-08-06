package com.itunion.demo.domain;

import java.io.Serializable;

public class HelpTopic implements Serializable{
  
    private Integer helpTopicId; /***/
    private String name; /***/
    private Integer helpCategoryId; /***/
    private Object description; /***/
    private Object example; /***/
    private String url; /***/

	public Integer getHelpTopicId(){
	    return this.helpTopicId;
    }
    public void setHelpTopicId(Integer helpTopicId){
	    this.helpTopicId = helpTopicId;
    }
	public String getName(){
	    return this.name;
    }
    public void setName(String name){
	    this.name = name;
    }
	public Integer getHelpCategoryId(){
	    return this.helpCategoryId;
    }
    public void setHelpCategoryId(Integer helpCategoryId){
	    this.helpCategoryId = helpCategoryId;
    }
	public Object getDescription(){
	    return this.description;
    }
    public void setDescription(Object description){
	    this.description = description;
    }
	public Object getExample(){
	    return this.example;
    }
    public void setExample(Object example){
	    this.example = example;
    }
	public String getUrl(){
	    return this.url;
    }
    public void setUrl(String url){
	    this.url = url;
    }

    @Override
    public String toString() {
        return "HelpTopic{" +
                "helpTopicId=" + helpTopicId +
                ", name='" + name + '\'' +
                ", helpCategoryId=" + helpCategoryId +
                ", description=" + description +
                ", example=" + example +
                ", url='" + url + '\'' +
                '}';
    }
}