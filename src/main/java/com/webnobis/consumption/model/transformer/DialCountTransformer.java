package com.webnobis.consumption.model.transformer;

import java.util.Optional;

public abstract class DialCountTransformer {

	public static float toFloat(String dialCount) {
		String text = Optional.ofNullable(dialCount).orElse("0").replaceAll(",", ".");
		int index = text.lastIndexOf('.');
		try {
			if (index > 0) {
				String text1 = text.substring(0, index).replaceAll("\\.", "");
				String text2 = text.substring(index);
				return Float.parseFloat(text1 + text2);
			} else {
				return Float.parseFloat(text);
			}
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public static String toText(float dialCount) {
		int i = Math.round(dialCount * 10);
		String text = String.valueOf(i);
		if (text.length() > 1) {
			return text.substring(0, text.length() - 1) + '.' + text.charAt(text.length() - 1);
		} else {
			return "0." + text;
		}
	}

	private DialCountTransformer() {
	}

}
