package com.example.a05_implementazionealgoritmo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

@Entity
public class EntityTest {
    @PrimaryKey (autoGenerate = true)
    public Integer test_id;
    public String test_values;
    public Integer number_of_steps;
    public String additional_notes;
    public String file_name;
}
