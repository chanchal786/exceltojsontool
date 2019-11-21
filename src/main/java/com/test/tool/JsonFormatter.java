package com.test.tool;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonFormatter {
	public static void main(String args[]) {

        int[] recentScores = {77, 72, 23, 57, 54, 36, 74, 17};
        Player cricketer = new Player("Virat", "cricket", 25, 121,
                                          recentScores);

        ObjectMapper mapper = new ObjectMapper();

        try {
			/*
			 * System.out.println("Default JSON String:" +
			 * mapper.writeValueAsString(cricketer));
			 */
            System.out.println("formatted JSON String \n" 
        + mapper.defaultPrettyPrintingWriter().writeValueAsString(cricketer));
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

class Player {

    private String name;
    private String sport;
    private int age;
    private int id;
    private int[] lastScores;

    public Player(String name, String sport, int age, int id, 
                     int[] lastinnings) {
        this.name = name;
        this.sport = sport;
        this.age = age;
        this.id = id;
        lastScores = lastinnings;
    }

    public final String getName() {
        return name;
    }

    public final String getSport() {
        return sport;
    }

    public final int getAge() {
        return age;
    }

    public final int getId() {
        return id;
    }

    public final int[] getLastScores() {
        return lastScores;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final void setSport(String sport) {
        this.sport = sport;
    }

    public final void setAge(int age) {
        this.age = age;
    }

    public final void setId(int id) {
        this.id = id;
    }

    public final void setLastScores(int[] lastScores) {
        this.lastScores = lastScores;
    }

    @Override
    public String toString() {
        return "Player [name=" + name + ", sport=" + sport 
                   + ", age=" + age + ", id=" + id + "]";
    }

}