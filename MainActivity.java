package jacky.idv.snaptodoor;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /*
    private final static int FILECHOOSER_RESULTCODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView myWebView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        myWebView.setWebViewClient(new WebViewClient());
        myWebView.loadUrl("https://snaptodoor.com");
    }*/

    private ValueCallback<Uri> mUploadMessage;
    private final static int FILECHOOSER_RESULTCODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null
                    : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        }
    }

    private class MyWebChromeClient extends WebChromeClient {



        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            MainActivity.this.startActivityForResult(Intent.createChooser(i, "Image Browser"), FILECHOOSER_RESULTCODE);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message,final JsResult result) {
            //handle Alert event, here we are showing AlertDialog
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("JavaScript Alert !")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok,
                            new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do your stuff
                                    result.confirm();
                                }
                            }).setCancelable(false).create().show();
            return true;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webView.setVerticalScrollBarEnabled(false);



       // webView.setScrollBarStyle(View.);

        //View decorView = getWindow().getDecorView();




// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.

        /*
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        */




        // webView.setWebViewClient(new WebViewClient());




/*
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(ProgressBar.VISIBLE);

                Log.d("Test",url);

                if (Uri.parse(url).getHost().equals("snaptodoor.com")) {
                    // This is my web site, do not override; let WebView load the page

                    Log.d("Test","Return False");
                    return false;
                }
                // Otherwise, the link is not for a page on my site
                // Launch another Activity that handles URLs
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                MainActivity.this.startActivityForResult(Intent.createChooser(i, "Image Browser"), FILECHOOSER_RESULTCODE);

                Log.d("Test","Return True");
                return true;
            }


            public void onFormResubmission(WebView view, Message dontResend, Message resend)
            {
                Log.d("Test","onFormResubmission");
               // resend.sendToTarget();
            }


        });
*/

        webView.setWebChromeClient(new MyWebChromeClient(){


            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
                if(progress < 100 && progressBar.getVisibility() == ProgressBar.GONE){
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                }
                progressBar.setProgress(progress);
                if(progress == 100) {
                    progressBar.setVisibility(ProgressBar.GONE);
                }
            }
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                Log.d("Test","openFileChooser");

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                MainActivity.this.startActivityForResult(Intent.createChooser(i, "Image Browser"), FILECHOOSER_RESULTCODE);
            }

            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                Log.d("Test", message + " -- From line "
                        + lineNumber + " of "
                        + sourceID);
            }
        });




        webView.loadUrl("https://snaptodoor.com");

    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if(((WebView)findViewById(R.id.webview)).canGoBack()){
                ((WebView)findViewById(R.id.webview)).goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_istyla, menu);
        return true;
    }*/

}
