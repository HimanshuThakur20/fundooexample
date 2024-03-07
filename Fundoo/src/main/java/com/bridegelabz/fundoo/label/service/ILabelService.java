package com.bridegelabz.fundoo.label.service;

import com.bridegelabz.fundoo.label.dto.LabelDto;
import com.bridegelabz.fundoo.response.Response;

public interface ILabelService 
{
	public Response createLabel(LabelDto labelDto, String token);
}
