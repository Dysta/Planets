package planets.ui;

import javafx.scene.control.TextField;

/**
 * A TextField that accepts only numeric characters
 * 
 * @author Adri and https://stackoverflow.com/a/18959399
 */
public class NumericField extends TextField
{
    
    public NumericField(int baseValue) {
        super();
        this.setText(Integer.toString(baseValue));
    }
    public NumericField() {
        this(1);
    }

    /**
     * replace the current text
     * @param start the start indice of the box
     * @param end the end indice of the box
     * @param text the text to set
     */
    @Override
    public void replaceText(int start, int end, String text)
    {
        if (validate(text))
        {
            super.replaceText(start, end, text);
        }
    }

    /**
     * replace the selection by a gived text
     * @param text the new text
     */
    @Override
    public void replaceSelection(String text)
    {
        if (validate(text))
        {
            super.replaceSelection(text);
        }
    }

    /**
     * check if a gived text is valide (only numeric field)
     * @param text the text to check
     * @return true if is valide, false otherwise
     */
    private boolean validate(String text)
    {
        return text.matches("[0-9]*");
    }
}