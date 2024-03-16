package com.pos.meli.domain.service;

import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractService
{
	@Value("${consecutive.sales.code:Sl}")
	protected String consecutiveSalesCode;

	@Value("${consecutive.sales.prefix:V-}")
	protected String consecutiveSalesPrefix;

	@Value("${empty.data:}")
	protected String emptyData;
}
