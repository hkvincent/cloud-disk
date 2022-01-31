package com.vincent.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.vincent.user.domain.User;


public class Constant {

	public static HashMap<String, Long> roleMap = new HashMap<String, Long>() {
		{
			put("normal", (long) (1024 * 1024 * 1024));
			put("permium", (long) (1024 * 1024 * 1024 * 5));
		}
	};

	public static long NORMALSIZE = 1024 * 1024 * 1024;
	public static long PERMIUMSIZE = NORMALSIZE * 5;

	public static String NORMAL = "normal";
	public static String PERMIUM = "permium";

	public static List<String> getRole() {

		List<String> roleList = new ArrayList<String>();
		roleList.add(NORMAL);
		roleList.add(PERMIUM);
		return roleList;
	}

	public static HashMap<String, Long> getRroleMap() {
		return roleMap;
	}

	public static double updateFileSize(User user) {
		HashMap<String, Long> roleMap = Constant.getRroleMap();
		long homeSpace = 0l;
		for (Entry<String, Long> role : roleMap.entrySet()) {
			if (user.getRole().equals(role.getKey())) {
				homeSpace = role.getValue();
				break;
			}
		}

		long remainSpace = 0l;
		if (homeSpace > 0) {
			remainSpace = homeSpace - Long.parseLong(user.getFileSize());
		}

		double percentSpace = ((double) remainSpace / (double) homeSpace) * 100;
		return percentSpace;
	}

	public static long getRoleSize(User user) {
		HashMap<String, Long> roleMap = Constant.getRroleMap();
		long homeSpace = 0l;
		for (Entry<String, Long> role : roleMap.entrySet()) {
			if (user.getRole().equals(role.getKey())) {
				homeSpace = role.getValue();
				break;
			}
		}
		return homeSpace;
	}
}
