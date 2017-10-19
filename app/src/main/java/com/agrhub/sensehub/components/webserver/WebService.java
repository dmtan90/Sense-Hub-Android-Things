package com.agrhub.sensehub.components.webserver;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.util.Log;

import com.agrhub.sensehub.components.devicemanager.DeviceManager;
import com.agrhub.sensehub.components.util.FileUtils;
import com.agrhub.sensehub.components.util.NetworkUtils;
import com.agrhub.sensehub.components.wifi.WifiManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by tanca on 10/15/2017.
 */


public class WebService extends NanoHTTPD {
    private final static int PORT = 8433;
    private Context mContext = null;

    private static SSLServerSocketFactory sslServerSocketFactory;
    private static SSLContext sslContext;

    /**
     * Keystore is valid for 25 years from 4/21/16, expiring April 21, 2041
     * The passPhrase is used to confirm the validity of the keystore
     */
    private static String keyFile = "/assets/key/www_agrhub_com.bks";
    private static char[] keystoreValidationKey = "agrhub.com".toCharArray();

    private Response.Status HTTP_OK = Response.Status.OK;
    private Response.Status HTTP_NOT_FOUND = Response.Status.NOT_FOUND;
    private Response.Status HTTP_BAD_REQUEST = Response.Status.BAD_REQUEST;
    private Response.Status HTTP_INTERNAL_ERROR = Response.Status.INTERNAL_ERROR;

    public static final String
            MIME_PLAINTEXT = "text/plain",
            MIME_HTML = "text/html",
            MIME_JS = "application/javascript",
            MIME_JSON = "application/json",
            MIME_CSS = "text/css",
            MIME_PNG = "image/png",
            MIME_ICON = "image/x-icon",
            MIME_SVG = "image/svg+xml",
            MIME_WOFF2 = "application/font-woff2",
            MIME_DEFAULT_BINARY = "application/octet-stream",
            MIME_XML = "text/xml";

    public WebService(Context ctx) throws IOException {
        super(PORT);
        //configureSSL(keyFile, keystoreValidationKey);
        //this.makeSecure( sslServerSocketFactory, null);
        //this.setServerSocketFactory(new SecureServerSocketFactory(NanoHTTPD.makeSSLSocketFactory("/" + f.getName(), "agrhub.com".toCharArray()), null));
        mContext = ctx;
        start();
        String ipAddress = NetworkUtils.getIPV4Address(ctx);
        Log.d(getClass().getSimpleName(), "Start web server host: http://" + ipAddress + ":" + PORT);
    }

