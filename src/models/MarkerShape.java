package models;

public enum MarkerShape {
    CHECKMARK ("checkmark.png"),
    X ("x.png"), 
    O ("o.png"),
    STAR ("Star.png"),
    FILLED ("ColorFilled.png"),
    OUTLINE ("OutlineFilled.png"),
    FROWNY ("FrownyFace.png"),
    SMILEY ("SmileyFace.png"),
    DRAGON ("Dragon.png"),
    CAT ("CatFace.png");

    private final String filename;

    MarkerShape(String filename){
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
