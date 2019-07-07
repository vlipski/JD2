package by.pvt.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Word {
    private int id;
    private String en;
    private String rus;


    public Word(String en, String rus) {
        this.en = en;
        this.rus = rus;
    }
}
