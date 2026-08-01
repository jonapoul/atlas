package atlas.test

fun isRunningOnCi(): Boolean = System.getenv("CI")?.toBooleanStrictOrNull() == true
