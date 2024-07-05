package org.cloud.sonic.driver.common.tool;

import com.github.zafarkhaja.semver.Version;

public class SemanticVersionTools {
	public static Version parseSemVer(final String s) {
		return Version.parse(s);
	} // end parseSemVer()
	
	public static Version getVersionCore(final Version v) {
		return Version.of(v.majorVersion(), v.minorVersion(), v.patchVersion());
	} // end getVersionCore()
	
	public static String pad4SemVer(final String s) {
		if (Version.isValid(s)) { return s; } // end if
		final String p[] = s.split("\\.");
		final String vv[] = new String[] {"0", "0", "0"};
		for (int i=0; i<3; i++) {
			try {
				vv[i] = String.format("%d", Integer.parseInt(p[i], 10));
			} catch (Exception e) {} // end try
		} // end for
		return String.join(".", vv);
	} // end pad4SemVer()
} // end class
