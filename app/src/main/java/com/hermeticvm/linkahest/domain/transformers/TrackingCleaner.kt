package com.hermeticvm.linkahest.domain.transformers

import android.net.Uri
import java.net.URLDecoder
import java.net.URLEncoder

object TrackingCleaner {
    
    private val TRACKING_PARAMS = setOf(
        // Google Analytics
        "utm_source", "utm_medium", "utm_campaign", "utm_term", "utm_content", "utm_id",
        "gclid", "gclsrc",
        
        // Facebook
        "fbclid", "fb_source", "fb_ref",
        
        // YouTube
        "feature", "si", "pp", "ref",
        
        // Twitter/X
        "t", "s", "ref_src", "ref_url",
        
        // Microsoft
        "msclkid", "ocid",
        
        // Instagram
        "igshid",
        
        // LinkedIn
        "li_fat_id",
        
        // Vero
        "vero_id", "vero_conv",
        
        // HubSpot
        "mkt_tok", "hs_email_id", "hsenc", "_hsenc", "hsmi", "hsCtaTracking",
        
        // Mailchimp
        "mc_cid", "mc_eid",
        
        // Generic tracking
        "ek", "sc", "source", "ef_id", "ev", "trk", "cmp", "ch", "rid", "jid",
        "partner", "aff", "ref", "subid", "clickid", "campaign_id",
        "ad_id", "creative", "keyword", "placement", "position",
        "device", "matchtype", "network", "target", "adgroupid"
    )
    
    private val CLOAKED_PATH_PATTERNS = listOf(
        Regex("""/(?:l|r|go|out|track|clk)/(https?://.+)$"""),
        Regex("""/(?:redirect|redir)/(https?://.+)$"""),
        Regex("""/(?:click|link)/(https?://.+)$""")
    )
    
    fun cleanUrl(url: String): String {
        if (url.isBlank()) return url
        
        try {
            var cleanedUrl = url
            
            // Step 1: Resolve redirects (simplified - we'll handle this in the transformer)
            cleanedUrl = resolveRedirects(cleanedUrl)
            
            // Step 2: Parse and clean the URL
            val uri = Uri.parse(cleanedUrl)
            
            // Step 3: Clean query parameters
            val cleanQuery = cleanQueryParameters(uri.query)
            
            // Step 4: Clean fragment
            val cleanFragment = cleanFragment(uri.fragment)
            
            // Step 5: Handle cloaked URLs
            val finalUrl = handleCloakedUrls(uri, cleanQuery, cleanFragment)
            
            return finalUrl
        } catch (e: Exception) {
            // If parsing fails, return original URL
            return url
        }
    }
    
    private fun resolveRedirects(url: String): String {
        // For now, return the URL as-is. In a real implementation, 
        // this would use HTTP requests to follow redirects
        return url
    }
    
    private fun cleanQueryParameters(query: String?): String? {
        if (query.isNullOrBlank()) return query
        
        val params = query.split('&')
            .mapNotNull { param ->
                val parts = param.split('=', limit = 2)
                if (parts.size == 2) {
                    val key = URLDecoder.decode(parts[0], "UTF-8")
                    val value = URLDecoder.decode(parts[1], "UTF-8")
                    key to value
                } else {
                    null
                }
            }
            .filter { (key, _) -> 
                !TRACKING_PARAMS.contains(key.lowercase()) 
            }
        
        if (params.isEmpty()) return null
        
        return params.joinToString("&") { (key, value) ->
            "${URLEncoder.encode(key, "UTF-8")}=${URLEncoder.encode(value, "UTF-8")}"
        }
    }
    
    private fun cleanFragment(fragment: String?): String? {
        if (fragment.isNullOrBlank()) return fragment
        
        // Check if fragment contains tracking parameters
        val hasTracking = TRACKING_PARAMS.any { param ->
            fragment.contains("$param=", ignoreCase = true) ||
            fragment.contains("?$param=", ignoreCase = true)
        }
        
        return if (hasTracking) null else fragment
    }
    
    private fun handleCloakedUrls(uri: Uri, cleanQuery: String?, cleanFragment: String?): String {
        val urlString = uri.toString()
        
        for (pattern in CLOAKED_PATH_PATTERNS) {
            val match = pattern.find(urlString)
            if (match != null) {
                val actualUrl = URLDecoder.decode(match.groupValues[1], "UTF-8")
                return cleanUrl(actualUrl)
            }
        }
        
        // Rebuild the cleaned URL
        val builder = Uri.Builder()
            .scheme(uri.scheme)
            .authority(uri.authority)
            .path(uri.path)
        
        if (!cleanQuery.isNullOrBlank()) {
            builder.encodedQuery(cleanQuery)
        }
        
        if (!cleanFragment.isNullOrBlank()) {
            builder.encodedFragment(cleanFragment)
        }
        
        return builder.build().toString()
    }
    
    fun getCleanedUrlOption(url: String): String {
        val cleaned = cleanUrl(url)
        return if (cleaned != url) cleaned else url
    }
    
    fun hasTrackingParameters(url: String): Boolean {
        return cleanUrl(url) != url
    }
}
