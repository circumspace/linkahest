package com.hermeticvm.linkahest.data.models

data class UserSettings(
    val selectedNitterInstance: String = "farside.link/nitter",
    val customNitterInstance: String = "",
    val selectedInvidiousInstance: String = "farside.link/invidious",
    val customInvidiousInstance: String = "",
    val selectedRedlibInstance: String = "farside.link/redlib",
    val customRedlibInstance: String = "",
    val selectedScribeInstance: String = "farside.link/scribe",
    val customScribeInstance: String = "",
    val themeMode: String = ThemeModes.SYSTEM,
    val historyEnabled: Boolean = false
)

object ThemeModes {
    const val SYSTEM = "system"
    const val LIGHT = "light"
    const val DARK = "dark"
}

object DefaultInstances {
    // list of instances: https://status.d420.de/
    val NITTER_INSTANCES = listOf(
        "farside.link/nitter",
        "twiiit.com",
        "nitter.tiekoetter.com",
        "nitter.privacyredirect.com",
        "nitter.net",
        "nitter.space"
    )
    // list of instances: https://docs.invidious.io/instances/
    val INVIDIOUS_INSTANCES = listOf(
        "farside.link/invidious",
        "redirect.invidious.io",
        "invidious.nerdvpn.de",
        "inv.thepixora.com"
    )
    // list of instances: https://github.com/redlib-org/redlib-instances/blob/main/instances.md
    val REDLIB_INSTANCES = listOf(
        "farside.link/redlib",
        "rl.bloat.cat",
        "redlib.perennialte.ch",
        "redlib.r4fo.com",
        "red.artemislena.eu",
        "redlib.privadency.com"
    )
    val SCRIBE_INSTANCES = listOf(
        "farside.link/scribe"
    )
}
