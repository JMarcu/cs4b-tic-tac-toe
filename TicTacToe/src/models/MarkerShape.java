package models;

public enum MarkerShape {
    CHECKMARK ("checkmark.png"),
    X ("x.png"), 
    O ("o.png");

    private final String filename;

    MarkerShape(String filename){
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
