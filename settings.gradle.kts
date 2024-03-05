pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "Calorie"

include(":app")

include(":core")

include(":onboarding")

include(":onboarding:onboarding_domain")

include(":onboarding:onboarding_presentation")

include(":tracker")

include(":tracker:tracker_data")

include(":tracker:tracker_domain")

include(":tracker:tracker_presentation")

include(":core-ui")
