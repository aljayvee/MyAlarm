package com.application.myalarm.mission

enum class MissionType(
    val displayName: String,
    val description: String,
    val iconResName: String,
    val isFree: Boolean
) {
    MATH_PROBLEM(
        displayName = "Math Problem",
        description = "Solve a short arithmetic problem to dismiss the alarm — quick mental warm-up that wakes your brain up.",
        iconResName = "calculate",
        isFree = true
    ),
    SHAKE(
        displayName = "Shake",
        description = "Shake your phone vigorously until the progress bar fills up. Forces you to physically move before going back to bed.",
        iconResName = "vibration",
        isFree = true
    ),
    SKY_PHOTO(
        displayName = "Sky Photo",
        description = "Step outside or to a window and snap a photo of the sky. Daylight + getting out of bed makes going back nearly impossible.",
        iconResName = "wb_sunny",
        isFree = false
    ),
    MAKE_YOUR_BED(
        displayName = "Make Your Bed",
        description = "Make your bed, then take a photo as proof. Starts your day with a small win you can't undo.",
        iconResName = "bed",
        isFree = false
    ),
    OBJECT_HUNT(
        displayName = "Object Hunt",
        description = "Find a specific object somewhere else in your home (e.g. the kettle) and take a photo of it — gets you up and walking.",
        iconResName = "search",
        isFree = false
    ),
    QUOTE_OF_THE_DAY(
        displayName = "Quote of the Day",
        description = "Read the morning quote, then tap to confirm. A gentle wake-up that nudges you into a thoughtful start.",
        iconResName = "format_quote",
        isFree = false
    ),
    AFFIRMATION(
        displayName = "Affirmation",
        description = "Type the morning affirmation word-for-word. Slow, deliberate typing forces focus and shakes off grogginess.",
        iconResName = "favorite",
        isFree = false
    ),
    PUSH_UPS(
        displayName = "Push-Ups",
        description = "Do the configured number of push-ups — the phone counts each rep with its motion sensor. Full-body wake-up in under a minute.",
        iconResName = "fitness_center",
        isFree = false
    ),
    MEMORY(
        displayName = "Memory",
        description = "Watch a short pattern of squares light up, then tap them back in the same order. Brain wake-up that demands attention.",
        iconResName = "grid_view",
        isFree = false
    ),
    TYPING(
        displayName = "Typing",
        description = "Type the displayed sentence exactly, no typos allowed. Sharp focus required — sleepy fingers will retry until it's right.",
        iconResName = "keyboard",
        isFree = false
    ),
    QR_CODE(
        displayName = "QR Code",
        description = "Forces you to get out of bed and scan a registered QR code (e.g. in your kitchen or bathroom) to turn off the alarm.",
        iconResName = "qr_code",
        isFree = false
    ),
    BARCODE(
        displayName = "Barcode",
        description = "Forces you to scan a barcode of a specific product (e.g. your toothpaste or coffee container) to dismiss the alarm.",
        iconResName = "barcode",
        isFree = false
    ),
    STEP_COUNT(
        displayName = "Step Count",
        description = "Forces you to walk a certain number of steps (configured by difficulty level 1-10) to dismiss the alarm.",
        iconResName = "directions_run",
        isFree = false
    )
}
