package com.hermeticvm.linkahest.data.models

data class UserSettings(
    val selectedNitterInstance: String = DefaultInstances.NITTER_INSTANCES.first(),
    val customNitterInstance: String = "",
    val selectedInvidiousInstance: String = DefaultInstances.INVIDIOUS_INSTANCES.first(),
    val customInvidiousInstance: String = "",
    val selectedRedlibInstance: String = DefaultInstances.REDLIB_INSTANCES.first(),
    val customRedlibInstance: String = "",
    val selectedScribeInstance: String = DefaultInstances.SCRIBE_INSTANCES.first(),
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
        "twiiit.com",
        "nitter.tiekoetter.com",
        "nitter.privacyredirect.com",
        "nitter.net",
        "nitter.space"
    )
    // list of instances: https://docs.invidious.io/instances/
    val INVIDIOUS_INSTANCES = listOf(
        "redirect.invidious.io",
        "invidious.nerdvpn.de",
        "inv.thepixora.com"
    )
    // list of instances: https://github.com/redlib-org/redlib-instances/blob/main/instances.md
    val REDLIB_INSTANCES = listOf(
        "red.artemislena.eu",
        "redlib.privadency.com",
        "redlib.nadeko.net",
        "redlib.cow.rip",
        "safereddit.com",
        "redlib.catsarch.com"
    )
    val SCRIBE_INSTANCES = listOf(
        "libmedium.batsense.net",
        "scribe.rawbit.ninja"
    )
}
