package com.example.organdonation.utils;

import java.util.regex.Pattern;


public class Globals {

	public static final String IP_KEY = "ip";
	public static String PREFERENCE_NAME = "ORGAN";
	
	public static String SERVER_IP = "192.168.43.220";

	public static String NAMESPACE = "http://" + SERVER_IP + "/soap/organdonation:service";

	public static String serviceUrl = "http://" + SERVER_IP + "/Organ/organ/service/service.php?wsdl";

	public static void setServer(String ip) {
		SERVER_IP = ip;
		serviceUrl = "http://" + ip + "/Organ/organ/service/service.php?wsdl";
		NAMESPACE = "http://" + ip + "/soap/organdonation:service";
		
	}

	public static boolean ip_validate(String ip_address) {

		return Pattern
				.compile(
						"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\"
								+ ".([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")
				.matcher(ip_address).matches();

	}


}
