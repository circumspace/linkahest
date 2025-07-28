package com.hermeticvm.linkahest.data.models

data class UserSettings(
    val selectedNitterInstance: String = "twiiit.com",
    val customNitterInstance: String = "",
    val selectedInvidiousInstance: String = "redirect.invidious.io",
    val customInvidiousInstance: String = "",
    val selectedRedlibInstance: String = "rl.bloat.cat",
    val customRedlibInstance: String = ""
)

object DefaultInstances {
    // list of instances: https://status.d420.de/
    val NITTER_INSTANCES = listOf(
        "twiiit.com",
        "nitter.net",
        "xcancel.com", 
        "nitter.space",
        "nitter.poast.org"
    )
    // list of instances: https://gitea.it/iv-org/documentation/src/commit/e3abe75be38dd719537bf4868ab80f54179d46b1/docs/instances.md
    val INVIDIOUS_INSTANCES = listOf(
        "redirect.invidious.io",
        "yewtu.be",
        "inv.nadeko.net",
        "invidious.nerdvpn.de"
    )
    // list of instances: https://github.com/redlib-org/redlib-instances/blob/main/instances.md
    val REDLIB_INSTANCES = listOf(
        "rl.bloat.cat",
        "redlib.tux.pizza",
        "redlib.ducks.party",
        "redlib.privadency.com",	
        "redlib.catsarch.com",
        "redlib.r4fo.com", 
        "red.ngn.tf"
    )
}