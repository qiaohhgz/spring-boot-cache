package com.itunion.demo.domain;

import java.io.Serializable;

public class HelpCategory implements Serializable{
  
    private Integer helpCategoryId; /***/
    private String name; /***/
    private Integer parentCategoryId; /***/
    private String url; /***/

	public Integer getHelpCategoryId(){
	    return this.helpCategoryId;
    }
    public void setHelpCategoryId(Integer helpCategoryId){
	    this.helpCategoryId = helpCategoryId;
    }
	public String getName(){
	    return this.name;
    }
    public void setName(String name){
	    this.name = name;
    }
	public Integer getParentCategoryId(){
	    return this.parentCategoryId;
    }
    public void setParentCategoryId(Integer parentCategoryId){
	    this.parentCategoryId = parentCategoryId;
    }
	public String getUrl(){
	    return this.url;
    }
    public void setUrl(String url){
	    this.url = url;
    }
}