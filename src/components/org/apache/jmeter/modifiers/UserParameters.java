package org.apache.jmeter.modifiers;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class UserParameters extends ConfigTestElement implements Serializable, PreProcessor
{

    public static final String NAMES = "UserParameters.names";
    public static final String THREAD_VALUES = "UserParameters.thread_values";
    private int counter = 0;
    /**
     * @see org.apache.jmeter.config.Modifier#modifyEntry(Sampler)
     */
    public boolean modifyEntry(Sampler Sampler)
    {
        return false;
    }

    public CollectionProperty getNames()
    {
        return (CollectionProperty) getProperty(NAMES);
    }

    public CollectionProperty getThreadLists()
    {
        return (CollectionProperty) getProperty(THREAD_VALUES);
    }

    /**
     * The list of names of the variables to hold values.  This list must come in
     * the same order as the sub lists that are given to setThreadLists(List).
     */
    public void setNames(Collection list)
    {
        setProperty(new CollectionProperty(NAMES, list));
    }

    /**
         * The list of names of the variables to hold values.  This list must come in
         * the same order as the sub lists that are given to setThreadLists(List).
         */
    public void setNames(CollectionProperty list)
    {
        setProperty(list);
    }

    /**
     * The thread list is a list of lists.  Each list within the parent list is a
     * collection of values for a simulated user.  As many different sets of 
     * values can be supplied in this fashion to cause JMeter to set different 
     * values to variables for different test threads.
     */
    public void setThreadLists(Collection threadLists)
    {
        setProperty(new CollectionProperty(THREAD_VALUES, threadLists));
    }

    /**
         * The thread list is a list of lists.  Each list within the parent list is a
         * collection of values for a simulated user.  As many different sets of 
         * values can be supplied in this fashion to cause JMeter to set different 
         * values to variables for different test threads.
         */
    public void setThreadLists(CollectionProperty threadLists)
    {
        setProperty(threadLists);
    }

    private synchronized CollectionProperty getValues()
    {
        CollectionProperty threadValues = (CollectionProperty) getProperty(THREAD_VALUES);
        if (threadValues.size() > 0)
        {
            return (CollectionProperty) threadValues.get(counter % threadValues.size());
        }
        else
        {
            return new CollectionProperty("noname", new LinkedList());
        }
    }

    public void process()
    {
        PropertyIterator namesIter = getNames().iterator();
        PropertyIterator valueIter = getValues().iterator();
        JMeterVariables jmvars = JMeterContextService.getContext().getVariables();
        while (namesIter.hasNext() && valueIter.hasNext())
        {
            String name = namesIter.next().getStringValue();
            String value = valueIter.next().getStringValue();
            jmvars.put(name, value);
        }
    }

}
