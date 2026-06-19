package com.application.myalarm.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object Localizer {

    enum class Language(val code: String, val displayName: String) {
        ENGLISH("en", "English (Default)"),
        CHINESE_SIMPLIFIED("zh_CN", "Chinese Simplify"),
        CHINESE_TRADITIONAL("zh_TW", "Chinese Traditional"),
        CHINESE_PINYIN("zh_PY", "Chinese Pinyin"),
        ARABIC("ar", "Arabic"),
        TAGALOG("tl", "Tagalog"),
        THAI("th", "Thai"),
        HINDI("hi", "Hindi"),
        CANTONESE("zh_HK", "Cantonese")
    }

    var currentLanguage by mutableStateOf(Language.ENGLISH)

    private val translations = mapOf(
        "Settings" to mapOf(
            Language.CHINESE_SIMPLIFIED to "设置",
            Language.CHINESE_TRADITIONAL to "設置",
            Language.CHINESE_PINYIN to "Shè zhì",
            Language.ARABIC to "الإعدادات",
            Language.TAGALOG to "Mga Setting",
            Language.THAI to "การตั้งค่า",
            Language.HINDI to "सेटिंग्स",
            Language.CANTONESE to "設定"
        ),
        "Home" to mapOf(
            Language.CHINESE_SIMPLIFIED to "首页",
            Language.CHINESE_TRADITIONAL to "首頁",
            Language.CHINESE_PINYIN to "Zhǔ yè",
            Language.ARABIC to "الرئيسية",
            Language.TAGALOG to "Home",
            Language.THAI to "หน้าแรก",
            Language.HINDI to "होम",
            Language.CANTONESE to "主頁"
        ),
        "Alarms" to mapOf(
            Language.CHINESE_SIMPLIFIED to "闹钟",
            Language.CHINESE_TRADITIONAL to "鬧鐘",
            Language.CHINESE_PINYIN to "Nào zhōng",
            Language.ARABIC to "المنبهات",
            Language.TAGALOG to "Mga Alarma",
            Language.THAI to "นาฬิกาปลุก",
            Language.HINDI to "अलार्म",
            Language.CANTONESE to "鬧鐘"
        ),
        "Insights" to mapOf(
            Language.CHINESE_SIMPLIFIED to "分析",
            Language.CHINESE_TRADITIONAL to "分析",
            Language.CHINESE_PINYIN to "Fēn xī",
            Language.ARABIC to "الإحصائيات",
            Language.TAGALOG to "Mga Insight",
            Language.THAI to "ข้อมูลเชิงลึก",
            Language.HINDI to "अंतरदृष्टि",
            Language.CANTONESE to "分析"
        ),
        "Version" to mapOf(
            Language.CHINESE_SIMPLIFIED to "版本",
            Language.CHINESE_TRADITIONAL to "版本",
            Language.CHINESE_PINYIN to "Bǎn běn",
            Language.ARABIC to "الإصدار",
            Language.TAGALOG to "Bersyon",
            Language.THAI to "เวอร์ชัน",
            Language.HINDI to "संस्करण",
            Language.CANTONESE to "版本"
        ),
        "Developer" to mapOf(
            Language.CHINESE_SIMPLIFIED to "开发者",
            Language.CHINESE_TRADITIONAL to "開發者",
            Language.CHINESE_PINYIN to "Kāi fā zhě",
            Language.ARABIC to "المطور",
            Language.TAGALOG to "Developer",
            Language.THAI to "ผู้พัฒนา",
            Language.HINDI to "डेवलपर",
            Language.CANTONESE to "開發者"
        ),
        "Language" to mapOf(
            Language.CHINESE_SIMPLIFIED to "语言",
            Language.CHINESE_TRADITIONAL to "語言",
            Language.CHINESE_PINYIN to "Yǔ yán",
            Language.ARABIC to "اللغة",
            Language.TAGALOG to "Wika",
            Language.THAI to "ภาษา",
            Language.HINDI to "भाषा",
            Language.CANTONESE to "語言"
        ),
        "Notifications" to mapOf(
            Language.CHINESE_SIMPLIFIED to "通知",
            Language.CHINESE_TRADITIONAL to "通知",
            Language.CHINESE_PINYIN to "Tōng zhī",
            Language.ARABIC to "الإشعارات",
            Language.TAGALOG to "Mga Notification",
            Language.THAI to "การแจ้งเตือน",
            Language.HINDI to "सूचनाएं",
            Language.CANTONESE to "通知"
        ),
        "PERMISSIONS" to mapOf(
            Language.CHINESE_SIMPLIFIED to "权限列表",
            Language.CHINESE_TRADITIONAL to "權限列表",
            Language.CHINESE_PINYIN to "Quán xiàn liè biǎo",
            Language.ARABIC to "الأذونات",
            Language.TAGALOG to "MGA PAHINTULOT",
            Language.THAI to "สิทธิ์การเข้าถึง",
            Language.HINDI to "अनुमतियाँ",
            Language.CANTONESE to "權限列表"
        ),
        "ABOUT" to mapOf(
            Language.CHINESE_SIMPLIFIED to "关于",
            Language.CHINESE_TRADITIONAL to "關於",
            Language.CHINESE_PINYIN to "Guān yú",
            Language.ARABIC to "حول",
            Language.TAGALOG to "TUNGKOL",
            Language.THAI to "เกี่ยวกับ",
            Language.HINDI to "के बारे में",
            Language.CANTONESE to "關於"
        ),
        "streak" to mapOf(
            Language.CHINESE_SIMPLIFIED to "连续天数",
            Language.CHINESE_TRADITIONAL to "連續天數",
            Language.CHINESE_PINYIN to "Lián xù tiān shù",
            Language.ARABIC to "سلسلة أيام",
            Language.TAGALOG to "streak",
            Language.THAI to "สถิติต่อเนื่อง",
            Language.HINDI to "लगातार दिन",
            Language.CANTONESE to "連續天數"
        ),
        "Next Alarm" to mapOf(
            Language.CHINESE_SIMPLIFIED to "下次闹钟",
            Language.CHINESE_TRADITIONAL to "下次鬧鐘",
            Language.CHINESE_PINYIN to "Xià cì nào zhōng",
            Language.ARABIC to "المنبه القادم",
            Language.TAGALOG to "Susunod na Alarma",
            Language.THAI to "ปลุกครั้งถัดไป",
            Language.HINDI to "अगला अलार्म",
            Language.CANTONESE to "下次鬧鐘"
        ),
        "Rings in" to mapOf(
            Language.CHINESE_SIMPLIFIED to "响铃剩余时间",
            Language.CHINESE_TRADITIONAL to "響鈴剩餘時間",
            Language.CHINESE_PINYIN to "Xiǎng líng shèng yú shí jiān",
            Language.ARABIC to "يرن في",
            Language.TAGALOG to "Tútúnóg sa loób ng",
            Language.THAI to "จะดังขึ้นในอีก",
            Language.HINDI to "बचेगा",
            Language.CANTONESE to "響鈴剩餘時間"
        ),
        "Today's Wake Up" to mapOf(
            Language.CHINESE_SIMPLIFIED to "今日起床",
            Language.CHINESE_TRADITIONAL to "今日起床",
            Language.CHINESE_PINYIN to "Jīn rì qǐ chuáng",
            Language.ARABIC to "الاستيقاظ اليوم",
            Language.TAGALOG to "Gising Ngayong Araw",
            Language.THAI to "การตื่นนอนวันนี้",
            Language.HINDI to "आज की सुबह",
            Language.CANTONESE to "今日起床"
        ),
        "History" to mapOf(
            Language.CHINESE_SIMPLIFIED to "历史",
            Language.CHINESE_TRADITIONAL to "歷史",
            Language.CHINESE_PINYIN to "Lì shǐ",
            Language.ARABIC to "السجل",
            Language.TAGALOG to "Kasaysayan",
            Language.THAI to "ประวัติ",
            Language.HINDI to "इतिहास",
            Language.CANTONESE to "歷史"
        ),
        "New alarm" to mapOf(
            Language.CHINESE_SIMPLIFIED to "新闹钟",
            Language.CHINESE_TRADITIONAL to "新鬧鐘",
            Language.CHINESE_PINYIN to "Xīn nào zhōng",
            Language.ARABIC to "منبه جديد",
            Language.TAGALOG to "Bagong Alarma",
            Language.THAI to "เพิ่มนาฬิกาปลุก",
            Language.HINDI to "नया अलार्म",
            Language.CANTONESE to "新鬧鐘"
        ),
        "Edit alarm" to mapOf(
            Language.CHINESE_SIMPLIFIED to "编辑闹钟",
            Language.CHINESE_TRADITIONAL to "編輯鬧鐘",
            Language.CHINESE_PINYIN to "Biān jí nào zhōng",
            Language.ARABIC to "تعديل المنبه",
            Language.TAGALOG to "I-edit ang Alarma",
            Language.THAI to "แก้ไขนาฬิกาปลุก",
            Language.HINDI to "अलार्म संपादित करें",
            Language.CANTONESE to "編輯鬧鐘"
        ),
        "Save" to mapOf(
            Language.CHINESE_SIMPLIFIED to "保存",
            Language.CHINESE_TRADITIONAL to "保存",
            Language.CHINESE_PINYIN to "Bǎo cún",
            Language.ARABIC to "حفظ",
            Language.TAGALOG to "I-save",
            Language.THAI to "บันทึก",
            Language.HINDI to "सहेजें",
            Language.CANTONESE to "保存"
        ),
        "LABEL" to mapOf(
            Language.CHINESE_SIMPLIFIED to "标签名称",
            Language.CHINESE_TRADITIONAL to "標籤名稱",
            Language.CHINESE_PINYIN to "Biāo qiān míng chēng",
            Language.ARABIC to "الملصق",
            Language.TAGALOG to "LABEL",
            Language.THAI to "ป้ายกำกับ",
            Language.HINDI to "लेबल",
            Language.CANTONESE to "標籤名稱"
        ),
        "REPEAT" to mapOf(
            Language.CHINESE_SIMPLIFIED to "重复星期",
            Language.CHINESE_TRADITIONAL to "重複星期",
            Language.CHINESE_PINYIN to "Chóng fù xīng qī",
            Language.ARABIC to "التكرار",
            Language.TAGALOG to "ULITIN",
            Language.THAI to "ซ้ำ",
            Language.HINDI to "दहराएं",
            Language.CANTONESE to "重複星期"
        ),
        "MISSION" to mapOf(
            Language.CHINESE_SIMPLIFIED to "解锁任务",
            Language.CHINESE_TRADITIONAL to "解鎖任務",
            Language.CHINESE_PINYIN to "Jiě suǒ rèn wu",
            Language.ARABIC to "المهمة",
            Language.TAGALOG to "MISYON",
            Language.THAI to "ภารกิจ",
            Language.HINDI to "मिशन",
            Language.CANTONESE to "解鎖任務"
        ),
        "SOUND" to mapOf(
            Language.CHINESE_SIMPLIFIED to "响铃铃声",
            Language.CHINESE_TRADITIONAL to "響鈴鈴聲",
            Language.CHINESE_PINYIN to "Xiǎng líng líng shēng",
            Language.ARABIC to "الصوت",
            Language.TAGALOG to "TUNOG",
            Language.THAI to "เสียงปลุก",
            Language.HINDI to "ध्वनि",
            Language.CANTONESE to "響鈴鈴聲"
        ),
        "BACKUP ALARM" to mapOf(
            Language.CHINESE_SIMPLIFIED to "备用闹钟",
            Language.CHINESE_TRADITIONAL to "備用鬧鐘",
            Language.CHINESE_PINYIN to "Bèi yòng nào zhōng",
            Language.ARABIC to "المنبه الاحتياطي",
            Language.TAGALOG to "BACKUP ALARMA",
            Language.THAI to "นาฬิกาปลุกสำรอง",
            Language.HINDI to "बैकअप अलार्म",
            Language.CANTONESE to "備用鬧鐘"
        ),
        "Backup fire" to mapOf(
            Language.CHINESE_SIMPLIFIED to "开启备用响铃",
            Language.CHINESE_TRADITIONAL to "開啟備用響鈴",
            Language.CHINESE_PINYIN to "Kāi qǐ bèi yòng xiǎng líng",
            Language.ARABIC to "تشغيل الاحتياطي",
            Language.TAGALOG to "Backup na tunog",
            Language.THAI to "การปลุกสำรองทำงาน",
            Language.HINDI to "बैकअप प्रारंभ",
            Language.CANTONESE to "開啟備用響鈴"
        ),
        "Fire again after (min)" to mapOf(
            Language.CHINESE_SIMPLIFIED to "多少分钟后再响",
            Language.CHINESE_TRADITIONAL to "多少分鐘後再響",
            Language.CHINESE_PINYIN to "Duō shǎo fēn zhōng hòu zài xiǎng",
            Language.ARABIC to "رنين مجدد بعد (دقائق)",
            Language.TAGALOG to "Tútúnóg ulít pagkatapós ng (min)",
            Language.THAI to "ดังขึ้นอีกครั้งหลังจาก (นาที)",
            Language.HINDI to "फिर बजने का समय (मिनट)",
            Language.CANTONESE to "多少分鐘後再響"
        ),
        "Back" to mapOf(
            Language.CHINESE_SIMPLIFIED to "返回",
            Language.CHINESE_TRADITIONAL to "返回",
            Language.CHINESE_PINYIN to "Fǎn huí",
            Language.ARABIC to "رجوع",
            Language.TAGALOG to "Bumalik",
            Language.THAI to "ย้อนกลับ",
            Language.HINDI to "पीछे",
            Language.CANTONESE to "返回"
        ),
        "Next" to mapOf(
            Language.CHINESE_SIMPLIFIED to "下一步",
            Language.CHINESE_TRADITIONAL to "下一步",
            Language.CHINESE_PINYIN to "Xià yī bù",
            Language.ARABIC to "التالي",
            Language.TAGALOG to "Susunod",
            Language.THAI to "ถัดไป",
            Language.HINDI to "आगे",
            Language.CANTONESE to "下一步"
        ),
        "Started" to mapOf(
            Language.CHINESE_SIMPLIFIED to "开始使用",
            Language.CHINESE_TRADITIONAL to "開始使用",
            Language.CHINESE_PINYIN to "Kāi shǐ shǐ yòng",
            Language.ARABIC to "ابدأ",
            Language.TAGALOG to "Simulan",
            Language.THAI to "เริ่มต้นใช้งาน",
            Language.HINDI to "शुरू करें",
            Language.CANTONESE to "開始使用"
        ),

        // NEW LOCALIZATION KEYS
        "Select Language" to mapOf(
            Language.CHINESE_SIMPLIFIED to "选择语言",
            Language.CHINESE_TRADITIONAL to "選擇語言",
            Language.CHINESE_PINYIN to "Xuǎn zé yǔ yán",
            Language.ARABIC to "اختر اللغة",
            Language.TAGALOG to "Pumili ng Wika",
            Language.THAI to "เลือกภาษา",
            Language.HINDI to "भाषा चुनें",
            Language.CANTONESE to "選擇語言"
        ),
        "You're All Set!" to mapOf(
            Language.CHINESE_SIMPLIFIED to "准备就绪！",
            Language.CHINESE_TRADITIONAL to "準備就緒！",
            Language.CHINESE_PINYIN to "Zhǔn bèi jiù xù!",
            Language.ARABIC to "أنت جاهز تمامًا!",
            Language.TAGALOG to "Handa Ka Na!",
            Language.THAI to "คุณพร้อมแล้ว!",
            Language.HINDI to "आप पूरी तरह तैयार हैं!",
            Language.CANTONESE to "準備就緒！"
        ),
        "Draw Over Other Apps" to mapOf(
            Language.CHINESE_SIMPLIFIED to "显示在其他应用上层",
            Language.CHINESE_TRADITIONAL to "顯示在其他應用上層",
            Language.CHINESE_PINYIN to "Xiǎn shì zài qí tā ying yòng shàng céng",
            Language.ARABIC to "الظهور فوق التطبيقات الأخرى",
            Language.TAGALOG to "I-display sa ibabaw ng ibang app",
            Language.THAI to "แสดงทับแอปอื่น",
            Language.HINDI to "अन्य ऐप्स के ऊपर प्रदर्शित करें",
            Language.CANTONESE to "顯示在其他應用上層"
        ),
        "Camera Access" to mapOf(
            Language.CHINESE_SIMPLIFIED to "相机权限",
            Language.CHINESE_TRADITIONAL to "相機權限",
            Language.CHINESE_PINYIN to "Xiāng jī quán xiàn",
            Language.ARABIC to "الوصول إلى الكاميرا",
            Language.TAGALOG to "Access sa Camera",
            Language.THAI to "การเข้าถึงกล้อง",
            Language.HINDI to "कैमरा एक्सेस",
            Language.CANTONESE to "相機權限"
        ),
        "Display over other apps" to mapOf(
            Language.CHINESE_SIMPLIFIED to "悬浮窗权限",
            Language.CHINESE_TRADITIONAL to "懸浮窗權限",
            Language.CHINESE_PINYIN to "Xuán fù chuāng quán xiàn",
            Language.ARABIC to "الظهور فوق التطبيقات الأخرى",
            Language.TAGALOG to "I-display sa ibabaw ng ibang apps",
            Language.THAI to "แสดงทับแอปอื่น",
            Language.HINDI to "अन्य ऐप्स के ऊपर प्रदर्शित करें",
            Language.CANTONESE to "懸浮窗權限"
        ),
        "Allowed — alarms can post lock-screen banners" to mapOf(
            Language.CHINESE_SIMPLIFIED to "已允许 — 闹钟可以在锁屏显示横幅",
            Language.CHINESE_TRADITIONAL to "已允許 — 鬧鐘可以在鎖屏顯示橫幅",
            Language.CHINESE_PINYIN to "Yǐ yǔn xǔ — nào zhōng kě yǐ zài suǒ píng xiǎn shì héng fú",
            Language.ARABIC to "مسموح — يمكن للمنبهات إظهار لافتات على شاشة القفل",
            Language.TAGALOG to "Pinapayagan — maaaring mag-post ang mga alarma ng mga banner sa lock-screen",
            Language.THAI to "อนุญาตแล้ว — นาฬิกาปลุกสามารถแสดงแบนเนอร์บนหน้าจอล็อกได้",
            Language.HINDI to "अनुमति है — अलार्म लॉक-स्क्रीन बैनर पोस्ट कर सकते हैं",
            Language.CANTONESE to "已允許 — 鬧鐘可以在鎖屏顯示橫幅"
        ),
        "Blocked — alarms cannot notify or show ringing UI" to mapOf(
            Language.CHINESE_SIMPLIFIED to "已禁用 — 闹钟无法发送通知或显示响铃界面",
            Language.CHINESE_TRADITIONAL to "已禁用 — 鬧鐘無法發送通知或顯示響鈴界面",
            Language.CHINESE_PINYIN to "Yǐ jìn yòng — nào zhōng wú fǎ fā sòng tōng zhī huò xiǎn shì xiǎng líng jiè miàn",
            Language.ARABIC to "محظور — لا يمكن للمنبهات التنبيه أو إظهار واجهة الرنين",
            Language.TAGALOG to "Blocked — hindi maabisuhan o maipakita ng mga alarma ang UI ng pagtunog",
            Language.THAI to "ถูกบล็อก — นาฬิกาปลุกไม่สามารถแจ้งเตือนหรือแสดงหน้าจอปลุกได้",
            Language.HINDI to "अवरुद्ध — अलार्म सूचित नहीं कर सकते या बजने वाला UI नहीं दिखा सकते",
            Language.CANTONESE to "已禁用 — 鬧鐘無法發送通知或顯示響鈴界面"
        ),
        "Without notifications Android won't show the alarm banner — and on Android 14+ the full-screen alarm UI is blocked too." to mapOf(
            Language.CHINESE_SIMPLIFIED to "没有通知权限，Android将无法显示闹钟横幅 — 并且在 Android 14+ 上，全屏闹钟界面也将被阻止。",
            Language.CHINESE_TRADITIONAL to "沒有通知權限，Android將無法顯示鬧鐘橫幅 — 並且在 Android 14+ 上，全屏鬧鐘界面也將被阻止。",
            Language.CHINESE_PINYIN to "Méi yǒu tōng zhī quán xiàn, Android jiāng wú fǎ xiǎn shì nào zhōng héng fú...",
            Language.ARABIC to "بدون الإشعارات، لن يعرض نظام Android لافتة المنبه — وعلى نظام Android 14+ سيتم حظر واجهة المنبه بملء الشاشة أيضًا.",
            Language.TAGALOG to "Kung walang notifications, hindi ipapakita ng Android ang banner ng alarma — at sa Android 14+ blocked din ang full-screen UI.",
            Language.THAI to "หากไม่มีการแจ้งเตือน Android จะไม่แสดงแบนเนอร์นาฬิกาปลุก — และบน Android 14+ หน้าจอปลุกแบบเต็มจอก็จะถูกบล็อกด้วย",
            Language.HINDI to "सूचनाओं के बिना Android अलार्म बैनर नहीं दिखाएगा — और Android 14+ पर फुल-स्क्रीन अलार्म UI भी अवरुद्ध हो जाएगा।",
            Language.CANTONESE to "沒有通知權限，Android將無法顯示鬧鐘橫幅 — 並且在 Android 14+ 上，全屏鬧鐘界面也將被阻止。"
        ),
        "Allowed — alarms will overlay the screen immediately" to mapOf(
            Language.CHINESE_SIMPLIFIED to "已允许 — 闹钟将立即覆盖屏幕",
            Language.CHINESE_TRADITIONAL to "已允許 — 鬧鐘將立即覆蓋屏幕",
            Language.CHINESE_PINYIN to "Yǐ yǔn xǔ — nào zhōng jiāng lì jí fù gài píng mù",
            Language.ARABIC to "مسموح — ستظهر المنبهات فوق الشاشة فورًا",
            Language.TAGALOG to "Pinapayagan — mag-o-overlay agad ang mga alarma sa screen",
            Language.THAI to "อนุญาตแล้ว — นาฬิกาปลุกจะแสดงทับหน้าจอทันที",
            Language.HINDI to "अनुमति है — अलार्म तुरंत स्क्रीन पर आ जाएंगे",
            Language.CANTONESE to "已允許 — 鬧鐘將立即覆蓋屏幕"
        ),
        "Blocked — alarms cannot show fullscreen if device is in use" to mapOf(
            Language.CHINESE_SIMPLIFIED to "已禁用 — 如果设备正在使用，闹钟将无法显示全屏",
            Language.CHINESE_TRADITIONAL to "已禁用 — 如果設備正在使用，鬧鐘將無法顯示全屏",
            Language.CHINESE_PINYIN to "Yǐ jìn yòng — rú guǒ shè bèi zhèng zài shǐ yòng, nào zhōng wú fǎ xiǎn shì quán píng",
            Language.ARABIC to "محظور — لا يمكن للمنبهات الظهور بملء الشاشة إذا كان الجهاز قيد الاستخدام",
            Language.TAGALOG to "Blocked — hindi maipapakita ng mga alarma ang fullscreen kung ginagamit ang device",
            Language.THAI to "ถูกบล็อก — นาฬิกาปลุกไม่สามารถแสดงผลแบบเต็มจอได้หากกำลังใช้งานอุปกรณ์อยู่",
            Language.HINDI to "अवरुद्ध — यदि डिवाइस उपयोग में है तो अलार्म फुलस्क्रीन नहीं दिखा सकते",
            Language.CANTONESE to "已禁用 — 如果設備正在使用，鬧鐘將無法顯示全屏"
        ),
        "This permission allows the alarm to overlay and show the wakeup screen even if you are using other apps. This prevents you from ignoring it." to mapOf(
            Language.CHINESE_SIMPLIFIED to "此权限允许闹钟在您使用其他应用时覆盖并显示唤醒屏幕。这可以防止您忽略它。",
            Language.CHINESE_TRADITIONAL to "此權限允許鬧鐘在您使用其他應用時覆蓋並顯示喚醒屏幕。這可以防止您忽略它。",
            Language.CHINESE_PINYIN to "Cǐ quán xiàn yǔn xǔ nào zhōng zài nín shǐ yòng qí tā yìng yòng shí fù gài bìng xiǎn shì huàn xǐng píng mù...",
            Language.ARABIC to "يسمح هذا الإذن للمنبه بالظهور فوق التطبيقات وعرض شاشة الاستيقاظ حتى لو كنت تستخدم تطبيقات أخرى. هذا يمنعك من تجاهله.",
            Language.TAGALOG to "Pinapayagan ng permission na ito ang alarma na mag-overlay at ipakita ang wakeup screen kahit gumagamit ka ng ibang apps. Hahadlangan ka nitong balewalain ito.",
            Language.THAI to "สิทธิ์นี้อนุญาตให้นาฬิกาปลุกแสดงทับหน้าจอและแสดงหน้าจอปลุกได้แม้ว่าคุณจะใช้งานแอปอื่นอยู่ ซึ่งจะช่วยป้องกันไม่ให้คุณเพิกเฉยต่อการปลุก",
            Language.HINDI to "यह अनुमति अलार्म को अन्य ऐप्स का उपयोग करने पर भी ऊपर आने और वेकअप स्क्रीन दिखाने की अनुमति देती है। यह आपको इसे अनदेखा करने से रोकता है।",
            Language.CANTONESE to "此權限允許鬧鐘在您使用其他應用時覆蓋並顯示喚醒屏幕。這可以防止您忽略它。"
        ),
        "Grant" to mapOf(
            Language.CHINESE_SIMPLIFIED to "允许",
            Language.CHINESE_TRADITIONAL to "允許",
            Language.CHINESE_PINYIN to "Yǔn xǔ",
            Language.ARABIC to "منح",
            Language.TAGALOG to "Ibigay",
            Language.THAI to "อนุญาต",
            Language.HINDI to "प्रदान करें",
            Language.CANTONESE to "允許"
        ),
        "Show on Lock Screen" to mapOf(
            Language.CHINESE_SIMPLIFIED to "在锁屏上显示",
            Language.CHINESE_TRADITIONAL to "在鎖屏上顯示",
            Language.CHINESE_PINYIN to "Zài suǒ píng shàng xiǎn shì",
            Language.TAGALOG to "Ipakita sa Lock Screen",
            Language.CANTONESE to "在鎖屏上顯示"
        ),
        "Required to display overlays on the lock screen" to mapOf(
            Language.CHINESE_SIMPLIFIED to "需要在锁屏上显示覆盖界面",
            Language.CHINESE_TRADITIONAL to "需要在鎖屏上顯示覆蓋介面",
            Language.CHINESE_PINYIN to "Xū yào zài suǒ píng shàng xiǎn shì fù gài jiè miàn",
            Language.TAGALOG to "Kailangan upang maipakita ang overlay sa lock screen",
            Language.CANTONESE to "需要在鎖屏上顯示覆蓋介面"
        ),
        "Show on Lock Screen (Xiaomi)" to mapOf(
            Language.CHINESE_SIMPLIFIED to "在锁屏上显示 (小米)",
            Language.CHINESE_TRADITIONAL to "在鎖屏上顯示 (小米)",
            Language.CHINESE_PINYIN to "Zài suǒ píng shàng xiǎn shì (Xiǎo mǐ)",
            Language.TAGALOG to "Ipakita sa Lock Screen (Xiaomi)",
            Language.CANTONESE to "在鎖屏上顯示 (小米)"
        ),
        "Manage MIUI custom permissions (Show on Lock screen, Display pop-up windows)" to mapOf(
            Language.CHINESE_SIMPLIFIED to "管理MIUI自定义权限 (在锁屏上显示、后台弹出界面)",
            Language.CHINESE_TRADITIONAL to "管理MIUI自定義權限 (在鎖屏上顯示、后台彈出介面)",
            Language.CHINESE_PINYIN to "Guǎn lǐ MIUI zì dìng yì quán xiàn (Zài suǒ píng shàng xiǎn shì, hòu tái tán chū jiè miàn)",
            Language.TAGALOG to "Pamahalaan ang mga custom permission ng MIUI (Show on Lock screen, Display pop-up windows)",
            Language.CANTONESE to "管理MIUI自定義權限 (在鎖屏上顯示、后台彈出介面)"
        ),
        "Manage" to mapOf(
            Language.CHINESE_SIMPLIFIED to "管理",
            Language.CHINESE_TRADITIONAL to "管理",
            Language.CHINESE_PINYIN to "Guǎn lǐ",
            Language.TAGALOG to "Pamahalaan",
            Language.CANTONESE to "管理"
        ),
        "Manage custom settings for OEM devices (Show on Lock screen, Autostart, Battery optimizations)" to mapOf(
            Language.CHINESE_SIMPLIFIED to "管理OEM设备的自定义设置 (在锁屏上显示、自启动、电池优化)",
            Language.CHINESE_TRADITIONAL to "管理OEM設備的自定義設置 (在鎖屏上顯示、自啟動、電池優化)",
            Language.CHINESE_PINYIN to "Guǎn lǐ OEM shè bèi de zì dìng yì shè zhì (Zài suǒ píng shàng xiǎn shì, zì qǐ dòng, diàn chí yōu huà)",
            Language.TAGALOG to "Pamahalaan ang mga custom setting para sa mga OEM device (Show on Lock screen, Autostart, Battery optimizations)",
            Language.CANTONESE to "管理OEM設備的自定義設置 (在鎖屏上顯示、自啟動、電池優化)"
        ),
        "Best streak" to mapOf(
            Language.CHINESE_SIMPLIFIED to "最佳连续",
            Language.CHINESE_TRADITIONAL to "最佳連續",
            Language.CHINESE_PINYIN to "Zuì jiā lián xù",
            Language.ARABIC to "أفضل سلسلة أيام",
            Language.TAGALOG to "Best streak",
            Language.THAI to "สถิติดีที่สุด",
            Language.HINDI to "सर्वश्रेष्ठ लगातार दिन",
            Language.CANTONESE to "最佳連續"
        ),
        "Dismisses" to mapOf(
            Language.CHINESE_SIMPLIFIED to "解除次数",
            Language.CHINESE_TRADITIONAL to "解除次數",
            Language.CHINESE_PINYIN to "Jiě chú cì shù",
            Language.ARABIC to "مرات الإيقاف",
            Language.TAGALOG to "Mga Dismiss",
            Language.THAI to "การปิดปลุก",
            Language.HINDI to "बंद किया गया",
            Language.CANTONESE to "解除次數"
        ),
        "Success rate" to mapOf(
            Language.CHINESE_SIMPLIFIED to "成功率",
            Language.CHINESE_TRADITIONAL to "成功率",
            Language.CHINESE_PINYIN to "Chéng gōng lǜ",
            Language.ARABIC to "معدل النجاح",
            Language.TAGALOG to "Success rate",
            Language.THAI to "อัตราสำเร็จ",
            Language.HINDI to "सफलता दर",
            Language.CANTONESE to "成功率"
        ),
        "Avg time" to mapOf(
            Language.CHINESE_SIMPLIFIED to "平均用时",
            Language.CHINESE_TRADITIONAL to "平均用時",
            Language.CHINESE_PINYIN to "Píng jūn yòng shí",
            Language.ARABIC to "متوسط الوقت",
            Language.TAGALOG to "Avg time",
            Language.THAI to "เวลาเฉลี่ย",
            Language.HINDI to "औसत समय",
            Language.CANTONESE to "平均用時"
        ),
        "Skipped" to mapOf(
            Language.CHINESE_SIMPLIFIED to "已跳过",
            Language.CHINESE_TRADITIONAL to "已跳過",
            Language.CHINESE_PINYIN to "Yǐ tiào guò",
            Language.ARABIC to "تم تخطيها",
            Language.TAGALOG to "Skipped",
            Language.THAI to "ข้าม",
            Language.HINDI to "छोड़ा गया",
            Language.CANTONESE to "已跳過"
        ),
        "Total runs" to mapOf(
            Language.CHINESE_SIMPLIFIED to "总运行次数",
            Language.CHINESE_TRADITIONAL to "總運行次數",
            Language.CHINESE_PINYIN to "Zǒng yùn xíng cì shù",
            Language.ARABIC to "إجمالي التشغيل",
            Language.TAGALOG to "Total runs",
            Language.THAI to "จำนวนครั้งทั้งหมด",
            Language.HINDI to "कुल रन",
            Language.CANTONESE to "總運行次數"
        ),
        "Last 4 weeks" to mapOf(
            Language.CHINESE_SIMPLIFIED to "最近 4 周",
            Language.CHINESE_TRADITIONAL to "最近 4 週",
            Language.CHINESE_PINYIN to "Zuì jìn 4 zhōu",
            Language.ARABIC to "آخر 4 أسابيع",
            Language.TAGALOG to "Nakaraang 4 na linggo",
            Language.THAI to "4 สัปดาห์ล่าสุด",
            Language.HINDI to "पिछले 4 सप्ताह",
            Language.CANTONESE to "最近 4 週"
        ),
        "Success" to mapOf(
            Language.CHINESE_SIMPLIFIED to "成功",
            Language.CHINESE_TRADITIONAL to "成功",
            Language.CHINESE_PINYIN to "Chéng gōng",
            Language.ARABIC to "نجاح",
            Language.TAGALOG to "Tagumpay",
            Language.THAI to "สำเร็จ",
            Language.HINDI to "सफल",
            Language.CANTONESE to "成功"
        ),
        "No run" to mapOf(
            Language.CHINESE_SIMPLIFIED to "无运行",
            Language.CHINESE_TRADITIONAL to "無運行",
            Language.CHINESE_PINYIN to "Wú yùn xíng",
            Language.ARABIC to "لم يتم تشغيله",
            Language.TAGALOG to "Hindi tumakbo",
            Language.THAI to "ไม่มีการปลุก",
            Language.HINDI to "कोई रन नहीं",
            Language.CANTONESE to "無運行"
        ),
        "By mission" to mapOf(
            Language.CHINESE_SIMPLIFIED to "按任务",
            Language.CHINESE_TRADITIONAL to "按任務",
            Language.CHINESE_PINYIN to "Àn rèn wu",
            Language.ARABIC to "حسب المهمة",
            Language.TAGALOG to "Sa pamamagitan ng misyon",
            Language.THAI to "ตามภารกิจ",
            Language.HINDI to "मिशन द्वारा",
            Language.CANTONESE to "按任務"
        ),
        "Recent missions" to mapOf(
            Language.CHINESE_SIMPLIFIED to "最近的任务",
            Language.CHINESE_TRADITIONAL to "最近的任務",
            Language.CHINESE_PINYIN to "Zuì jìn de rèn wu",
            Language.ARABIC to "المهام الأخيرة",
            Language.TAGALOG to "Mga kamakailang misyon",
            Language.THAI to "ภารกิจล่าสุด",
            Language.HINDI to "हाल के मिशन",
            Language.CANTONESE to "最近的任務"
        ),
        "Why SnoozeOff?" to mapOf(
            Language.CHINESE_SIMPLIFIED to "为什么选择 SnoozeOff？",
            Language.CHINESE_TRADITIONAL to "為什麼選擇 SnoozeOff？",
            Language.CHINESE_PINYIN to "Wèi shén me xuǎn zé SnoozeOff?",
            Language.ARABIC to "لماذا SnoozeOff؟",
            Language.TAGALOG to "Bakit SnoozeOff?",
            Language.THAI to "ทำไมต้องใช้ SnoozeOff?",
            Language.HINDI to "SnoozeOff क्यों?",
            Language.CANTONESE to "為什麼選擇 SnoozeOff？"
        ),
        "Are you tired of oversleeping and hitting snooze repeatedly? SnoozeOff is designed to wake you up completely. To silence the alarm, you must complete interactive missions (solving math problems, shaking the phone, taking photos of the sky, or walking a specified step count). Once the alarm triggers, navigation controls are restricted to ensure you don't cheat!" to mapOf(
            Language.CHINESE_SIMPLIFIED to "您是否厌倦了睡过头并反复按贪睡键？SnoozeOff 旨在彻底唤醒您。要关闭闹钟，您必须完成互动任务（解决数学题、摇晃手机、拍摄天空照片或行走指定步数）。一旦闹钟触发，导航控制将被限制以确保您不会作弊！",
            Language.CHINESE_TRADITIONAL to "您是否厭倦了睡過頭並反覆按貪睡鍵？SnoozeOff 旨在徹底喚醒您。要關閉鬧鐘，您必須完成互動任務（解決數學題、搖晃手機、拍攝天空照片或行走指定步數）。一旦鬧鐘觸發，導航控制將被限制以確保您不會作弊！",
            Language.CHINESE_PINYIN to "Nín shì fǒu yàn juàn le shuì guò tóu...",
            Language.ARABIC to "هل تعبت من النوم الزائد والضغط على زر الغفوة مرارًا؟ تم تصميم SnoozeOff لإيقاظك تمامًا. لإسكات المنبه، يجب عليك إكمال مهام تفاعلية (حل مسائل رياضية، أو هز الهاتف، أو التقاط صورة للسماء، أو مشي عدد خطوات محدد). بمجرد رنين المنبه، سيتم تقييد عناصر التحكم لمنع الغش!",
            Language.TAGALOG to "Sawa ka na ba sa labis na pagtulog at paulit-ulit na pag-snooze? Ang SnoozeOff ay dinisenyo upang gisingin ka nang lubusan. Upang patayin ang alarma, kailangan mong kumpletuhin ang mga interactive na misyon (paglutas ng mga problema sa math, pag-shake ng telepono, pagkuha ng larawan ng langit, o paglalakad ng nakatakdang bilang ng hakbang). Kapag tumunog na ang alarma, hahadlangan ang mga navigation controls para hindi ka makapandaya!",
            Language.THAI to "คุณเบื่อกับการนอนตื่นสายและกดปุ่มเลื่อนปลุกซ้ำๆ หรือไม่? SnoozeOff ออกแบบมาเพื่อปลุกคุณอย่างสมบูรณ์แบบ ในการปิดนาฬิกาปลุก คุณต้องทำภารกิจแบบโต้ตอบให้สำเร็จ (แก้โจทย์คณิตศาสตร์, เขย่าโทรศัพท์, ถ่ายภาพท้องฟ้า หรือเดินตามจำนวนก้าวที่กำหนด) เมื่อนาฬิกาปลุกดัง ปุ่มควบคุมการนำทางจะถูกจำกัดเพื่อให้แน่ใจว่าคุณจะไม่โกง!",
            Language.HINDI to "क्या आप सो जाने और बार-बार स्नूज़ दबाने से तंग आ चुके हैं? SnoozeOff आपको पूरी तरह से जगाने के लिए बनाया गया है। अलार्म को बंद करने के लिए, आपको इंटरैक्टिव मिशन (गणित की समस्याओं को हल करना, फोन हिलाना, आकाश की फोटो लेना, या एक निर्दिष्ट कदम चलना) पूरे करने होंगे। अलार्म बजने के बाद नेविगेशन को प्रतिबंधित कर दिया जाता है ताकि आप धोखा न दे सकें!",
            Language.CANTONESE to "您是否厭倦了睡過頭並反覆按貪睡鍵？SnoozeOff 旨在徹底喚醒您。要關閉鬧鐘，您必須完成互動任務（解決數學題、搖晃手機、拍攝天空照片或行走指定步数）。一旦鬧鐘觸發，導航控制將被限制以確保您不會作弊！"
        ),
        "Meet the Developer" to mapOf(
            Language.CHINESE_SIMPLIFIED to "开发者介绍",
            Language.CHINESE_TRADITIONAL to "開發者介紹",
            Language.CHINESE_PINYIN to "Kāi fā zhě jiè shào",
            Language.ARABIC to "تعرف على المطور",
            Language.TAGALOG to "Kilalanin ang Developer",
            Language.THAI to "พบกับผู้พัฒนา",
            Language.HINDI to "डेवलपर से मिलें",
            Language.CANTONESE to "開發者介紹"
        ),
        "Hi! I am the creator of this alarm application. I developed SnoozeOff to help you break bad sleeping habits, cultivate morning self-discipline, and get out of bed on time with engaging wake-up missions. Thank you for choosing my app to master your mornings!" to mapOf(
            Language.CHINESE_SIMPLIFIED to "你好！我是这个闹钟应用的创作者。我开发 SnoozeOff 是为了帮助您打破不良睡眠习惯，培养清晨的自律，并通过有趣的唤醒任务按时起床。感谢您选择我的应用来掌控您的早晨！",
            Language.CHINESE_TRADITIONAL to "你好！我是這個鬧鐘應用的創作者。我開發 SnoozeOff 是為了幫助您打破不良睡眠習慣，培養清晨的自律，並通過有趣的喚醒任務按時起床。感謝您選擇我的應用來掌控您的早晨！",
            Language.CHINESE_PINYIN to "Nǐ hǎo! Wǒ shì zhè gè nào zhōng yìng yòng de chuàng zhe...",
            Language.ARABIC to "مرحبًا! أنا مبتكر تطبيق المنبه هذا. لقد قمت بتطوير SnoozeOff لمساعدتك على التخلص من عادات النوم السيئة، وتنمية الانضباط الذاتي في الصباح، والنهوض من السرير في الوقت المحدد مع مهام تفاعلية ممتعة. شكراً لاختيارك تطبيقي للتحكم في صباحك!",
            Language.TAGALOG to "Kumusta! Ako ang lumikha ng application ng alarma na ito. binuo ko ang SnoozeOff para matulungan kang basagin ang masamang gawi sa pagtulog, linangin ang disiplina sa umaga, at bumangon sa kama sa tamang oras gamit ang mga nakakaengganyong wakeup mission. Salamat sa pagpili sa aking app upang makabisado ang iyong mga umaga!",
            Language.THAI to "สวัสดี! ฉันคือผู้สร้างแอปพลิเคชันนาฬิกาปลุกนี้ ฉันพัฒนา SnoozeOff เพื่อช่วยคุณเลิกนิสัยการนอนที่ไม่ดี ฝึกวินัยในตนเองตอนเช้า และลุกจากเตียงตรงเวลาด้วยภารกิจปลุกที่น่าสนใจ ขอบคุณที่เลือกใช้แอปของฉันเพื่อควบคุมเช้าวันใหม่ของคุณ!",
            Language.HINDI to "नमस्ते! मैं इस अलार्मक एप्लिकेशन का निर्माता हूँ। मैंने आपकी नींद की बुरी आदतों को तोड़ने, सुबह के आत्म-अनुशासन को विकसित करने और आकर्षक मिशनों के साथ समय पर बिस्तर से उठने में मदद करने के लिए SnoozeOff विकसित किया है। अपने सुबह को बेहतर बनाने के लिए मेरे ऐप को चुनने के लिए धन्यवाद!",
            Language.CANTONESE to "你好！我是這個鬧鐘應用的創作者。我開發 SnoozeOff 是為了幫助您打破不良睡眠習慣，培養清晨的自律，並通過有趣的喚醒任務按时起床。感謝您選擇我的應用來掌控您的早晨！"
        ),
        "Help Us Know You" to mapOf(
            Language.CHINESE_SIMPLIFIED to "让我们了解您",
            Language.CHINESE_TRADITIONAL to "讓我們了解您",
            Language.CHINESE_PINYIN to "Ràng wǒ men liǎo jiě nín",
            Language.ARABIC to "ساعدنا لنتعرف عليك",
            Language.TAGALOG to "Tulungan kaming makilala ka",
            Language.THAI to "ช่วยให้เรารู้จักคุณ",
            Language.HINDI to "हमें आपको जानने में मदद करें",
            Language.CANTONESE to "讓我們了解您"
        ),
        "Tell us about your waking habits so we can assist you better." to mapOf(
            Language.CHINESE_SIMPLIFIED to "告诉我们您的起床习惯，以便我们能更好地协助您。",
            Language.CHINESE_TRADITIONAL to "告訴我們您的起床習慣，以便我們能更好地協助您。",
            Language.CHINESE_PINYIN to "Gào su wǒ men nín de qǐ chuáng xí guàn...",
            Language.ARABIC to "أخبرنا عن عادات استيقاظك لنتمكن من مساعدتك بشكل أفضل.",
            Language.TAGALOG to "Ipaalam sa amin ang iyong mga gawi sa paggising upang matulungan ka namin nang mas mahusay.",
            Language.THAI to "บอกเราเกี่ยวกับพฤติกรรมการตื่นนอนของคุณเพื่อให้เราช่วยเหลือคุณได้ดียิ่งขึ้น",
            Language.HINDI to "हमें अपनी जागने की आदतों के बारे में बताएं ताकि हम आपकी बेहतर सहायता कर सकें।",
            Language.CANTONESE to "告訴我們您的起床習慣，以便我們能更好地協助您。"
        ),
        "1. What is your biggest morning challenge?" to mapOf(
            Language.CHINESE_SIMPLIFIED to "1. 您早上最大的挑战是什么？",
            Language.CHINESE_TRADITIONAL to "1. 您早上最大的挑戰是什麼？",
            Language.CHINESE_PINYIN to "1. Nín zǎo shang zuì dà de tiǎo zhàn shì shén me?",
            Language.ARABIC to "1. ما هو أكبر تحدٍ يواجهك في الصباح؟",
            Language.TAGALOG to "1. Ano ang iyong pinakamalaking hamon sa umaga?",
            Language.THAI to "1. อะไรคือความท้าทายที่ยิ่งใหญ่ที่สุดของคุณในตอนเช้า?",
            Language.HINDI to "1. सुबह की आपकी सबसे बड़ी चुनौती क्या है?",
            Language.CANTONESE to "1. 您早上最大的挑戰是什麼？"
        ),
        "Oversleeping through normal alarms" to mapOf(
            Language.CHINESE_SIMPLIFIED to "普通闹钟叫不醒，经常睡过头",
            Language.CHINESE_TRADITIONAL to "普通鬧鐘叫不醒，經常睡過頭",
            Language.CHINESE_PINYIN to "Pǔ tōng nào zhōng jiào bù xǐng...",
            Language.ARABIC to "النوم الزائد وتجاوز المنبهات العادية",
            Language.TAGALOG to "Labis na pagtulog kahit may normal na alarma",
            Language.THAI to "นอนตื่นสายและไม่ได้ยินเสียงนาฬิกาปลุกธรรมดา",
            Language.HINDI to "सामान्य अलार्म के बावजूद सो जाना",
            Language.CANTONESE to "普通鬧鐘叫不醒，經常睡過頭"
        ),
        "Clicking snooze repeatedly" to mapOf(
            Language.CHINESE_SIMPLIFIED to "反复按贪睡键",
            Language.CHINESE_TRADITIONAL to "反覆按貪睡鍵",
            Language.CHINESE_PINYIN to "Fǎn fù àn tān shuì jiàn",
            Language.ARABIC to "الضغط على زر الغفوة مرارًا",
            Language.TAGALOG to "Paulit-ulit na pag-click ng snooze",
            Language.THAI to "กดปุ่มเลื่อนปลุกซ้ำแล้วซ้ำเล่า",
            Language.HINDI to "बार-बार स्नूज़ दबाना",
            Language.CANTONESE to "反覆按貪睡鍵"
        ),
        "Waking up feeling tired" to mapOf(
            Language.CHINESE_SIMPLIFIED to "醒来时感觉疲惫",
            Language.CHINESE_TRADITIONAL to "醒來時感覺疲憊",
            Language.CHINESE_PINYIN to "Xǐng lái shí gǎn jué pí bèi",
            Language.ARABIC to "الاستيقاظ مع الشعور بالتعب",
            Language.TAGALOG to "Paggising nang nakakaramdam ng pagod",
            Language.THAI to "ตื่นนอนแล้วรู้สึกเหนื่อยล้า",
            Language.HINDI to "जागने पर थकान महसूस होना",
            Language.CANTONESE to "醒來時感覺疲憊"
        ),
        "2. What type of wake-up mission do you prefer?" to mapOf(
            Language.CHINESE_SIMPLIFIED to "2. 您倾向于哪种类型的唤醒任务？",
            Language.CHINESE_TRADITIONAL to "2. 您傾向於哪種類型的喚醒任務？",
            Language.CHINESE_PINYIN to "2. Nín qīng xiàng yú nǎ zhǒng lèi xíng...",
            Language.ARABIC to "2. ما نوع مهمة الاستيقاظ التي تفضلها؟",
            Language.TAGALOG to "2. Anong uri ng wakeup mission ang gusto mo?",
            Language.THAI to "2. คุณชอบภารกิจปลุกประเภทใด?",
            Language.HINDI to "2. आप किस प्रकार का वेक-अप मिशन पसंद करते हैं?",
            Language.CANTONESE to "2. 您傾向於哪種類型的喚醒任務？"
        ),
        "Mental warm-up (Math, Memory)" to mapOf(
            Language.CHINESE_SIMPLIFIED to "脑力热身 (数学、记忆)",
            Language.CHINESE_TRADITIONAL to "腦力熱身 (順學、記憶)",
            Language.CHINESE_PINYIN to "Nǎo lì rè shēn...",
            Language.ARABIC to "نشاط عقلي (رياضيات، ذاكرة)",
            Language.TAGALOG to "Mental warm-up (Math, Memory)",
            Language.THAI to "วอร์มอัพสมอง (คณิตศาสตร์, ความจำ)",
            Language.HINDI to "मानसिक वार्म-अप (गणित, स्मृति)",
            Language.CANTONESE to "腦力熱身 (數學、記憶)"
        ),
        "Physical activity (Shaking, Push-ups)" to mapOf(
            Language.CHINESE_SIMPLIFIED to "身体活动 (摇晃、俯卧撑)",
            Language.CHINESE_TRADITIONAL to "身體活動 (搖晃、俯臥撐)",
            Language.CHINESE_PINYIN to "Shēn tǐ huó dòng...",
            Language.ARABIC to "نشاط بدني (هز الهاتف、تمارين ضغط)",
            Language.TAGALOG to "Physical activity (Shaking, Push-ups)",
            Language.THAI to "กิจกรรมทางกาย (เขย่า, วิดพื้น)",
            Language.HINDI to "शारीरिक गतिविधि (हिलाना, पुश-अप्स)",
            Language.CANTONESE to "身體活動 (搖晃、俯臥撐)"
        ),
        "Out-of-bed actions (Sky Photo, Code scan)" to mapOf(
            Language.CHINESE_SIMPLIFIED to "下床行动 (天空照片、条码扫描)",
            Language.CHINESE_TRADITIONAL to "下床行動 (天空照片、條碼掃描)",
            Language.CHINESE_PINYIN to "Xià chuáng xíng dòng...",
            Language.ARABIC to "الخروج من السرير (صورة السماء، مسح الرمز)",
            Language.TAGALOG to "Mga kilos sa labas ng kama (Sky Photo, Code scan)",
            Language.THAI to "กิจกรรมนอกเตียง (ภาพถ่ายท้องฟ้า, สแกนรหัส)",
            Language.HINDI to "बिस्तर से उठने की क्रियाएं (आकाश फोटो, कोड स्कैन)",
            Language.CANTONESE to "下床行動 (天空照片、條碼掃描)"
        ),
        "Grant Access Permissions" to mapOf(
            Language.CHINESE_SIMPLIFIED to "授予访问权限",
            Language.CHINESE_TRADITIONAL to "授予訪問權限",
            Language.CHINESE_PINYIN to "Shòu yǔ fàng wèn quán xiàn",
            Language.ARABIC to "منح أذونات الوصول",
            Language.TAGALOG to "Ibigay ang mga Permission",
            Language.THAI to "อนุญาตสิทธิ์การเข้าถึง",
            Language.HINDI to "पहुंच अनुमतियां प्रदान करें",
            Language.CANTONESE to "授予訪問權限"
        ),
        "For alarms to ring reliably and show overlay screens, these permissions are required." to mapOf(
            Language.CHINESE_SIMPLIFIED to "为确保闹钟可靠响铃并显示覆盖屏幕，需要授予以下权限。",
            Language.CHINESE_TRADITIONAL to "為確保鬧鐘可靠響鈴並顯示覆蓋屏幕，需要授予以下權限。",
            Language.CHINESE_PINYIN to "Wèi què bǎo nào zhōng kě kào xiǎng líng...",
            Language.ARABIC to "لضمان رنين المنبه بشكل موثوق وإظهار شاشات التراكب، فإن هذه الأذونات مطلوبة.",
            Language.TAGALOG to "Upang tumunog nang maaasahan ang mga alarma at magpakita ng mga overlay, kinakailangan ang mga permission na ito.",
            Language.THAI to "เพื่อให้สัญญาณเตือนดังอย่างน่าเชื่อถือและแสดงผลทับแอปอื่น จำเป็นต้องได้รับสิทธิ์เหล่านี้",
            Language.HINDI to "अलार्म के विश्वसनीय रूप से बजने और ओवरले स्क्रीन दिखाने के लिए, इन अनुमतियों की आवश्यकता है।",
            Language.CANTONESE to "為確保鬧鐘可靠響鈴並顯示覆蓋屏幕，需要授予以下權限。"
        ),
        "Access Allowed" to mapOf(
            Language.CHINESE_SIMPLIFIED to "已允许访问",
            Language.CHINESE_TRADITIONAL to "已允許訪問",
            Language.CHINESE_PINYIN to "Yǐ yǔn xǔ fàng wèn",
            Language.ARABIC to "تم السماح بالوصول",
            Language.TAGALOG to "Pinapayagan",
            Language.THAI to "ได้รับอนุญาตแล้ว",
            Language.HINDI to "पहुंच की अनुमति है",
            Language.CANTONESE to "已允許訪問"
        ),
        "Required to trigger wake-up notifications" to mapOf(
            Language.CHINESE_SIMPLIFIED to "向您发送起床闹铃通知时必需",
            Language.CHINESE_TRADITIONAL to "向您發送起床鬧鈴通知時必需",
            Language.CHINESE_PINYIN to "Xiàng nín fā sòng qǐ chuáng nào líng...",
            Language.ARABIC to "مطلوب لتشغيل إشعارات الاستيقاظ",
            Language.TAGALOG to "Kailangan para ma-trigger ang mga gising na notification",
            Language.THAI to "จำเป็นสำหรับกระตุ้นการแจ้งเตือนปลุก",
            Language.HINDI to "वेक-अप सूचनाएं ट्रिगर करने के लिए आवश्यक",
            Language.CANTONESE to "向您發送起床鬧鈴通知時必需"
        ),
        "Required to display overlays over other apps" to mapOf(
            Language.CHINESE_SIMPLIFIED to "在其他应用之上显示闹钟响铃界面时必需",
            Language.CHINESE_TRADITIONAL to "在其他應用之上顯示鬧鐘響鈴界面時必需",
            Language.CHINESE_PINYIN to "Zài qí tā yìng yòng zhī shàng xiǎn shì...",
            Language.ARABIC to "مطلوب لعرض التراكبات فوق التطبيقات الأخرى",
            Language.TAGALOG to "Kailangan upang maipakita ang overlay sa ibabaw ng ibang apps",
            Language.THAI to "จำเป็นสำหรับแสดงหน้าต่างซ้อนทับแอปอื่น",
            Language.HINDI to "अन्य ऐप्स के ऊपर ओवरले प्रदर्शित करने के लिए आवश्यक",
            Language.CANTONESE to "在其他應用之上顯示鬧鐘響鈴界面時必需"
        ),
        "Required for photo capture and scanning missions" to mapOf(
            Language.CHINESE_SIMPLIFIED to "用于拍照和条码扫描解锁任务时必需",
            Language.CHINESE_TRADITIONAL to "用於拍照和條碼掃描解鎖任務時必需",
            Language.CHINESE_PINYIN to "Yòng yú pāi zhào hé tiáo mǎ sǎo miáo...",
            Language.ARABIC to "مطلوب لالتقاط الصور ومهام مسح الرموز",
            Language.TAGALOG to "Kailangan para sa pagkuha ng larawan at pag-scan",
            Language.THAI to "จำเป็นสำหรับถ่ายภาพและภารกิจสแกน",
            Language.HINDI to "फोटो लेने और स्कैनिंग मिशन के लिए आवश्यक",
            Language.CANTONESE to "用於拍照和條碼掃描解鎖任務時必需"
        ),
        "Choose your preferred language of use." to mapOf(
            Language.CHINESE_SIMPLIFIED to "选择您偏好使用的语言。",
            Language.CHINESE_TRADITIONAL to "選擇您偏好使用的語言。",
            Language.CHINESE_PINYIN to "Xuǎn zé nín piān hào shǐ yòng de yǔ yán.",
            Language.ARABIC to "اختر لغتك المفضلة للاستخدام.",
            Language.TAGALOG to "Pumili ng iyong gustong wika sa paggamit.",
            Language.THAI to "เลือกภาษาที่คุณต้องการใช้งาน",
            Language.HINDI to "अपनी पसंद की उपयोग की भाषा चुनें।",
            Language.CANTONESE to "選擇您偏好使用的語言。"
        ),
        "Congratulations! Setup is complete and you are fully prepared to wake up disciplined. Go ahead, set your first alarm and conquer your mornings!" to mapOf(
            Language.CHINESE_SIMPLIFIED to "恭喜！设置已完成，您已准备好自律起床。去吧，设置您的第一个闹钟，征服您的清晨！",
            Language.CHINESE_TRADITIONAL to "恭喜！設置已完成，您已準備好自律起床。去吧，設置您的第一個鬧鐘，征服您的清晨！",
            Language.CHINESE_PINYIN to "Gōng xǐ! Shè zhì yǐ wán chéng...",
            Language.ARABIC to "تهانينا! اكتمل الإعداد وأنت مستعد تمامًا للاستيقاظ بانضباط. تفضل، اضبط منبهك الأول واكتسح صباحك!",
            Language.TAGALOG to "Maligayang pagbati! Kumpleto na ang setup at handa ka nang gumising nang may disiplina. Sige na, i-set ang iyong unang alarma at lupigin ang iyong umaga!",
            Language.THAI to "ยินดีด้วย! การตั้งค่าเสร็จสิ้นแล้ว และคุณพร้อมอย่างเต็มที่ที่จะตื่นนอนอย่างมีวินัย ตั้งนาฬิกาปลุกเรือนแรกของคุณและพิชิตเช้าวันใหม่ได้เลย!",
            Language.HINDI to "बधाई हो! सेटअप पूरा हो गया है और आप जागने के लिए पूरी तरह तैयार हैं। आगे बढ़ें, अपना पहला अलार्म सेट करें और अपनी सुबह पर विजय प्राप्त करें!",
            Language.CANTONESE to "恭喜！設置已完成，您已準備好自律起床。去吧，設置您的第一個鬧鐘，征服您的清晨！"
        ),

        // Date and week names mapping
        "Monday" to mapOf(
            Language.CHINESE_SIMPLIFIED to "星期一", Language.CHINESE_TRADITIONAL to "星期一",
            Language.CHINESE_PINYIN to "Xīng qī yī", Language.ARABIC to "الإثنين",
            Language.TAGALOG to "Lunes", Language.THAI to "วันจันทร์", Language.HINDI to "सोमवार", Language.CANTONESE to "星期一"
        ),
        "Tuesday" to mapOf(
            Language.CHINESE_SIMPLIFIED to "星期二", Language.CHINESE_TRADITIONAL to "星期二",
            Language.CHINESE_PINYIN to "Xīng qī èr", Language.ARABIC to "الثلاثاء",
            Language.TAGALOG to "Martes", Language.THAI to "วันอังคาร", Language.HINDI to "मंगलवार", Language.CANTONESE to "星期二"
        ),
        "Wednesday" to mapOf(
            Language.CHINESE_SIMPLIFIED to "星期三", Language.CHINESE_TRADITIONAL to "星期三",
            Language.CHINESE_PINYIN to "Xīng qī sān", Language.ARABIC to "الأربعاء",
            Language.TAGALOG to "Miyerkules", Language.THAI to "วันพุธ", Language.HINDI to "बुधवार", Language.CANTONESE to "星期三"
        ),
        "Thursday" to mapOf(
            Language.CHINESE_SIMPLIFIED to "星期四", Language.CHINESE_TRADITIONAL to "星期四",
            Language.CHINESE_PINYIN to "Xīng qī sì", Language.ARABIC to "الخميس",
            Language.TAGALOG to "Huwebes", Language.THAI to "วันพฤหัสบดี", Language.HINDI to "गुरुवार", Language.CANTONESE to "星期四"
        ),
        "Friday" to mapOf(
            Language.CHINESE_SIMPLIFIED to "星期五", Language.CHINESE_TRADITIONAL to "星期五",
            Language.CHINESE_PINYIN to "Xīng qī wǔ", Language.ARABIC to "الجمعة",
            Language.TAGALOG to "Biyernes", Language.THAI to "วันศุกร์", Language.HINDI to "शुक्रवार", Language.CANTONESE to "星期五"
        ),
        "Saturday" to mapOf(
            Language.CHINESE_SIMPLIFIED to "星期六", Language.CHINESE_TRADITIONAL to "星期六",
            Language.CHINESE_PINYIN to "Xīng qī liù", Language.ARABIC to "السبت",
            Language.TAGALOG to "Sabado", Language.THAI to "วันเสาร์", Language.HINDI to "शनिवार", Language.CANTONESE to "星期六"
        ),
        "Sunday" to mapOf(
            Language.CHINESE_SIMPLIFIED to "星期日", Language.CHINESE_TRADITIONAL to "星期日",
            Language.CHINESE_PINYIN to "Xīng qī rì", Language.ARABIC to "الأحد",
            Language.TAGALOG to "Linggo", Language.THAI to "วันอาทิตย์", Language.HINDI to "रविवार", Language.CANTONESE to "星期日"
        ),

        // Initials (Avoid key conflicts by using unique keys)
        "Day_Mon_Initial" to mapOf(
            Language.ENGLISH to "M",
            Language.CHINESE_SIMPLIFIED to "一", Language.CHINESE_TRADITIONAL to "一", Language.CHINESE_PINYIN to "Yī",
            Language.ARABIC to "ن", Language.TAGALOG to "M", Language.THAI to "จ", Language.HINDI to "सोम", Language.CANTONESE to "一"
        ),
        "Day_Tue_Initial" to mapOf(
            Language.ENGLISH to "T",
            Language.CHINESE_SIMPLIFIED to "二", Language.CHINESE_TRADITIONAL to "二", Language.CHINESE_PINYIN to "Èr",
            Language.ARABIC to "ث", Language.TAGALOG to "T", Language.THAI to "อ", Language.HINDI to "मं", Language.CANTONESE to "二"
        ),
        "Day_Wed_Initial" to mapOf(
            Language.ENGLISH to "W",
            Language.CHINESE_SIMPLIFIED to "三", Language.CHINESE_TRADITIONAL to "三", Language.CHINESE_PINYIN to "Sān",
            Language.ARABIC to "ر", Language.TAGALOG to "W", Language.THAI to "พ", Language.HINDI to "बुध", Language.CANTONESE to "三"
        ),
        "Day_Thu_Initial" to mapOf(
            Language.ENGLISH to "T",
            Language.CHINESE_SIMPLIFIED to "四", Language.CHINESE_TRADITIONAL to "四", Language.CHINESE_PINYIN to "Sì",
            Language.ARABIC to "خ", Language.TAGALOG to "T", Language.THAI to "พฤ", Language.HINDI to "गुरु", Language.CANTONESE to "四"
        ),
        "Day_Fri_Initial" to mapOf(
            Language.ENGLISH to "F",
            Language.CHINESE_SIMPLIFIED to "五", Language.CHINESE_TRADITIONAL to "五", Language.CHINESE_PINYIN to "Wǔ",
            Language.ARABIC to "ج", Language.TAGALOG to "F", Language.THAI to "ศ", Language.HINDI to "शुक्र", Language.CANTONESE to "五"
        ),
        "Day_Sat_Initial" to mapOf(
            Language.ENGLISH to "S",
            Language.CHINESE_SIMPLIFIED to "六", Language.CHINESE_TRADITIONAL to "六", Language.CHINESE_PINYIN to "Liù",
            Language.ARABIC to "س", Language.TAGALOG to "S", Language.THAI to "ส", Language.HINDI to "शनि", Language.CANTONESE to "六"
        ),
        "Day_Sun_Initial" to mapOf(
            Language.ENGLISH to "S",
            Language.CHINESE_SIMPLIFIED to "日", Language.CHINESE_TRADITIONAL to "日", Language.CHINESE_PINYIN to "Rì",
            Language.ARABIC to "ح", Language.TAGALOG to "S", Language.THAI to "อา", Language.HINDI to "रवि", Language.CANTONESE to "日"
        ),

        // Helper time/repeat strings
        "Every day" to mapOf(
            Language.CHINESE_SIMPLIFIED to "每天", Language.CHINESE_TRADITIONAL to "每天",
            Language.CHINESE_PINYIN to "Měi tiān", Language.ARABIC to "كل يوم",
            Language.TAGALOG to "Araw-araw", Language.THAI to "ทุกวัน", Language.HINDI to "हर दिन", Language.CANTONESE to "每天"
        ),
        "Weekdays" to mapOf(
            Language.CHINESE_SIMPLIFIED to "工作日", Language.CHINESE_TRADITIONAL to "工作日",
            Language.CHINESE_PINYIN to "Gōng zuò rì", Language.ARABIC to "أيام العمل",
            Language.TAGALOG to "Mga araw ng linggo", Language.THAI to "วันธรรมดา", Language.HINDI to "कार्यदिवस", Language.CANTONESE to "工作日"
        ),
        "Weekends" to mapOf(
            Language.CHINESE_SIMPLIFIED to "周末", Language.CHINESE_TRADITIONAL to "週末",
            Language.CHINESE_PINYIN to "Zhōu mò", Language.ARABIC to "عطلة نهاية الأسبوع",
            Language.TAGALOG to "Katapusan ng linggo", Language.THAI to "วันหยุดสุดสัปดาห์", Language.HINDI to "सप्ताहांत", Language.CANTONESE to "週末"
        ),
        "Rings now" to mapOf(
            Language.CHINESE_SIMPLIFIED to "现在响铃", Language.CHINESE_TRADITIONAL to "現在響鈴",
            Language.CHINESE_PINYIN to "Xiàn zài xiǎng líng", Language.ARABIC to "يرن الآن",
            Language.TAGALOG to "Tumutunog na", Language.THAI to "ดังขึ้นตอนนี้", Language.HINDI to "अब बज रहा है", Language.CANTONESE to "現在響鈴"
        ),
        "Rings in {time}" to mapOf(
            Language.CHINESE_SIMPLIFIED to "{time}后响铃", Language.CHINESE_TRADITIONAL to "{time}後響鈴",
            Language.CHINESE_PINYIN to "{time} hòu xiǎng líng", Language.ARABIC to "يرن في {time}",
            Language.TAGALOG to "Tútúnóg sa loób ng {time}", Language.THAI to "จะดังขึ้นในอีก {time}", Language.HINDI to "{time} में बजेगा", Language.CANTONESE to "{time}後響鈴"
        ),
        "Next: Today {time}" to mapOf(
            Language.CHINESE_SIMPLIFIED to "下次响铃：今天 {time}", Language.CHINESE_TRADITIONAL to "下次響鈴：今天 {time}",
            Language.CHINESE_PINYIN to "Xià cì xiǎng líng: Jīn tiān {time}", Language.ARABIC to "المنبه القادم: اليوم {time}",
            Language.TAGALOG to "Susunod: Ngayong araw {time}", Language.THAI to "ปลุกครั้งถัดไป: วันนี้ {time}", Language.HINDI to "अगला: आज {time}", Language.CANTONESE to "下次響鈴：今天 {time}"
        ),
        "Next: Tomorrow {time}" to mapOf(
            Language.CHINESE_SIMPLIFIED to "下次响铃：明天 {time}", Language.CHINESE_TRADITIONAL to "下次響鈴：明天 {time}",
            Language.CHINESE_PINYIN to "Xià cì xiǎng líng: Míng tiān {time}", Language.ARABIC to "المنبه القادم: غدًا {time}",
            Language.TAGALOG to "Susunod: Bukas {time}", Language.THAI to "ปลุกครั้งถัดไป: พรุ่งนี้ {time}", Language.HINDI to "अगला: कल {time}", Language.CANTONESE to "下次響鈴：明天 {time}"
        ),
        "Next: {day} {time}" to mapOf(
            Language.CHINESE_SIMPLIFIED to "下次响铃：{day} {time}", Language.CHINESE_TRADITIONAL to "下次響鈴：{day} {time}",
            Language.CHINESE_PINYIN to "Xià cì xiǎng líng: {day} {time}", Language.ARABIC to "المنبه القادم: {day} {time}",
            Language.TAGALOG to "Susunod: {day} {time}", Language.THAI to "ปลุกครั้งถัดไป: {day} {time}", Language.HINDI to "अगला: {day} {time}", Language.CANTONESE to "下次響鈴：{day} {time}"
        ),

        // Main screens
        "No alarms set" to mapOf(
            Language.CHINESE_SIMPLIFIED to "未设置闹钟", Language.CHINESE_TRADITIONAL to "未設置鬧鐘",
            Language.CHINESE_PINYIN to "Wèi shè zhì nào zhōng", Language.ARABIC to "لا توجد منبهات",
            Language.TAGALOG to "Walang nakatakdang alarma", Language.THAI to "ไม่มีการตั้งนาฬิกาปลุก", Language.HINDI to "कोई अलार्म सेट नहीं है", Language.CANTONESE to "未設置鬧鐘"
        ),
        "Tap the Alarms tab to create one" to mapOf(
            Language.CHINESE_SIMPLIFIED to "点击闹钟选项卡创建一个", Language.CHINESE_TRADITIONAL to "點擊鬧鐘選項卡創建一個",
            Language.CHINESE_PINYIN to "Diǎn jī nào zhōng xuǎn xiàng kǎ chuàng jiàn...",
            Language.ARABIC to "اضغط على علامة تبويب المنبهات لإنشاء منبه", Language.TAGALOG to "I-tap ang tab ng Alarma para gumawa",
            Language.THAI to "แตะแท็บนาฬิกาปลุกเพื่อสร้างใหม่", Language.HINDI to "अलार्म टैब पर टैप करके बनाएं", Language.CANTONESE to "點擊鬧鐘選項卡創建一個"
        ),
        "Today" to mapOf(
            Language.CHINESE_SIMPLIFIED to "今天", Language.CHINESE_TRADITIONAL to "今天",
            Language.CHINESE_PINYIN to "Jīn tiān", Language.ARABIC to "اليوم",
            Language.TAGALOG to "Ngayon", Language.THAI to "วันนี้", Language.HINDI to "आज", Language.CANTONESE to "今天"
        ),
        "Mission" to mapOf(
            Language.CHINESE_SIMPLIFIED to "任务", Language.CHINESE_TRADITIONAL to "任務",
            Language.CHINESE_PINYIN to "Rèn wu", Language.ARABIC to "المهمة",
            Language.TAGALOG to "Misyon", Language.THAI to "ภารกิจ", Language.HINDI to "मिशन", Language.CANTONESE to "任務"
        ),
        "Sound" to mapOf(
            Language.CHINESE_SIMPLIFIED to "铃声", Language.CHINESE_TRADITIONAL to "鈴聲",
            Language.CHINESE_PINYIN to "Líng shēng", Language.ARABIC to "الصوت",
            Language.TAGALOG to "Tunog", Language.THAI to "เสียง", Language.HINDI to "ध्वनि", Language.CANTONESE to "鈴聲"
        ),
        "No wake-ups today yet" to mapOf(
            Language.CHINESE_SIMPLIFIED to "今天还没有起床记录", Language.CHINESE_TRADITIONAL to "今天還沒有起床記錄",
            Language.CHINESE_PINYIN to "Jīn tiān hái méi...", Language.ARABIC to "لا استيقاظ اليوم بعد",
            Language.TAGALOG to "Wala pang gising ngayong araw", Language.THAI to "ยังไม่มีการตื่นนอนในวันนี้", Language.HINDI to "आज अभी तक कोई गवाही नहीं", Language.CANTONESE to "今天還沒有起床記錄"
        ),
        "Delete Alarm" to mapOf(
            Language.CHINESE_SIMPLIFIED to "删除闹钟", Language.CHINESE_TRADITIONAL to "刪除鬧鐘",
            Language.CHINESE_PINYIN to "Shān chú nào zhōng", Language.ARABIC to "حذف المنبه",
            Language.TAGALOG to "I-delete ang Alarma", Language.THAI to "ลบนาฬิกาปลุก", Language.HINDI to "अलार्म हटाएं", Language.CANTONESE to "刪除鬧鐘"
        ),
        "Are you sure you want to delete this alarm?" to mapOf(
            Language.CHINESE_SIMPLIFIED to "确定要删除此闹钟吗？", Language.CHINESE_TRADITIONAL to "確定要刪除此鬧鐘嗎？",
            Language.CHINESE_PINYIN to "Què dìng yào shān chú cǐ nào zhōng ma?", Language.ARABIC to "هل أنت متأكد من حذف هذا المنبه؟",
            Language.TAGALOG to "Sigurado ka bang gusto mong burahin ang alarma na ito?", Language.THAI to "คุณแน่ใจหรือไม่ว่าต้องการลบนาฬิกาปลุกนี้?",
            Language.HINDI to "क्या आप वाकई इस अलार्म को हटाना चाहते हैं?", Language.CANTONESE to "確定要刪除此鬧鐘嗎？"
        ),
        "Delete" to mapOf(
            Language.CHINESE_SIMPLIFIED to "删除", Language.CHINESE_TRADITIONAL to "刪除",
            Language.CHINESE_PINYIN to "Shān chú", Language.ARABIC to "حذف",
            Language.TAGALOG to "I-delete", Language.THAI to "ลบ", Language.HINDI to "हटाएं", Language.CANTONESE to "刪除"
        ),
        "Cancel" to mapOf(
            Language.CHINESE_SIMPLIFIED to "取消", Language.CHINESE_TRADITIONAL to "取消",
            Language.CHINESE_PINYIN to "Qǔ xiāo", Language.ARABIC to "إلغاء",
            Language.TAGALOG to "Kanselahin", Language.THAI to "ยกเลิก", Language.HINDI to "रद्द करें", Language.CANTONESE to "取消"
        ),
        "1 alarm" to mapOf(
            Language.CHINESE_SIMPLIFIED to "1 个闹钟", Language.CHINESE_TRADITIONAL to "1 個鬧鐘",
            Language.CHINESE_PINYIN to "1 gè nào zhōng", Language.ARABIC to "منبه واحد",
            Language.TAGALOG to "1 alarma", Language.THAI to "นาฬิกาปลุก 1 รายการ", Language.HINDI to "1 अलार्म", Language.CANTONESE to "1 個鬧鐘"
        ),
        "{count} alarms" to mapOf(
            Language.CHINESE_SIMPLIFIED to "{count} 个闹钟", Language.CHINESE_TRADITIONAL to "{count} 個鬧鐘",
            Language.CHINESE_PINYIN to "{count} gè nào zhōng", Language.ARABIC to "{count} منبهات",
            Language.TAGALOG to "{count} mga alarma", Language.THAI to "นาฬิกาปลุก {count} รายการ", Language.HINDI to "{count} अलार्म", Language.CANTONESE to "{count} 個鬧鐘"
        ),
        "+ New alarm" to mapOf(
            Language.CHINESE_SIMPLIFIED to "+ 新建闹钟", Language.CHINESE_TRADITIONAL to "+ 新建鬧鐘",
            Language.CHINESE_PINYIN to "+ Xīn jiàn nào zhōng", Language.ARABIC to "+ منبه جديد",
            Language.TAGALOG to "+ Bagong Alarma", Language.THAI to "+ เพิ่มนาฬิกาปลุก", Language.HINDI to "+ नया अलार्म", Language.CANTONESE to "+ 新建鬧鐘"
        ),
        "Set Time" to mapOf(
            Language.CHINESE_SIMPLIFIED to "设置时间", Language.CHINESE_TRADITIONAL to "設置時間",
            Language.CHINESE_PINYIN to "Shè zhì shí jiān", Language.ARABIC to "ضبط الوقت",
            Language.TAGALOG to "I-set ang Oras", Language.THAI to "ตั้งเวลา", Language.HINDI to "समय सेट करें", Language.CANTONESE to "設置時間"
        ),
        "Optional label" to mapOf(
            Language.CHINESE_SIMPLIFIED to "可选标签", Language.CHINESE_TRADITIONAL to "可選標籤",
            Language.CHINESE_PINYIN to "Kě xuǎn biāo qiān", Language.ARABIC to "ملصق اختياري",
            Language.TAGALOG to "Opsyonal na label", Language.THAI to "ป้ายกำกับเพิ่มเติม", Language.HINDI to "वैकल्पिक लेबल", Language.CANTONESE to "可選標籤"
        ),
        "Tap to change" to mapOf(
            Language.CHINESE_SIMPLIFIED to "点击修改", Language.CHINESE_TRADITIONAL to "點擊修改",
            Language.CHINESE_PINYIN to "Diǎn jī xiū gǎi", Language.ARABIC to "اضغط للتغيير",
            Language.TAGALOG to "I-tap para palitan", Language.THAI to "แตะเพื่อเปลี่ยน", Language.HINDI to "बदलने के लिए टैप करें", Language.CANTONESE to "點擊修改"
        ),
        "QR Code Value" to mapOf(
            Language.CHINESE_SIMPLIFIED to "QR码内容", Language.CHINESE_TRADITIONAL to "QR碼內容",
            Language.CHINESE_PINYIN to "QR mǎ nèi róng", Language.ARABIC to "قيمة رمز QR",
            Language.TAGALOG to "QR Code Value", Language.THAI to "ค่ารหัส QR", Language.HINDI to "क्यूआर कोड मान", Language.CANTONESE to "QR碼內容"
        ),
        "Barcode Value" to mapOf(
            Language.CHINESE_SIMPLIFIED to "条形码内容", Language.CHINESE_TRADITIONAL to "條形碼內容",
            Language.CHINESE_PINYIN to "Tiáo xíng mǎ nèi róng", Language.ARABIC to "قيمة الرمز البارز",
            Language.TAGALOG to "Barcode Value", Language.THAI to "ค่าบาร์โค้ด", Language.HINDI to "बारकोड मान", Language.CANTONESE to "條形碼內容"
        ),
        "Enter target code value" to mapOf(
            Language.CHINESE_SIMPLIFIED to "输入目标条码值", Language.CHINESE_TRADITIONAL to "輸入目標條碼值",
            Language.CHINESE_PINYIN to "Shū rù mù biāo tiáo mǎ zhí", Language.ARABIC to "أدخل قيمة الرمز المستهدفة",
            Language.TAGALOG to "Ilagay ang target na code", Language.THAI to "ป้อนค่ารหัสที่ต้องการ", Language.HINDI to "लक्ष्य कोड मान दर्ज करें", Language.CANTONESE to "輸入目標條碼值"
        ),
        "Scan" to mapOf(
            Language.CHINESE_SIMPLIFIED to "扫描", Language.CHINESE_TRADITIONAL to "掃描",
            Language.CHINESE_PINYIN to "Sǎo miáo", Language.ARABIC to "مسح",
            Language.TAGALOG to "I-scan", Language.THAI to "สแกน", Language.HINDI to "स्कैन", Language.CANTONESE to "掃描"
        ),
        "Step Count Level" to mapOf(
            Language.CHINESE_SIMPLIFIED to "步数等级", Language.CHINESE_TRADITIONAL to "步數等級",
            Language.CHINESE_PINYIN to "Bù shù děng jí", Language.ARABIC to "مستوى عد الخطوات",
            Language.TAGALOG to "Antas ng Bilang ng Hakbang", Language.THAI to "ระดับการนับก้าว", Language.HINDI to "कदम गिनती स्तर", Language.CANTONESE to "步數等級"
        ),
        "Level {number}" to mapOf(
            Language.CHINESE_SIMPLIFIED to "等级 {number}", Language.CHINESE_TRADITIONAL to "等級 {number}",
            Language.CHINESE_PINYIN to "Děng jí {number}", Language.ARABIC to "مستوى {number}",
            Language.TAGALOG to "Antas {number}", Language.THAI to "ระดับ {number}", Language.HINDI to "स्तर {number}", Language.CANTONESE to "等級 {number}"
        ),
        "Level 1: 5 steps (Easy) to Level 10: 50 steps (Difficult)" to mapOf(
            Language.CHINESE_SIMPLIFIED to "等级 1: 5 步 (简单) 至 等级 10: 50 步 (困难)",
            Language.CHINESE_TRADITIONAL to "等級 1: 5 步 (簡單) 至 等級 10: 50 步 (困難)",
            Language.CHINESE_PINYIN to "Děng jí 1: 5 bù (jiǎn dān) zhì Děng jí 10: 50 bù (kùn nan)",
            Language.ARABIC to "المستوى 1: 5 خطوات (سهل) إلى المستوى 10: 50 خطوة (صعب)",
            Language.TAGALOG to "Antas 1: 5 hakbang (Madali) hanggang Antas 10: 50 hakbang (Mahirap)",
            Language.THAI to "ระดับ 1: 5 ก้าว (ง่าย) ถึง ระดับ 10: 50 ก้าว (ยาก)",
            Language.HINDI to "स्तर 1: 5 कदम (आसान) से स्तर 10: 50 कदम (कठिन)",
            Language.CANTONESE to "等級 1: 5 步 (簡單) 至 等級 10: 50 步 (困難)"
        ),
        "Tap to change sound" to mapOf(
            Language.CHINESE_SIMPLIFIED to "点击修改铃声", Language.CHINESE_TRADITIONAL to "點擊修改鈴聲",
            Language.CHINESE_PINYIN to "Diǎn jī xiū gǎi líng shēng", Language.ARABIC to "اضغط لتغيير الصوت",
            Language.TAGALOG to "I-tap para palitan ang tunog", Language.THAI to "แตะเพื่อเปลี่ยนเสียง", Language.HINDI to "ध्वनि बदलने के लिए टैप करें", Language.CANTONESE to "點擊修改鈴聲"
        ),
        "Choose mission" to mapOf(
            Language.CHINESE_SIMPLIFIED to "选择任务", Language.CHINESE_TRADITIONAL to "選擇任務",
            Language.CHINESE_PINYIN to "Xuǎn zé rèn wu", Language.ARABIC to "اختر المهمة",
            Language.TAGALOG to "Pumili ng Misyon", Language.THAI to "เลือกภารกิจ", Language.HINDI to "मिशन चुनें", Language.CANTONESE to "選擇任務"
        ),
        "FREE" to mapOf(
            Language.CHINESE_SIMPLIFIED to "免费", Language.CHINESE_TRADITIONAL to "免費",
            Language.CHINESE_PINYIN to "Miǎn fèi", Language.ARABIC to "مجاني",
            Language.TAGALOG to "LIBRE", Language.THAI to "ฟรี", Language.HINDI to "मुफ़्त", Language.CANTONESE to "免費"
        ),
        "PREMIUM" to mapOf(
            Language.CHINESE_SIMPLIFIED to "高级", Language.CHINESE_TRADITIONAL to "高級",
            Language.CHINESE_PINYIN to "Gāo jí", Language.ARABIC to "مدفوع",
            Language.TAGALOG to "PREMIUM", Language.THAI to "พรีเมียม", Language.HINDI to "प्रीमियम", Language.CANTONESE to "高級"
        ),
        "Demonstration Mode" to mapOf(
            Language.CHINESE_SIMPLIFIED to "演示模式", Language.CHINESE_TRADITIONAL to "演示模式",
            Language.CHINESE_PINYIN to "Yǎn shì mó shì", Language.ARABIC to "وضع العرض التوضيحي",
            Language.TAGALOG to "Demonstration Mode", Language.THAI to "โหมดสาธิต", Language.HINDI to "प्रदर्शन मोड", Language.CANTONESE to "演示模式"
        ),
        "Testing: {name}" to mapOf(
            Language.CHINESE_SIMPLIFIED to "测试：{name}", Language.CHINESE_TRADITIONAL to "測試：{name}",
            Language.CHINESE_PINYIN to "Cè shì: {name}", Language.ARABIC to "اختبار: {name}",
            Language.TAGALOG to "Testing: {name}", Language.THAI to "กำลังทดสอบ: {name}", Language.HINDI to "परीक्षण: {name}", Language.CANTONESE to "測試：{name}"
        ),
        "Exit Demo" to mapOf(
            Language.CHINESE_SIMPLIFIED to "退出演示", Language.CHINESE_TRADITIONAL to "退出演示",
            Language.CHINESE_PINYIN to "Tuì chū yǎn shì", Language.ARABIC to "إنهاء العرض",
            Language.TAGALOG to "Lumabas sa Demo", Language.THAI to "ออกจากโหมดสาธิต", Language.HINDI to "डेमो से बाहर निकलें", Language.CANTONESE to "退出演示"
        ),
        "Alarm sound" to mapOf(
            Language.CHINESE_SIMPLIFIED to "闹钟铃声", Language.CHINESE_TRADITIONAL to "鬧鐘鈴聲",
            Language.CHINESE_PINYIN to "Nào zhōng líng shēng", Language.ARABIC to "صوت المنبه",
            Language.TAGALOG to "Tunog ng Alarma", Language.THAI to "เสียงนาฬิกาปลุก", Language.HINDI to "अलार्म ध्वनि", Language.CANTONESE to "鬧鐘鈴聲"
        ),
        "VOLUME" to mapOf(
            Language.CHINESE_SIMPLIFIED to "音量", Language.CHINESE_TRADITIONAL to "音量",
            Language.CHINESE_PINYIN to "Yīn liàng", Language.ARABIC to "مستوى الصوت",
            Language.TAGALOG to "VOLUME", Language.THAI to "ระดับเสียง", Language.HINDI to "वॉल्यूम", Language.CANTONESE to "音量"
        ),
        "CUSTOM SONG" to mapOf(
            Language.CHINESE_SIMPLIFIED to "自定义歌曲", Language.CHINESE_TRADITIONAL to "自定義歌曲",
            Language.CHINESE_PINYIN to "Zì dìng yì gē qǔ", Language.ARABIC to "أغنية مخصصة",
            Language.TAGALOG to "CUSTOM NA KANTA", Language.THAI to "เพลงที่กำหนดเอง", Language.HINDI to "कस्टम गीत", Language.CANTONESE to "自定義歌曲"
        ),
        "Tap ▶ to preview." to mapOf(
            Language.CHINESE_SIMPLIFIED to "点击 ▶ 试听。", Language.CHINESE_TRADITIONAL to "點擊 ▶ 試聽。",
            Language.CHINESE_PINYIN to "Diǎn jī ▶ shì tīng.", Language.ARABIC to "اضغط على ▶ للمعاينة.",
            Language.TAGALOG to "Tapikin ang ▶ para sa preview.", Language.THAI to "แตะ ▶ เพื่อฟังเสียงตัวอย่าง", Language.HINDI to "पूर्वावलोकन के लिए ▶ टैप करें।", Language.CANTONESE to "點擊 ▶ 試聽。"
        ),

        // Mission display names
        "Math Problem" to mapOf(
            Language.CHINESE_SIMPLIFIED to "数学问题", Language.CHINESE_TRADITIONAL to "數學問題", Language.CHINESE_PINYIN to "Shù xué wèn tí",
            Language.ARABIC to "مسألة رياضية", Language.TAGALOG to "Problemang Math", Language.THAI to "โจทย์คณิตศาสตร์", Language.HINDI to "गणित की समस्या", Language.CANTONESE to "數學問題"
        ),
        "Shake" to mapOf(
            Language.CHINESE_SIMPLIFIED to "摇晃手机", Language.CHINESE_TRADITIONAL to "搖晃手機", Language.CHINESE_PINYIN to "Yáo huàng shǒu jī",
            Language.ARABIC to "هز الهاتف", Language.TAGALOG to "Shake", Language.THAI to "เขย่า", Language.HINDI to "हिलाएं", Language.CANTONESE to "搖晃手機"
        ),
        "Sky Photo" to mapOf(
            Language.CHINESE_SIMPLIFIED to "天空照片", Language.CHINESE_TRADITIONAL to "天空照片", Language.CHINESE_PINYIN to "Tiān kōng zhào piàn",
            Language.ARABIC to "صورة السماء", Language.TAGALOG to "Larawan ng Langit", Language.THAI to "ภาพถ่ายท้องฟ้า", Language.HINDI to "आकाश की तस्वीर", Language.CANTONESE to "天空照片"
        ),
        "Make Your Bed" to mapOf(
            Language.CHINESE_SIMPLIFIED to "整理床铺", Language.CHINESE_TRADITIONAL to "整理床鋪", Language.CHINESE_PINYIN to "Zhěng lǐ chuáng pù",
            Language.ARABIC to "ترتيب السرير", Language.TAGALOG to "Iayos ang Kama", Language.THAI to "จัดเตียง", Language.HINDI to "बिस्तर सही करें", Language.CANTONESE to "整理床鋪"
        ),
        "Object Hunt" to mapOf(
            Language.CHINESE_SIMPLIFIED to "寻找物品", Language.CHINESE_TRADITIONAL to "尋找物品", Language.CHINESE_PINYIN to "Xún zhǎo wù pǐn",
            Language.ARABIC to "البحث عن غرض", Language.TAGALOG to "Paghahanap ng Bagay", Language.THAI to "ล่าสิ่งของ", Language.HINDI to "वस्तु की खोज", Language.CANTONESE to "尋找物品"
        ),
        "Quote of the Day" to mapOf(
            Language.CHINESE_SIMPLIFIED to "每日名言", Language.CHINESE_TRADITIONAL to "每日名言", Language.CHINESE_PINYIN to "Měi rì míng yán",
            Language.ARABIC to "مقولة اليوم", Language.TAGALOG to "Quote of the Day", Language.THAI to "คำคมประจำวัน", Language.HINDI to "आज का विचार", Language.CANTONESE to "每日名言"
        ),
        "Affirmation" to mapOf(
            Language.CHINESE_SIMPLIFIED to "每日宣誓", Language.CHINESE_TRADITIONAL to "每日宣誓", Language.CHINESE_PINYIN to "Měi rì xuān shì",
            Language.ARABIC to "توكيد إيجابي", Language.TAGALOG to "Affirmation", Language.THAI to "การยืนยันเชิงบวก", Language.HINDI to "प्रतिज्ञा", Language.CANTONESE to "每日宣誓"
        ),
        "Push-Ups" to mapOf(
            Language.CHINESE_SIMPLIFIED to "俯卧撑", Language.CHINESE_TRADITIONAL to "俯臥撐", Language.CHINESE_PINYIN to "Fǔ wò chēng",
            Language.ARABIC to "تمارين الضغط", Language.TAGALOG to "Push-Ups", Language.THAI to "วิดพื้น", Language.HINDI to "पुश-अप्स", Language.CANTONESE to "俯臥撐"
        ),
        "Memory" to mapOf(
            Language.CHINESE_SIMPLIFIED to "记忆力游戏", Language.CHINESE_TRADITIONAL to "記憶力遊戲", Language.CHINESE_PINYIN to "Jì yì lì yóu xì",
            Language.ARABIC to "لعبة الذاكرة", Language.TAGALOG to "Memory Game", Language.THAI to "ความจำ", Language.HINDI to "मेमोरी गेम", Language.CANTONESE to "記憶力遊戲"
        ),
        "Typing" to mapOf(
            Language.CHINESE_SIMPLIFIED to "打字练习", Language.CHINESE_TRADITIONAL to "打字練習", Language.CHINESE_PINYIN to "Dǎ zì liàn xí",
            Language.ARABIC to "الكتابة", Language.TAGALOG to "Typing", Language.THAI to "พิมพ์ข้อความ", Language.HINDI to "टाइपिंग", Language.CANTONESE to "打字練習"
        ),
        "QR Code" to mapOf(
            Language.CHINESE_SIMPLIFIED to "扫描二维码", Language.CHINESE_TRADITIONAL to "掃描二維碼", Language.CHINESE_PINYIN to "Sǎo miáo èr wéi mǎ",
            Language.ARABIC to "رمز QR", Language.TAGALOG to "QR Code", Language.THAI to "รหัส QR", Language.HINDI to "क्यूआर कोड", Language.CANTONESE to "掃描二維碼"
        ),
        "Barcode" to mapOf(
            Language.CHINESE_SIMPLIFIED to "扫描条形码", Language.CHINESE_TRADITIONAL to "掃描條形碼", Language.CHINESE_PINYIN to "Sǎo miáo tiáo xíng mǎ",
            Language.ARABIC to "الرمز البارز", Language.TAGALOG to "Barcode", Language.THAI to "บาร์โค้ด", Language.HINDI to "बारकोड", Language.CANTONESE to "掃描條形碼"
        ),
        "Step Count" to mapOf(
            Language.CHINESE_SIMPLIFIED to "计步解锁", Language.CHINESE_TRADITIONAL to "計步解鎖", Language.CHINESE_PINYIN to "Jì bù jiě suǒ",
            Language.ARABIC to "عد الخطوات", Language.TAGALOG to "Bilang ng Hakbang", Language.THAI to "นับก้าว", Language.HINDI to "कदम गिनती", Language.CANTONESE to "計步解鎖"
        ),

        // Mission descriptions
        "Solve a short arithmetic problem to dismiss the alarm — quick mental warm-up that wakes your brain up." to mapOf(
            Language.CHINESE_SIMPLIFIED to "解决一道简短的算术题来关闭闹钟 — 快速唤醒你的大脑。",
            Language.CHINESE_TRADITIONAL to "解決一道簡短的算術題來關閉鬧鐘 — 快速喚醒你的大腦。",
            Language.CHINESE_PINYIN to "Jiě jué yī dào jiǎn duǎn shù xué tí...",
            Language.ARABIC to "حل مسألة حسابية قصيرة لإيقاف المنبه - عملية إحماء عقلي سريعة توقظ دماغك.",
            Language.TAGALOG to "Lutasin ang isang maikling arithmetic problem upang patayin ang alarma — mabilis na paggising ng utak.",
            Language.THAI to "แก้โจทย์คณิตศาสตร์สั้นๆ เพื่อปิดการปลุก — วอร์มอัพสมองอย่างรวดเร็วเพื่อให้สมองตื่นตัว",
            Language.HINDI to "अलार्म बंद करने के लिए एक छोटी अंकगणितीय समस्या को हल करें - त्वरित मानसिक वार्म-अप जो आपके मस्तिष्क को जगाता है।",
            Language.CANTONESE to "解決一道簡短的算術題來關閉鬧鐘 — 快速喚醒你的大腦。"
        ),
        "Shake your phone vigorously until the progress bar fills up. Forces you to physically move before going back to bed." to mapOf(
            Language.CHINESE_SIMPLIFIED to "用力摇晃手机，直到进度条充满。迫使你在回床睡觉前动起来。",
            Language.CHINESE_TRADITIONAL to "用力搖晃手機，直到進度條充滿。迫使你在回床睡覺前動起來。",
            Language.CHINESE_PINYIN to "Yòng lì yáo huàng shǒu jī...",
            Language.ARABIC to "هز هاتفك بقوة حتى يكتمل شريط التقدم. يجبرك على التحرك جسديًا قبل العودة إلى النوم.",
            Language.TAGALOG to "I-shake nang malakas ang iyong telepono hanggang mapuno ang progress bar. Pinipilit kang gumalaw bago bumalik sa kama.",
            Language.THAI to "เขย่าโทรศัพท์ของคุณอย่างแรงจนกว่าแถบความคืบหน้าจะเต็ม บังคับให้คุณเคลื่อนไหวร่างกายก่อนกลับไปนอน",
            Language.HINDI to "प्रगति बार भरने तक अपने फोन को जोर से हिलाएं। बिस्तर पर वापस जाने से पहले आपको शारीरिक रूप से स्थानांतरित करने के लिए मजबूर करता है।",
            Language.CANTONESE to "用力搖晃手機，直到進度條充滿。迫使你在回床睡覺前動起來。"
        ),
        "Step outside or to a window and snap a photo of the sky. Daylight + getting out of bed makes going back nearly impossible." to mapOf(
            Language.CHINESE_SIMPLIFIED to "走到室外或窗前拍一张天空的照片。日光和起床让你几乎不可能再睡回去。",
            Language.CHINESE_TRADITIONAL to "走到室外或窗前拍一張天空的照片。日光和起床讓你幾乎不可能再睡回去。",
            Language.CHINESE_PINYIN to "Zǒu dào shì wài huò chuāng qián pāi...",
            Language.ARABIC to "اخرج أو اذهب إلى النافذة والتقط صورة للسماء. ضوء النهار + النهوض من السرير يجعل العودة إلى النوم مستحيلة تقريبًا.",
            Language.TAGALOG to "Lumabas o pumunta sa bintana at kumuha ng larawan ng langit. Ang liwanag ng araw + pagbangon ay gumagawa ng pagtulog muli na halos imposible.",
            Language.THAI to "ก้าวออกไปข้างนอกหรือไปที่หน้าต่างแล้วถ่ายภาพท้องฟ้า แสงแดด + การลุกจากเตียงทำให้แทบจะเป็นไปไม่ได้เลยที่จะกลับไปนอน",
            Language.HINDI to "बाहर या खिड़की के पास कदम रखें और आकाश की एक तस्वीर खींचें। दिन का उजाला + बिस्तर से बाहर निकलना वापस सोने को लगभग असंभव बना देता है।",
            Language.CANTONESE to "走到室外或窗前拍一張天空的照片。日光和起床讓你幾乎不可能再睡回去。"
        ),
        "Make your bed, then take a photo as proof. Starts your day with a small win you can't undo." to mapOf(
            Language.CHINESE_SIMPLIFIED to "整理你的床铺，然后拍照作为证明。用一个不可撤销的小胜利开始你的一天。",
            Language.CHINESE_TRADITIONAL to "整理你的床鋪，然後拍照作為證明。用一個不可撤銷的小勝利開始你的一天。",
            Language.CHINESE_PINYIN to "Zhěng lǐ nǐ de chuáng pù...",
            Language.ARABIC to "رتب سريرك، ثم التقط صورة كدليل. ابدأ يومك بإنجاز صغير لا يمكنك التراجع عنه.",
            Language.TAGALOG to "Iayos ang iyong kama, pagkatapos ay kumuha ng larawan bilang patunay. Sinisimulan ang iyong araw sa isang maliit na panalo na hindi mo pwedeng bawiin.",
            Language.THAI to "จัดเตียงของคุณ จากนั้นถ่ายรูปเป็นหลักฐาน เริ่มต้นวันใหม่ของคุณด้วยชัยชนะเล็กๆ ที่คุณยกเลิกไม่ได้",
            Language.HINDI to "अपना बिस्तर सही करें, फिर सबूत के तौर पर एक फोटो लें। अपने दिन की शुरुआत एक छोटी सी जीत के साथ करें जिसे आप बदल नहीं सकते।",
            Language.CANTONESE to "整理你的床鋪，然後拍照作為證明。用一個不可撤銷的小勝利開始你的一天。"
        ),
        "Find a specific object somewhere else in your home (e.g. the kettle) and take a photo of it — gets you up and walking." to mapOf(
            Language.CHINESE_SIMPLIFIED to "在家里其他地方找到一件特定的物品（例如水壶）并拍照 — 让你起床并走动起来。",
            Language.CHINESE_TRADITIONAL to "在家里其他地方找到一件特定的物品（例如水壺）並拍照 — 讓你起床並走動起來。",
            Language.CHINESE_PINYIN to "Zài jiā lǐ qí tā dì fang zhǎo dào...",
            Language.ARABIC to "ابحث عن غرض معين في مكان آخر بالمنزل (مثل الغلاية) والتقط صورة له - يجعلك تنهض وتمشي.",
            Language.TAGALOG to "Maghanap ng isang partikular na bagay sa ibang bahagi ng iyong bahay (hal. ang kettle) at kumuha ng larawan nito — ginagawa kang bumangon at lumakad.",
            Language.THAI to "ค้นหาสิ่งของเฉพาะเจาะจงที่อื่นในบ้านของคุณ (เช่น กาต้มน้ำ) แล้วถ่ายรูปมัน — ช่วยให้คุณลุกขึ้นและเดิน",
            Language.HINDI to "अपने घर में कहीं और कोई विशिष्ट वस्तु (जैसे केतली) ढूंढें और उसकी एक तस्वीर लें - यह आपको जगाता है और चलने पर मजबूर करता है।",
            Language.CANTONESE to "在家里其他地方找到一件特定的物品（例如水壺）並拍照 — 讓你起床並走動起來。"
        ),
        "Read the morning quote, then tap to confirm. A gentle wake-up that nudges you into a thoughtful start." to mapOf(
            Language.CHINESE_SIMPLIFIED to "阅读早晨的名言，然后点击确认。一个温和的唤醒，开启有思考的一天。",
            Language.CHINESE_TRADITIONAL to "閱讀早晨的名言，然後點擊確認。一個溫和的喚醒，開啟有思考的一天。",
            Language.CHINESE_PINYIN to "Yuè dú zǎo shang de míng yán...",
            Language.ARABIC to "اقرأ مقولة الصباح، ثم اضغط للتأكيد. طريقة استيقاظ لطيفة تدفعك لبداية يوم مليئة بالتفكير.",
            Language.TAGALOG to "Basahin ang quote sa umaga, pagkatapos ay i-tap para kumpirmahin. Isang banayad na paggising na nagtutulak sa iyo sa isang maalalahaning simula.",
            Language.THAI to "อ่านคำคมยามเช้าแล้วแตะเพื่อยืนยัน การปลุกอย่างนุ่มนวลเพื่อช่วยให้คุณเริ่มต้นวันใหม่ด้วยความคิดสร้างสรรค์",
            Language.HINDI to "सुबह का विचार पढ़ें, फिर पुष्टि करने के लिए टैप करें। एक सौम्य जागरण जो आपको एक विचारशील शुरुआत की ओर ले जाता है।",
            Language.CANTONESE to "閱讀早晨的名言，然後點擊確認。一個溫和的喚醒，開啟有思考的一天。"
        ),
        "Type the morning affirmation word-for-word. Slow, deliberate typing forces focus and shakes off grogginess." to mapOf(
            Language.CHINESE_SIMPLIFIED to "逐字输入清晨宣誓。缓慢、深思熟虑的打字迫使你集中注意力并消除困意。",
            Language.CHINESE_TRADITIONAL to "逐字輸入清晨宣誓。緩慢、深思熟慮的打字迫使你集中注意力並消除困意。",
            Language.CHINESE_PINYIN to "Zhú zì shū rù qīng chén xuān shì...",
            Language.ARABIC to "اكتب التوكيد الصباحي حرفًا بحرف. الكتابة البطيئة والمتعمدة تجبرك على التركيز وتطرد النعاس.",
            Language.TAGALOG to "I-type ang affirmation sa umaga nang sunod-sunod. Ang mabagal at sinadyang pag-type ay nagpipilit ng pokus at nag-aalis ng antok.",
            Language.THAI to "พิมพ์คำยืนยันยามเช้าคำต่อคำ การพิมพ์ที่ช้าและตั้งใจช่วยบังคับให้มีสมาธิและขจัดความงัวเงีย",
            Language.HINDI to "सुबह के प्रतिज्ञा को शब्द-दर-शब्द टाइप करें। धीमी, जानबूझकर की जाने वाली टाइपिंग ध्यान केंद्रित करने और उनींदापन को दूर करने के लिए मजबूर करती है।",
            Language.CANTONESE to "逐字輸入清晨宣誓。緩慢、深思熟慮的打字迫使你集中注意力並消除困意。"
        ),
        "Do the configured number of push-ups — the phone counts each rep with its motion sensor. Full-body wake-up in under a minute." to mapOf(
            Language.CHINESE_SIMPLIFIED to "做设定次数的俯卧撑 — 手机将通过运动传感器计算次数。不到一分钟的全身唤醒。",
            Language.CHINESE_TRADITIONAL to "做設定次數的俯臥撐 — 手機將通過運動傳感器計算次數。不到一分鐘的全身喚醒。",
            Language.CHINESE_PINYIN to "Zuò shè dìng cì shù de fǔ wò chēng...",
            Language.ARABIC to "قم بعمل العدد المحدد من تمارين الضغط - يقوم الهاتف بعد كل تكرار باستخدام مستشعر الحركة. إيقاظ لكامل الجسم في أقل من دقيقة.",
            Language.TAGALOG to "Gawin ang na-configure na bilang ng mga push-up — binibilang ng telepono ang bawat rep gamit ang motion sensor nito. Paggising ng buong katawan sa wala pang isang minuto.",
            Language.THAI to "วิดพื้นตามจำนวนที่ตั้งค่าไว้ — โทรศัพท์จะนับแต่ละครั้งด้วยเซ็นเซอร์ตรวจจับความเคลื่อนไหว ปลุกร่างกายทุกส่วนในเวลาไม่ถึงนาที",
            Language.HINDI to "निर्धारित संख्या में पुश-अप्स करें - फोन अपने मोशन सेंसर से प्रत्येक प्रतिनिधि को गिनता है। एक मिनट से भी कम समय में पूरे शरीर का जागरण।",
            Language.CANTONESE to "做設定次數的俯臥撐 — 手機將通過運動傳感器計算次數。不到一分鐘的全身喚醒。"
        ),
        "Watch a short pattern of squares light up, then tap them back in the same order. Brain wake-up that demands attention." to mapOf(
            Language.CHINESE_SIMPLIFIED to "观察方块亮起的简短图案，然后按相同顺序点击它们。唤醒大脑的注意力训练。",
            Language.CHINESE_TRADITIONAL to "觀察方塊亮起的簡短圖案，然後按相同順序點擊它們。喚醒大腦的注意力訓練。",
            Language.CHINESE_PINYIN to "Guān chá fāng kuài liàng qǐ de...",
            Language.ARABIC to "شاهد نمطًا قصيرًا من المربعات وهي تضيء، ثم اضغط عليها بنفس الترتيب. إيقاظ للدماغ يتطلب التركيز.",
            Language.TAGALOG to "Panoorin ang isang maikling pattern ng mga square na umiilaw, pagkatapos ay i-tap ang mga ito sa parehong pagkakasunud-sunod. Paggising ng utak na nangangailangan ng pansin.",
            Language.THAI to "ดูรูปแบบช่องสี่เหลี่ยมสั้นๆ ที่สว่างขึ้น จากนั้นแตะกลับในลำดับเดียวกัน การปลุกสมองที่ต้องการสมาธิ",
            Language.HINDI to "चौकोर बक्सों के चमकने के एक छोटे पैटर्न को देखें, फिर उन्हें उसी क्रम में वापस टैप करें। दिमागी जागरण जिसके लिए ध्यान देने की आवश्यकता होती है।",
            Language.CANTONESE to "觀察方塊亮起的簡短圖案，然後按相同順序點擊它們。喚醒大腦的注意力訓練。"
        ),
        "Type the displayed sentence exactly, no typos allowed. Sharp focus required — sleepy fingers will retry until it's right." to mapOf(
            Language.CHINESE_SIMPLIFIED to "精确输入显示的句子，不允许错别字。需要高度集中注意力 — 昏睡的手指需要重试直到输入正确。",
            Language.CHINESE_TRADITIONAL to "精確輸入顯示的句子，不允許錯別字。需要高度集中注意力 — 昏睡的手指需要重試直到輸入正確。",
            Language.CHINESE_PINYIN to "Jīng què shū rù xiǎn shì de jù zi...",
            Language.ARABIC to "اكتب الجملة المعروضة تمامًا، لا يُسمح بأي أخطاء. مطلوب تركيز شديد - الأصابع الناعسة ستعيد المحاولة حتى تصبح صحيحة.",
            Language.TAGALOG to "I-type nang eksakto ang ipinapakitang pangungusap, bawal ang typo. Kailangan ng matalas na pokus — ang mga inaantok na daliri ay mag-uulit hanggang sa maging tama.",
            Language.THAI to "พิมพ์ประโยคที่แสดงให้ถูกต้อง ห้ามพิมพ์ผิด ต้องใช้สมาธิอย่างมาก — นิ้วมือที่งัวเงียจะต้องลองใหม่จนกว่าจะถูกต้อง",
            Language.HINDI to "प्रदर्शित वाक्य को बिल्कुल सही टाइप करें, कोई टाइपो नहीं। तीव्र ध्यान केंद्रित करने की आवश्यकता - नींद में डूबी उंगलियां तब तक पुनः प्रयास करेंगी जब तक वह सही न हो जाए।",
            Language.CANTONESE to "精確輸入顯示的句子，不允許錯別字。需要高度集中注意力 — 昏睡的手指需要重試直到輸入正確。"
        ),
        "Scan a pre-registered QR code located in another room (e.g. the bathroom) to dismiss. Absolutely forces you to get out of bed." to mapOf(
            Language.CHINESE_SIMPLIFIED to "扫描位于另一个房间（如浴室）的预注册二维码以关闭。绝对迫使你起床。",
            Language.CHINESE_TRADITIONAL to "掃描位於另一個房間（如浴室）的預註冊二維碼以關閉。絕對迫使你起床。",
            Language.CHINESE_PINYIN to "Sǎo miáo wèi yú lìng yī gè fáng jiān...",
            Language.ARABIC to "امسح رمز QR مسجل مسبقًا موجود في غرفة أخرى (مثل الحمام) لإيقاف المنبه. يجبرك تمامًا على النهوض من السرير.",
            Language.TAGALOG to "Mag-scan ng rehistradong QR code sa kabilang kwarto (hal. banyo) para patayin. Talagang pinipilit kang bumangon sa kama.",
            Language.THAI to "สแกนรหัส QR ที่ลงทะเบียนไว้ล่วงหน้าซึ่งอยู่ในห้องอื่น (เช่น ห้องน้ำ) เพื่อปิด บังคับให้คุณลุกจากเตียงอย่างแน่นอน",
            Language.HINDI to "बंद करने के लिए दूसरे कमरे (जैसे बाथरूम) में स्थित पहले से पंजीकृत क्यूआर कोड को स्कैन करें। आपको बिस्तर से उठने के लिए मजबूर करता है।",
            Language.CANTONESE to "掃描位於另一個房間（如浴室）的預註冊二維碼以關閉。絕對迫使你起床。"
        ),
        "Scan a pre-registered barcode (e.g. on your toothpaste or coffee jar) to silence the alarm. Ensures you start your morning routine." to mapOf(
            Language.CHINESE_SIMPLIFIED to "扫描预注册的条形码（例如在牙膏或咖啡罐上）以静音闹钟。确保你开始你的清晨例行公事。",
            Language.CHINESE_TRADITIONAL to "掃描預註冊的條形碼（例如在牙膏或咖啡罐上）以靜音鬧鐘。確保你開始你的清晨例行公事。",
            Language.CHINESE_PINYIN to "Sǎo miáo yù zhù cè de tiáo xíng mǎ...",
            Language.ARABIC to "امسح رمزًا بارزًا مسجلاً مسبقًا (مثل الذي على معجون الأسنان أو علبة القهوة) لإسكات المنبه. يضمن بدء روتينك الصباحي.",
            Language.TAGALOG to "Mag-scan ng rehistradong barcode (hal. sa toothpaste o garapon ng kape) upang patahimikin ang alarma. Tinitiyak na simulan mo ang iyong gawain sa umaga.",
            Language.THAI to "สแกนบาร์โค้ดที่ลงทะเบียนไว้ล่วงหน้า (เช่น บนยาสีฟันหรือโถกาแฟ) เพื่อปิดเสียงนาฬิกาปลุก ช่วยให้คุณเริ่มต้นกิจวัตรยามเช้าได้ทันที",
            Language.HINDI to "अलार्म को शांत करने के लिए पहले से पंजीकृत बारकोड (जैसे कि आपके टूथपेस्ट या कॉफी जार पर) को स्कैन करें। सुनिश्चित करता है कि आप अपनी सुबह की दिनचर्या शुरू करें।",
            Language.CANTONESE to "掃描預註冊的條形碼（例如在牙膏或咖啡罐上）以靜音鬧鐘。確保你開始你的清晨例行公事。"
        ),
        "Walk the configured number of steps to stop the alarm. Simple, foolproof way to get your blood flowing." to mapOf(
            Language.CHINESE_SIMPLIFIED to "行走设定的步数来停止闹钟。简单、极其有效的方法来促进血液循环。",
            Language.CHINESE_TRADITIONAL to "行走設定的步數來停止鬧鐘。簡單、極其有效的方法來促進血液循環。",
            Language.CHINESE_PINYIN to "Xíng zǒu shè dìng de bù shù...",
            Language.ARABIC to "امشِ عدد الخطوات المحدد لإيقاف المنبه. طريقة بسيطة ومضمونة لتنشيط دورتك الدموية.",
            Language.TAGALOG to "Maglakad ng na-configure na bilang ng mga hakbang upang ihinto ang alarma. Simple at siguradong paraan para gumana ang iyong sirkulasyon ng dugo.",
            Language.THAI to "เดินตามจำนวนก้าวที่ตั้งค่าไว้เพื่อปิดการปลุก วิธีที่ง่ายและได้ผลแน่นอนในการช่วยให้เลือดสูบฉีด",
            Language.HINDI to "अलार्म बंद करने के लिए कदमों की निर्धारित संख्या तक चलें। रक्त प्रवाह को सक्रिय करने का सरल, अचूक तरीका।",
            Language.CANTONESE to "行走設定的步數來停止鬧鐘。簡單、極其有效的方法來促進血液循環。"
        ),

        // Walk steps count key
        "Walk {steps} steps" to mapOf(
            Language.CHINESE_SIMPLIFIED to "行走 {steps} 步", Language.CHINESE_TRADITIONAL to "行走 {steps} 步", Language.CHINESE_PINYIN to "Xíng zǒu {steps} bù",
            Language.ARABIC to "امشِ {steps} خطوات", Language.TAGALOG to "Maglakad ng {steps} hakbang", Language.THAI to "เดิน {steps} ก้าว", Language.HINDI to "{steps} कदम चलें", Language.CANTONESE to "行走 {steps} 步"
        ),
        "Walk steps to dismiss" to mapOf(
            Language.CHINESE_SIMPLIFIED to "走动以关闭闹钟", Language.CHINESE_TRADITIONAL to "走動以關閉鬧鐘", Language.CHINESE_PINYIN to "Zǒu dòng yǐ jiě chú nào zhōng",
            Language.ARABIC to "امشِ لإيقاف المنبه", Language.TAGALOG to "Maglakad para patayin", Language.THAI to "เดินเพื่อปิดการปลุก", Language.HINDI to "बंद करने के लिए कदम चलें", Language.CANTONESE to "走動以關閉鬧鐘"
        ),
        "Watch the pattern" to mapOf(
            Language.CHINESE_SIMPLIFIED to "观察图案", Language.CHINESE_TRADITIONAL to "觀察圖案", Language.CHINESE_PINYIN to "Guān chá tú àn",
            Language.ARABIC to "راقب النمط", Language.TAGALOG to "Panoorin ang pattern", Language.THAI to "ดูรูปแบบ", Language.HINDI to "पैटर्न देखें", Language.CANTONESE to "觀察圖案"
        ),
        "Tap the squares in order!" to mapOf(
            Language.CHINESE_SIMPLIFIED to "按顺序点击方块！", Language.CHINESE_TRADITIONAL to "按順序點擊方塊！", Language.CHINESE_PINYIN to "Àn shùn xù diǎn jī fāng kuài!",
            Language.ARABIC to "اضغط على المربعات بالترتيب!", Language.TAGALOG to "I-tap ang mga square sa tamang pagkakasunud-sunod!", Language.THAI to "แตะช่องสี่เหลี่ยมตามลำดับ!", Language.HINDI to "क्रम में चौकोर पर टैप करें!", Language.CANTONESE to "按順序點擊方塊！"
        ),
        "shakes" to mapOf(
            Language.CHINESE_SIMPLIFIED to "次摇晃", Language.CHINESE_TRADITIONAL to "次搖晃", Language.CHINESE_PINYIN to "cì yáo huàng",
            Language.ARABIC to "هزات", Language.TAGALOG to "shakes", Language.THAI to "ครั้ง", Language.HINDI to "बार हिलाया", Language.CANTONESE to "次搖晃"
        ),
        "Shake your phone vigorously!" to mapOf(
            Language.CHINESE_SIMPLIFIED to "用力摇晃手机！", Language.CHINESE_TRADITIONAL to "用力搖晃手機！", Language.CHINESE_PINYIN to "Yòng lì yáo huàng shǒu jī!",
            Language.ARABIC to "هز هاتفك بقوة!", Language.TAGALOG to "I-shake nang malakas ang iyong telepono!", Language.THAI to "เขย่าโทรศัพท์ของคุณอย่างแรง!", Language.HINDI to "अपने फोन को जोर से हिलाएं!", Language.CANTONESE to "用力搖晃手機！"
        ),
        "Type the sentence exactly — no typos allowed" to mapOf(
            Language.CHINESE_SIMPLIFIED to "精确输入句子 — 不允许拼写错误", Language.CHINESE_TRADITIONAL to "精確輸入句子 — 不允許拼寫錯誤", Language.CHINESE_PINYIN to "Jīng què shū rù jù zi — bù yǔn xǔ pīn xiě cuò wù",
            Language.ARABIC to "اكتب الجملة تمامًا - لا يُسمح بأي أخطاء", Language.TAGALOG to "I-type nang eksakto ang pangungusap — bawal ang typo", Language.THAI to "พิมพ์ประโยคให้ถูกต้อง — ห้ามพิมพ์ผิด", Language.HINDI to "वाक्य को बिल्कुल वैसे ही टाइप करें - कोई टाइपो नहीं होना चाहिए", Language.CANTONESE to "精確輸入句子 — 不允許拼寫錯誤"
        ),
        "Place phone face-down on the floor and do push-ups" to mapOf(
            Language.CHINESE_SIMPLIFIED to "将手机正面朝下放在地板上做俯卧撑", Language.CHINESE_TRADITIONAL to "將手機正面朝下放在地板上做俯臥撐", Language.CHINESE_PINYIN to "Jiāng shǒu jī zhèng miàn cháo xià fàng zài dì bǎn shàng zuò fǔ wò chēng",
            Language.ARABIC to "ضع الهاتف وجهه لأسفل على الأرض وقم بتمارين الضغط", Language.TAGALOG to "Ilagay ang telepono nang nakaharap sa sahig at mag-push-up", Language.THAI to "วางโทรศัพท์คว่ำหน้าลงบนพื้นแล้ววิดพื้น", Language.HINDI to "फोन को फर्श पर उल्टा रखें और पुश-अप्स करें", Language.CANTONESE to "將手機正面朝下放在地板上做俯臥撐"
        ),
        "push-ups done" to mapOf(
            Language.CHINESE_SIMPLIFIED to "个俯卧撑已完成", Language.CHINESE_TRADITIONAL to "個俯臥撐已完成", Language.CHINESE_PINYIN to "gè fǔ wò chēng huán chéng",
            Language.ARABIC to "تمارين ضغط مكتملة", Language.TAGALOG to "push-ups done", Language.THAI to "ครั้งสำเร็จ", Language.HINDI to "पुश-अप्स पूरे किए", Language.CANTONESE to "個俯臥撐已完成"
        ),
        "Read for {seconds}s..." to mapOf(
            Language.CHINESE_SIMPLIFIED to "阅读 {seconds} 秒...", Language.CHINESE_TRADITIONAL to "閱讀 {seconds} 秒...", Language.CHINESE_PINYIN to "Yuè dú {seconds} miǎo...",
            Language.ARABIC to "اقرأ لمدة {seconds} ثوان...", Language.TAGALOG to "Basahin ng {seconds}s...", Language.THAI to "อ่านอีก {seconds} วินาที...", Language.HINDI to "{seconds} सेकंड तक पढ़ें...", Language.CANTONESE to "閱讀 {seconds} 秒..."
        ),
        "I've read this" to mapOf(
            Language.CHINESE_SIMPLIFIED to "我已阅读完毕", Language.CHINESE_TRADITIONAL to "我已閱讀完畢", Language.CHINESE_PINYIN to "Wǒ shǐ yuè dú wán bì",
            Language.ARABIC to "لقد قرأت هذا", Language.TAGALOG to "Nabasa ko na ito", Language.THAI to "ฉันอ่านข้อความนี้แล้ว", Language.HINDI to "मैंने इसे पढ़ लिया है", Language.CANTONESE to "我已閱讀完畢"
        ),
        "Type the affirmation exactly as shown above" to mapOf(
            Language.CHINESE_SIMPLIFIED to "精确输入上面显示的清晨宣誓", Language.CHINESE_TRADITIONAL to "精確輸入上面顯示的清晨宣誓", Language.CHINESE_PINYIN to "Jīng què shū rù shàng miàn xiǎn shì de xuān shì",
            Language.ARABIC to "اكتب التوكيد تمامًا كما هو موضح أعلاه", Language.TAGALOG to "I-type ang affirmation nang eksakto sa ipinapakita sa itaas", Language.THAI to "พิมพ์คำยืนยันให้ตรงตามที่แสดงด้านบน", Language.HINDI to "प्रतिज्ञा को बिल्कुल वैसे ही टाइप करें जैसा ऊपर दिखाया गया है", Language.CANTONESE to "精確輸入上面顯示的清晨宣誓"
        ),
        "Scan target QR Code to dismiss" to mapOf(
            Language.CHINESE_SIMPLIFIED to "扫描目标二维码以关闭闹钟", Language.CHINESE_TRADITIONAL to "掃描目標二維碼以關閉鬧鐘", Language.CHINESE_PINYIN to "Sǎo miáo mù biāo èr wéi mǎ yǐ jiě chú",
            Language.ARABIC to "امسح رمز QR المستهدف لإيقاف المنبه", Language.TAGALOG to "I-scan ang target na QR Code upang patayin", Language.THAI to "สแกนรหัส QR ที่กำหนดเพื่อปิดการปลุก", Language.HINDI to "बंद करने के लिए लक्ष्य क्यूआर कोड स्कैन करें", Language.CANTONESE to "掃描目標二維碼以關閉鬧鐘"
        ),
        "Scan target Barcode to dismiss" to mapOf(
            Language.CHINESE_SIMPLIFIED to "扫描目标条形码以关闭闹钟", Language.CHINESE_TRADITIONAL to "掃描目標條形碼以關閉鬧鐘", Language.CHINESE_PINYIN to "Sǎo miáo mù biāo tiáo xíng mǎ yǐ jiě chú",
            Language.ARABIC to "امسح الرمز البارز المستهدف لإيقاف المنبه", Language.TAGALOG to "I-scan ang target na Barcode upang patayin", Language.THAI to "สแกนบาร์โค้ดที่กำหนดเพื่อปิดการปลุก", Language.HINDI to "बंद करने के लिए लक्ष्य बारकोड स्कैन करें", Language.CANTONESE to "掃描目標條形碼以關閉鬧鐘"
        ),
        "Take a photo of the sky to dismiss" to mapOf(
            Language.CHINESE_SIMPLIFIED to "拍摄天空的照片以关闭闹钟", Language.CHINESE_TRADITIONAL to "拍攝天空的照片以關閉鬧鐘", Language.CHINESE_PINYIN to "Pāi zhào tiān kōng yǐ jiě chú nào zhōng",
            Language.ARABIC to "التقط صورة للسماء لإيقاف المنبه", Language.TAGALOG to "Kumuha ng larawan ng langit upang patayin", Language.THAI to "ถ่ายภาพท้องฟ้าเพื่อปิดการปลุก", Language.HINDI to "बंद करने के लिए आकाश की एक तस्वीर लें", Language.CANTONESE to "拍攝天空的照片以關閉鬧鐘"
        ),
        "Make your bed, then take a photo as proof" to mapOf(
            Language.CHINESE_SIMPLIFIED to "整理你的床铺，然后拍照作为证明以关闭闹钟", Language.CHINESE_TRADITIONAL to "整理你的床鋪，然後拍照作為證明以關閉鬧鐘", Language.CHINESE_PINYIN to "Zhěng lǐ nǐ de chuáng pù, rán hòu pāi zhào...",
            Language.ARABIC to "رتب سريرك، ثم التقط صورة كدليل لإيقاف المنبه", Language.TAGALOG to "Iayos ang iyong kama, pagkatapos ay kumuha ng larawan bilang patunay para patayin ang alarma", Language.THAI to "จัดเตียงของคุณ จากนั้นถ่ายรูปเป็นหลักฐานเพื่อปิดการปลุก", Language.HINDI to "अपना बिस्तर सही करें, फिर बंद करने के लिए सबूत के तौर पर एक फोटो लें", Language.CANTONESE to "整理你的床鋪，然後拍照作為證明以關閉鬧鐘"
        ),
        "Find your {target} and take a photo of it" to mapOf(
            Language.CHINESE_SIMPLIFIED to "找到您的 {target} 并拍照以关闭闹钟", Language.CHINESE_TRADITIONAL to "找到您的 {target} 並拍照以關閉鬧鐘", Language.CHINESE_PINYIN to "Zhǎo dào nín de {target} bìng pāi zhào...",
            Language.ARABIC to "ابحث عن {target} الخاص بك والتقط صورة له لإيقاف المنبه", Language.TAGALOG to "Hanapin ang iyong {target} at kumuha ng larawan nito upang patayin ang alarma", Language.THAI to "ค้นหา {target} ของคุณแล้วถ่ายรูปมันเพื่อปิดการปลุก", Language.HINDI to "अपना {target} ढूंढें और अलार्म बंद करने के लिए इसकी एक तस्वीर लें", Language.CANTONESE to "找到您的 {target} 並拍照以關閉鬧鐘"
        ),
        "Capture Photo" to mapOf(
            Language.CHINESE_SIMPLIFIED to "拍照", Language.CHINESE_TRADITIONAL to "拍照", Language.CHINESE_PINYIN to "Pāi zhào",
            Language.ARABIC to "التقاط الصورة", Language.TAGALOG to "Kumuha ng Larawan", Language.THAI to "ถ่ายภาพ", Language.HINDI to "तस्वीर लें", Language.CANTONESE to "拍照"
        ),
        "Camera initialization error" to mapOf(
            Language.CHINESE_SIMPLIFIED to "相机初始化错误", Language.CHINESE_TRADITIONAL to "相機初始化錯誤", Language.CHINESE_PINYIN to "Xiāng jī chū shǐ huà cuò wù",
            Language.ARABIC to "خطأ في تشغيل الكاميرا", Language.TAGALOG to "Error sa pag-initialize ng camera", Language.THAI to "เกิดข้อผิดพลาดในการเริ่มต้นใช้งานกล้อง", Language.HINDI to "कैमरा इनिशियलाइज़ेशन त्रुटि", Language.CANTONESE to "相機初始化錯誤"
        ),
        "Skip Mission (Camera Error)" to mapOf(
            Language.CHINESE_SIMPLIFIED to "跳过任务 (相机故障)", Language.CHINESE_TRADITIONAL to "跳過任務 (相機故障)", Language.CHINESE_PINYIN to "Tiào guò rèn wu (Xiāng jī gù zhàng)",
            Language.ARABIC to "تخطي المهمة (خطأ في الكاميرا)", Language.TAGALOG to "Laktawan ang Misyon (Error sa Camera)", Language.THAI to "ข้ามภารกิจ (เกิดข้อผิดพลาดของกล้อง)", Language.HINDI to "मिशन छोड़ें (कैमरा त्रुटि)", Language.CANTONESE to "跳過任務 (相機故障)"
        ),
        "SnoozeOff" to mapOf(
            Language.CHINESE_SIMPLIFIED to "SnoozeOff", Language.CHINESE_TRADITIONAL to "SnoozeOff",
            Language.CHINESE_PINYIN to "SnoozeOff", Language.ARABIC to "SnoozeOff",
            Language.TAGALOG to "SnoozeOff", Language.THAI to "SnoozeOff", Language.HINDI to "SnoozeOff", Language.CANTONESE to "SnoozeOff"
        ),

        // Objects
        "kettle" to mapOf(
            Language.CHINESE_SIMPLIFIED to "水壶", Language.CHINESE_TRADITIONAL to "水壺", Language.CHINESE_PINYIN to "shuǐ hú",
            Language.ARABIC to "غلاية", Language.TAGALOG to "kettle", Language.THAI to "กาต้มน้ำ", Language.HINDI to "केतली", Language.CANTONESE to "水壺"
        ),
        "toothbrush" to mapOf(
            Language.CHINESE_SIMPLIFIED to "牙刷", Language.CHINESE_TRADITIONAL to "牙刷", Language.CHINESE_PINYIN to "yá shuā",
            Language.ARABIC to "فرشاة أسنان", Language.TAGALOG to "toothbrush", Language.THAI to "แปรงสีฟัน", Language.HINDI to "टूथब्रश", Language.CANTONESE to "牙刷"
        ),
        "remote control" to mapOf(
            Language.CHINESE_SIMPLIFIED to "遥控器", Language.CHINESE_TRADITIONAL to "遙控器", Language.CHINESE_PINYIN to "yáo kòng qì",
            Language.ARABIC to "جهاز تحكم", Language.TAGALOG to "remote control", Language.THAI to "รีโมทคอนโทรล", Language.HINDI to "रिमोट कंट्रोल", Language.CANTONESE to "遙控器"
        ),
        "coffee mug" to mapOf(
            Language.CHINESE_SIMPLIFIED to "咖啡杯", Language.CHINESE_TRADITIONAL to "咖啡杯", Language.CHINESE_PINYIN to "kā fēi bēi",
            Language.ARABIC to "كوب قهوة", Language.TAGALOG to "coffee mug", Language.THAI to "แก้วกาแฟ", Language.HINDI to "कॉफी मग", Language.CANTONESE to "咖啡杯"
        ),
        "book" to mapOf(
            Language.CHINESE_SIMPLIFIED to "书籍", Language.CHINESE_TRADITIONAL to "書籍", Language.CHINESE_PINYIN to "shū jí",
            Language.ARABIC to "كتاب", Language.TAGALOG to "aklat", Language.THAI to "หนังสือ", Language.HINDI to "किताब", Language.CANTONESE to "書籍"
        ),
        "shoes" to mapOf(
            Language.CHINESE_SIMPLIFIED to "鞋子", Language.CHINESE_TRADITIONAL to "鞋子", Language.CHINESE_PINYIN to "xié zi",
            Language.ARABIC to "حذاء", Language.TAGALOG to "sapatos", Language.THAI to "รองเท้า", Language.HINDI to "जूते", Language.CANTONESE to "鞋子"
        ),
        "water bottle" to mapOf(
            Language.CHINESE_SIMPLIFIED to "水瓶", Language.CHINESE_TRADITIONAL to "水瓶", Language.CHINESE_PINYIN to "shuǐ píng",
            Language.ARABIC to "زجاجة ماء", Language.TAGALOG to "bote ng tubig", Language.THAI to "ขวดน้ำ", Language.HINDI to "पानी की बोतल", Language.CANTONESE to "水瓶"
        ),
        "plant" to mapOf(
            Language.CHINESE_SIMPLIFIED to "植物", Language.CHINESE_TRADITIONAL to "植物", Language.CHINESE_PINYIN to "zhí wù",
            Language.ARABIC to "نبتة", Language.TAGALOG to "halaman", Language.THAI to "ต้นไม้", Language.HINDI to "पौधा", Language.CANTONESE to "植物"
        )
    ,
        // Mission screens and camera permission rationales
        "Your answer" to mapOf(
            Language.CHINESE_SIMPLIFIED to "您的回答", Language.CHINESE_TRADITIONAL to "您的回答", Language.CHINESE_PINYIN to "Nín de huí dá",
            Language.ARABIC to "إجابتك", Language.TAGALOG to "Iyong sagot", Language.THAI to "คำตอบของคุณ", Language.HINDI to "आपका उत्तर", Language.CANTONESE to "您的回答"
        ),
        "Enter number" to mapOf(
            Language.CHINESE_SIMPLIFIED to "输入数字", Language.CHINESE_TRADITIONAL to "輸入數字", Language.CHINESE_PINYIN to "Shū rù shù zì",
            Language.ARABIC to "أدخل الرقم", Language.TAGALOG to "Ilagay ang numero", Language.THAI to "ป้อนตัวเลข", Language.HINDI to "संख्या दर्ज करें", Language.CANTONESE to "輸入數字"
        ),
        "Please enter a valid number" to mapOf(
            Language.CHINESE_SIMPLIFIED to "请输入有效的数字", Language.CHINESE_TRADITIONAL to "請輸入有效的數字", Language.CHINESE_PINYIN to "Qǐng shū rù yǒu xiào de shù zì",
            Language.ARABIC to "يرجى إدخال رقم صالح", Language.TAGALOG to "Mangyaring maglagay ng wastong numero", Language.THAI to "โปรดป้อนตัวเลขที่ถูกต้อง", Language.HINDI to "कृपया एक मानने योग्य संख्या दर्ज करें", Language.CANTONESE to "請輸入有效的數字"
        ),
        "Wrong answer, try again!" to mapOf(
            Language.CHINESE_SIMPLIFIED to "回答错误，请重试！", Language.CHINESE_TRADITIONAL to "回答錯誤，請重試！", Language.CHINESE_PINYIN to "Huí dá cuò wù, qǐng chóng shì!",
            Language.ARABIC to "إجابة خاطئة، حاول مجددًا!", Language.TAGALOG to "Maling sagot, subukan ulit!", Language.THAI to "คำตอบผิด ลองอีกครั้ง!", Language.HINDI to "गलत उत्तर, पुनः प्रयास करें!", Language.CANTONESE to "回答錯誤，請重試！"
        ),
        "Submit" to mapOf(
            Language.CHINESE_SIMPLIFIED to "提交", Language.CHINESE_TRADITIONAL to "提交", Language.CHINESE_PINYIN to "Tí jiāo",
            Language.ARABIC to "إرسال", Language.TAGALOG to "I-submit", Language.THAI to "ส่งคำตอบ", Language.HINDI to "जमा करें", Language.CANTONESE to "提交"
        ),
        "Problem {number} of {total}" to mapOf(
            Language.CHINESE_SIMPLIFIED to "第 {number} / {total} 题", Language.CHINESE_TRADITIONAL to "第 {number} / {total} 題", Language.CHINESE_PINYIN to "Dì {number} / {total} tí",
            Language.ARABIC to "المسألة {number} من {total}", Language.TAGALOG to "Problema {number} ng {total}", Language.THAI to "โจทย์ที่ {number} จาก {total}", Language.HINDI to "समस्या {number} की {total}", Language.CANTONESE to "第 {number} / {total} 題"
        ),
        "Shake vigorously to fill the progress bar" to mapOf(
            Language.CHINESE_SIMPLIFIED to "用力摇晃以充满进度条", Language.CHINESE_TRADITIONAL to "用力搖晃以充滿進度條", Language.CHINESE_PINYIN to "Yòng lì yáo huàng yǐ chōng mǎn jìn dù tiáo",
            Language.ARABIC to "هز الهاتف بقوة لملء شريط التقدم", Language.TAGALOG to "I-shake nang malakas para mapuno ang progress bar", Language.THAI to "เขย่าอย่างแรงเพื่อเติมแถบความคืบหน้า", Language.HINDI to "प्रगति बार भरने के लिए जोर से हिलाएं", Language.CANTONESE to "用力搖晃以充滿進度條"
        ),
        "Progress" to mapOf(
            Language.CHINESE_SIMPLIFIED to "进度", Language.CHINESE_TRADITIONAL to "進度", Language.CHINESE_PINYIN to "Jìn dù",
            Language.ARABIC to "التقدم", Language.TAGALOG to "Progress", Language.THAI to "ความคืบหน้า", Language.HINDI to "प्रगति", Language.CANTONESE to "進度"
        ),
        "Simulate Shake" to mapOf(
            Language.CHINESE_SIMPLIFIED to "模拟摇晃", Language.CHINESE_TRADITIONAL to "模擬搖晃", Language.CHINESE_PINYIN to "Mó nǐ yáo huàng",
            Language.ARABIC to "محاكاة الهز", Language.TAGALOG to "Simulate Shake", Language.THAI to "จำลองการเขย่า", Language.HINDI to "हिलाने का अनुकरण करें", Language.CANTONESE to "模擬搖晃"
        ),
        "Start typing here..." to mapOf(
            Language.CHINESE_SIMPLIFIED to "在此处开始输入...", Language.CHINESE_TRADITIONAL to "在此處開始輸入...", Language.CHINESE_PINYIN to "Zài cǐ chù kāi shǐ shū rù...",
            Language.ARABIC to "ابدأ الكتابة هنا...", Language.TAGALOG to "Magsimulang mag-type dito...", Language.THAI to "เริ่มพิมพ์ที่นี่...", Language.HINDI to "यहाँ टाइप करना शुरू करें...", Language.CANTONESE to "在此處開始輸入..."
        ),
        "Start typing..." to mapOf(
            Language.CHINESE_SIMPLIFIED to "开始输入...", Language.CHINESE_TRADITIONAL to "開始輸入...", Language.CHINESE_PINYIN to "Kāi shǐ shū rù...",
            Language.ARABIC to "ابدأ الكتابة...", Language.TAGALOG to "Magsimulang mag-type...", Language.THAI to "เริ่มพิมพ์...", Language.HINDI to "टाइप करना शुरू करें...", Language.CANTONESE to "開始輸入..."
        ),
        "Type the sentence exactly (case-sensitive):" to mapOf(
            Language.CHINESE_SIMPLIFIED to "精确输入句子 (区分大小写)：", Language.CHINESE_TRADITIONAL to "精確輸入句子 (區分大小寫)：", Language.CHINESE_PINYIN to "Jīng què shū rù jù zi (qū fēn dà xiǎo xiě):",
            Language.ARABIC to "اكتب الجملة تمامًا (حساس لحالة الأحرف):", Language.TAGALOG to "I-type nang eksakto ang pangungusap (case-sensitive):", Language.THAI to "พิมพ์ประโยคให้ถูกต้อง (แยกตัวพิมพ์เล็ก-ใหญ่):", Language.HINDI to "वाक्य को बिल्कुल वैसे ही टाइप करें (केस-संवेदी):", Language.CANTONESE to "精確輸入句子 (區分大小寫)："
        ),
        "Type the affirmation exactly:" to mapOf(
            Language.CHINESE_SIMPLIFIED to "精确输入清晨宣誓：", Language.CHINESE_TRADITIONAL to "精確輸入清晨宣誓：", Language.CHINESE_PINYIN to "Jīng què shū rù qīng chén xuān shì:",
            Language.ARABIC to "اكتب التوكيد تمامًا:", Language.TAGALOG to "I-type ang affirmation nang eksakto:", Language.THAI to "พิมพ์คำยืนยันให้ถูกต้อง:", Language.HINDI to "प्रतिज्ञा को बिल्कुल वैसे ही टाइप करें:", Language.CANTONESE to "精確輸入清晨宣誓："
        ),
        "Push-Ups Mission" to mapOf(
            Language.CHINESE_SIMPLIFIED to "俯卧撑任务", Language.CHINESE_TRADITIONAL to "俯臥撐任務", Language.CHINESE_PINYIN to "Fǔ wò chēng rèn wu",
            Language.ARABIC to "مهمة تمارين الضغط", Language.TAGALOG to "Misyong Push-Ups", Language.THAI to "ภารกิจวิดพื้น", Language.HINDI to "पुश-अप्स मिशन", Language.CANTONESE to "俯臥撐任務"
        ),
        "Accelerometer not detected. Tap the screen to count push-ups manually." to mapOf(
            Language.CHINESE_SIMPLIFIED to "未检测到加速度计。点击屏幕以手动计算俯卧撑。", Language.CHINESE_TRADITIONAL to "未檢測到加速度計。點擊屏幕以手動計算俯臥撐。", Language.CHINESE_PINYIN to "Wèi jiǎn cè dào jiā sù dù jì...",
            Language.ARABIC to "لم يتم اكتشاف مقياس التسارع. اضغط على الشاشة لعد تمارين الضغط يدويًا.", Language.TAGALOG to "Hindi natukoy ang accelerometer. I-tap ang screen para manu-manong magbilang ng push-ups.", Language.THAI to "ไม่พบเครื่องวัดความเร่ง แตะหน้าจอเพื่อวิดพื้นด้วยตนเอง", Language.HINDI to "एक्सेलेरोमीटर का पता नहीं चला। पुश-अप्स को मैन्युअल रूप से गिनने के लिए स्क्रीन पर टैप करें。", Language.CANTONESE to "未檢測到加速度計。點擊屏幕以手動計算俯臥撐。"
        ),
        "of {target} reps" to mapOf(
            Language.CHINESE_SIMPLIFIED to "/ {target} 次俯卧撑", Language.CHINESE_TRADITIONAL to "/ {target} 次俯臥撐", Language.CHINESE_PINYIN to "/ {target} cì fǔ wò chēng",
            Language.ARABIC to "من أصل {target} تكرارات", Language.TAGALOG to "ng {target} rep", Language.THAI to "จาก {target} ครั้ง", Language.HINDI to "{target} प्रतिनिधि का", Language.CANTONESE to "/ {target} 次俯臥撐"
        ),
        "Simulate Rep" to mapOf(
            Language.CHINESE_SIMPLIFIED to "模拟次数", Language.CHINESE_TRADITIONAL to "模擬次數", Language.CHINESE_PINYIN to "Mó nǐ cì shù",
            Language.ARABIC to "محاكاة التكرار", Language.TAGALOG to "Simulate Rep", Language.THAI to "จำลองการทำซ้ำ", Language.HINDI to "प्रतिनिधि का अनुकरण करें", Language.CANTONESE to "模擬次數"
        ),
        "Memory Game" to mapOf(
            Language.CHINESE_SIMPLIFIED to "记忆力游戏", Language.CHINESE_TRADITIONAL to "記憶力遊戲", Language.CHINESE_PINYIN to "Jì yì lì yóu xì",
            Language.ARABIC to "لعبة الذاكرة", Language.TAGALOG to "Memory Game", Language.THAI to "เกมความจำ", Language.HINDI to "मेमोरी गेम", Language.CANTONESE to "記憶力遊戲"
        ),
        "Round {number} of {total}" to mapOf(
            Language.CHINESE_SIMPLIFIED to "第 {number} / {total} 轮", Language.CHINESE_TRADITIONAL to "第 {number} / {total} 輪", Language.CHINESE_PINYIN to "Dì {number} / {total} lún",
            Language.ARABIC to "الجولة {number} من {total}", Language.TAGALOG to "Round {number} ng {total}", Language.THAI to "รอบที่ {number} จาก {total}", Language.HINDI to "दौर {number} की {total}", Language.CANTONESE to "第 {number} / {total} 輪"
        ),
        "Prepare for Round {number}..." to mapOf(
            Language.CHINESE_SIMPLIFIED to "准备第 {number} 轮...", Language.CHINESE_TRADITIONAL to "準備第 {number} 輪...", Language.CHINESE_PINYIN to "Zhǔn bèi dì {number} lún...",
            Language.ARABIC to "الاستعداد للجولة {number}...", Language.TAGALOG to "Paghahanda para sa Round {number}...", Language.THAI to "เตรียมตัวสำหรับรอบที่ {number}...", Language.HINDI to "दौर {number} के लिए तैयारी करें...", Language.CANTONESE to "準備第 {number} 輪..."
        ),
        "Watch the pattern closely!" to mapOf(
            Language.CHINESE_SIMPLIFIED to "仔细观察图案！", Language.CHINESE_TRADITIONAL to "仔細觀察圖案！", Language.CHINESE_PINYIN to "Zǐ xì guān chá tú àn!",
            Language.ARABIC to "راقب النمط عن كثب!", Language.TAGALOG to "Panoorin nang mabuti ang pattern!", Language.THAI to "สังเกตรูปแบบอย่างใกล้ชิด!", Language.HINDI to "पैटर्न को ध्यान से देखें!", Language.CANTONESE to "仔細觀察圖案！"
        ),
        "Repeat the sequence: {current} / {total}" to mapOf(
            Language.CHINESE_SIMPLIFIED to "重复序列：{current} / {total}", Language.CHINESE_TRADITIONAL to "重複序列：{current} / {total}", Language.CHINESE_PINYIN to "Chóng fù xù liè: {current} / {total}",
            Language.ARABIC to "كرر التسلسل: {current} / {total}", Language.TAGALOG to "Ulitin ang pagkakasunod-sunod: {current} / {total}", Language.THAI to "ทำซ้ำลำดับ: {current} / {total}", Language.HINDI to "अनुक्रम दोहराएं: {current} / {total}", Language.CANTONESE to "重複序列：{current} / {total}"
        ),
        "Correct! Get ready..." to mapOf(
            Language.CHINESE_SIMPLIFIED to "正确！准备...", Language.CHINESE_TRADITIONAL to "正確！準備...", Language.CHINESE_PINYIN to "Zhèng què! Zhǔn bèi...",
            Language.ARABIC to "صحيح! استعد...", Language.TAGALOG to "Tama! Humanda...", Language.THAI to "ถูกต้อง! เตรียมตัว...", Language.HINDI to "सही! तैयार हो जाओ...", Language.CANTONESE to "正確！準備..."
        ),
        "Oops! Let's try again." to mapOf(
            Language.CHINESE_SIMPLIFIED to "糟糕！再试一次。", Language.CHINESE_TRADITIONAL to "糟糕！再試一次。", Language.CHINESE_PINYIN to "Zāo gāo! Zài shì yī cì.",
            Language.ARABIC to "أوبس! لنحاول مجددًا.", Language.TAGALOG to "Oops! Subukan ulit.", Language.THAI to "อุ๊ย! ลองอีกครั้ง", Language.HINDI to "ओह! फिर कोशिश करते हैं。", Language.CANTONESE to "糟糕！再試一次。"
        ),
        "Camera unavailable" to mapOf(
            Language.CHINESE_SIMPLIFIED to "相机不可用", Language.CHINESE_TRADITIONAL to "相機不可用", Language.CHINESE_PINYIN to "Xiāng jī bù kě yòng",
            Language.ARABIC to "الكاميرا غير متوفرة", Language.TAGALOG to "Hindi magamit ang camera", Language.THAI to "กล้องไม่พร้อมใช้งาน", Language.HINDI to "कैमरा अनुपलब्ध है", Language.CANTONESE to "相機不可用"
        ),
        "Skip Mission" to mapOf(
            Language.CHINESE_SIMPLIFIED to "跳过任务", Language.CHINESE_TRADITIONAL to "跳過任務", Language.CHINESE_PINYIN to "Tiào guò rèn wu",
            Language.ARABIC to "تخطي المهمة", Language.TAGALOG to "Laktawan ang Misyon", Language.THAI to "ข้ามภารกิจ", Language.HINDI to "मिशन छोड़ें", Language.CANTONESE to "跳過任務"
        ),
        "Photo captured!" to mapOf(
            Language.CHINESE_SIMPLIFIED to "照片已捕获！", Language.CHINESE_TRADITIONAL to "照片已捕獲！", Language.CHINESE_PINYIN to "Zhào piàn yǐ bǔ huò!",
            Language.ARABIC to "تم التقاط الصورة!", Language.TAGALOG to "Larawan ay nakuha!", Language.THAI to "ถ่ายภาพแล้ว!", Language.HINDI to "फोटो ली गई!", Language.CANTONESE to "照片已捕獲！"
        ),
        "Mission complete" to mapOf(
            Language.CHINESE_SIMPLIFIED to "任务完成", Language.CHINESE_TRADITIONAL to "任務完成", Language.CHINESE_PINYIN to "Rèn wu wán chéng",
            Language.ARABIC to "اكتملت المهمة", Language.TAGALOG to "Kumpleto na ang misyon", Language.THAI to "ภารกิจเสร็จสิ้น", Language.HINDI to "मिशन पूरा हुआ", Language.CANTONESE to "任務完成"
        ),
        "Camera Access Required" to mapOf(
            Language.CHINESE_SIMPLIFIED to "需要相机权限", Language.CHINESE_TRADITIONAL to "需要相機權限", Language.CHINESE_PINYIN to "Xiāng jī quán xiàn xū yào",
            Language.ARABIC to "مطلب الوصول إلى الكاميرا", Language.TAGALOG to "Kailangan ng Access sa Camera", Language.THAI to "จำเป็นต้องเข้าถึงกล้อง", Language.HINDI to "कैमरा एक्सेस आवश्यक है", Language.CANTONESE to "需要相機權限"
        ),
        "The {mission} mission requires camera access to scan codes or capture images. Please grant permission to continue." to mapOf(
            Language.CHINESE_SIMPLIFIED to "{mission} 任务需要访问相机以扫描条码或拍照。请授予权限以继续。",
            Language.CHINESE_TRADITIONAL to "{mission} 任務需要訪問相機以掃描條碼或拍照。請授予權限以繼續。",
            Language.CHINESE_PINYIN to "{mission} rèn wu xū yào fàng wèn xiāng jī...",
            Language.ARABIC to "تتطلب مهمة {mission} الوصول إلى الكاميرا لمسح الرموز أو التقاط الصور. يرجى منح الإذن للمتابعة.",
            Language.TAGALOG to "Ang misyong {mission} ay nangangailangan ng access sa camera upang mag-scan ng mga code o kumuha ng mga larawan. Mangyaring ibigay ang pahintulot upang magpatuloy.",
            Language.THAI to "ภารกิจ {mission} จำเป็นต้องเข้าถึงกล้องเพื่อสแกนรหัสหรือถ่ายภาพ โปรดอนุญาตสิทธิ์เพื่อดำเนินการต่อ",
            Language.HINDI to "{mission} मिशन को कोड स्कैन करने या तस्वीरें लेने के लिए कैमरा एक्सेस की आवश्यकता होती है। जारी रखने के लिए कृपया अनुमति प्रदान करें।",
            Language.CANTONESE to "{mission} 任務需要訪問相機以掃描條碼或拍照。請授予權限以繼續。"
        ),
        "Grant Permission" to mapOf(
            Language.CHINESE_SIMPLIFIED to "授予权限", Language.CHINESE_TRADITIONAL to "授予權限", Language.CHINESE_PINYIN to "Shòu yǔ quán xiàn",
            Language.ARABIC to "منح الإذن", Language.TAGALOG to "Ibigay ang Pahintulot", Language.THAI to "อนุญาตสิทธิ์", Language.HINDI to "अनुमति प्रदान करें", Language.CANTONESE to "授予權限"
        ),
        "Skip Mission (Complete Anyway)" to mapOf(
            Language.CHINESE_SIMPLIFIED to "跳过任务 (直接完成)", Language.CHINESE_TRADITIONAL to "跳過任務 (直接完成)", Language.CHINESE_PINYIN to "Tiào guò rèn wu (Zhí jiē wán chéng)",
            Language.ARABIC to "تخطي المهمة (الإكمال على أي حال)", Language.TAGALOG to "Laktawan ang Misyon (Kumpletuhin pa rin)", Language.THAI to "ข้ามภารกิจ (ทำให้สำเร็จต่อไป)", Language.HINDI to "मिशन छोड़ें (वैसे भी पूरा करें)", Language.CANTONESE to "跳過任務 (直接完成)"
        ),
        "Camera Permission Needed" to mapOf(
            Language.CHINESE_SIMPLIFIED to "需要相机权限", Language.CHINESE_TRADITIONAL to "需要相機權限", Language.CHINESE_PINYIN to "Xiāng jī quán xiàn xū yào",
            Language.ARABIC to "مطلوب إذن الكاميرا", Language.TAGALOG to "Kailangan ng Pahintulot sa Camera", Language.THAI to "จำเป็นต้องขอสิทธิ์เข้าใช้งานกล้อง", Language.HINDI to "कैमरा अनुमति की आवश्यकता है", Language.CANTONESE to "需要相機權限"
        ),
        "The {mission} mission requires camera access. Grant camera permission in settings, or tap below to skip." to mapOf(
            Language.CHINESE_SIMPLIFIED to "{mission} 任务需要访问相机。请在设置中授予相机权限，或点击下方跳过。",
            Language.CHINESE_TRADITIONAL to "{mission} 任務需要訪問相機。請在設置中授予相機權限，或點擊下方跳過。",
            Language.CHINESE_PINYIN to "{mission} rèn wu xū yào fàng wèn xiāng jī...",
            Language.ARABIC to "تتطلب مهمة {mission} الوصول إلى الكاميرا. امنح إذن الكاميرا في الإعدادات، أو اضغط أدناه للتخطي.",
            Language.TAGALOG to "Ang misyong {mission} ay nangangailangan ng access sa camera. Ibigay ang pahintulot sa camera sa mga setting, o i-tap sa ibaba upang laktawan.",
            Language.THAI to "ภารกิจ {mission} จำเป็นต้องเข้าใช้งานกล้อง อนุญาตสิทธิ์กล้องในการตั้งค่า หรือแตะด้านล่างเพื่อข้าม",
            Language.HINDI to "{mission} मिशन के लिए कैमरा एक्सेस की आवश्यकता होती है। सेटिंग्स में कैमरा अनुमति प्रदान करें, या छोड़ने के लिए नीचे टैप करें।",
            Language.CANTONESE to "{mission} 任務需要訪問相機。請在設置中授予相機權限，或點擊下方跳過。"
        ),
        "Complete Anyway" to mapOf(
            Language.CHINESE_SIMPLIFIED to "直接完成", Language.CHINESE_TRADITIONAL to "直接完成", Language.CHINESE_PINYIN to "Zhí jiē wán chéng",
            Language.ARABIC to "إكمال على أي حال", Language.TAGALOG to "Kumpletuhin Pa Rin", Language.THAI to "ทำให้สำเร็จต่อไป", Language.HINDI to "वैसे भी पूरा करें", Language.CANTONESE to "直接完成"
        ),
        "Scan QR Code" to mapOf(
            Language.CHINESE_SIMPLIFIED to "扫描二维码", Language.CHINESE_TRADITIONAL to "掃描二維碼", Language.CHINESE_PINYIN to "Sǎo miáo èr wéi mǎ",
            Language.ARABIC to "مسح رمز QR", Language.TAGALOG to "I-scan ang QR Code", Language.THAI to "สแกนรหัส QR", Language.HINDI to "क्यूआर कोड स्कैन करें", Language.CANTONESE to "掃描二維碼"
        ),
        "Scan the registered QR code matching:\n\"{value}\"" to mapOf(
            Language.CHINESE_SIMPLIFIED to "扫描匹配以下内容的已注册二维码：\n\"{value}\"",
            Language.CHINESE_TRADITIONAL to "掃描匹配以下內容的已註冊二維碼：\n\"{value}\"",
            Language.CHINESE_PINYIN to "Sǎo miáo pǐ pèi...\n\"{value}\"",
            Language.ARABIC to "امسح رمز QR المسجل المطابق لـ:\n\"{value}\"",
            Language.TAGALOG to "I-scan ang nakarehistrong QR code na tumutugma sa:\n\"{value}\"",
            Language.THAI to "สแกนรหัส QR ที่ลงทะเบียนที่ตรงกับ:\n\"{value}\"",
            Language.HINDI to "मिलान करने वाले पंजीकृत क्यूआर कोड को स्कैन करें:\n\"{value}\"",
            Language.CANTONESE to "掃描匹配以下內容的已註冊二維碼：\n\"{value}\""
        ),
        "Scan any QR code to turn off the alarm" to mapOf(
            Language.CHINESE_SIMPLIFIED to "扫描任意二维码关闭闹钟", Language.CHINESE_TRADITIONAL to "掃描任意二維碼關閉鬧鐘", Language.CHINESE_PINYIN to "Sǎo miáo rèn yì èr wéi mǎ guān bì",
            Language.ARABIC to "امسح أي رمز QR لإيقاف المنبه", Language.TAGALOG to "I-scan ang anumang QR code upang patayin ang alarma", Language.THAI to "สแกนรหัส QR ใดก็ได้เพื่อปิดการปลุก", Language.HINDI to "अलार्म बंद करने के लिए किसी भी क्यूआर कोड को स्कैन करें", Language.CANTONESE to "掃描任意二維碼關閉鬧鐘"
        ),
        "QR Code matched!" to mapOf(
            Language.CHINESE_SIMPLIFIED to "二维码匹配成功！", Language.CHINESE_TRADITIONAL to "二維碼匹配成功！", Language.CHINESE_PINYIN to "Èr wéi mǎ pǐ pèi chéng gōng!",
            Language.ARABIC to "تم تطابق رمز QR!", Language.TAGALOG to "Tumugma ang QR Code!", Language.THAI to "รหัส QR ตรงกัน!", Language.HINDI to "क्यूआर कोड का मिलान हो गया!", Language.CANTONESE to "二維碼匹配成功！"
        ),
        "Scan Barcode" to mapOf(
            Language.CHINESE_SIMPLIFIED to "扫描条形码", Language.CHINESE_TRADITIONAL to "掃描條形碼", Language.CHINESE_PINYIN to "Sǎo miáo tiáo xíng mǎ",
            Language.ARABIC to "مسح الرمز البارز", Language.TAGALOG to "I-scan ang Barcode", Language.THAI to "สแกนบาร์โค้ด", Language.HINDI to "बारकोड स्कैन करें", Language.CANTONESE to "掃描條形碼"
        ),
        "Scan the registered barcode matching:\n\"{value}\"" to mapOf(
            Language.CHINESE_SIMPLIFIED to "扫描匹配以下内容的已注册条形码：\n\"{value}\"",
            Language.CHINESE_TRADITIONAL to "掃描匹配以下內容的已註冊條形碼：\n\"{value}\"",
            Language.CHINESE_PINYIN to "Sǎo miáo pǐ pèi...\n\"{value}\"",
            Language.ARABIC to "امسح الرمز البارز المسجل المطابق لـ:\n\"{value}\"",
            Language.TAGALOG to "I-scan ang nakarehistrong barcode na tumutugma sa:\n\"{value}\"",
            Language.THAI to "สแกนบาร์โค้ดที่ลงทะเบียนที่ตรงกับ:\n\"{value}\"",
            Language.HINDI to "मिलान करने वाले पंजीकृत बारकोड को स्कैन करें:\n\"{value}\"",
            Language.CANTONESE to "掃描匹配以下內容的已註冊條形碼：\n\"{value}\""
        ),
        "Scan any barcode to turn off the alarm" to mapOf(
            Language.CHINESE_SIMPLIFIED to "扫描任意条形码关闭闹钟", Language.CHINESE_TRADITIONAL to "掃描任意條形碼關閉鬧鐘", Language.CHINESE_PINYIN to "Sǎo miáo rèn yì tiáo xíng mǎ guān bì",
            Language.ARABIC to "امسح أي رمز بارز لإيقاف المنبه", Language.TAGALOG to "I-scan ang anumang barcode upang patayin ang alarma", Language.THAI to "สแกนบาร์โค้ดใดก็ได้เพื่อปิดการปลุก", Language.HINDI to "अलार्म बंद करने के लिए किसी भी बारकोड को स्कैन करें", Language.CANTONESE to "掃描任意條形碼關閉鬧鐘"
        ),
        "Barcode matched!" to mapOf(
            Language.CHINESE_SIMPLIFIED to "条形码匹配成功！", Language.CHINESE_TRADITIONAL to "條形碼匹配成功！", Language.CHINESE_PINYIN to "Tiáo xíng mǎ pǐ pèi chéng gōng!",
            Language.ARABIC to "تم تطابق الرمز البارز!", Language.TAGALOG to "Tumugma ang Barcode!", Language.THAI to "บาร์โค้ดตรงกัน!", Language.HINDI to "बारकोड का मिलान हो गया!", Language.CANTONESE to "條形碼匹配成功！"
        ),
        "Walk to dismiss" to mapOf(
            Language.CHINESE_SIMPLIFIED to "走动以关闭闹钟", Language.CHINESE_TRADITIONAL to "走動以關閉鬧鐘", Language.CHINESE_PINYIN to "Zǒu dòng yǐ jiě chú",
            Language.ARABIC to "امشِ لإيقاف المنبه", Language.TAGALOG to "Maglakad para patayin", Language.THAI to "เดินเพื่อปิดการปลุก", Language.HINDI to "बंद करने के लिए कदम चलें", Language.CANTONESE to "走動以關閉鬧鐘"
        ),
        "Steps count level is set to {level}. Walk around until the steps are complete." to mapOf(
            Language.CHINESE_SIMPLIFIED to "步数等级已设为 {level}。四处走动直到步数达标。",
            Language.CHINESE_TRADITIONAL to "步數等級已設為 {level}。四處走動直到步數達標。",
            Language.CHINESE_PINYIN to "Bù shù děng jí yǐ shè wéi {level}...",
            Language.ARABIC to "تم ضبط مستوى عد الخطوات على {level}. تجول حتى تكتمل الخطوات.",
            Language.TAGALOG to "Ang antas ng bilang ng hakbang ay nakatakda sa {level}. Maglakad-lakad hanggang sa makumpleto ang mga hakbang.",
            Language.THAI to "ระดับการนับก้าวถูกตั้งค่าเป็น {level} เดินไปรอบๆ จนกว่าจะครบตามจำนวนก้าว",
            Language.HINDI to "कदम गिनती का स्तर {level} पर सेट है। कदम पूरे होने तक इधर-उधर चलें।",
            Language.CANTONESE to "步數等級已設為 {level}。四處走動直到步數達標。"
        ),
        "Simulate Step" to mapOf(
            Language.CHINESE_SIMPLIFIED to "模拟前进一步", Language.CHINESE_TRADITIONAL to "模擬前進一步", Language.CHINESE_PINYIN to "Mó nǐ qián jìn yī bù",
            Language.ARABIC to "محاكاة خطوة", Language.TAGALOG to "Simulate Step", Language.THAI to "จำลองการก้าวเดิน", Language.HINDI to "कदम का अनुकरण करें", Language.CANTONESE to "模擬前進一步"
        ),
        "Goal Reached!" to mapOf(
            Language.CHINESE_SIMPLIFIED to "目标达成！", Language.CHINESE_TRADITIONAL to "目標達成！", Language.CHINESE_PINYIN to "Mù biāo dá chéng!",
            Language.ARABIC to "تم الوصول للهدف!", Language.TAGALOG to "Naabot ang Layunin!", Language.THAI to "บรรลุเป้าหมายแล้ว!", Language.HINDI to "लक्ष्य प्राप्त हुआ!", Language.CANTONESE to "目標達成！"
        ),
        "Simulate Match Scan" to mapOf(
            Language.CHINESE_SIMPLIFIED to "模拟扫码成功", Language.CHINESE_TRADITIONAL to "模擬掃碼成功", Language.CHINESE_PINYIN to "Mó nǐ sǎo mǎ chéng gōng",
            Language.ARABIC to "محاكاة مسح ناجح", Language.TAGALOG to "Simulate Match Scan", Language.THAI to "จำลองการสแกนตรงกัน", Language.HINDI to "मैच स्कैन का अनुकरण करें", Language.CANTONESE to "模擬掃碼成功"
        )
,
        // Quotes
        "The secret of getting ahead is getting started." to mapOf(
            Language.CHINESE_SIMPLIFIED to "取得成功的秘诀在于开始。", Language.CHINESE_TRADITIONAL to "取得成功的秘訣在於開始。", Language.CHINESE_PINYIN to "Qǔ dé chéng gōng de mì jué zài yú kāi shǐ.",
            Language.ARABIC to "سر المضي قدمًا هو البدء.", Language.TAGALOG to "Ang sikreto ng pag-unlad ay ang pagsisimula.", Language.THAI to "ความลับของการก้าวไปข้างหน้าคือการเริ่มต้น", Language.HINDI to "आगे बढ़ने का रहस्य शुरू करना है।", Language.CANTONESE to "取得成功嘅秘訣在於開始。"
        ),
        "Believe you can and you're halfway there." to mapOf(
            Language.CHINESE_SIMPLIFIED to "相信你能做到，你就已经成功了一半。", Language.CHINESE_TRADITIONAL to "相信你能做到，你就已經成功了一半。", Language.CHINESE_PINYIN to "Xiāng xìn nǐ néng zuò dào, nǐ jiù yǐ jīng chéng gōng le yī bàn.",
            Language.ARABIC to "آمن بأنك تستطيع وستكون في منتصف الطريق.", Language.TAGALOG to "Maniwala kang kaya mo at nasa kalahati ka na.", Language.THAI to "เชื่อว่าคุณทำได้ คุณก็สำเร็จไปครึ่งทางแล้ว", Language.HINDI to "विश्वास करें कि आप कर सकते हैं और आप आधा रास्ता तय कर चुके हैं।", Language.CANTONESE to "相信你能做到，你就已經成功咗一半。"
        ),
        "The only way to do great work is to love what you do." to mapOf(
            Language.CHINESE_SIMPLIFIED to "成就伟大事业的唯一方法是热爱你的工作。", Language.CHINESE_TRADITIONAL to "成就偉大事業的唯一方法是熱愛你的工作。", Language.CHINESE_PINYIN to "Chéng jiù wěi dà shì yè de wéi yī fāng fǎ shì rè ài nǐ de gōng zuò.",
            Language.ARABIC to "السبيل الوحيد للقيام بعمل رائع هو أن تحب ما تفعل.", Language.TAGALOG to "Ang tanging paraan para makagawa ng mahusay na trabaho ay ang mahalin ang iyong ginagawa.", Language.THAI to "วิธีเดียวที่จะทำงานที่ยิ่งใหญ่ได้คือต้องรักในสิ่งที่คุณทำ", Language.HINDI to "महान कार्य करने का एकमात्र तरीका यह है कि आप जो करते हैं उससे प्यार करें।", Language.CANTONESE to "成就偉大事業嘅唯一方法係熱愛你嘅工作。"
        ),
        "The best way to predict the future is to create it." to mapOf(
            Language.CHINESE_SIMPLIFIED to "预测未来的最好方法是创造它。", Language.CHINESE_TRADITIONAL to "預測未來的最好方法是創造它。", Language.CHINESE_PINYIN to "Yù cè wèi lái de zuì hǎo fāng fǎ shì chuàng zào tā.",
            Language.ARABIC to "أفضل طريقة للتنبؤ بالمستقبل هي صناعته.", Language.TAGALOG to "Ang pinakamahusay na paraan upang mahulaan ang hinaharap ay ang likhain ito.", Language.THAI to "วิธีที่ดีที่สุดในการทำนายอนาคตคือการสร้างมันขึ้นมา", Language.HINDI to "भविष्य का अनुमान लगाने का सबसे अच्छा तरीका इसे बनाना है।", Language.CANTONESE to "預測未來嘅最好方法係創造佢。"
        ),
        "Don't count the days, make the days count." to mapOf(
            Language.CHINESE_SIMPLIFIED to "不要去数日子，要让日子变得有意义。", Language.CHINESE_TRADITIONAL to "不要去數日子，要讓日子變得有意義。", Language.CHINESE_PINYIN to "Bú yào qù shǔ rì zi, yào ràng rì zi biàn de yǒu yì yì.",
            Language.ARABIC to "لا تعد الأيام، بل اجعل الأيام ذات قيمة.", Language.TAGALOG to "Huwag mong bilangin ang mga araw, gawin mong may halaga ang mga araw.", Language.THAI to "อย่ามัวแต่นับวัน แต่จงทำทุกวันให้มีความหมาย", Language.HINDI to "दिनों को मत गिनो, दिनों को सार्थक बनाओ।", Language.CANTONESE to "唔好去數日子，要令日子變得有意義。"
        ),

        // Affirmations
        "I am grateful for this new day and its opportunities" to mapOf(
            Language.CHINESE_SIMPLIFIED to "我很感激这新的一天和其中的机遇", Language.CHINESE_TRADITIONAL to "我很感激這新的一天和其中的機遇", Language.CHINESE_PINYIN to "Wǒ hěn gǎn jī zhè xīn de yī tiān hé qí zhōng de jī yù",
            Language.ARABIC to "أنا ممتن لهذا اليوم الجديد وفرصه", Language.TAGALOG to "Nagpapasalamat ako sa bagong araw na ito at sa mga pagkakataon nito", Language.THAI to "ฉันรู้สึกขอบคุณสำหรับวันใหม่นี้และโอกาสที่ได้รับ", Language.HINDI to "मैं इस नए दिन और इसके अवसरों के लिए आभारी हूँ", Language.CANTONESE to "我很感激呢個新嘅一天同埋其中嘅機遇"
        ),
        "I choose to be happy, healthy, and positive today" to mapOf(
            Language.CHINESE_SIMPLIFIED to "我选择今天保持快乐、健康和积极", Language.CHINESE_TRADITIONAL to "我選擇今天保持快樂、健康和積極", Language.CHINESE_PINYIN to "Wǒ xuǎn zé jīn tiān bǎo chí kuài lè, jiàn kāng hé jī jí",
            Language.ARABIC to "أختار أن أكون سعيدًا، وصحيًا، وإيجابيًا اليوم", Language.TAGALOG to "Pinipili kong maging masaya, malusog, at positibo ngayon", Language.THAI to "ฉันเลือกที่จะมีความสุข มีสุขภาพดี และคิดบวกในวันนี้", Language.HINDI to "मैं आज खुश, स्वस्थ और सकारात्मक रहना चुनता हूँ", Language.CANTONESE to "我選擇今日保持快樂、健康同積極"
        ),
        "I am capable of achieving all my goals today" to mapOf(
            Language.CHINESE_SIMPLIFIED to "我有能力实现今天所有的目标", Language.CHINESE_TRADITIONAL to "我有能力實現今天所有的目標", Language.CHINESE_PINYIN to "Wǒ yǒu néng lì shí xiàn jīn tiān suǒ yǒu de mù biāo",
            Language.ARABIC to "أنا قادر على تحقيق كل أهدافي اليوم", Language.TAGALOG to "Kaya kong makamit ang lahat ng aking mga layunin ngayon", Language.THAI to "ฉันมีความสามารถในการบรรลุเป้าหมายทั้งหมดของฉันในวันนี้", Language.HINDI to "मैं आज अपने सभी लक्ष्यों को प्राप्त करने में सक्षम हूँ", Language.CANTONESE to "我有能力實現今日所有嘅目標"
        ),
        "I believe in myself and my abilities completely" to mapOf(
            Language.CHINESE_SIMPLIFIED to "我完全相信自己和我的能力", Language.CHINESE_TRADITIONAL to "我完全相信自己和我的能力", Language.CHINESE_PINYIN to "Wǒ wán quán xiāng xìn zì jǐ hé wǒ de néng lì",
            Language.ARABIC to "أنا أؤمن بنفسي وبقدراتي تمامًا", Language.TAGALOG to "Naniniwala ako sa aking sarili at sa aking mga kakayahan nang lubos", Language.THAI to "ฉันเชื่อมั่นในตัวเองและความสามารถของฉันอย่างเต็มที่", Language.HINDI to "मैं खुद पर और अपनी क्षमताओं पर पूरा भरोसा करता हूँ", Language.CANTONESE to "我完全相信自己同我嘅能力"
        ),
        "I am strong, resilient, and ready for anything" to mapOf(
            Language.CHINESE_SIMPLIFIED to "我很强大，有韧性，并做好了应对一切的准备", Language.CHINESE_TRADITIONAL to "我很強大，有韌性，並做好了應對一切的準備", Language.CHINESE_PINYIN to "Wǒ hěn qiáng dà, yǒu rèn xìng, bìng zuò hǎo le yìng duì yī qiè de zhǔn bèi",
            Language.ARABIC to "أنا قوي، ومرن، ومستعد لأي شيء", Language.TAGALOG to "Ako ay malakas, matatag, at handa sa anumang bagay", Language.THAI to "ฉันแข็งแกร่ง ยืดหยุ่น และพร้อมสำหรับทุกสิ่ง", Language.HINDI to "मैं मजबूत, लचीला और किसी भी चीज़ के लिए तैयार हूँ", Language.CANTONESE to "我很強大，有韌性，並做好咗應對一切嘅準備"
        ),

        // Sentences
        "The early bird catches the worm" to mapOf(
            Language.CHINESE_SIMPLIFIED to "捷足先登者得天独厚", Language.CHINESE_TRADITIONAL to "捷足先登者得天獨厚", Language.CHINESE_PINYIN to "Jié zú xiān dēng zhě dé tiān dú hòu",
            Language.ARABIC to "العصفور الباكر يصطاد الدود", Language.TAGALOG to "Ang maagang ibon ay nakakahuli ng uod", Language.THAI to "นกที่ตื่นเช้าจะได้กินหนอน", Language.HINDI to "जल्दी जागने वाले को सफलता मिलती है", Language.CANTONESE to "捷足先登者得天獨厚"
        ),
        "A journey of a thousand miles begins with a single step" to mapOf(
            Language.CHINESE_SIMPLIFIED to "千里之行始于足下", Language.CHINESE_TRADITIONAL to "千里之行始於足下", Language.CHINESE_PINYIN to "Qiān lǐ zhī xíng shǐ yú zú xià",
            Language.ARABIC to "رحلة الألف ميل تبدأ بخطوة واحدة", Language.TAGALOG to "Ang paglalakbay ng isang libong milya ay nagsisimula sa isang hakbang", Language.THAI to "การเดินทางพันไมล์เริ่มต้นด้วยก้าวแรก", Language.HINDI to "हजारों मील की यात्रा एक कदम से शुरू होती है", Language.CANTONESE to "千里之行始於足下"
        ),
        "Every morning brings new potential, utilize it" to mapOf(
            Language.CHINESE_SIMPLIFIED to "每个清晨都带来新的潜能，请善加利用", Language.CHINESE_TRADITIONAL to "每個清晨都帶來新的潛能，請善加利用", Language.CHINESE_PINYIN to "Měi gè qīng chén dōu dài lái xīn de qián néng, qǐng shàn jiā lì yòng",
            Language.ARABIC to "كل صباح يأتي بإمكانيات جديدة، فاستغلها", Language.TAGALOG to "Bawat umaga ay nagdadala ng bagong potensyal, gamitin ito", Language.THAI to "ทุกเช้านำพาศักยภาพใหม่ๆ มาให้ จงใช้มัน", Language.HINDI to "हर सुबह नई क्षमताएं लाती है, इसका उपयोग करें", Language.CANTONESE to "每個清晨都帶來新嘅潛能，請善加利用"
        ),
        "Believe you can and you are halfway there" to mapOf(
            Language.CHINESE_SIMPLIFIED to "相信自己能做到，你就成功了一半", Language.CHINESE_TRADITIONAL to "相信自己能做到，你就成功了一半", Language.CHINESE_PINYIN to "Xiāng xìn zì jǐ néng zuò dào, nǐ jiù chéng gōng le yī bàn",
            Language.ARABIC to "آمن بأنك تستطيع وستكون في منتصف الطريق", Language.TAGALOG to "Maniwala kang kaya mo at nasa kalahati ka na", Language.THAI to "เชื่อว่าคุณทำได้ คุณก็สำเร็จไปครึ่งทางแล้ว", Language.HINDI to "विश्वास करें कि आप कर सकते हैं और आप आधा रास्ता तय कर चुके हैं", Language.CANTONESE to "相信自己能做到，你就成功咗一半"
        ),
        "Great things never came from comfort zones" to mapOf(
            Language.CHINESE_SIMPLIFIED to "伟大的成就从未出自舒适圈", Language.CHINESE_TRADITIONAL to "偉大的成就從未出自舒適圈", Language.CHINESE_PINYIN to "Wěi dà de chéng jiù cóng wèi chū zì shū shì juān",
            Language.ARABIC to "الأشياء العظيمة لا تأتي أبدًا من مناطق الراحة", Language.TAGALOG to "Ang mga dakilang bagay ay hindi kailanman nanggaling sa comfort zones", Language.THAI to "สิ่งดีๆ ไม่เคยเกิดขึ้นจากพื้นที่ที่สะดวกสบาย", Language.HINDI to "महान चीजें कभी भी आरामदेह दायरे से नहीं आतीं", Language.CANTONESE to "偉大嘅成就從未出自舒適圈"
        )
,
        "Decrease" to mapOf(
            Language.CHINESE_SIMPLIFIED to "减少", Language.CHINESE_TRADITIONAL to "減少", Language.CHINESE_PINYIN to "Jiǎn shǎo",
            Language.ARABIC to "تقليل", Language.TAGALOG to "Bawasan", Language.THAI to "ลด", Language.HINDI to "कम करें", Language.CANTONESE to "減少"
        ),
        "Increase" to mapOf(
            Language.CHINESE_SIMPLIFIED to "增加", Language.CHINESE_TRADITIONAL to "增加", Language.CHINESE_PINYIN to "Zēng jiā",
            Language.ARABIC to "زيادة", Language.TAGALOG to "Dagdagan", Language.THAI to "เพิ่ม", Language.HINDI to "बढ़ाएं", Language.CANTONESE to "增加"
        ),
        "Volume Down" to mapOf(
            Language.CHINESE_SIMPLIFIED to "降低音量", Language.CHINESE_TRADITIONAL to "降低音量", Language.CHINESE_PINYIN to "Jiàng dī yīn liàng",
            Language.ARABIC to "خفض الصوت", Language.TAGALOG to "Hinaan ang Volume", Language.THAI to "ลดเสียง", Language.HINDI to "आवाज कम करें", Language.CANTONESE to "降低音量"
        ),
        "Volume Up" to mapOf(
            Language.CHINESE_SIMPLIFIED to "提高音量", Language.CHINESE_TRADITIONAL to "提高音量", Language.CHINESE_PINYIN to "Tí gāo yīn liàng",
            Language.ARABIC to "رفع الصوت", Language.TAGALOG to "Lakasan ang Volume", Language.THAI to "เพิ่มเสียง", Language.HINDI to "आवाज बढ़ाएं", Language.CANTONESE to "提高音量"
        ),
        "1 day" to mapOf(
            Language.CHINESE_SIMPLIFIED to "1 天", Language.CHINESE_TRADITIONAL to "1 天", Language.CHINESE_PINYIN to "1 tiān",
            Language.ARABIC to "يوم واحد", Language.TAGALOG to "1 araw", Language.THAI to "1 วัน", Language.HINDI to "1 दिन", Language.CANTONESE to "1 天"
        ),
        "{streak} days" to mapOf(
            Language.CHINESE_SIMPLIFIED to "{streak} 天", Language.CHINESE_TRADITIONAL to "{streak} 天", Language.CHINESE_PINYIN to "{streak} tiān",
            Language.ARABIC to "{streak} أيام", Language.TAGALOG to "{streak} araw", Language.THAI to "{streak} วัน", Language.HINDI to "{streak} दिन", Language.CANTONESE to "{streak} 天"
        ),
        "Current streak" to mapOf(
            Language.CHINESE_SIMPLIFIED to "当前连续天数", Language.CHINESE_TRADITIONAL to "當前連續天數", Language.CHINESE_PINYIN to "Dāng qián lián xù tiān shù",
            Language.ARABIC to "السلسلة الحالية", Language.TAGALOG to "Kasalukuyang streak", Language.THAI to "สถิติต่อเนื่องปัจจุบัน", Language.HINDI to "वर्तमान सिलसिला", Language.CANTONESE to "當前連續天數"
        ),
        "Custom Song" to mapOf(
            Language.CHINESE_SIMPLIFIED to "自定义歌曲", Language.CHINESE_TRADITIONAL to "自定義歌曲", Language.CHINESE_PINYIN to "Zì dìng yì gē qǔ",
            Language.ARABIC to "أغنية مخصصة", Language.TAGALOG to "Custom na Kanta", Language.THAI to "เพลงที่เลือกเอง", Language.HINDI to "कस्टम गीत", Language.CANTONESE to "自定義歌曲"
        ),
        "Select Custom Song" to mapOf(
            Language.CHINESE_SIMPLIFIED to "选择自定义歌曲", Language.CHINESE_TRADITIONAL to "選擇自定義歌曲", Language.CHINESE_PINYIN to "Xuǎn zé zì dìng yì gē qǔ",
            Language.ARABIC to "اختر أغنية مخصصة", Language.TAGALOG to "Pumili ng Custom na Kanta", Language.THAI to "เลือกเพลงที่กำหนดเอง", Language.HINDI to "कस्टम गीत चुनें", Language.CANTONESE to "選擇自定義歌曲"
        ),
        "Choose any audio file from device" to mapOf(
            Language.CHINESE_SIMPLIFIED to "从设备选择任意音频文件", Language.CHINESE_TRADITIONAL to "從設備選擇任意音頻文件", Language.CHINESE_PINYIN to "Cóng shè bèi xuǎn zé rèn yì yīn pín wén jiàn",
            Language.ARABIC to "اختر أي ملف صوتي من الجهاز", Language.TAGALOG to "Pumili ng anumang audio file mula sa device", Language.THAI to "เลือกไฟล์เสียงใดก็ได้จากอุปกรณ์", Language.HINDI to "डिवाइस से कोई भी ऑडियो फ़ाइल चुनें", Language.CANTONESE to "從設備選擇任意音頻文件"
        ),
        "If you don't dismiss within the chosen window, the alarm fires again automatically." to mapOf(
            Language.CHINESE_SIMPLIFIED to "如果您未在选定窗口内关闭，闹钟将自动再次响铃。",
            Language.CHINESE_TRADITIONAL to "如果您未在選定窗口內關閉，鬧鐘將自動再次響鈴。",
            Language.CHINESE_PINYIN to "Rú guǒ nín wèi zài xuǎn dìng chuāng kǒu nèi guān bì, nào zhōng jiāng zì dòng zài cì xiǎng líng.",
            Language.ARABIC to "إذا لم تقم بالإيقاف خلال النافذة المحددة، فسيتم تشغيل المنبه مرة أخرى تلقائيًا.",
            Language.TAGALOG to "Kung hindi mo ito pinatay sa loob ng napiling window, awtomatikong tutunog muli ang alarma.",
            Language.THAI to "หากคุณไม่ปิดการปลุกภายในเวลาที่กำหนด นาฬิกาปลุกจะดังขึ้นอีกครั้งโดยอัตโนมัติ",
            Language.HINDI to "यदि आप चुनी गई अवधि के भीतर अलार्म बंद नहीं करते हैं, तो अलार्म स्वचालित रूप से फिर से बज जाएगा।",
            Language.CANTONESE to "如果您未在選定窗口內關閉，鬧鐘將自動再次響鈴。"
        ),
        "Center the code in the screen" to mapOf(
            Language.CHINESE_SIMPLIFIED to "将二维码对准屏幕中央", Language.CHINESE_TRADITIONAL to "將二維碼對準屏幕中央", Language.CHINESE_PINYIN to "Jiāng èr wéi mǎ duì zhǔn píng mú zhōng yāng",
            Language.ARABIC to "ضع الرمز في وسط الشاشة", Language.TAGALOG to "Igitna ang code sa screen", Language.THAI to "วางรหัสให้อยู่ตรงกลางหน้าจอ", Language.HINDI to "कोड को स्क्रीन के केंद्र में रखें", Language.CANTONESE to "將二維碼對準屏幕中央"
        ),
        "Shake Your Phone!" to mapOf(
            Language.CHINESE_SIMPLIFIED to "摇晃你的手机！", Language.CHINESE_TRADITIONAL to "搖晃你的手機！", Language.CHINESE_PINYIN to "Yáo huàng nǐ de shǒu jī!",
            Language.ARABIC to "هز هاتفك!", Language.TAGALOG to "I-shake ang Iyong Telepono!", Language.THAI to "เขย่าโทรศัพท์ของคุณ!", Language.HINDI to "अपना फोन हिलाएं!", Language.CANTONESE to "搖晃你嘅手機！"
        ),
        "Place phone face-down on the floor under your chest, then start push-ups." to mapOf(
            Language.CHINESE_SIMPLIFIED to "将手机屏幕朝下放在胸部下方的地板上，然后开始俯卧撑。",
            Language.CHINESE_TRADITIONAL to "將手機屏幕朝下放在胸部下方的地板上，然後開始俯臥撐。",
            Language.CHINESE_PINYIN to "Jiāng shǒu jī píng mú cháo xià fàng zài xiōng bù xià fāng de dì bǎn sh่าง...",
            Language.ARABIC to "ضع الهاتف وجهه لأسفل على الأرض تحت صدرك، ثم ابدأ تمارين الضغط.",
            Language.TAGALOG to "Ilagay ang telepono nang nakaharap sa sahig sa ilalim ng iyong dibdib, pagkatapos ay simulan ang push-ups.",
            Language.THAI to "วางโทรศัพท์คว่ำหน้าลงบนพื้นใต้หน้าอกของคุณ จากนั้นเริ่มวิดพื้น",
            Language.HINDI to "फोन को अपने सीने के नीचे फर्श पर स्क्रीन नीचे की ओर रखें, फिर पुश-अप्स शुरू करें।",
            Language.CANTONESE to "將手機屏幕朝下放在胸部下方嘅地板上，然後開始俯臥撐。"
        ),
        "Basic" to mapOf(
            Language.CHINESE_SIMPLIFIED to "基础", Language.CHINESE_TRADITIONAL to "基礎", Language.CHINESE_PINYIN to "Jī chǔ",
            Language.ARABIC to "أساسي", Language.TAGALOG to "Basic", Language.THAI to "พื้นฐาน", Language.HINDI to "बुनियादी", Language.CANTONESE to "基礎"
        ),
        "Friendly default alarm tone (Kalimba)" to mapOf(
            Language.CHINESE_SIMPLIFIED to "温和的默认闹钟铃声 (卡林巴)", Language.CHINESE_TRADITIONAL to "溫和的默認鬧鐘鈴聲 (卡林巴)", Language.CHINESE_PINYIN to "Wēn hé de mò rèn nào zhōng líng shēng (Kǎ lín bā)",
            Language.ARABIC to "نغمة المنبه الافتراضية اللطيفة (كاليمبا)", Language.TAGALOG to "Friendly na default na tunog ng alarma (Kalimba)", Language.THAI to "เสียงเตือนเริ่มต้นที่เป็นมิตร (คาลิมบา)", Language.HINDI to "अनुकूल डिफ़ॉल्ट अलार्म टोन (कालिम्बा)", Language.CANTONESE to "溫和嘅默認鬧鐘鈴聲 (卡林巴)"
        ),
        "Alarm Clock" to mapOf(
            Language.CHINESE_SIMPLIFIED to "复古闹钟", Language.CHINESE_TRADITIONAL to "復古鬧鐘", Language.CHINESE_PINYIN to "Fù gǔ nào zhōng",
            Language.ARABIC to "ساعة المنبه", Language.TAGALOG to "Alarm Clock", Language.THAI to "นาฬิกาปลุก", Language.HINDI to "अलार्म घड़ी", Language.CANTONESE to "復古鬧鐘"
        ),
        "Classic loud alarm clock ringing" to mapOf(
            Language.CHINESE_SIMPLIFIED to "经典的响亮闹钟铃声", Language.CHINESE_TRADITIONAL to "經典的響亮鬧鐘鈴聲", Language.CHINESE_PINYIN to "Jīng diǎn de xiǎng liàng nào zhōng líng shēng",
            Language.ARABIC to "رنين ساعة منبه عالي كلاسيكي", Language.TAGALOG to "Klasikong malakas na tunog ng alarm clock", Language.THAI to "เสียงนาฬิกาปลุกดังสุดคลาสสิก", Language.HINDI to "क्लासिक तेज अलार्म घड़ी बजना", Language.CANTONESE to "經典嘅響亮鬧鐘鈴聲"
        ),
        "Bugle Tune" to mapOf(
            Language.CHINESE_SIMPLIFIED to "军号调", Language.CHINESE_TRADITIONAL to "軍號調", Language.CHINESE_PINYIN to "Jūn hào tiáo",
            Language.ARABIC to "نغمة البوق", Language.TAGALOG to "Tugtog ng Trumpeta", Language.THAI to "เพลงแตรเดี่ยว", Language.HINDI to "बिगुल ट्यून", Language.CANTONESE to "軍號調"
        ),
        "Triumphant military bugle horn call" to mapOf(
            Language.CHINESE_SIMPLIFIED to "凯旋的军号声", Language.CHINESE_TRADITIONAL to "凱旋的軍號聲", Language.CHINESE_PINYIN to "Kǎi xuán de jūn hào shēng",
            Language.ARABIC to "نداء بوق عسكري منتصر", Language.TAGALOG to "Matagumpay na tawag ng militar na trumpeta", Language.THAI to "เสียงแตรทหารที่ได้รับชัยชนะ", Language.HINDI to "विजयी सैन्य बिगुल कॉल", Language.CANTONESE to "凱旋嘅軍號聲"
        ),
        "Medium Bell" to mapOf(
            Language.CHINESE_SIMPLIFIED to "中型警铃", Language.CHINESE_TRADITIONAL to "中型警鈴", Language.CHINESE_PINYIN to "Zhōng xíng jǐng líng",
            Language.ARABIC to "جرس متوسط", Language.TAGALOG to "Katamtamang Kampana", Language.THAI to "ระฆังขนาดกลาง", Language.HINDI to "मध्यम घंटी", Language.CANTONESE to "中型警鈴"
        ),
        "Resonant metallic bell ringing near" to mapOf(
            Language.CHINESE_SIMPLIFIED to "回荡的金属铃声", Language.CHINESE_TRADITIONAL to "回蕩的金屬鈴聲", Language.CHINESE_PINYIN to "Huí dàng de jīn shǔ líng shēng",
            Language.ARABIC to "رنين جرس معدني رنان بالقرب", Language.TAGALOG to "Umaalingawngaw na metal na kampana sa malapit", Language.THAI to "เสียงระฆังโลหะดังกังวานใกล้ๆ", Language.HINDI to "पास में बजती गूंजती धातु की घंटी", Language.CANTONESE to "回蕩嘅金屬鈴聲"
        ),
        "Short Beeps" to mapOf(
            Language.CHINESE_SIMPLIFIED to "短促嘀嘀声", Language.CHINESE_TRADITIONAL to "短促嘀嘀聲", Language.CHINESE_PINYIN to "Duǎn cù dí dí shēng",
            Language.ARABIC to "صافرات قصيرة", Language.TAGALOG to "Maikling Beep", Language.THAI to "เสียงบี๊บสั้น", Language.HINDI to "लघु बीप", Language.CANTONESE to "短促嘀嘀聲"
        ),
        "Quick alarm notification beeps" to mapOf(
            Language.CHINESE_SIMPLIFIED to "快速的警报通知嘀嘀声", Language.CHINESE_TRADITIONAL to "快速的警報通知嘀嘀聲", Language.CHINESE_PINYIN to "Kuài sù de jǐng bào tōng zhī dí dí shēng",
            Language.ARABIC to "صافرات إشعار منبه سريعة", Language.TAGALOG to "Mabilis na beeps ng notification ng alarm", Language.THAI to "เสียงบี๊บแจ้งเตือนสั้นๆ และรวดเร็ว", Language.HINDI to "त्वरित अलार्म अधिसूचना बीप", Language.CANTONESE to "快速嘅警報通知嘀嘀聲"
        ),
        "Clown Horn" to mapOf(
            Language.CHINESE_SIMPLIFIED to "小丑喇叭", Language.CHINESE_TRADITIONAL to "小丑喇叭", Language.CHINESE_PINYIN to "Xiǎo chǒu lǎ ba",
            Language.ARABIC to "بوق المهرج", Language.TAGALOG to "Busina ng Clown", Language.THAI to "แตรตัวตลก", Language.HINDI to "जोकर हॉर्न", Language.CANTONESE to "小丑喇叭"
        ),
        "Funny circus comedy honk sound" to mapOf(
            Language.CHINESE_SIMPLIFIED to "滑稽的马戏团喜剧喇叭声", Language.CHINESE_TRADITIONAL to "滑稽的馬戲團喜劇喇叭聲", Language.CHINESE_PINYIN to "Huá jī de mǎ xì tuán xǐ jù lǎ ba shēng",
            Language.ARABIC to "صوت بوق كوميدي مضحك للسيرك", Language.TAGALOG to "Nakakatawang tunog ng busina sa sirko", Language.THAI to "เสียงแตรตลกๆ ในละครสัตว์", Language.HINDI to "मज़ेदार सर्कस कॉमेडी हॉन्क ध्वनि", Language.CANTONESE to "滑稽嘅馬戲團喜劇喇叭聲"
        ),
        "Wake Up" to mapOf(
            Language.CHINESE_SIMPLIFIED to "醒来", Language.CHINESE_TRADITIONAL to "醒來", Language.CHINESE_PINYIN to "Xǐng lái",
            Language.ARABIC to "استيقظ", Language.TAGALOG to "Gising Na", Language.THAI to "ตื่นนอน", Language.HINDI to "जागो", Language.CANTONESE to "醒來"
        ),
        "Energizing modern morning melody" to mapOf(
            Language.CHINESE_SIMPLIFIED to "充满活力的现代早晨旋律", Language.CHINESE_TRADITIONAL to "充滿活力的現代早晨旋律", Language.CHINESE_PINYIN to "Chōng mǎn huó lì de xiàn dài zǎo chén xuán lǜ",
            Language.ARABIC to "لحن صباحي حديث مفعم بالحيوية", Language.TAGALOG to "Masiglang makabagong himig sa umaga", Language.THAI to "ท่วงทำนองยามเช้าที่ทันสมัยและกระฉับกระเฉง", Language.HINDI to "ऊर्जावान आधुनिक सुबह का संगीत", Language.CANTONESE to "充滿活力嘅現代早晨旋律"
        ),
        "Nature" to mapOf(
            Language.CHINESE_SIMPLIFIED to "自然", Language.CHINESE_TRADITIONAL to "自然", Language.CHINESE_PINYIN to "Zì rán",
            Language.ARABIC to "الطبيعة", Language.TAGALOG to "Kalikasan", Language.THAI to "ธรรมชาติ", Language.HINDI to "प्रकृति", Language.CANTONESE to "自然"
        ),
        "Calming acoustic synth melody" to mapOf(
            Language.CHINESE_SIMPLIFIED to "宁静的声学合成器旋律", Language.CHINESE_TRADITIONAL to "寧靜的聲學合成器旋律", Language.CHINESE_PINYIN to "Níng jìng de shēng xué hé chéng qì xuán lǜ",
            Language.ARABIC to "لحن سنثسيزر صوتي مهدئ", Language.TAGALOG to "Kumakalmang tono ng acoustic synth", Language.THAI to "ท่วงทำนองสังเคราะห์เสียงที่ผ่อนคลาย", Language.HINDI to "शांत ध्वनिक सिंथ धुन", Language.CANTONESE to "寧靜嘅聲學合成器旋律"
        ),
        "Digital Watch" to mapOf(
            Language.CHINESE_SIMPLIFIED to "电子表", Language.CHINESE_TRADITIONAL to "電子表", Language.CHINESE_PINYIN to "Diàn zǐ biǎo",
            Language.ARABIC to "ساعة رقمية", Language.TAGALOG to "Digital na Orasan", Language.THAI to "นาฬิกาดิจิทัล", Language.HINDI to "डिजिटल घड़ी", Language.CANTONESE to "電子表"
        ),
        "Beeping of a digital wristwatch" to mapOf(
            Language.CHINESE_SIMPLIFIED to "电子手表的嘀嘀声", Language.CHINESE_TRADITIONAL to "電子手表的嘀嘀聲", Language.CHINESE_PINYIN to "Diàn zǐ shǒu biǎo de dí dí shēng",
            Language.ARABIC to "رنين ساعة يد رقمية", Language.TAGALOG to "Beep ng digital na wristwatch", Language.THAI to "เสียงเตือนของนาฬิกาข้อมือดิจิทัล", Language.HINDI to "डिजिटल कलाई घड़ी की बीप", Language.CANTONESE to "電子手錶嘅嘀嘀聲"
        ),
        "Spaceship" to mapOf(
            Language.CHINESE_SIMPLIFIED to "飞船太空舱", Language.CHINESE_TRADITIONAL to "飛船太空艙", Language.CHINESE_PINYIN to "Fēi chuán tài kōng cāng",
            Language.ARABIC to "سفينة فضاء", Language.TAGALOG to "Spaceship", Language.THAI to "ยานอวกาศ", Language.HINDI to "अंतरिक्ष यान", Language.CANTONESE to "飛船太空艙"
        ),
        "Sci-fi ship alarm theme" to mapOf(
            Language.CHINESE_SIMPLIFIED to "科幻飞船警报主题", Language.CHINESE_TRADITIONAL to "科幻飛船警報主題", Language.CHINESE_PINYIN to "Kē huàn fēi chuán jǐng bào zhǔ tí",
            Language.ARABIC to "نغمة إنذار سفينة خيال علمي", Language.TAGALOG to "Sci-fi na tema ng alarm ng barko", Language.THAI to "ธีมเสียงเตือนยานไซไฟ", Language.HINDI to "विज्ञान-कथा जहाज अलार्म थीम", Language.CANTONESE to "科幻飛船警報主題"
        ),
        "Dosimeter" to mapOf(
            Language.CHINESE_SIMPLIFIED to "辐射仪", Language.CHINESE_TRADITIONAL to "辐射仪", Language.CHINESE_PINYIN to "Fú shè yí",
            Language.ARABIC to "مقياس الجرعات", Language.TAGALOG to "Dosimeter", Language.THAI to "เครื่องวัดรังสี", Language.HINDI to "डोसीमीटर", Language.CANTONESE to "辐射仪"
        ),
        "Clicking radiation dosimeter alarm" to mapOf(
            Language.CHINESE_SIMPLIFIED to "辐射仪咔哒咔哒的警报声", Language.CHINESE_TRADITIONAL to "輻射儀咔噠咔噠的警報聲", Language.CHINESE_PINYIN to "Fú shè yí kā dā kā dā de jǐng bào shēng",
            Language.ARABIC to "إنذار مقياس جرعات الإشعاع المتقطع", Language.TAGALOG to "Tumutunog na radiation dosimeter alarm", Language.THAI to "เสียงเตือนของเครื่องวัดปริมาณรังสี", Language.HINDI to "विकिरण डोसीमीटर अलार्म की आवाज", Language.CANTONESE to "輻射儀咔噠咔噠嘅警報聲"
        ),
        "Phone Alerts" to mapOf(
            Language.CHINESE_SIMPLIFIED to "电话提醒", Language.CHINESE_TRADITIONAL to "電話提醒", Language.CHINESE_PINYIN to "Diàn huà tí xǐng",
            Language.ARABIC to "تنبيهات الهاتف", Language.TAGALOG to "Mga Alert sa Telepono", Language.THAI to "การแจ้งเตือนโทรศัพท์", Language.HINDI to "फोन अलर्ट", Language.CANTONESE to "電話提醒"
        ),
        "Assorted electronic phone rings" to mapOf(
            Language.CHINESE_SIMPLIFIED to "各种电子电话铃声", Language.CHINESE_TRADITIONAL to "各種電子電話鈴聲", Language.CHINESE_PINYIN to "Gè zhǒng diàn zǐ diàn huà líng shēng",
            Language.ARABIC to "رنات هاتف إلكترونية متنوعة", Language.TAGALOG to "Sari-saring tunog ng telepono", Language.THAI to "เสียงเรียกเข้าโทรศัพท์อิเล็กทรอนิกส์ต่างๆ", Language.HINDI to "विभिन्न इलेक्ट्रॉनिक फोन रिंग", Language.CANTONESE to "各種電子電話鈴聲"
        ),
        "Computer Sounds" to mapOf(
            Language.CHINESE_SIMPLIFIED to "电脑音效", Language.CHINESE_TRADITIONAL to "電腦音效", Language.CHINESE_PINYIN to "Diàn nǎo yīn xiào",
            Language.ARABIC to "أصوات الكمبيوتر", Language.TAGALOG to "Mga Tunog ng Kompyuter", Language.THAI to "เสียงคอมพิวเตอร์", Language.HINDI to "कंप्यूटर ध्वनि", Language.CANTONESE to "電腦音效"
        ),
        "Retro computer alert notifications" to mapOf(
            Language.CHINESE_SIMPLIFIED to "复古电脑提示音效", Language.CHINESE_TRADITIONAL to "復古電腦提示音效", Language.CHINESE_PINYIN to "Fù gǔ diàn nǎo tí shì yīn xiào",
            Language.ARABIC to "إشعارات تنبيه كمبيوتر قديم", Language.TAGALOG to "Retro na abiso ng kompyuter", Language.THAI to "การแจ้งเตือนเสียงคอมพิวเตอร์ย้อนยุค", Language.HINDI to "रेट्रो कंप्यूटर अलर्ट सूचनाएं", Language.CANTONESE to "復古電腦提示音效"
        ),
        "Alien Beam" to mapOf(
            Language.CHINESE_SIMPLIFIED to "外星光束", Language.CHINESE_TRADITIONAL to "外星光束", Language.CHINESE_PINYIN to "Wài xīng guāng shù",
            Language.ARABIC to "شعاع فضائي", Language.TAGALOG to "Alien Beam", Language.THAI to "ลำแสงเอเลี่ยน", Language.HINDI to "एलियन बीम", Language.CANTONESE to "外星光束"
        ),
        "Deep pulsing extraterrestrial beam" to mapOf(
            Language.CHINESE_SIMPLIFIED to "深沉脉冲的外星光束声", Language.CHINESE_TRADITIONAL to "深沉脈衝的外星光束聲", Language.CHINESE_PINYIN to "Shēn chén mài chōng de wài xīng guāng shù shēng",
            Language.ARABIC to "شعاع فضائي عميق ونابض", Language.TAGALOG to "Malalim at tumitibok na alien beam", Language.THAI to "ลำแสงนอกโลกที่เต้นเป็นจังหวะลึก", Language.HINDI to "गहरी धड़कती हुई अलौकिक किरण", Language.CANTONESE to "深沉脈衝嘅外星光束聲"
        ),
        "Electric Ring" to mapOf(
            Language.CHINESE_SIMPLIFIED to "电磁电圈", Language.CHINESE_TRADITIONAL to "電磁電圈", Language.CHINESE_PINYIN to "Diàn cí diàn quān",
            Language.ARABIC to "رنين كهربائي", Language.TAGALOG to "Electric Ring", Language.THAI to "เสียงกริ่งไฟฟ้า", Language.HINDI to "इलेक्ट्रिक रिंग", Language.CANTONESE to "電磁電圈"
        ),
        "High voltage electric ringtone sound" to mapOf(
            Language.CHINESE_SIMPLIFIED to "高压电磁铃声声效", Language.CHINESE_TRADITIONAL to "高壓電磁鈴聲聲效", Language.CHINESE_PINYIN to "Gāo yā diàn cí líng shēng shēng xiào",
            Language.ARABIC to "صوت رنين كهربائي عالي الجهد", Language.TAGALOG to "Tunog ng mataas na boltahe na kuryente", Language.THAI to "เสียงเรียกเข้าไฟฟ้าแรงสูง", Language.HINDI to "उच्च वोल्टेज इलेक्ट्रिक रिंगटोन ध्वनि", Language.CANTONESE to "高壓電磁鈴聲聲效"
        ),
        "High Low Sweep" to mapOf(
            Language.CHINESE_SIMPLIFIED to "高低音频扫频", Language.CHINESE_TRADITIONAL to "高低音頻掃頻", Language.CHINESE_PINYIN to "Gāo dī yīn pín sǎo pín",
            Language.ARABIC to "مسح مرتفع منخفض", Language.TAGALOG to "High Low Sweep", Language.THAI to "กวาดเสียงสูงต่ำ", Language.HINDI to "हाई लो स्वीप", Language.CANTONESE to "高低音頻掃頻"
        ),
        "Pitch-sweeping alert sound effect" to mapOf(
            Language.CHINESE_SIMPLIFIED to "音调起伏的扫频警报声", Language.CHINESE_TRADITIONAL to "音調起伏的掃頻警報聲", Language.CHINESE_PINYIN to "Yīn diào qǐ fú de sǎo pín jǐng bào shēng",
            Language.ARABIC to "تأثير صوتي لتنبيه مغير طبقة الصوت", Language.TAGALOG to "Tunog ng nagbabagong tono ng alarma", Language.THAI to "เอฟเฟกต์เสียงเตือนแบบกวาดระดับเสียง", Language.HINDI to "पिच-स्वीपिंग अलर्ट ध्वनि प्रभाव", Language.CANTONESE to "音調起伏嘅掃頻警報聲"
        )
)

    fun t(key: String): String {
        val lang = currentLanguage
        if (key.startsWith("Day_") && key.endsWith("_Initial")) {
            val initial = when (key) {
                "Day_Mon_Initial" -> when (lang) {
                    Language.ENGLISH, Language.TAGALOG -> "M"
                    Language.CHINESE_SIMPLIFIED, Language.CHINESE_TRADITIONAL, Language.CANTONESE -> "一"
                    Language.CHINESE_PINYIN -> "Yī"
                    Language.ARABIC -> "ن"
                    Language.THAI -> "จ"
                    Language.HINDI -> "सोम"
                }
                "Day_Tue_Initial" -> when (lang) {
                    Language.ENGLISH, Language.TAGALOG -> "T"
                    Language.CHINESE_SIMPLIFIED, Language.CHINESE_TRADITIONAL, Language.CANTONESE -> "二"
                    Language.CHINESE_PINYIN -> "Èr"
                    Language.ARABIC -> "ث"
                    Language.THAI -> "อ"
                    Language.HINDI -> "मं"
                }
                "Day_Wed_Initial" -> when (lang) {
                    Language.ENGLISH, Language.TAGALOG -> "W"
                    Language.CHINESE_SIMPLIFIED, Language.CHINESE_TRADITIONAL, Language.CANTONESE -> "三"
                    Language.CHINESE_PINYIN -> "Sān"
                    Language.ARABIC -> "ر"
                    Language.THAI -> "พ"
                    Language.HINDI -> "बुध"
                }
                "Day_Thu_Initial" -> when (lang) {
                    Language.ENGLISH, Language.TAGALOG -> "T"
                    Language.CHINESE_SIMPLIFIED, Language.CHINESE_TRADITIONAL, Language.CANTONESE -> "四"
                    Language.CHINESE_PINYIN -> "Sì"
                    Language.ARABIC -> "خ"
                    Language.THAI -> "พฤ"
                    Language.HINDI -> "गुरु"
                }
                "Day_Fri_Initial" -> when (lang) {
                    Language.ENGLISH, Language.TAGALOG -> "F"
                    Language.CHINESE_SIMPLIFIED, Language.CHINESE_TRADITIONAL, Language.CANTONESE -> "五"
                    Language.CHINESE_PINYIN -> "Wǔ"
                    Language.ARABIC -> "ج"
                    Language.THAI -> "ศ"
                    Language.HINDI -> "शुक्र"
                }
                "Day_Sat_Initial" -> when (lang) {
                    Language.ENGLISH, Language.TAGALOG -> "S"
                    Language.CHINESE_SIMPLIFIED, Language.CHINESE_TRADITIONAL, Language.CANTONESE -> "六"
                    Language.CHINESE_PINYIN -> "Liù"
                    Language.ARABIC -> "س"
                    Language.THAI -> "ส"
                    Language.HINDI -> "शनि"
                }
                "Day_Sun_Initial" -> when (lang) {
                    Language.ENGLISH, Language.TAGALOG -> "S"
                    Language.CHINESE_SIMPLIFIED, Language.CHINESE_TRADITIONAL, Language.CANTONESE -> "日"
                    Language.CHINESE_PINYIN -> "Rì"
                    Language.ARABIC -> "ح"
                    Language.THAI -> "อา"
                    Language.HINDI -> "रवि"
                }
                else -> null
            }
            if (initial != null) return initial
        }
        val translation = translations[key]?.get(lang)
        if (translation != null) return translation
        if (lang == Language.ENGLISH) return key
        return translations[key]?.get(Language.ENGLISH) ?: key
    }
}
