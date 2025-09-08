# Retrofit/Gson (avisos comuns)
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepattributes Signature,*Annotation*

# Mantenha interfaces Retrofit (anotações de HTTP)
-keep class * {
    @retrofit2.http.* <methods>;
}

# Modelos do Gson (campos)
-keep class br.com.fiap.helpweather.data.model.** { *; }

# Osmdroid
-dontwarn org.osmdroid.**

# Coil
-dontwarn coil.**
