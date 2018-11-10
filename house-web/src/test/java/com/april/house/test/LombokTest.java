package com.april.house.test;

import com.april.house.common.model.House;
import org.junit.Test;

public class LombokTest extends BaseTest {

    @Test
    public void testHouseFields() {
        String images = "/abc/1.jpg, /abc/2.jpg, /abc/3.jpg";
        House house = new House();
        house.setImages(images);
        house.setFloorPlan(images);

    }
}
