package com.test.tool;

import java.io.IOException;
import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class CreateJsonObjEx {
	private static void test() throws JsonGenerationException, JsonMappingException, IOException {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		jsonBuilder.add("name", "sample");
		JsonArrayBuilder plnArrBld = Json.createArrayBuilder();

		JsonObjectBuilder arrayElementOne = Json.createObjectBuilder();
		arrayElementOne.add("setId", 1);
		arrayElementOne.add("setDef2", "xyz");
		JsonObjectBuilder arrayElementTwo = Json.createObjectBuilder();
		arrayElementTwo.add("setId", 2);
		arrayElementTwo.add("setDef2", "setDef2");
		/*
		 * JsonArrayBuilder arrayElementOneArray =Json.createArrayBuilder();
		 * JsonObjectBuilder arrayElementOneArrayElementOne =
		 * Json.createObjectBuilder(); arrayElementOneArrayElementOne.add("name",
		 * "ABC"); arrayElementOneArrayElementOne.add("type", "STRING");
		 * 
		 * JsonObjectBuilder arrayElementOneArrayElementTwo =
		 * Json.createObjectBuilder(); arrayElementOneArrayElementTwo.add("name",
		 * "XYZ"); arrayElementOneArrayElementTwo.add("type", "STRING");
		 * 
		 * arrayElementOneArray.add(arrayElementOneArrayElementOne);
		 * arrayElementOneArray.add(arrayElementOneArrayElementTwo);
		 */

		plnArrBld.add(arrayElementOne);
		plnArrBld.add(arrayElementTwo);

		jsonBuilder.add("def", plnArrBld);
		JsonObject empObj = jsonBuilder.build();
		StringWriter strWtr = new StringWriter();
		JsonWriter jsonWtr = Json.createWriter(strWtr);
		jsonWtr.writeObject(empObj);
		jsonWtr.close();
		String ret = strWtr.toString();
		ObjectMapper mapper = new ObjectMapper();
		Object json = mapper.readValue(ret, Object.class);
		String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
		System.out.println(indented);
		
		System.out.println(ret);
	}

	private static void test1() {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		jsonBuilder.add("dist_refid", "");
		jsonBuilder.add("date_updated", "2019-07-19T00:00:00");

		// create Json array with only values
		JsonArrayBuilder plnArrBld = Json.createArrayBuilder();
		plnArrBld.add("Rakesh");
		plnArrBld.add("John");
		JsonArray arr = plnArrBld.build();
		// the array got created, add it to the json as a child element
		// jsonBuilder.add("direct_contacts", arr);

		// create an array of key-value pairs
		JsonArrayBuilder kvArrBld = Json.createArrayBuilder();
		// create each key-value pair as seperate object and add it to the array
		kvArrBld.add(Json.createObjectBuilder().add("dist_refid", "java2novice@gmail.com").build());
		kvArrBld.add(Json.createObjectBuilder().add("name", "123123123123").build());
		kvArrBld.add(Json.createObjectBuilder().add("features", "123123123123").build());

		JsonArray contactsArr = kvArrBld.build();
		// add contacts array object
		jsonBuilder.add("whitelisted_ids", contactsArr);

		JsonObject empObj = jsonBuilder.build();
		// here we are writing to String writer.
		// if you want you can write it to a file as well
		StringWriter strWtr = new StringWriter();
		JsonWriter jsonWtr = Json.createWriter(strWtr);
		jsonWtr.writeObject(empObj);
		jsonWtr.close();

		System.out.println(strWtr.toString());
	}

	public static void main(String a[]) throws JsonGenerationException, JsonMappingException, IOException {

		test();

	}
}