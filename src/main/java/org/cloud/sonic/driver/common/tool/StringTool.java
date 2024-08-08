package org.cloud.sonic.driver.common.tool;

import java.util.UUID;

public class StringTool {
	private static String[] CHARS = new String[] {
		"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
		"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
		"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
	};
	
	public static String generateShortUuid() { // http://www.java2s.com/example/java-utility-method/uuid-create/generateshortuuid-db743.html
        StringBuilder shortBuilder = new StringBuilder();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i=0; i<8; i++) {
            String str = uuid.substring(i*4, i*4+4);
            int x = Integer.parseInt(str, 16);
            shortBuilder.append(CHARS[x%0x3E]);
        } // end for
        return shortBuilder.toString();
    } // end generateShortUuid()
} // end class
