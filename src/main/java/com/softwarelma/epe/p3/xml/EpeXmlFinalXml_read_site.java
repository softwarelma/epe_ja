package com.softwarelma.epe.p3.xml;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppLogger;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeXmlFinalXml_read_site extends EpeXmlAbstract {

	// maybe also nonProxyHosts and java.net.useSystemProxies
	public static final String PROP_PROXY_HOST = "proxyHost";
	public static final String PROP_PROXY_PORT = "proxyPort";
	public static final String PROP_PROXY_USER = "proxyUser";
	public static final String PROP_PROXY_PASSWORD = "proxyPassword";

	/*
	 * "http.nonProxyHosts", "http.proxyHost", "http.proxyPort", "http.proxyUser", "http.proxyPassword", "https.nonProxyHosts", "https.proxyHost",
	 * "https.proxyPort", "https.proxyUser", "https.proxyPassword", "java.net.useSystemProxies"
	 */

	@Override
	public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
		String postMessage = "xml_read_site, expected the URL.";
		String host = retrievePropValueOrNull("xml_read_site", listExecResult, EpeXmlFinalXml_read_site.PROP_PROXY_HOST);
		String port = retrievePropValueOrNull("xml_read_site", listExecResult, EpeXmlFinalXml_read_site.PROP_PROXY_PORT);
		String username = retrievePropValueOrNull("xml_read_site", listExecResult, EpeXmlFinalXml_read_site.PROP_PROXY_USER);
		String password = retrievePropValueOrNull("xml_read_site", listExecResult, EpeXmlFinalXml_read_site.PROP_PROXY_PASSWORD);
		List<String> listSite = readSite(execParams, listExecResult, postMessage, host, port, username, password);

		if (listSite.size() == 1) {
			String str = listSite.get(0);
			log(execParams, str);
			return createResult(str);
		}

		log(execParams, listSite);
		return createResult(listSite);
	}

	public List<String> readSite(EpeExecParams execParams, List<EpeExecResult> listExecResult, String postMessage, String host, String port, String username,
			String password) throws EpeAppException {
		EpeAppUtils.checkNull("listExecResult", listExecResult);
		List<String> listSite = new ArrayList<>();
		String url;

		for (int i = 0; i < listExecResult.size(); i++) {
			if (isPropAt(listExecResult, i, postMessage)) {
				continue;
			}

			url = getStringAt(listExecResult, i, postMessage);
			log(execParams, "URL: " + url);
			String site = readSite(url, host, port, username, password);
			log(execParams, "Content:");
			log(execParams, site);
			listSite.add(site);
		}

		return listSite;
	}

	// TODO post, from:
	// https://stackoverflow.com/questions/4205980/java-sending-http-parameters-via-post-method-easily
	// or:
	// https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/

	/*
	 * from:
	 * 
	 * 1- Java Networking and Proxies: http://docs.oracle.com/javase/7/docs/technotes/guides/net/proxies.html
	 * 
	 * 2- stackoverflow example: https://stackoverflow.com/questions/1432961/how-do-i-make-httpurlconnection-use-a-proxy
	 */

	public static String readSite(String url, String host, String port, String username, String password) throws EpeAppException {
		try {
			Proxy proxy = retrieveProxy(host, port, username, password);
			URL url2 = new URL(url);
			URLConnection conn = proxy == null ? url2.openConnection() : url2.openConnection(proxy);
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuilder sb = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
				sb.append("\n");
			}

			in.close();
			return sb.toString();
		} catch (IOException e) {
			throw new EpeAppException("readSite url: " + url, e);
		}
	}

	public static String readSite(String url) throws EpeAppException {
		return readSite(url, null, null, null, null);
	}

	public static String readSiteHeaderAttributeCustomWre(boolean doLog, String url) throws EpeAppException {
		Map<String, List<String>> headers = readSiteHeaders(doLog, url, null, null, null, null);
		String filename = readSiteHeaderAttribute(doLog, headers, "Content-Disposition", 0, "filename=", "\"", "");
		if (doLog)
			EpeAppLogger.log("readSiteHeaderAttributeCustomWre filename: " + filename);
		return filename;
	}

	public static String readSiteHeaderAttribute(boolean doLog, Map<String, List<String>> headers, String key, int valueIndex, String valueSplit,
			String valueReplaceTarget, String valueReplacement) {
		String headerAttribute = headers.get(key).get(valueIndex);
		if (doLog)
			EpeAppLogger.log("readSiteHeaderAttribute " + valueSplit + " " + headerAttribute);
		return headerAttribute.split(valueSplit)[1].replace(valueReplaceTarget, valueReplacement);
	}

	public static Map<String, List<String>> readSiteHeaders(boolean doLog, String url, String host, String port, String username, String password)
			throws EpeAppException {
		try {
			Proxy proxy = retrieveProxy(host, port, username, password);
			URL url2 = new URL(url);
			URLConnection conn = proxy == null ? url2.openConnection() : url2.openConnection(proxy);
			Map<String, List<String>> headers = conn.getHeaderFields();
			if (doLog) {
				EpeAppLogger.log("readSiteHeaders url: " + url);
				StringBuilder sb = new StringBuilder("{");
				String sep = "\n    ";
				for (String key : headers.keySet()) {
					sb.append(sep);
					sep = ",\n    ";
					sb.append(key);
					sb.append("=");
					sb.append(headers.get(key));
				}
				sb.append("\n}");
				EpeAppLogger.log("readSiteHeaders headers: \n" + sb.toString() + "\n");
			}
			return headers;
		} catch (IOException e) {
			throw new EpeAppException("readSiteHeaders url: " + url, e);
		}
	}

	// FIXME ko
	public static String writeSite(String urlStr, String host, String port, String username, String password) throws EpeAppException {
		try {
			String urlParameters = "text1=abc&text2=def";
			byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
			int postDataLength = postData.length;
			URL url = new URL(urlStr);
			Proxy proxy = retrieveProxy(host, port, username, password);
			URLConnection conn0 = proxy == null ? new URL(urlStr).openConnection() : new URL(urlStr).openConnection(proxy);
			HttpURLConnection conn = (HttpURLConnection) conn0;
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "UTF-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			StringBuilder sb = new StringBuilder();

			try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
				wr.write(postData);
				Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				for (int c; (c = in.read()) >= 0;)
					sb.append((char) c);
				// System.out.println(sb);
			}

			return sb.toString();
		} catch (Exception e) {
			throw new EpeAppException("???", e);
		}
	}

	@Deprecated
	public static String readSiteOld(String url) throws EpeAppException {
		EpeAppUtils.checkEmpty("url", url);

		try {
			URL oracle = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
			String inputLine;
			StringBuilder sb = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
				sb.append("\n");
			}

			in.close();
			return sb.toString();
		} catch (MalformedURLException e) {
			throw new EpeAppException("readSite url: " + url, e);
		} catch (IOException e) {
			throw new EpeAppException("readSite url: " + url, e);
		}
	}

	public static Proxy retrieveProxy(String host, String port, String username, String password) throws EpeAppException {
		if (host == null || port == null) {
			return null;
		}

		setAuthenticator(username, password);
		int portInt = EpeAppUtils.parseInt(port);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, portInt));
		return proxy;
	}

	public static void setAuthenticator(final String username, final String password) {
		if (username == null || password == null) {
			return;
		}

		Authenticator authenticator = new Authenticator() {

			public PasswordAuthentication getPasswordAuthentication() {
				// or "domainName\\user"
				return (new PasswordAuthentication(username, password.toCharArray()));
			}

		};

		Authenticator.setDefault(authenticator);
	}

}
