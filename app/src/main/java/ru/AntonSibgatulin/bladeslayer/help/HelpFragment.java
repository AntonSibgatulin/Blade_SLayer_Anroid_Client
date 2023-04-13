package ru.AntonSibgatulin.bladeslayer.help;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import ru.AntonSibgatulin.bladeslayer.MainActivity;
import ru.AntonSibgatulin.bladeslayer.R;


public class HelpFragment extends Fragment {

    String url = null;
    WebView webView = null;

    public HelpFragment(String url) {
        this.url = url;
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_help, container, false);

        webView = view.findViewById(R.id.MainWebView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        Button backButton = view.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.connection.mainActivity.startMenu();
            }
        });
        webView.setWebViewClient(new WebViewClient ());

        webView.loadUrl(url);


        return view;

    }
    WebViewClient webViewClient = new WebViewClient() {
        @SuppressWarnings("deprecation") @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("URL",url);
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N) @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    };
   /*
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
                    }
    }

    */
}