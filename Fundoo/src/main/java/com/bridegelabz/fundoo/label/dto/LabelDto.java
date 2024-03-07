package com.bridegelabz.fundoo.label.dto;

public class LabelDto 
{
	private String name;
	public LabelDto() 
	{
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "LabelDto [name=" + name + "]";
	}
	
	
}
