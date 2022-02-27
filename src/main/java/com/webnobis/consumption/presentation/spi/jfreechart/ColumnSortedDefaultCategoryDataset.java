package com.webnobis.consumption.presentation.spi.jfreechart;

import java.lang.reflect.Field;
import java.util.Collections;

import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Column sortable extension
 * 
 * @author steffen
 *
 */
public class ColumnSortedDefaultCategoryDataset extends DefaultCategoryDataset {

	private static final long serialVersionUID = 1L;

	private final ColumnSortedDefaultKeyedValues2D columnSortedDefaultKeyedValues2D;

	/**
	 * Sets the internal data field with key value extension
	 * 
	 * @see ColumnSortedDefaultKeyedValues2D
	 */
	public ColumnSortedDefaultCategoryDataset() {
		super();
		columnSortedDefaultKeyedValues2D = new ColumnSortedDefaultKeyedValues2D();
		try {
			Field dataField = DefaultCategoryDataset.class.getDeclaredField("data");
			dataField.setAccessible(true);
			dataField.set(this, columnSortedDefaultKeyedValues2D);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalStateException("DefaultCategoryDataset.data not modificable");
		}
	}

	/**
	 * Sorts the columns
	 */
	public void sort() {
		Collections.sort(columnSortedDefaultKeyedValues2D.superColumnKeys);
	}

}