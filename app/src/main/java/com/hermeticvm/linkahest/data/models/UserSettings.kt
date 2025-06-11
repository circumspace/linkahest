package com.hermeticvm.linkahest.data.models

data class UserSettings(
    val selectedNitterInstance: String = "nitter.net",
    val customNitterInstance: String = "",
    val selectedInvidiousInstance: String = "yewtu.be",
    val customInvidiousInstance: String = "",
    val selectedRedlibInstance: String = "rl.bloat.cat",
    val customRedlibInstance: String = ""
)

object DefaultInstances {
    val NITTER_INSTANCES = listOf(
        "nitter.net",
        "xcancel.com", 
        "nitter.space",
        "nitter.poast.org"
    )
    
    val INVIDIOUS_INSTANCES = listOf(
        "yewtu.be",
        "inv.nadeko.net",
        "invidious.nerdvpn.de"
    )
    
    val REDLIB_INSTANCES = listOf(
        "rl.bloat.cat",
        "redlib.catsarch.com",
        "redlib.r4fo.com", 
        "red.ngn.tf"
    )
}