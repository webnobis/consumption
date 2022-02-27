package com.webnobis.consumption.presentation.spi.jfreechart;

import java.lang.reflect.Field;
import java.util.List;

import org.jfree.data.DefaultKeyedValues2D;

/**
 * Key value extension
 * 
 * @author steffen
 *
 */
public class ColumnSortedDefaultKeyedValues2D extends DefaultKeyedValues2D {

	List superColumnKeys;

	/**
	 * Opens the columns keys
	 */
	ColumnSortedDefaultKeyedValues2D() {
		super();
		try {
			Field columnKeysField = DefaultKeyedValues2D.class.getDeclaredField("columnKeys");
			columnKeysField.setAccessible(true);
			superColumnKeys = (List) columnKeysField.get(this);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalStateException("DefaultKeyedValues2D.columnKeys not modificable");
		}
	}

}