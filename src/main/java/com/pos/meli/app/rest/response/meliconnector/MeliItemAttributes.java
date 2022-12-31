package com.pos.meli.app.rest.response.meliconnector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeliItemAttributes
{
	@JsonProperty("id")
	public String id;

	@JsonProperty("name")
	public String name;

	@JsonProperty("value_id")
	public String valueId;

	@JsonProperty("value_name")
	public String valueName;

	@JsonProperty("attribute_group_id")
	public String attributeGroupId;

	@JsonProperty("attribute_group_name")
	public String attributeGroupName;

	@JsonProperty("value_struct")
	public String valueStruct;

	@JsonProperty("values")
	public ArrayList<MeliItemAttributeValue> values;

	@JsonProperty("source")
	public long source;

	@JsonProperty("value_type")
	public String valueType;
}
