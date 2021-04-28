package com.makeusteam.milliewillie.ui.info

import android.annotation.SuppressLint
import android.os.Build
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import androidx.annotation.RequiresApi
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityRuleWebViewBinding

class RuleWebViewActivity:BaseDataBindingActivity<ActivityRuleWebViewBinding>(R.layout.activity_rule_web_view) {


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    override fun ActivityRuleWebViewBinding.onBind() {
        vi = this@RuleWebViewActivity

        binding.ruleWebView.apply {
//            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            loadUrl("https://www.notion.so/6f20856604aa45c4a50836dd15362e3d")
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.builtInZoomControls = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.domStorageEnabled = true

        }


    }


}