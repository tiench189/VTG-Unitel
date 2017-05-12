package com.vtg.app.util;

import java.net.URI;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;

import android.util.Log;

import com.vtg.app.model.ModelParam;
import com.vtg.app.model.ModelTag;

public class SOAPUtil {

	private static final String TAG = "SOAPUtil";

	public static final String WSDL_URL = "http://183.182.100.169:8999/BCCSGateway?wsdl";
	public static final String WS_NAMESPACE = "http://webservice.bccsgw.viettel.com/";

	public static final String PREFIX_REQUEST = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.bccsgw.viettel.com/\">"
			+ "<soapenv:Header/>"
			+ "<soapenv:Body>"
			+ "<web:gwOperation>"
			+ "<Input>";

	public static final String SUFFIX_REQUEST = "</Input>"
			+ "</web:gwOperation>" + "</soapenv:Body>" + "</soapenv:Envelope>";

	public static String createTag(ModelTag tag) {
		return "<" + tag.tag + ">" + tag.value + "</" + tag.tag + ">";
	}

	public static String createParam(ModelParam param) {
		return "<param name=\"" + param.name + "\" value=\"" + param.value
				+ "\"/>";
	}

	public String wscode;
	public List<ModelTag> tags;
	public List<ModelParam> params;

	public Document mDoc;
	public String request;
	public String result;

	public SOAPUtil(String wscode, List<ModelTag> tags, List<ModelParam> params) {
		this.wscode = wscode;
		this.tags = tags;
		this.params = params;
	}

	public String createRequest() {
		request = "";
		request += PREFIX_REQUEST;
		request += "<wscode>" + wscode + "</wscode>";
		for (int i = 0; i < tags.size(); i++) {
			request += createTag(tags.get(i));
		}
		for (int i = 0; i < params.size(); i++) {
			request += createParam(params.get(i));
		}
		request += SUFFIX_REQUEST;
		Log.d(TAG, "tiench request: " + request);
		return request;
	}

	@SuppressWarnings("deprecation")
	public String makeSOAPRequest() {
		String request = createRequest();
		result = "";
		try {
			String soapAction = WS_NAMESPACE;

			StringEntity se = new StringEntity(request, HTTP.UTF_8);
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(new URI(WSDL_URL));

			httpPost.addHeader("Content-Type", "text/xml; charset=utf-8");
			httpPost.addHeader("SOAPAction", soapAction);

			httpPost.setEntity(se);
			HttpResponse response = client.execute(httpPost);
			int responseStatusCode = response.getStatusLine().getStatusCode();
			Log.d(TAG, "HTTP Status code:" + responseStatusCode);
			if (responseStatusCode >= 200 && responseStatusCode < 300) {
				String responseStr = EntityUtils.toString(response.getEntity());
				result = responseStr;
				Log.i(TAG, "tiench response root: " + result);
				mDoc = getDocumentFromXML();
			}
		} catch (Exception e) {
			Log.e("Response Exception", "tiench error: " + e.getMessage() + "",
					e);
		}
		return result;
	}

	public Document getDocumentFromXML() {
		Document doc = XMLParser.getDomElement(result);
		String apiResponse = XMLParser.getValueFromDoc(doc, "original");
		Log.d(TAG, "Tiench " + wscode + " Response:: " + apiResponse);
		Document resDoc = XMLParser.getDomElement(apiResponse);
		return resDoc;
	}

	public int getError() {
		int err = -1;

		try {
			Document doc = XMLParser.getDomElement(result);
			String errCode = XMLParser.getValueFromDoc(doc, "error");
			err = Integer.parseInt(errCode);
		} catch (Exception e) {

		}
		return err;
	}

	public String getValue(String str) {
		return XMLParser.getValueFromDoc(mDoc, str);
	}
}
