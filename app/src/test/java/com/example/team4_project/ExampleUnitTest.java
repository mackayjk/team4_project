package com.example.team4_project;

import android.widget.Button;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExampleUnitTest {

    @Test
    public void testOneRandomRestaurantReturnedWhenButtonPressed(){
        assertEquals(getRandomRestaurant().length, 1);
    }

    @Test
    public void testGoogleAPIConnectsProperly(){
        assertTrue(connectToAPI());
    }

    @Test
    public void testDatabaseReturnsRestaurantTypeObject(){
        assertTrue(getRestaurant() instanceof Restaurant);
    }

}