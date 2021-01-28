package com.example.a05_implementazionealgoritmo;

public class Card_Test {
    private String test_id;
    private String test_values;
    private Integer number_of_steps;
    private String additional_notes;
    private Boolean selected;
    private String file_path_name;

    public Card_Test(String test_id, String test_values, Integer number_of_steps, String additional_notes, String file_path_name) {
        this.test_id = test_id;
        this.test_values = test_values;
        this.number_of_steps = number_of_steps;
        this.additional_notes = additional_notes;
        this.file_path_name = file_path_name;
        this.selected = false;
    }

    public String getTest_id() {
        return test_id;
    }

    public String getTest_values() {
        return test_values;
    }

    public Integer getNumber_of_steps() {
        return number_of_steps;
    }

    public String getAdditional_notes() {
        return additional_notes;
    }

    public Boolean getSelected() {
        return selected;
    }

    public String getFile_path_name() {
        return file_path_name;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
