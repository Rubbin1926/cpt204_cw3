package cpt204_cw3;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CityTest {
    @Test
    public void testCityName() {
        City city = new City("New York");
        assertEquals("New York", city.getCityName());
        city.setCityName("Los Angeles");
        assertEquals("Los Angeles", city.getCityName());
    }

    @Test
    public void testCityNameWithNull() {
        City city = new City(null);
        assertNull(city.getCityName());
        city.setCityName("Chicago");
        assertEquals("Chicago", city.getCityName());
    }
}
