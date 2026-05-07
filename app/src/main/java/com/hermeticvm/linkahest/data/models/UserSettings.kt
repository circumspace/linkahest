package com.hermeticvm.linkahest.data.models

data class UserSettings(
    val selectedNitterInstance: String = "farside.link/nitter",
    val customNitterInstance: String = "",
    val selectedInvidiousInstance: String = "farside.link/invidious",
    val customInvidiousInstance: String = "",
    val selectedRedlibInstance: String = "farside.link/redlib",
    val customRedlibInstance: String = ""
)

object DefaultInstances {
    // list of instances: https://status.d420.de/
    val NITTER_INSTANCES = listOf(
        "farside.link/nitter",
        "twiiit.com",
        "nitter.net",
        "xcancel.com", 
        "nitter.space",
        "nitter.poast.org"
    )
    // list of instances: https://gitea.it/iv-org/documentation/src/commit/e3abe75be38dd719537bf4868ab80f54179d46b1/docs/instances.md
    val INVIDIOUS_INSTANCES = listOf(
        "farside.link/invidious",
        "redirect.invidious.io",
        "yewtu.be",
        "inv.nadeko.net",
        "invidious.nerdvpn.de"
    )
    // list of instances: https://github.com/redlib-org/redlib-instances/blob/main/instances.md
    val REDLIB_INSTANCES = listOf(
        "farside.link/redlib",
        "rl.bloat.cat",
        "redlib.tux.pizza",
        "redlib.ducks.party",
        "redlib.privadency.com",	
        "redlib.catsarch.com",
        "redlib.r4fo.com", 
        "red.ngn.tf"
    )
}
