package ui;

import javafx.scene.control.TextField;

// https://stackoverflow.com/a/18959399
public class NumericField extends TextField
{
    
    public NumericField(int baseValue) {
        super();
        this.setText(Integer.toString(baseValue));
    }
    public NumericField() {
        this(1);
    }

    @Override
    public void replaceText(int start, int end, String text)
    {
        if (validate(text))
        {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text)
    {
        if (validate(text))
        {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String text)
    {
        return text.matches("[0-9]*");
    }
}