package com.example.organdonation.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class WebServiceClient {

	public static String connect(String methodName, String[] args,
			String[] values) {

		try {
			SoapObject request = new SoapObject(Globals.NAMESPACE, methodName);
			int i = 0;

			for (String s : args) {
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.name = s;
				propInfo.type = PropertyInfo.STRING_CLASS;
				request.addProperty(propInfo, values[i]);
				i++;
			}

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					Globals.serviceUrl);
			androidHttpTransport.call(Globals.NAMESPACE + methodName, envelope);
			String resultsRequestSOAP = (String) envelope
					.getResponse(); // Receiving return string

			String result = resultsRequestSOAP;
			if (result != null) {
				return result;
			}
			return "";
		} catch (Exception ex) {
			Log.d("Url", Globals.serviceUrl);
			Log.d("Web Service Exception", ex.toString());
			return "";
		}
	}

	public static String GetSMS() {
		String[] args = new String[0];
		String[] values = new String[0];
		
		String result = connect("getSMS", args, values);
		return result;
	}
	
	
}
