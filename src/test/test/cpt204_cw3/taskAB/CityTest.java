package cpt204_cw3.taskAB;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CityTest {
    @Test
    public void testCityName() {
        City city = new City("New York", "Statue of Liberty");
        assertEquals("New York", city.getCityName());
        city.setCityName("Los Angeles");
        assertEquals("Los Angeles", city.getCityName());
    }

    @Test
    public void testCityNameWithNull() {
        City city = new City(null, null);
        assertNull(city.getCityName());
        city.setCityName("Chicago");
        assertEquals("Chicago", city.getCityName());
    }

    @Test
    public void testInterest() {
        City city = new City("Paris", "Eiffel Tower");
        assertEquals("Eiffel Tower", city.getInterest());
        city.setInterest("Louvre Museum");
        assertEquals("Louvre Museum", city.getInterest());
    }

    @Test
    public void testInterestWithNull() {
        City city = new City("Tokyo", null);
        assertNull(city.getInterest());
        city.setInterest("Shibuya Crossing");
        assertEquals("Shibuya Crossing", city.getInterest());
    }
}
