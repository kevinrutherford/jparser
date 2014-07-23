package javancss;

/**
 * Basic data class to store all metrics attached to a function.
 *
 * @author  Hervé Boutemy
 * @version $Id: FunctionMetric.java 301 2014-07-12 13:32:33Z pkofler $
 */
public class FunctionMetric
    extends Metric
{
    public int ccn = 0;

    public FunctionMetric()
    {
        super();
    }

    @Override
    public void clear()
    {
        super.clear();
        ccn = 0;
    }
}
