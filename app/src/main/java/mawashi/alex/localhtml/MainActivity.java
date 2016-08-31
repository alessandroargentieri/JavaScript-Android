package mawashi.alex.localhtml;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class MainActivity extends AppCompatActivity {

    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wv = (WebView)findViewById(R.id.webview);
        //setting the web Browser as Chrome allows to implements alerts in Javascript
        wv.setWebChromeClient(new WebChromeClient());
        //enable Javascript
        wv.getSettings().setJavaScriptEnabled(true);
        //passing an object which implements JavascriptInterface and then communicate to Javascript
        wv.addJavascriptInterface(new WebViewJavaScriptInterface(this), "AndroidObject");
        //just allows the zoom action
        wv.getSettings().setBuiltInZoomControls(true);

        wv.loadUrl("file:///android_asset/web/post_book_form.html");


    }


    //inner class
    /*
     * JavaScript Interface. Web code can access methods in here
     * (as long as they have the @JavascriptInterface annotation)
     */
    public class WebViewJavaScriptInterface{

        private Context context;

        /*
         * Need a reference to the context in order to sent a post message
         */
        public WebViewJavaScriptInterface(Context context){
            this.context = context;
        }

        /*
         * This method can be called from Android. @JavascriptInterface
         * required after SDK version 17.
         */
        @JavascriptInterface
        public void makeToast(String message, boolean lengthLong){
            Toast.makeText(context, message, (lengthLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();
        }

        @JavascriptInterface
        public void filePicker(){

        }


        //this Annotation means that this method is called from within the JavaScript loaded into WebView
        @JavascriptInterface
        public void CalledFromJavascript(final String param_from_js){
            try {
                runOnUiThread(new Runnable(){
                    public void run() {
                        TextView txtView = (TextView) findViewById(R.id.textResult);
                        txtView.setText(param_from_js);
                    }
                });
            }catch(Exception e){
                Toast.makeText(getApplicationContext(), "Error: " + e.toString(),Toast.LENGTH_LONG).show();
            }
        }




    }






}


