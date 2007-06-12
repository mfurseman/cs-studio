package org.csstudio.util.formula;


/** Named Variable.
 *  @author Kay Kasemir
 */
public class VariableNode implements Node
{
    /** Name of the variable. */
    private String name;
    
    /** Current value of the variable. */
    private double value;
    
    /** Create Variable with given name. */
    public VariableNode(String name)
    {
        this.name = name;
    }

    /** Create Variable with given name and value. */
    public VariableNode(String name, double value)
    {
        this.name = name;
        this.value = value;
    }

    /** @return Returns the name. */
    public String getName()
    {
        return name;
    }

    /** Set to a new value. */
    public void setValue(double value)
    {
        this.value = value;
    }

    /** @return Returns the value. */
    public double getValue()
    {
        return value;
    }

    public double eval()
    {
        return value;
    }
    
    /** {@inheritDoc} */
    public boolean hasSubnode(Node node)
    {
        return false;
    }
    
    @SuppressWarnings("nls")
    @Override
    public String toString()
    {
        return name;
    }
}