    /**
     * Creates an SSLSocketFactory for HTTPS.
     *
     * Pass a KeyStore resource with your certificate and passphrase
     */
    public static void configureSSL(String keyAndTrustStoreClasspathPath, char[] passphrase) throws IOException {

        try {
            // Android does not have the default jks but uses bks
            KeyStore keystore = KeyStore.getInstance("BKS");
            InputStream keystoreStream = WebService.class.getResourceAsStream(keyAndTrustStoreClasspathPath);
            keystore.load(keystoreStream, passphrase);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keystore);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keystore, passphrase);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            sslServerSocketFactory = sslContext.getServerSocketFactory();

        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Method method = session.getMethod();
        Map<String, String> params = session.getParms();
        InputStream mBuffer = null;
        String mMimeType = MIME_PLAINTEXT;
        Response.Status mStatus = HTTP_OK;
        boolean isGzip = false;
        try{
            String assetPath = "";
            if(uri.equals("/") || uri.equals("/index.html")){
                assetPath = "www/index.html";
                mMimeType = MIME_HTML;
            }
            else if(uri.equals("/favicon.ico") || uri.equals("/img/favicon.ico")){
                assetPath = "www/img/favicon.ico";
                mMimeType = MIME_ICON;
            }
            else if(uri.equals("/img/logo.png")){
                assetPath = "www/img/logo.png";
                mMimeType = MIME_PNG;
            }
            else if(uri.equals("/css/style.min.css")){
                assetPath = "www/css/style.min.css";
                mMimeType = MIME_CSS;
                isGzip = true;
            }
            else if(uri.equals("/fonts/material-icons.svg")){
                assetPath = "www/fonts/material-icons.svg";
                mMimeType = MIME_SVG;
            }
            else if(uri.equals("/fonts/material-icons.woff2")){
                assetPath = "www/fonts/material-icons.woff2";
                mMimeType = MIME_WOFF2;
            }
            else if(uri.equals("/js/core.js")){
                assetPath = "www/js/core.js";
                mMimeType = MIME_JS;
            }
            else if(uri.equals("/js/device.min.js")){
                assetPath = "www/js/device.min.js";
                mMimeType = MIME_JS;
                isGzip = true;
            }
            else if(uri.equals("/js/init.min.js")){
                assetPath = "www/js/init.min.js";
                mMimeType = MIME_JS;
                isGzip = true;
            }
            else if(uri.equals("/js/jquery.min.js")){
                assetPath = "www/js/jquery.min.js";
                mMimeType = MIME_JS;
                isGzip = true;
            }
            else if(uri.equals("/js/lang.min.js")){
                assetPath = "www/js/lang.min.js";
                mMimeType = MIME_JS;
                isGzip = true;
            }
            else if(uri.equals("/js/materialize.1.min.js")){
                assetPath = "www/js/materialize.1.min.js";
                mMimeType = MIME_JS;
                isGzip = true;
            }
            else if(uri.equals("/js/materialize.2.min.js")){
                assetPath = "www/js/materialize.2.min.js";
                mMimeType = MIME_JS;
                isGzip = true;
            }
            else if(uri.equals("/api/v1/get_data")){
                return apiGetData(session);
            }
            else if(uri.equals("/api/v1/devices")){
                return apiGetDevices(session);
            }
            else if(uri.equals("/api/v1/scan_wifi")){
                return apiGetWifis(session);
            }

            if(!assetPath.equals("")){
                mBuffer = mContext.getAssets().open(assetPath);
            }

            if(mBuffer  == null){
                mStatus = HTTP_NOT_FOUND;
                String mString = "FILE NOT FOUND";
                try{
                    mBuffer = new ByteArrayInputStream(mString.getBytes("UTF-8"));
                }catch (Exception e){

                }
            }
        }catch (Exception e){

        }


        Response response = newChunkedResponse(mStatus, mMimeType, mBuffer);
        if(isGzip){
            response.addHeader("Content-Encoding", "gzip");
        }
        return response;
    }

    public Response apiGetData(IHTTPSession session){
        String uri = session.getUri();
        Method method = session.getMethod();
        Map<String, String> params = session.getParms();
        InputStream mBuffer = null;
        String mMimeType = MIME_JSON;
        Response.Status mStatus = HTTP_OK;
        try{
            String mJson = DeviceManager.instance.getGatewayInfo();
            mBuffer = new ByteArrayInputStream(mJson.getBytes("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }

        Response response = newChunkedResponse(mStatus, mMimeType, mBuffer);
        return response;
    }

    public Response apiGetDevices(IHTTPSession session){
        String uri = session.getUri();
        Method method = session.getMethod();
        Map<String, String> params = session.getParms();
        InputStream mBuffer = null;
        String mMimeType = MIME_JSON;
        Response.Status mStatus = HTTP_OK;
        try{
            String mJson = DeviceManager.instance.getDevicesJson();
            mBuffer = new ByteArrayInputStream(mJson.getBytes("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }

        Response response = newChunkedResponse(mStatus, mMimeType, mBuffer);
        return response;
    }

    public Response apiGetWifis(IHTTPSession session){
        InputStream mBuffer = null;
        String mMimeType = MIME_JSON;
        Response.Status mStatus = HTTP_OK;
        try{
            List<ScanResult> lstWifi = WifiManager.instance.scanWifi();
            String mJson = "[";
            for(ScanResult rs : lstWifi){
                if(!mJson.equals("[")){
                    mJson += ",";
                }

                mJson += String.format("{\"name\":\"%s\",\"signal\":\"%d\",\"encrypted\":%s}",
                        rs.SSID, rs.level, rs.capabilities.substring(0,4).equals("NONE") ? 0 : 1);
            }
            mJson += "]";
            mBuffer = new ByteArrayInputStream(mJson.getBytes("UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }

        Response response = newChunkedResponse(mStatus, mMimeType, mBuffer);
        return response;
    }
}
